pipeline {
  agent {
    kubernetes {
      label 'kaniko-agent'
      defaultContainer 'kaniko'
      yaml """
apiVersion: v1
kind: Pod
spec:
  containers:
    - name: kaniko
      image: gcr.io/kaniko-project/executor:latest
      command:
        - cat
      tty: true
      volumeMounts:
        - name: kaniko-secret
          mountPath: /kaniko/.docker
  restartPolicy: Never
  volumes:
    - name: kaniko-secret
      secret:
        secretName: regcred
"""
    }
  }

  environment {
    IMAGE = "ismail/monapp"
    TAG = "v1"
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
              --context=dir://$(pwd) \
              --dockerfile=Dockerfile \
              --destination=$REGISTRY/$IMAGE:$TAG \
              --verbosity=info
          '''
        }
      }
    }
  }
}
