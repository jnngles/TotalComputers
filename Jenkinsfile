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
    }
}