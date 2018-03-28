#!/usr/bin/env groovy

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

class jenkinsEnv implements Serializable {
    def labelForOS(String os) {
        switch (os) {
            case 'linux':
                return 'ubuntu'
            case 'windows':
                return 'Windows'
            default:
                return null
        }
    }
    def jdkFromVersion(String os, String version) {
        switch (os) {
            case 'linux':
                switch (version) {
                    case '7':
                        return 'JDK 1.7 (latest)'
                    case '8':
                        return 'JDK 1.8 (latest)'
                    case '9':
                        return 'JDK 1.9 (latest)'
                    case '10':
                        return 'JDK 10 (latest)'
                    default:
                        return null
                }
            case 'windows':
                switch(version) {
                    case '7':
                        return 'JDK 1.7 (latest)'
                    case '8':
                        return 'JDK 1.8 (latest)'
                    case '9':
                        return 'JDK 1.9 (latest)'
                    case '10':
                        return 'JDK 10 b46 (Windows Only)'
                    default:
                        return null
                }
            default:
                return null
        }
    }
    def mvnFromVersion(String os, String version) {
        switch (os) {
            case 'linux':
                switch(version) {
                    case '3.0.x':
                        return 'Maven 3.0.5'
                    case '3.0.5':
                        return 'Maven 3.0.5'
                    case '3.2.x':
                        return 'Maven 3.2.5'
                    case '3.2.5':
                        return 'Maven 3.2.5'
                    case '3.3.x':
                        return 'Maven 3.3.9'
                    case '3.3.9':
                        return 'Maven 3.3.9'
                    case '3.x.x':
                        return 'Maven 3.5.2'
                    case '3.5.x':
                        return 'Maven 3.5.2'
                    case '3.5.2':
                        return 'Maven 3.5.2'
                    default:
                        return 'Maven 3.5.2'
                }
            case 'windows':
                switch(version) {
                    case '3.0.x':
                        return 'Maven 3.0.5 (Windows)'
                    case '3.0.5':
                        return 'Maven 3.0.5 (Windows)'
                    case '3.2.x':
                        return 'Maven 3.2.5 (Windows)'
                    case '3.2.5':
                        return 'Maven 3.2.5 (Windows)'
                    case '3.3.x':
                        return 'Maven 3.3.9 (Windows)'
                    case '3.3.9':
                        return 'Maven 3.3.9 (Windows)'
                    case '3.x.x':
                        return 'Maven 3.5.0 (Windows)'
                    case '3.5.x':
                        return 'Maven 3.5.0 (Windows)'
                    case '3.5.2':
                        return null
                    default:
                        return 'Maven 3.5.0 (Windows)'
                }
            default:
                return null
        }
    }
}
