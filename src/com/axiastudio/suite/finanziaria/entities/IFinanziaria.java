/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite.finanziaria.entities;

import com.axiastudio.suite.deliberedetermine.entities.ImpegnoDetermina;
import java.util.List;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 * 
 * Per le realtà che hanno una gestione delle finanziaria è possibile scrivere
 * e registrare un utilità per la gestione dell'interoperabilità.
 * 
 * IFinanziaria è un'interfaccia segnaposto. L'utilità registrata non deve
 * necessariamente dichiarare la sua implementazione.
 * 
 */
public interface IFinanziaria {
    
    public List<ImpegnoDetermina> getImpegniDetermina(String attoOBozza, String organoSettore, String anno, String numero);
    
}
