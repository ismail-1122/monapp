pipeline {
    agent any

    stages {
        stage('Debug Info') {
            steps {
                echo "=== PIPELINE STARTED ==="
                echo "Build Number: ${BUILD_NUMBER}"
                echo "Job Name: ${JOB_NAME}"
                echo "Workspace: ${WORKSPACE}"

                script {
                    echo "Current directory contents:"
                    sh 'pwd && ls -la'

                    if (fileExists('Dockerfile')) {
                        echo "✓ Dockerfile found"
                        sh 'echo "Dockerfile content:" && cat Dockerfile'
                    } else {
                        echo "✗ Dockerfile not found"
                    }

                    if (fileExists('Jenkinsfile')) {
                        echo "✓ Jenkinsfile found"
                    } else {
                        echo "✗ Jenkinsfile not found"
                    }
                }
            }
        }

        stage('Test Stage') {
            steps {
                echo "=== TEST STAGE EXECUTING ==="
                sh 'echo "This is a test command"'
                sh 'date'
            }
        }
    }

    post {
        always {
            echo "=== PIPELINE COMPLETED ==="
        }
        success {
            echo "✓ Pipeline succeeded"
        }
        failure {
            echo "✗ Pipeline failed"
        }
    }
}