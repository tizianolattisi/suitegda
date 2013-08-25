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

import com.axiastudio.pypapi.IStreamProvider;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
public class FileStreamProvider implements IStreamProvider {

    String fileUrl;
    
    public FileStreamProvider(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    @Override
    public InputStream getInputStream() {
        InputStream inputStream=null;
        try {    
            FileInputStream fileInputStream = new FileInputStream(this.fileUrl);
            inputStream = new BufferedInputStream(fileInputStream);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileStreamProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        return inputStream;
    }
    
}
