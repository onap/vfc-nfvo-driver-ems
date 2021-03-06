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
package org.onap.vfc.nfvo.emsdriver;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.jetty.HttpConnectorFactory;
import io.dropwizard.server.SimpleServerFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.log4j.PropertyConfigurator;
import org.onap.vfc.nfvo.emsdriver.commons.constant.Constant;
import org.onap.vfc.nfvo.emsdriver.commons.utils.DriverThread;
import org.onap.vfc.nfvo.emsdriver.northbound.service.CommandResource;
import org.onap.vfc.nfvo.emsdriver.serviceregister.MsbConfiguration;
import org.onap.vfc.nfvo.emsdriver.serviceregister.MsbRestServiceProxy;
import org.onap.vfc.nfvo.emsdriver.serviceregister.model.MsbRegisterVo;
import org.onap.vfc.nfvo.emsdriver.serviceregister.model.ServiceNodeVo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class EmsDriverApplication extends Application<EmsDriverConfiguration> {

    protected static final Logger log = LoggerFactory.getLogger(EmsDriverApplication.class);
    public static final String LOGCONFIG_PROPERTIES_LOCATION = Constant.SYS_CFG + "log4j.properties";
    private ApplicationContext context = null;

    public static void main(String[] args) throws Exception {
    	logConfig();
        log.info("EmsDriverApplication start");
        new EmsDriverApplication().run(args);
        log.info("EmsDriverApplication start sucess");
    }

    @Override
    protected void bootstrapLogging() {
    }
    
    public static void logConfig(){
    	PropertyConfigurator.configure(LOGCONFIG_PROPERTIES_LOCATION);
    }
    
    @Override
    public String getName() {
        return "ems-driver";
    }

    @Override
    public void initialize(Bootstrap<EmsDriverConfiguration> bootstrap) {
        // nothing to do yet
        context = new FileSystemXmlApplicationContext("file:" + Constant.SYS_CFG + "spring.xml");
        bootstrap.addBundle(new AssetsBundle("/api-doc", "/api-doc", "index.html", "api-doc"));
    }

    @Override
    public void run(EmsDriverConfiguration configuration, Environment environment) {
        // register CommandResource
        environment.jersey().register(new CommandResource());
        MsbConfiguration.setMsbAddress(configuration.getMsbAddress());
        //MSB register
        String registerFlag = configuration.getAutoServiceRegister();
        if ("true".equalsIgnoreCase(registerFlag)) {
            this.msbRegisteEmsDriverService(configuration);
        }
        //Start workThread
        this.startThread();
        initSwaggerConfig(environment, configuration);
    }

    private void startThread() {
        String[] allThreadName = context.getBeanNamesForType(DriverThread.class);
        log.info("worker num :" + allThreadName.length);
        for (String threadName : allThreadName) {
            DriverThread thread = (DriverThread) context.getBean(threadName);
            if (thread == null) {
                log.error(threadName + "Thread start error,system exit");
                System.exit(1);
            } else {
                thread.setName(threadName);
                thread.start();
            }

        }
    }

    // init swagger
    private void initSwaggerConfig(Environment environment, EmsDriverConfiguration configuration) {
        environment.jersey().register(new ApiListingResource());
        environment.getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

        BeanConfig config = new BeanConfig();
        config.setTitle(" Console Service rest API");
        config.setVersion("1.0.0");
        config.setResourcePackage("org.onap.vfc.nfvo.emsdriver.northbound.service");
        //swagger rest api basepath
        SimpleServerFactory simpleServerFactory = (SimpleServerFactory) configuration.getServerFactory();
        String basePath = simpleServerFactory.getApplicationContextPath();
        String rootPath = simpleServerFactory.getJerseyRootPath().get();
        rootPath = rootPath.substring(0, rootPath.indexOf("/*"));
        basePath ="/".equals(basePath) ? rootPath : (new StringBuilder()).append(basePath).append(rootPath).toString();
        config.setBasePath(basePath);
        config.setScan(true);
    }

    private void msbRegisteEmsDriverService(EmsDriverConfiguration configuration) {
        SimpleServerFactory simpleServerFactory = (SimpleServerFactory) configuration.getServerFactory();
        HttpConnectorFactory connector = (HttpConnectorFactory) simpleServerFactory.getConnector();
        MsbRegisterVo registerVo = new MsbRegisterVo();
        ServiceNodeVo serviceNode = new ServiceNodeVo();
/*        String ip = "";
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.error("Unable to get host ip: ", e);
        }
        if ("".equals(ip)) {
            ip = connector.getBindHost();
        }*/
        serviceNode.setIp(configuration.getServiceIp());
        serviceNode.setPort(String.valueOf(connector.getPort()));
        serviceNode.setTtl(0);

        List<ServiceNodeVo> nodeList = new ArrayList<>();
        nodeList.add(serviceNode);
        registerVo.setServiceName("emsdriver");
        registerVo.setUrl("/api/emsdriver/v1");
        registerVo.setNodes(nodeList);

        MsbRestServiceProxy.registerService(registerVo);
        log.info("register vfc-emsdriver service to msb finished.");

    }


}
