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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

public final class PathProperties implements Configuration {

    private final File file;

    public PathProperties(String fileName) {
        this.file = new File(fileName);
    }

    public PathProperties(File file) {
        this.file = file;
    }

    @Override
    public Map<String, String> deepCopyAsUnmodifiableMap() throws IOException {
        Properties properties = new Properties();
        try (final InputStream in = Files.newInputStream(file.toPath())) {
            properties.load(in);
        }
        return Collections
                .unmodifiableMap(
                        properties
                                .entrySet()
                                .stream()
                                .collect(Collectors.toMap(k -> k.getKey().toString(), k -> k.getValue().toString()))
                );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        PathProperties that = (PathProperties) o;
        return file.equals(that.file);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(file);
    }
}
