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
package com.axiastudio.suite.plugins.cmis;

import com.axiastudio.pypapi.Resolver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */

class PathTemplate{
    
    final private String template;
    final private Pattern pattern;
    final private Matcher matcher;

    public PathTemplate(String template) {
        this.pattern = Pattern.compile("\\$\\{([a-zA-Z][a-zA-Z0-9_]*(\\.[a-zA-Z][a-zA-Z0-9_]*)*(\\,(number|date|time|choice)*(\\,[#\\.0-9\\|<>YyMmDd]*)?)?)\\}");
        this.template = template;
        this.matcher = this.pattern.matcher(template);
    }
    
    public String mergeObject(Object object){
        this.matcher.reset();
        StringBuffer stringBuffer = new StringBuffer();
        while( this.matcher.find() ){
            String propertyName;
            String formatTypeAndPattern;
            String group0 = this.matcher.group();
            String group1 = this.matcher.group(1);
            int n = group1.indexOf(",");
            if( n>0 ){
                propertyName = group1.substring(0, n);
                formatTypeAndPattern = group1.substring(n);
            } else {
                propertyName = group1;
                formatTypeAndPattern = null;
            }
            Method getter = Resolver.getterFromFieldName(object.getClass(), propertyName);
            Object value = null;
            String s;
            try {
                value = getter.invoke(object);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(PathTemplate.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(PathTemplate.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(PathTemplate.class.getName()).log(Level.SEVERE, null, ex);
            }
            if( n>0 ){
                MessageFormat mf = new MessageFormat("{0" + formatTypeAndPattern + "}");
                Object[] args = {value};
                s = mf.format(args);
            } else {
                s = value.toString();
            }
            this.matcher.appendReplacement(stringBuffer, Matcher.quoteReplacement(s));
        }
        this.matcher.appendTail(stringBuffer);
        return stringBuffer.toString();
    }
}

public class CmisUtil {
    
    public static String cmisPathGenerator(String template, Object object){
        String path;
        PathTemplate pathTemplate = new PathTemplate(template);
        
        path = pathTemplate.mergeObject(object);
        
        return path;
    }
}
