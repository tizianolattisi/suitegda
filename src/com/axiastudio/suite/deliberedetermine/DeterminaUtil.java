package com.axiastudio.suite.deliberedetermine;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Controller;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.pypapi.db.Validation;
import com.axiastudio.suite.deliberedetermine.entities.Determina;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Calendar;

/**
 * User: tiziano
 * Date: 14/03/14
 * Time: 11:27
 */
public class DeterminaUtil {


    public static Boolean numeroDiDetermina(Determina determina) {

        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManager em = db.getEntityManagerFactory().createEntityManager();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        determina.setAnno(year);

        // cerchiamo il numero di determina
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Determina> cq = cb.createQuery(Determina.class);
        Root<Determina> root = cq.from(Determina.class);
        cq.select(root);
        cq.where(cb.and(cb.equal(root.get("anno"), year), cb.isNotNull(root.get("numero"))));
        cq.orderBy(cb.desc(root.get("numero")));
        TypedQuery<Determina> tq = em.createQuery(cq).setMaxResults(1);
        Integer numero;
        try {
            numero = tq.getSingleResult().getNumero()+1;
        } catch (NoResultException ex) {
            numero = 1;
        } catch (NullPointerException ex) {
            numero = 1;
        } catch (Exception ex) {
            System.out.println("Errore generico");
            return Boolean.FALSE;
        }
        determina.setNumero(numero);

        // setto anche la data della determina
        determina.setData(Calendar.getInstance().getTime());

        // commit
        Controller controller = db.createController(Determina.class);
        Validation validation = controller.commit(determina);

        return validation.getResponse();
    }
}
