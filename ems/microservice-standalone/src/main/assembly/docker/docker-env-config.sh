#!/bin/bash

install_sf(){

        sed -i 's/enabled=1/enabled=0/' /etc/yum/pluginconf.d/fastestmirror.conf
        sed -i 's|#baseurl=http://mirror.centos.org/centos|baseurl=http://mirrors.ocf.berkeley.edu/centos|' /etc/yum.repos.d/*.repo
        yum -y update
        yum -y install wget unzip socat java-1.8.0-openjdk-headless
        sed -i 's|#networkaddress.cache.ttl=-1|networkaddress.cache.ttl=10|' /usr/lib/jvm/jre/lib/security/java.security

        mkdir emsdriver

        # get binary zip from nexus
        wget -q -O emsdiver-standalone.zip 'https://nexus.onap.org/service/local/artifact/maven/redirect?r=snapshots&g=org.onap.vfc.nfvo.driver.ems.ems&a=emsdriv
er-standalone&v=LATEST&e=zip' && \
            unzip emsdiver-standalone.zip -d emsdriver && \
            rm -rf emsdiver-standalone.zip

        chmod +x /service/emsdriver/*.sh
        chmod +x /service/emsdriver/docker/*.sh
}

add_user(){

        useradd onap
        yum -y install sudo
        chmod u+x /etc/sudoers
        sed -i '/Same thing without a password/a\onap    ALL=(ALL:ALL) NOPASSWD:ALL' /etc/sudoers
        chmod u-x /etc/sudoers
        chown onap:onap -R /service
}

clean_sf_cache(){

        yum clean all
}

install_sf
wait
add_user
clean_sf_cache
