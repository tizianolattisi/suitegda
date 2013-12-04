package com.axiastudio.suite.procedimenti;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.Resolver;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.pratiche.entities.FasePratica;
import com.axiastudio.suite.pratiche.entities.Pratica;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: tiziano
 * Date: 02/12/13
 * Time: 15:00
 */
public class SimpleWorkFlow {

    List<FasePratica> fasi = new ArrayList<FasePratica>();
    Map<String, Object> map = null;
    Object obj;

    public SimpleWorkFlow(Object object) {
        Pratica pratica=null;
        this.obj = object;
        if( object instanceof Pratica ){
            pratica = (Pratica) object;
        } else {
            Method getPratica = Resolver.getterFromFieldName(object.getClass(), "pratica");
            try {
                pratica = (Pratica) getPratica.invoke(object);
            } catch (IllegalAccessException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (InvocationTargetException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        if( pratica != null ){
            for( FasePratica fp: pratica.getFasePraticaCollection() ){
                fasi.add(fp);
            }
        }
    }

    public List<FasePratica> getFasi() {
        return fasi;
    }

    public FasePratica getFase(Integer i){
        return fasi.get(i);
    }

    public void bind(Map<String, Object> map){
        this.map = map;
    }

    public Boolean attivabile(FasePratica fp){
        String groovyClosure = fp.getCondizione();
        if( groovyClosure== null ){
            return true;
        }
        Binding binding = new Binding();
        binding.setVariable("obj", obj);
        Utente utente = (Utente) Register.queryUtility(IUtente.class);
        binding.setVariable("utente", utente);

        // extra bindings
        for( String key: map.keySet() ){
            binding.setVariable(key, map.get(key));
        }

        GroovyShell shell = new GroovyShell(binding);
        String groovy = groovyClosure + "(obj)";
        Boolean value = (Boolean) shell.evaluate(groovy);
        return value;
    }

    public Boolean attivabile(Integer i){
        return attivabile(fasi.get(i));
    }

    public FasePratica getFaseAttiva(){
        for( FasePratica fp: getFasi() ){
            if( !fp.getCompletata() ){
                return fp;
            }
        }
        return null;
    }

    public void completaFase(FasePratica fp){
        completaFase(fp, Boolean.TRUE);
    }

    public void completaFase(FasePratica fp, Boolean confermata){
        //fp.setCompletata(confermata);
        if( confermata ){
            setFaseAttiva(fp.getConfermata());
        } else {
            setFaseAttiva(fp.getRifiutata());
        }

    }

    public void setFaseAttiva(FasePratica faseAttiva){
        Boolean trovata = Boolean.FALSE;
        for( FasePratica fp: getFasi() ){
            if( !trovata ){
                if( !fp.equals(faseAttiva) ){
                    fp.setCompletata(Boolean.TRUE);
                } else {
                    fp.setCompletata(Boolean.FALSE);
                    trovata = Boolean.TRUE;
                }
            } else {
                fp.setCompletata(Boolean.FALSE);
            }
        }
    }

}
