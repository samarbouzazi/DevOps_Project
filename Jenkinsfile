pipeline {
  agent any

  environment {
    
    DOCKERHUB_USERNAME = "samarbouzezi"
    BACK_TAG = "${DOCKERHUB_USERNAME}/devops_project:spring"
  }


  stages {
   stage('Checkout') {
            steps {
                checkout scm
            }
        }
    
    stage('Github') {
      steps {
        git branch: branchName,
          url: 'https://github.com/samarbouzazi/DevOps_Project.git',
          credentialsId: 'githubid'
      }
    }

    stage('MVN BUILD') {
      steps {
        sh 'mvn clean install'
        echo 'Build stage done'
      }
    }
      
    stage('MVN COMPILE') {
      steps {
        sh 'mvn compile'
        echo 'Compile stage done'
      }
    }
  }
}
