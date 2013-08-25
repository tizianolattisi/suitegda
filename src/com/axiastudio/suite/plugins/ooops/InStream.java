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

import com.sun.star.io.*;

import java.io.ByteArrayInputStream;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 * 
 * from: http://www.oooforum.org/forum/viewtopic.phtml?t=13205
 * 
 */
public class InStream extends ByteArrayInputStream implements XInputStream, XSeekable {

    public InStream(byte[] buf) {
        super(buf);
    }
    
    @Override
    public int readBytes(byte[][] bytes, int i) throws NotConnectedException, BufferSizeExceededException, IOException {
        int numberOfReadBytes;
        try {
            byte[] tmpBytes = new byte[i];
            numberOfReadBytes = super.read(tmpBytes);
            if( numberOfReadBytes > 0 ) {
                if( numberOfReadBytes < i ) {
                    byte[] smallerBuffer = new byte[numberOfReadBytes];
                    System.arraycopy(tmpBytes, 0, smallerBuffer, 0, numberOfReadBytes);
                    tmpBytes = smallerBuffer;
                }
            }
            else {
                tmpBytes = new byte[0];
                numberOfReadBytes = 0;
            }

            bytes[0] = tmpBytes;
            return numberOfReadBytes;
        }
        catch (java.io.IOException e) {
            throw new com.sun.star.io.IOException(e.getMessage(),this);
        }
    }

    @Override
    public int readSomeBytes(byte[][] bytes, int i) throws NotConnectedException, BufferSizeExceededException, IOException {
        return readBytes(bytes, i);
    }

    @Override
    public void skipBytes(int i) throws NotConnectedException, BufferSizeExceededException, IOException {
        skip(i);
    }

    @Override
    public void closeInput() throws NotConnectedException, IOException {
        try {
            close();
        }
        catch (java.io.IOException e) {
            throw new com.sun.star.io.IOException(e.getMessage(), this);
        }
    }

    @Override
    public void seek(long l) throws IllegalArgumentException, IOException {
        this.pos = (int) l;
    }

    @Override
    public long getPosition() throws IOException {
        return this.pos;
    }

    @Override
    public long getLength() throws IOException {
        return this.count;
    }
    
}
