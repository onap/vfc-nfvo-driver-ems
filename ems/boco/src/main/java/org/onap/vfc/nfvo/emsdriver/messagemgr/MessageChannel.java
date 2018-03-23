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
package org.onap.vfc.nfvo.emsdriver.messagemgr;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.io.IOException;


public class MessageChannel {

    private BlockingQueue<Object> queue = null;

    public MessageChannel(int size) {
        if (size > 0) {
            queue = new LinkedBlockingQueue<>(size);
        } else {
            queue = new LinkedBlockingQueue<>();
        }
    }

    public MessageChannel() {
        queue = new LinkedBlockingQueue<>();
    }

    public void put(Object msg) throws IOException {
        while (!queue.offer(msg)) {
            queue.poll();
        }
    }

    public Object get() throws InterruptedException {
        return queue.take();
    }

    public Object poll() throws InterruptedException {
        return queue.poll(100, TimeUnit.MILLISECONDS);
    }

    public int size() {
        return queue.size();
    }

    public void clear() {
        queue.clear();
    }

    public BlockingQueue<Object> getQueue() {
        return queue;
    }


}
