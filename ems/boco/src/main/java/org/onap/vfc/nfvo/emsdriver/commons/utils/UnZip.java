/*
 * Copyright 2017 BOCO Corporation.  CMCC Technologies Co., Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onap.vfc.nfvo.emsdriver.commons.utils;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;


public class UnZip {
    protected String deCompressPath = null;
    protected String zipFilePath = null;

    public UnZip(String zipFilePath, String toPath) throws IOException {
        File zipFile = new File(
                new File(zipFilePath).getAbsolutePath());
        if (!zipFile.isFile())
            throw new IOException("not found file '" + zipFilePath + "'");

        this.deCompressPath = toPath;
        this.zipFilePath = zipFile.getAbsolutePath();

        if (deCompressPath == null)
            deCompressPath = zipFile.getParent() + File.separator;

        else if (deCompressPath.charAt(deCompressPath.length() - 1) != '/')
            deCompressPath = deCompressPath + File.separator;
    }

    public void deCompress() throws IOException {
        try(ZipFile zipFile = new ZipFile(zipFilePath)){
            Enumeration<ZipEntry> e = zipFile.getEntries();
            for (ZipEntry entry; e.hasMoreElements(); ) {
		entry = e.nextElement();
                if (!entry.isDirectory()) {
                    String toPath = new StringBuffer(
                            deCompressPath).append(entry.getName()).toString();
                    toPath = toPath.replace("\\", File.separator);
                    deCompressFile(zipFile.getInputStream(entry), toPath);
                }
            }
        } catch (IOException e) {
            throw new IOException("deCompress: ",e);
        }  
   }

    protected void deCompressFile(InputStream input, String toPath)
            throws IOException {
        byte[] byteBuf = new byte[2048];
        String path = new File(toPath).getParent();
        if (!new File(path).exists()) {
            new File(path).mkdirs();
        }
        try(FileOutputStream output = new FileOutputStream(toPath, false)){
            for (int count = 0; (count = input.read(byteBuf, 0, byteBuf.length)) != -1; )
                output.write(byteBuf, 0, count);
        } catch (IOException e) {
            throw new IOException ("deCompressFile: ", e);
        } 
    }
}
