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
        bat 'mvn package -Dmaven.test.skip'
      }
    }

    stage('Static Analysis') {
      steps {
        bat 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.0.2155:sonar'
      }
    }

    stage('Push image to Container Repo') {
      steps {
        bat 'docker build -t pixels-userservice .'
        bat 'docker tag pixels-userservice abhi2104/pixels-userservice:latest'
        bat 'docker push abhi2104/pixels-userservice:latest'
      }
    }

    stage('Deploy') {
      agent {
        node {
          label 'jenkinsagent'
        }

      }
      options { skipDefaultCheckout() }
      steps {
        sh 'docker pull abhi2104/pixels-userservice:latest'
      }
    }

  }
}