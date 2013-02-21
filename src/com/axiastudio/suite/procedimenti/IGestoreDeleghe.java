/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite.procedimenti;

import com.axiastudio.suite.procedimenti.entities.CodiceCarica;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
public interface IGestoreDeleghe {
    
    public Boolean checkDelega(CodiceCarica codiceCarica);
    
}
