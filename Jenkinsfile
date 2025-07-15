pipeline {
    agent {
        docker {
            image 'gradle:8.3-jdk17' // ✅ Image Docker avec Gradle + Java 17
            args '-v /var/run/docker.sock:/var/run/docker.sock'
        }
    }

    environment {
        IMAGE_NAME = "ismail/monapp:${env.BRANCH_NAME}"
    }

    stages {
        stage('Build') {
            steps {
                sh './gradlew build -x test'
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t $IMAGE_NAME .'
            }
        }

        stage('Deploy to Minikube') {
            steps {
                sh 'kubectl apply -f k8s/deployment.yaml'
                sh 'kubectl apply -f k8s/service.yaml'
            }
        }
    }
}
