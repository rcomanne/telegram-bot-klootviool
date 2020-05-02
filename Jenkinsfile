pipeline {
	agent any

	options {
	    disableConcurrentBuilds()
	    buildDiscarder(logRotator(numToKeepStr: '10'))
	    ansiColor(colorMapName: 'XTerm')
	}

	triggers {
		bitbucketPush()
	}

	tools {
		jdk 'jdk-11'
		maven 'mvn'
	}

	stages {
		stage('Build') {
			steps {
				sh 'mvn clean package -DskipTests'
				archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
			}
		}

		stage('Test - Unit - API - Performance - GUI') {
			steps {
				sh 'mvn verify'
				junit '**/target/surefire-reports/*.xml'
			}
		}

		stage('Deploy') {
			when {
				anyOff {
					branch 'master'
					branch 'postgres'
				}
			}
			steps {
				sh "./scripts/cleanup.sh"
				sh "./scripts/copy_and_start.sh"
				sh "./scripts/notify.sh"
			}
		}
	}
}
