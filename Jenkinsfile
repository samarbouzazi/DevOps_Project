def getGitBranchName() {
  return scm.branches[0].name
}

def branchName
def targetBranch

pipeline {
  agent any

  environment {
    
    DOCKERHUB_USERNAME = "samarbouzezi"
    BACK_TAG = "${DOCKERHUB_USERNAME}/devops_project:spring"
  }

  parameters {
    string(name: 'BRANCH_NAME', defaultValue: "${scm.branches[0].name}", description: 'Git branch name')
    string(name: 'CHANGE_ID', defaultValue: '', description: 'Git change ID for merge requests')
    string(name: 'CHANGE_TARGET', defaultValue: '', description: 'Git change ID for the target merge requests')
  }




  stages {
  stage('branch name') {
      steps {
        script {
          branchName = params.BRANCH_NAME
          echo "Current branch name: ${branchName}"
        }
      }
    }

    stage('target branch') {
      steps {
        script {
          targetBranch = branchName
          echo "Target branch name: ${targetBranch}"
        }
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
    stage('SonarQube Analysis') {
  steps {
    withSonarQubeEnv('sonar-scanner') {
      sh 'mvn sonar:sonar'
    }
  }
}

        stage('Build Docker') {
      steps {
            sh "docker build -t $BACK_TAG ."
      }
        }
    
    
    stage('Docker Login') {
      steps {
        withCredentials([usernamePassword(credentialsId: 'dockercred', usernameVariable: 'DOCKERHUB_USERNAME', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
          sh 'docker login -u $DOCKERHUB_USERNAME -p $DOCKERHUB_PASSWORD'
        }
      }
    }

    //  stage('Docker Push') {
    //   steps {
    //     sh "docker push ${BACK_TAG}"
    //   }
    // }


    // stage('Remove Containers') {
    //   steps {
    //     sh '''
    //     container_ids=$(docker ps -q --filter "publish=8082/tcp")
    //     if [ -n "$container_ids" ]; then
    //       echo "Stopping and removing containers..."
    //       docker stop $container_ids
    //       docker rm $container_ids
    //     else
    //       echo "No containers found using port 8082."
    //     fi
    //     '''
    //   }
    // }

      stage('docker compose') {
      steps {
            sh "docker-compose up "
      }
        }

  }
}
