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
		
          docker.build registry + ":$BUILD_NUMBER"
		}
        
      }
    }
	   
	
   stage('Docker Push') {
      agent any
      steps {
        withCredentials([usernamePassword(credentialsId: 'payalmantri10', passwordVariable: 'Gul@221771', usernameVariable: 'payalmantri10')]) {
          sh "docker login -u payalmantri10 -p Gul@221771"
          sh 'docker push payalmantri10/staffing:latest'
        }
      }
	   
	   
        }	   
     } 
}  



