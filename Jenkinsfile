pipeline {

    environment {
        dockerImage = ''
        registry = 'sarayutorion/demo-user-api'
        registryCredential = 'dockerhub'
        k8s_namespace_dev='demo-api-dev'
        k8s_deployment='demo-user-api'
    }

    agent any

    stages {
        stage('Build') {
            steps {
                sh './gradlew clean build'
            }
        }
        stage('Test') {
            steps {
                sh './gradlew check'
            }
        }
        stage('Build image') {
            steps{
                script {
                dockerImage = docker.build registry + ":$BUILD_NUMBER"
                }
            }
        }
        stage('Push Image') {
          steps{
             script {
                docker.withRegistry( '', registryCredential ) {
                dockerImage.push()
              }
            }
          }
        }
        stage('Deployment') {
            steps {
                sh 'kubectl apply -f ./kubernetes/dev/deployment.yml';
                sh 'kubectl set image deployment/${k8s_deployment} ${k8s_deployment}=${registry}:${BUILD_NUMBER} -n ${k8s_namespace_dev}';
            }
        }
    }
}