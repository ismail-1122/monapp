pipeline {
    agent {
        kubernetes {
            yaml """
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: kaniko
    image: gcr.io/kaniko-project/executor:latest
    command:
    - /busybox/sh
    args:
    - -c
    - cat
    tty: true
    volumeMounts:
    - name: kaniko-secret
      mountPath: /kaniko/.docker
  volumes:
  - name: kaniko-secret
    secret:
      secretName: regcred
"""
        }
    }

    environment {
        IMAGE_NAME = "docker.io/ismail/monapp:\${BRANCH_NAME}"
        DOCKERFILE_PATH = "./Dockerfile"
        CONTEXT = "."
    }

    stages {
        stage('Build JAR') {
            steps {
                // Ce build se fait hors du conteneur kaniko (par défaut)
                sh 'chmod +x ./gradlew && ./gradlew build -x test'
            }
        }

        stage('Build and Push with Kaniko') {
            steps {
                container('kaniko') {
                    sh '''
                    /kaniko/executor \
                      --dockerfile=$DOCKERFILE_PATH \
                      --destination=$IMAGE_NAME \
                      --context=dir://$(pwd) \
                      --cleanup
                    '''
                }
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
