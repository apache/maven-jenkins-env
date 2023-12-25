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
	  return "\"${osLabel}\""
	}

    def labelForOS(String os) {
        switch (os) {
            case 'linux':
//			     return 'H22'
                return 'ubuntu'
            case 'windows':
//                return 'windows-2016-3'
//                return 'Windows && !jenkins-win-he-de-1'
                return 'Windows'
            default:
                return null
        }
    }
    // https://cwiki.apache.org/confluence/display/INFRA/JDK+Installation+Matrix
    def jdkFromVersion(String os, String version) {
		switch (version) {
			case '7':
				return 'jdk_1.7_latest'
			case '8':
				return 'jdk_1.8_latest'
			case '11':
				return 'jdk_11_latest'
			case '17':
				return 'jdk_17_latest'
            case '20':
                return 'jdk_20_latest'
            case '21':
                return 'jdk_21_latest'
			default:
				return null
		}
    }
    // https://cwiki.apache.org/confluence/display/INFRA/Maven+Installation+Matrix
    def mvnFromVersion(String os, String version) {
        switch (os) {
            case 'linux':
                switch(version) {
//                    case ~/^3\.0\.[x5]$/:
//                        return 'maven_3.0.5'
                    case ~/^3\.1\.[x1]$/:
                        return 'maven_3.1.1'
                    case ~/^3\.2\.[x5]$/:
                        return 'maven_3.2.5'
                    case ~/^3\.3\.[x9]$/:
                        return 'maven_3.3.9'
                    case ~/^3\.5\.[x4]$/:
                        return 'maven_3.5.4'
                    case ~/^3\.6\.[x3]$/:
                        return 'maven_3.6.3'
                    case ~/^3\.8\.x$/:
                        return 'maven_3.8.7'
                    case '3.x.x':
                        return 'maven_3_latest'
                    default:
                        return 'maven_3_latest'
                }
            case 'windows':
                switch(version) {
//                    case ~/^3\.0\.[x5]$/:
//                        return 'maven_3.0.5_windows'
                    case ~/^3\.1\.[x1]$/:
                        return 'maven_3.1.1_windows'
                    case ~/^3\.2\.[x5]$/:
                        return 'maven_3.2.5_windows'
                    case ~/^3\.3\.[x9]$/:
                        return 'maven_3.3.9_windows'
                    case ~/^3\.5\.[x4]$/:
                        return 'maven_3.5.4_windows'
                    case ~/^3\.6\.[x4]$/:
                        return 'maven_3.6.3_windows'
                    case ~/^3\.8\.x$/:
                        return 'maven_3.8.7_windows'
                    case '3.x.x':
                        return 'maven_3_latest'
                    default:
                        return 'maven_3_latest'
                }
            default:
                return null
        }
    }
    def jdkForMaven(String version) {
        switch(version) {
            case '4.x.x':
                return '8'
            default:
                return '7'
        }
    }
    def mavenForJdk(String version) {
        switch(version) {
//            case '6':
//                return '3.0.x'
            default:
                return '3.6.x'
        }
    }
}
