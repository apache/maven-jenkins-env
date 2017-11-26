#!/usr/bin/env groovy

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
                        return 'Maven 3.5.2'
                    case '3.5.x':
                        return 'Maven 3.5.2'
                    case '3.5.2':
                        return 'Maven 3.5.2'
                    default:
                        return 'Maven 3.5.2'
                }
            default:
                return null
        }
    }
}