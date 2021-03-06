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
package org.onap.vfc.nfvo.emsdriver.commons.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class CollectMsgTest {

    private CollectMsg collectMsg;

    @Before
    public void setUp() {
        collectMsg = new CollectMsg();
    }

    @Test
    public void testCollectMsg() {
        collectMsg.setEmsName("emsName");
        collectMsg.setId(100L);
        collectMsg.setType("PM");
        assertNotNull(collectMsg.toString());
    }

}
