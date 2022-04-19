pipeline() {
    agent any
    stages {
        stage('Checkout') {
            steps {
                dir('tcmpb') {
                    checkout scm
                }
            }
        }
        stage('Build Java') {
            steps {
                dir('tcmpb') {
                    bat "./gradlew build"
                }
            }
        }
        stage('Build Native') {
            steps {
                dir('tcmpb') {
                    dir('build') {
                        bat "cmake .. -G \"NMake Makefiles\""
                        bat "nmake"
                    }
                }
            }
        }
    }
}