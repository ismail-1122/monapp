pipeline {
    agent any  // Agent standard avec Docker

    environment {
        REGISTRY = "docker.io"
        REPO = "ismailov25/monapp"
        TAG = "${env.GIT_BRANCH ?: 'latest'}"  // Utilise le nom de la branche ou 'latest'
        KANIKO_IMAGE = "gcr.io/kaniko-project/executor:v1.9.1"  // Version fixe
        DOCKER_CONFIG = credentials('dockerhub-creds')  // Référence aux credentials
    }

    stages {
        // Étape 1 - Build de l'application
        stage('Build JAR') {
            steps {
                sh '''
                    chmod +x ./gradlew
                    ./gradlew build -x test
                    mkdir -p docker-context
                    cp build/libs/*.jar docker-context/app.jar
                    cp Dockerfile docker-context/
                '''
            }
        }

        // Étape 2 - Build/Push avec Kaniko
        stage('Build & Push Image') {
            steps {
                script {
                    // Exécute Kaniko dans un conteneur Docker
                    docker.image(KANIKO_IMAGE).inside("--entrypoint='' -v ${DOCKER_CONFIG}:/kaniko/.docker") {
                        sh """
                            /kaniko/executor \
                                --context ${WORKSPACE}/docker-context \
                                --dockerfile ${WORKSPACE}/docker-context/Dockerfile \
                                --destination=${REGISTRY}/${REPO}:${TAG} \
                                --cache=true \
                                --verbosity=info
                        """
                    }
                }
            }
        }

        // Étape 3 - Déploiement sur Minikube
        stage('Deploy to Minikube') {
            steps {
                sh """
                    kubectl set image deployment/myapp myapp=${REGISTRY}/${REPO}:${TAG} --record
                    kubectl rollout status deployment/myapp
                """
            }
        }
    }

    post {
        success {
            echo "✅ Image poussée avec succès : ${REGISTRY}/${REPO}:${TAG}"
        }
        failure {
            echo "❌ Échec du pipeline. Vérifiez les logs."
        }
    }
}