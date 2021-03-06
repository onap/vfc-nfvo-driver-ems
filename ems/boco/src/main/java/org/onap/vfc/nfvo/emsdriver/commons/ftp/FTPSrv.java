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
package org.onap.vfc.nfvo.emsdriver.commons.ftp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.net.ftp.*;
import org.onap.vfc.nfvo.emsdriver.commons.utils.StringUtil;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.TimeZone;


public class FTPSrv implements FTPInterface {
    private Logger log = LoggerFactory.getLogger(FTPSrv.class);
    private FTPClient ftpClient = null;


    /**
     * login FTP
     *
     * @param host
     * @param port
     * @param user
     * @param pwd
     * @param encode
     * @param timeout
     * @throws Exception
     */
    public void login(String host, int port, String user, String pwd, String encode, boolean isPassiveMode, int timeout) throws IOException {
        ftpClient = new FTPClient();

        FTPClientConfig ftpClientConfig = new FTPClientConfig();
        ftpClientConfig.setServerTimeZoneId(TimeZone.getDefault().getID());
        this.ftpClient.setControlEncoding("GBK");
        this.ftpClient.configure(ftpClientConfig);

        if (encode != null && encode.length() > 0) {
            ftpClient.setControlEncoding(encode);
        }

        ftpClient.connect(host, port);
        int reply = this.ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            this.ftpClient.disconnect();
            return;
        }

        if (!ftpClient.login(user, pwd)) {
            throw new IOException("login[" + host + "],port[" + port + "] fail, please check user and password");
        }
        if (isPassiveMode) {
            ftpClient.enterLocalPassiveMode();
        } else {
            ftpClient.enterLocalActiveMode();
        }
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        this.ftpClient.setBufferSize(1024 * 2);
        this.ftpClient.setDataTimeout(3 * 60 * 1000);
        try {
            this.ftpClient.setSoTimeout(timeout);
        } catch (Exception e) {
            log.error(" StackTrace :", e);
        }
    }


    /**
     * logout
     */
    public void logout() {
        if (ftpClient != null) {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (Exception e) {
		    log.error(" StackTrace :", e);
            }
            ftpClient = null;
        }
    }


    public boolean chdir(String dir) {
        boolean sucess = false;
        try {
            if (ftpClient.changeWorkingDirectory(dir)) {
                sucess = true;
            } else {
                sucess = false;
            }
        } catch (IOException e) {
            log.error("chdir dir =" + dir + " is error" + StringUtil.getStackTrace(e));
            sucess = false;
        }

        return sucess;
    }


    public boolean downloadFile(String remoteFile, String localFile) {
        boolean sucess = false;
        try (
            BufferedOutputStream toLfileOutput = new BufferedOutputStream(new FileOutputStream(localFile))){
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            if (ftpClient.retrieveFile(remoteFile, toLfileOutput)) {
                sucess = true;
            } else {
                sucess = false;
            }
        } catch (Exception ioe) {
            sucess = false;
            log.error("downloadFile remoteFile =" + remoteFile + " is fail ", ioe);
        } 

        return sucess;
    }


    public RemoteFile[] list() {
        AFtpRemoteFile[] ftpRemoteFiles = null;
        String currdir = null;
        try {
            currdir = ftpClient.printWorkingDirectory();
            if (!currdir.endsWith("/")) {
                currdir = currdir + "/";
            }
            FTPFile[] rfileList = null;
            rfileList = ftpClient.listFiles(currdir);
            ftpRemoteFiles = new AFtpRemoteFile[rfileList.length];
            for (int i = 0; i < rfileList.length; i++) {
                ftpRemoteFiles[i] = new AFtpRemoteFile(rfileList[i], ftpClient, currdir);
            }
        } catch (IOException e) {
            log.error("Ftp list currdir = " + currdir + " is fail " + StringUtil.getStackTrace(e));
        }
        return ftpRemoteFiles;
    }


    @Override
    public String pwd() throws IOException {
        return ftpClient.printWorkingDirectory();
    }

}

