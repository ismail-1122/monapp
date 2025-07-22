pipeline {
    agent {
        kubernetes {
            yaml """
apiVersion: v1
kind: Pod
metadata:
  labels:
    jenkins: slave
spec:
  securityContext:
    runAsUser: 0
    runAsGroup: 1000
  containers:
  - name: gradle
    image: gradle:8.7-jdk17-alpine
    command:
    - cat
    tty: true
  - name: kaniko
    image: gcr.io/kaniko-project/executor:debug
    command:
    - /busybox/cat
    tty: true
    volumeMounts:
    - name: kaniko-secret
      mountPath: /kaniko/.docker
  volumes:
  - name: kaniko-secret
    secret:
      secretName: regcred
      items:
       - key: .dockerconfigjson
         path: config.json
"""
        }
    }

    environment {
        IMAGE = "ismailov25/monapp"
        REGISTRY = "docker.io"
        TAG = "build-${env.BUILD_ID}-${new Random().nextInt(10000)}"
    }

    stages {
        stage('Build JAR') {
            steps {
                container('gradle') {
                    sh './gradlew clean bootJar'
                }
            }
        }

        stage('Build & Push Image') {
            steps {
                container('kaniko') {
                    sh 'ls -lh build/libs/' // Debug : vérifier que le .jar existe
                    sh """
                        /kaniko/executor \
                          --context=dir://${env.WORKSPACE} \
                          --dockerfile=Dockerfile \
                          --destination=${env.REGISTRY}/${env.IMAGE}:${env.TAG} \
                          --verbosity=info
                    """
                }
            }
        }
    }

    post {
        always {
            echo "Pipeline completed"
        }
    }
}
