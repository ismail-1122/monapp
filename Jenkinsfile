pipeline {
    agent any

    environment {
        IMAGE = "ismail/monapp"
        TAG = "1.0"
    }

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/ismail-1122/monapp', branch: 'master'
            }
        }

        stage('Build Docker Image with Kaniko') {
            steps {
                sh '''
                    mkdir -p /kaniko/.docker
                    echo '{ "auths": { "https://index.docker.io/v1/": { "auth": "'"$(echo -n $DOCKER_USERNAME:$DOCKER_PASSWORD | base64)"'" } } }' > /kaniko/.docker/config.json

                    /kaniko/executor \
                      --dockerfile=Dockerfile \
                      --context=`pwd` \
                      --destination=${IMAGE}:${TAG} \
                      --insecure \
                      --skip-tls-verify
                '''
            }
        }
    }
}
