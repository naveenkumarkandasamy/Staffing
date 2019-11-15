pipeline {
	
   environment {
    registry = "payalmantri10/staffing"
    registryCredential = 'payalmantri10'
  }
	
	
   agent  any;
   tools {
      maven 'maven 3'
      jdk 'java 8'
   }
	
   stages {
	   
	   
  
        stage('Build') {
           steps {
		
			sh "mvn -B -DskipTests clean install";
		}
	    
         }

	   
	   
	   
        stage('Test') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml';
                }
            }
         
         }
	   
	     stage('Building image') {
      steps{
        script {
		
          docker.build registry + ":latest"
		}
        
      }
    }
	   
	
   stage('Docker Push') {
  
      steps {
        withCredentials([usernamePassword(credentialsId: 'dockerHub', passwordVariable: 'dockerHubPassword', usernameVariable: 'dockerHubUser')]) {
          
	  sh "docker login -u ${env.dockerHubUser} -p ${env.dockerHubPassword}"
          sh 'docker push payalmantri10/staffing:latest'
        }
      }
	   
	   
        }
	   
	   stage('Deploy'){
		   steps{
		   		sh 'cd /home/accoliteadmin/Desktop/Staffing'
		   		sh './deploy.sh'
		   	}
		   
	   }
     } 
}  



