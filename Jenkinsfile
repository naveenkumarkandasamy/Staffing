pipeline {
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
     } 
}  



