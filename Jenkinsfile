node('nimble-jenkins-slave') {

    stage('Clone and Update') {
        git(url: 'https://github.com/nimble-platform/trust-scoring-service', branch: env.BRANCH_NAME)
    }

    stage('Build Java') {
        sh 'mvn clean package -DskipTests'
    }

    if (env.BRANCH_NAME == 'staging') {
        stage('Build Docker') {
            sh 'mvn docker:build -P docker -DdockerImageTag=staging'
        }

        stage('Push Docker') {
            sh 'docker push nimbleplatform/trust-service:staging'
        }

        stage('Deploy') {
            sh 'ssh staging "cd /srv/nimble-staging/ && ./run-staging.sh restart-single trust-service"'
        }
    }

    if (env.BRANCH_NAME == 'master') {
        stage('Build Docker') {
            sh 'mvn docker:build -P docker'
        }

        stage('Push Docker') {
            sh 'mvn docker:build -P docker -DdockerImageTag=latest'
            sh 'mvn docker:build -P docker'
        }

        stage('Deploy') {
            sh 'ssh nimble "cd /data/deployment_setup/prod/ && sudo ./run-prod.sh restart-single trust-service"'
        }
    }
}
