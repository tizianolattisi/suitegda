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

import com.axiastudio.suite.menjazo.AlfrescoHelper;
import com.axiastudio.pypapi.IStreamProvider;

import java.io.InputStream;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
public class CmisStreamProvider implements IStreamProvider {
    private final String cmisUrl;
    private final String user;
    private final String password;
    private String idObject;

    public CmisStreamProvider(String cmisUrl, String user, String password, String idObject) {
        this.cmisUrl = cmisUrl;
        this.user = user;
        this.password = password;
        this.idObject = idObject;
    }

    @Override
    public InputStream getInputStream() {
        AlfrescoHelper helper = new AlfrescoHelper(this.cmisUrl, this.user, this.password);
        return helper.getDocumentStream(this.idObject);
    }
    
}
