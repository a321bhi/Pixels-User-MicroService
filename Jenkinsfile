pipeline {
  agent any
  stages {
    stage('Initialize') {
      steps {
        echo 'Initializing'
        bat 'mvn clean'
      }
    }

    stage('Build') {
      steps {
        bat 'mvn package'
      }
    }

    stage('Static Analysis') {
      steps {
        bat 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.0.2155:sonar'
      }
    }

    stage('Deploy') {
      steps {
        bat 'docker build -t pixels-userservice .'
        bat 'docker tag pixels-userservice abhi2104/pixels-userservice'
        bat 'docker push abhi2104/pixels-userservice'
      }
    }

    stage('') {
      agent {
        node {
          label 'jenkinsagent'
        }

      }
      steps {
        sh 'docker pull 070761564037.dkr.ecr.us-east-2.amazonaws.com/pixels'
      }
    }

  }
}