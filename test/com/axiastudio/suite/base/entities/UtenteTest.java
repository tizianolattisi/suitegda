/*
 * Copyright (C) 2013 AXIA Studio (http://www.axiastudio.com)
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
 * You should have received a copy of the GNU Afffero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axiastudio.suite.base.entities;

import com.axiastudio.suite.EntityBaseTest;
import org.junit.Test;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
public class UtenteTest extends EntityBaseTest {

    @Test
    public void testToString() throws Exception {
        Utente tiziano = new Utente();
        tiziano.setNome("Tiziano Lattisi");
        tiziano.setLogin("tiziano");
        tiziano.setAmministratore(Boolean.TRUE);

        em.getTransaction().begin();
        em.persist(tiziano);
        em.getTransaction().commit();

        Long id = tiziano.getId();
        assert tiziano.toString().equals("Tiziano Lattisi");

        em.getTransaction().begin();
        em.remove(tiziano);
        em.getTransaction().commit();

    }

}
