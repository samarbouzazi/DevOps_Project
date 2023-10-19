pipeline { 
     agent any 
  
     stages { 
         stage('Github') { 
             steps { 
                 script { 
                    
                     git branch: branchName, 
                         url: 'https://github.com/samarbouzazi/DevOps_Project', 
                         credentialsId: 'githubid' 
                 } 
             } 
         } 
  
       stage('Clean') { 
             steps { 
                 sh 'rm -rf node_modules' 
                 sh 'npm cache clean --force' 
             } 
         } 
  
         stage('Install') { 
             steps { 
                 sh 'npm install --legacy-peer-deps' 
             } 
         } 
     }
}
