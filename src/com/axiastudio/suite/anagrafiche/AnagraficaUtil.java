package com.axiastudio.suite.anagrafiche;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.suite.anagrafiche.entities.Riferimento;
import com.axiastudio.suite.anagrafiche.entities.Soggetto;
import com.axiastudio.suite.anagrafiche.entities.TipoRiferimento;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * User: tiziano
 * Date: 11/02/14
 * Time: 09:43
 */
public class AnagraficaUtil {

    public static List<Soggetto> trovaSoggettoDaEmail(String email){
        return trovaSoggettoDaEmail(email, null);
    }

    public static List<Soggetto> trovaSoggettoDaEmail(String email, TipoRiferimento tipo){

        List<Soggetto> soggetti = new ArrayList<Soggetto>();

        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManager em = db.getEntityManagerFactory().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Riferimento> cq = cb.createQuery(Riferimento.class);
        Root from = cq.from(Riferimento.class);
        Predicate predicate = cb.equal(from.get("riferimento"), email);
        cq.select(from);
        cq.where(predicate);
        TypedQuery<Riferimento> tq = em.createQuery(cq);
        List<Riferimento> riferimenti = tq.getResultList();
        for( Riferimento riferimento: riferimenti){
            soggetti.add(riferimento.getSoggetto());
        }
        return soggetti;
    }

}
