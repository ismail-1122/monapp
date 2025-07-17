pipeline {
    agent any

    environment {
        IMAGE_NAME = "ismail/monapp"
        IMAGE_TAG  = "1.0"
    }

    stages {
        stage('Cloner le repo') {
            steps {
                git 'https://github.com/ismail-1122/monapp'
            }
        }

        stage('Build Docker image') {
            steps {
                sh 'docker build -t $IMAGE_NAME:$IMAGE_TAG .'
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh '''
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push $IMAGE_NAME:$IMAGE_TAG
                    '''
                }
            }
        }

        stage('Déployer sur Kubernetes (Minikube)') {
            steps {
                sh '''
                    kubectl delete deployment monapp-deployment --ignore-not-found
                    kubectl apply -f k8s/
                '''
            }
        }
    }
}
