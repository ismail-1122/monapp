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
  containers:
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
"""
        }
    }

    environment {
        IMAGE = "ismailov25/monapp"
        TAG = "build-${env.BUILD_ID}-${sh(script: 'echo $((RANDOM % 10000))', returnStdout: true).trim()}"
        REGISTRY = "docker.io"
    }

    stages {
        stage('Test') {
            steps {
                echo "Pipeline reached the Test stage"
            }
        }

        stage('Build & Push Image') {
            steps {
                container('kaniko') {
                    sh '''
                        /kaniko/executor \
                          --context=dir://\$(pwd) \
                          --dockerfile=Dockerfile \
                          --destination=\$REGISTRY/\$IMAGE:\$TAG \
                          --verbosity=info
                    '''
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