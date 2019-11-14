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
		dir('/home/accoliteadmin/Desktop/Staffing/Staffing') {
			sh "mvn -B -DskipTests clean package";
		}
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
		dir('/home/accoliteadmin/Desktop/Staffing/Staffing') {
          docker.build registry + ":$BUILD_NUMBER"
		}
        }
      }
    }
	   
	   
	   
     } 
}  



