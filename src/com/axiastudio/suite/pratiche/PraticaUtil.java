/*
 * Copyright (C) 2012 AXIA Studio (http://www.axiastudio.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axiastudio.suite.pratiche;

import com.axiastudio.mapformat.MessageMapFormat;
import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.suite.pratiche.entities.TipoPratica;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class PraticaUtil {

    public static String creaCodificaInterna(TipoPratica tipoPratica){

        Map<String, Object> map = new HashMap();

        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManager em = db.getEntityManagerFactory().createEntityManager();

        // anno
        Calendar calendar = Calendar.getInstance();
        Integer year = calendar.get(Calendar.YEAR);
        map.put("anno", year.toString());

        // giunta
        map.put("giunta", "2010");

        // codici sezione e numerici
        String formulacodifica = tipoPratica.getFormulacodifica();
        List<TipoPratica> tipi = new ArrayList();
        while( tipoPratica != null ){
            tipi.add(0, tipoPratica);
            tipoPratica = tipoPratica.getTipopadre();
        }
        int i = 0;
        for( TipoPratica tipo: tipi ){
            i++;
            int n = 5; // XXX: porzione numerica nella codifica
            Query q = em.createQuery("select max(p.codiceinterno) from Pratica p where p.anno = " + year.toString() + " and p.codiceinterno like '"+tipo.getCodice()+"%'");
            String maxString = (String) q.getSingleResult();
            Integer max=1;
            if( maxString != null ){
                max = Integer.parseInt(maxString.substring(maxString.length() - n));
            }
            map.put("s"+i, tipo.getCodice());
            map.put("n"+i, max);
        }

        // composizione codifica
        MessageMapFormat mmp = new MessageMapFormat(formulacodifica);
        String codifica = mmp.format(map);
        return codifica;
    }

}
