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
package com.axiastudio.suite.plugins.ooops;

import java.util.List;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 * 
 * The form that is implementing IDocumentFolder should return a List of
 * Template, depending on the current entity.
 * 
 */
public interface IDocumentFolder {
    
    public List<Template> getTemplates();
    public void createDocument(String subpath, String name, String title, String description, byte[] content, String mimeType);
    
}
