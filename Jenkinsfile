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

	parameters {
    booleanParam(defaultValue: false, description: 'Run tests with or without SonarQube', name: 'runSonarQube')
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
				branch 'master'
			}
			steps {
				sh "./scripts/copy_and_start.sh"
			}
		}
	}
}
