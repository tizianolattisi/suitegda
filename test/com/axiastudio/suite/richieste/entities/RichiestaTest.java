package com.axiastudio.suite.richieste.entities;

import org.junit.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created with IntelliJ IDEA.
 * User: Mickey
 * Date: 18/11/13
 * Time: 18.19
 * To change this template use File | Settings | File Templates.
 */
public class RichiestaTest {
    private static EntityManagerFactory emf;
    private EntityManager em;
    private Long aPrimaryKey;

    @BeforeClass
    public static void setUpClass() {
        emf = Persistence.createEntityManagerFactory("SuitePU");
    }

    @AfterClass
    public static void tearDownClass() {
    }


    @Before
    public void setUp() {
        em = emf.createEntityManager();
    }

    @After
    public void tearDown() {
        em.getTransaction().begin();
        //em.createQuery("DELETE FROM Richiesta m").executeUpdate();
        em.getTransaction().commit();
        em.close();
    }

    @Test
    public void testInsert() {

        Richiesta msg = new Richiesta();
        //msg.setTesto("Bene, ecco tutto.");

        em.getTransaction().begin();
        em.persist(msg);
        em.getTransaction().commit();

    }
}
