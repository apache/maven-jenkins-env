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
    def nodeSelection(String osLabel) {
    // Problem with Node H31
	  return "\"${osLabel}\" && !H31"
	}

    def labelForOS(String os) {
        switch (os) {
            case 'linux':
                return 'ubuntu'
            case 'windows':
//			      return 'windows-2016-1'
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
                    case '11':
                        return 'JDK 11 (latest)'
                    case '12':
                        return 'JDK 12 (latest)'
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
                        return 'JDK 10 (latest)'
                    case '11':
                        return 'JDK 11 (latest)'
                    case '12':
                        return 'JDK 12 (latest)'
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
                    case ~/^3\.0\.[x5]$/:
                        return 'Maven 3.0.5'
                    case ~/^3\.2\.[x5]$/:
                        return 'Maven 3.2.5'
                    case ~/^3\.3\.[x9]$/:
                        return 'Maven 3.3.9'
                    case ~/^3\.5\.[x4]$/:
                    case '3.x.x':
                        return 'Maven 3.5.4'
                    default:
                        return 'Maven 3.5.4'
                }
            case 'windows':
                switch(version) {
                    case ~/^3\.0\.[x5]$/:
                        return 'Maven 3.0.5 (Windows)'
                    case ~/^3\.2\.[x5]$/:
                        return 'Maven 3.2.5 (Windows)'
                    case ~/^3\.3\.[x9]$/:
                        return 'Maven 3.3.9 (Windows)'
                    case ~/^3\.5\.[x4]$/:
                    case '3.x.x':
                        return 'Maven 3.5.2 (Windows)'
                    default:
                        return 'Maven 3.5.2 (Windows)'
                }
            default:
                return null
        }
    }
    def jdkForMaven(String version) {
        switch(version) {
            case ~/^3\.0\..+/:
                return '6'
            case ~/^3\.[235]\..+/:
            case '3.x.x':
                return '7'
            default:
                return null
        }
    }
    def mavenForJdk(String version) {
        switch(version) {
            case '6':
                return '3.0.x'
            default:
                return '3.5.x'
        }
    }
}
