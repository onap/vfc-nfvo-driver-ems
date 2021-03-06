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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class ZipTest {

    private String file = "./test.txt";
    //private String file1 = "./";
    private String tofile1 = "./test.zip";
    private Zip zip = null;
    //private Zip zip1 = null;

    @Before
    public void setUp() throws IOException {
        new File(file).createNewFile();
        zip = new Zip(file, tofile1);
        //zip1 = new Zip(file1, null);
    }
    
    @Test
    public void compress() throws IOException {
    	//zip1.compress();
        zip.compress();
        
        assertTrue(tofile1.endsWith(".zip"));
    }


    @After
    public void setDown() throws IOException {
        new File(file).delete();
        new File(tofile1).delete();

    }
}
