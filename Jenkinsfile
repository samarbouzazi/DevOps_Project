def getGitBranchName() {
    return scm.branches[0].name
}

def branchName
def targetBranch

pipeline {
    agent any

    environment {
        DOCKERHUB_USERNAME = "samarbouzezi"
        Front_TAG = "${DOCKERHUB_USERNAME}/devops_project:angular"
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

      stage('Clean') {
            steps {
                sh 'rm -rf node_modules'
                sh 'npm cache clean --force'
                sh'npm cache clean -f'
            }
        }

        stage('Install') {
            steps {
              sh 'npm install @popperjs/core --legacy-peer-deps'
                sh 'npm install --legacy-peer-deps'
            }
        }

        stage('Build') {
            steps {
                
                sh 'ng build --configuration=production --output-path=dist'
              //sh 'ng serve --watch --proxy-config proxy.conf.json'
                echo 'Build stage done'
            }
        }

    stage('Docker Login') {
      steps {
        withCredentials([usernamePassword(credentialsId: 'dockercred', usernameVariable: 'DOCKERHUB_USERNAME', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
          sh 'docker login -u $DOCKERHUB_USERNAME -p $DOCKERHUB_PASSWORD'
        }
      }
    }

      stage('Build Docker') {
      steps {
            sh "docker build -t $FRONT_TAG ."
      }
        }
      

    //  stage('Docker Push') {
    //   steps {
    //     sh "docker push ${FRONT_TAG}"
    //   }
    // }


    // stage('Remove Containers') {
    //   steps {
    //     sh '''
    //     container_ids=$(docker ps -q --filter "publish=4200/tcp")
    //     if [ -n "$container_ids" ]; then
    //       echo "Stopping and removing containers..."
    //       docker stop $container_ids
    //       docker rm $container_ids
    //     else
    //       echo "No containers found using port 4200."
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
