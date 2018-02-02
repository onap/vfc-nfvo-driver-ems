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
package org.onap.vfc.nfvo.emsdriver.collector.alarm;

import org.onap.vfc.nfvo.emsdriver.commons.constant.Constant;

import java.io.UnsupportedEncodingException;


public class Msg {

    public final static String reqLoginAlarm = "reqLoginAlarm;user=%s;key=%s;type=%s";
    public final static String reqHeartBeat = "reqHeartBeat;reqId=%s";
    public final static String disconnectMsg = "closeConnAlarm";
    public final static String syncAlarmMessageMsg = "reqSyncAlarmMsg;reqID=%s;alarmSeq=%s";
    public final static String syncAlarmMessageByalarmSeq = "reqSyncAlarmFile;reqID=%s;alarmSeq=%s;syncSource=1";
    public final static String syncActiveAlarmFileMsg = "reqSyncAlarmFile;reqID=%s;startTime=%s;endTime=%s;syncSource=0";
    public final static String syncAlarmFileMsg = "reqSyncAlarmFile;reqID=%s;startTime=%s;endTime=%s;syncSource=1";
    public static short StartSign = (short) 0xffff;
    private MsgType msgType;
    private int timeStamp = 0;
    private int lenOfBody = 0;
    private String body = null;

    public Msg() {
    }

    public Msg(String body, MsgType msgType) {
        this.body = body;
        this.setMsgType(msgType);
    }

    public static int creatMsgTimeStamp() {
        return (int) System.currentTimeMillis() / 1000;
    }

    public void newBodyfromBytes(byte b[]) throws UnsupportedEncodingException {
        this.body = new String(b, Constant.ENCODING_UTF8);
    }

    public int getBodyLenNow() {
        return getBody().getBytes().length;
    }

    public int getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getLenOfBody() {
        return lenOfBody;
    }

    public void setLenOfBody(int lenOfBody) {
        this.lenOfBody = lenOfBody;
    }

    public byte[] getBodyBytes() throws UnsupportedEncodingException {
        return getBody().getBytes(Constant.ENCODING_UTF8);
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public MsgType getMsgType() {
        return msgType;
    }

    public void setMsgType(MsgType msgType) {
        this.msgType = msgType;
    }

    public String toString(boolean isRead) {
        StringBuilder sb = new StringBuilder();
        sb.append("StartSign[").append(StartSign).append("]msgType[").append(msgType.value).append("]timeStamp[");
        if (isRead) {
            sb.append(timeStamp).append("]lenOfBody[").append(lenOfBody);
        } else {
            sb.append(creatMsgTimeStamp()).append("]lenOfBody[").append(getBodyLenNow());
        }
        sb.append("]body[").append(body).append("]");
        return sb.toString();
    }
}
