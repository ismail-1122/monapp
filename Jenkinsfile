pipeline {
    agent any  // This will use any available agent (not necessarily Kubernetes)

    environment {
        IMAGE_NAME = "ismailov25/monapp:${env.BRANCH_NAME}"
    }

    stages {
        stage('Set gradlew executable') {
            steps {
                sh 'chmod +x ./gradlew'
            }
        }

        stage('Build JAR') {
            steps {
                sh './gradlew build -x test'
            }
        }

        stage('Docker Build and Push') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', 'dockerhub-credentials') {
                        def customImage = docker.build("${IMAGE_NAME}")
                        customImage.push()
                    }
                }
            }
        }

        stage('Deploy to Minikube') {
            steps {
                // Ensure kubectl is configured on the Jenkins agent
                sh 'kubectl apply -f k8s/deployment.yaml'
                sh 'kubectl apply -f k8s/service.yaml'
            }
        }
    }
}