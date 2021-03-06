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

/**
 * @author boco
 */
public class CollectVo {

    private String emsName;

    private String type;

    private String crontab;

    private String ip;

    private String port;

    private String user;

    private String password;

    private String remotepath;

    private String match;

    private String passive;

    private String ftptype;

    private String granularity;

    private boolean iscollect = false;

    private String readTimeout;


    /**
     * @return the iscollect
     */
    public boolean isIscollect() {
        return iscollect;
    }

    /**
     * @param iscollect the iscollect to set
     */
    public void setIscollect(boolean iscollect) {
        this.iscollect = iscollect;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the crontab
     */
    public String getCrontab() {
        return crontab;
    }

    /**
     * @param crontab the crontab to set
     */
    public void setCrontab(String crontab) {
        this.crontab = crontab;
    }

    /**
     * @return the iP
     */
    public String getIP() {
        return ip;
    }

    /**
     * @param ip the iP to set
     */
    public void setIP(String inIP) {
        ip = inIP;
    }

    /**
     * @return the port
     */
    public String getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(String port) {
        this.port = port;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the remotepath
     */
    public String getRemotepath() {
        return remotepath;
    }

    /**
     * @param remotepath the remotepath to set
     */
    public void setRemotepath(String remotepath) {
        this.remotepath = remotepath;
    }

    /**
     * @return the match
     */
    public String getMatch() {
        return match;
    }

    /**
     * @param match the match to set
     */
    public void setMatch(String match) {
        this.match = match;
    }

    /**
     * @return the passive
     */
    public String getPassive() {
        return passive;
    }

    /**
     * @param passive the passive to set
     */
    public void setPassive(String passive) {
        this.passive = passive;
    }

    /**
     * @return the ftptype
     */
    public String getFtptype() {
        return ftptype;
    }

    /**
     * @param ftptype the ftptype to set
     */
    public void setFtptype(String ftptype) {
        this.ftptype = ftptype;
    }

    /**
     * @return the granularity
     */
    public String getGranularity() {
        return granularity;
    }

    /**
     * @param granularity the granularity to set
     */
    public void setGranularity(String granularity) {
        this.granularity = granularity;
    }


    @Override
    public String toString() {
        return "CollectVo [emsName=" + emsName + ", type=" + type
                + ", crontab=" + crontab + ", IP=" + ip + ", port=" + port
                + ", user=" + user + ", password=" + password + ", remotepath="
                + remotepath + ", match=" + match + ", passive=" + passive
                + ", ftptype=" + ftptype + ", granularity=" + granularity
                + ", iscollect=" + iscollect + ", readTimeout=" + readTimeout
                + "]";
    }

    /**
     * @return the emsName
     */
    public String getEmsName() {
        return emsName;
    }

    /**
     * @param emsName the emsName to set
     */
    public void setEmsName(String emsName) {
        this.emsName = emsName;
    }

    /**
     * @return the readTimeout
     */
    public String getReadTimeout() {
        return readTimeout;
    }

    /**
     * @param read_timeout the read_timeout to set
     */
    public void setReadTimeout(String inReadTimeout) {
        this.readTimeout = inReadTimeout;
    }


}
