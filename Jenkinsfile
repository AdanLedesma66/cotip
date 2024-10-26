pipeline {
    agent any

    tools {
        jdk 'OpenJDK21'
    }

    stages {
        stage('Checkout SCM') {
            steps {
                git url: 'https://github.com/AdanLedesma66/cotip.git',
                    branch: 'main',
                    credentialsId: ''
            }
        }

        stage('Build') {
            steps {
                echo "Building the project"
                bat 'mvn clean install'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    dockerImage = docker.build("cotip:${env.BUILD_NUMBER}")
                }
            }
        }

        stage('Deploy Docker') {
            steps {
                script {
                    echo "Deploying Docker Image"

                    bat "docker stop cotip || exit 0"
                    bat "docker rm cotip || exit 0"
                    bat "docker run --name cotip -d -p 8081:8081 cotip:${env.BUILD_NUMBER}"
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline Finished'
        }
    }
}
