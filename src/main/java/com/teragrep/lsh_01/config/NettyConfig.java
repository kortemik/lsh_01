/*
  logstash-http-input to syslog bridge
  Copyright 2024 Suomen Kanuuna Oy

  Derivative Work of Elasticsearch
  Copyright 2012-2015 Elasticsearch

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/
package com.teragrep.lsh_01.config;

public class NettyConfig implements Validateable {

    public final String listenAddress;
    public final int listenPort;
    public final int threads;
    public final int maxPendingRequests;
    public final int maxContentLength;

    public NettyConfig() {
        PropertiesReaderUtilityClass propertiesReader = new PropertiesReaderUtilityClass(
                System.getProperty("properties.file", "etc/config.properties")
        );
        listenAddress = propertiesReader.getStringProperty("server.listenAddress");
        listenPort = propertiesReader.getIntProperty("server.listenPort");
        threads = propertiesReader.getIntProperty("server.threads");
        maxPendingRequests = propertiesReader.getIntProperty("server.maxPendingRequests");
        maxContentLength = propertiesReader.getIntProperty("server.maxContentLength");
    }

    @Override
    public void validate() {

    }

    @Override
    public String toString() {
        return "NettyConfig{" + "listenAddress='" + listenAddress + '\'' + ", listenPort=" + listenPort + ", threads="
                + threads + ", maxPendingRequests=" + maxPendingRequests + ", maxContentLength=" + maxContentLength
                + '}';
    }
}
