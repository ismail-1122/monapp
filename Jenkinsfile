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
  - name: kaniko
    image: gcr.io/kaniko-project/executor:debug
    command:
    - /busybox/sleep
    args:
    - "99999"
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
        TAG = "dev-${env.BUILD_ID}-${new Random().nextInt(10000)}"
    }

    stages {
        stage('Test') {
            steps {
                echo "Pipeline reached the Test stage on DEV branch"
            }
        }

        stage('Build & Push Image') {
            steps {
                container('kaniko') {
                    sh "ls -la /kaniko/.docker && /busybox/cat /kaniko/.docker/config.json"
                    sh """
                        /kaniko/executor \\
                          --context=dir://${env.WORKSPACE} \\
                          --dockerfile=Dockerfile \\
                          --destination=${env.REGISTRY}/${env.IMAGE}:${env.TAG} \\
                          --verbosity=info
                    """
                }
            }
        }
    }

    post {
        always {
            echo "DEV Pipeline completed"
        }
    }
}