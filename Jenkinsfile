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

//        stage('Deploy') {
//            sh 'ssh staging "cd /srv/nimble-staging/ && ./run-staging.sh restart-single catalog-service-srdc"'
//        }
    }

    if (env.BRANCH_NAME == 'master') {
        stage('Build Docker') {
            sh '/bin/bash -xe deploy.sh docker-build'
        }

//        stage('Deploy') {
            // not yet working
//            sh 'ssh nimble "cd /data/deployment_setup/prod/ && sudo ./run-prod.sh restart-single catalog-service-srdc"'
//        }
    }

    // Kubernetes setup is disabled for now
//    if (env.BRANCH_NAME == 'master') {
//        stage('Push Docker') {
//            withDockerRegistry([credentialsId: 'NimbleDocker']) {
//                sh '/bin/bash -xe deploy.sh docker-push'
//            }
//        }
//
//        stage('Apply to Cluster') {
//            sh 'kubectl apply -f kubernetes/deploy.yml -n prod --validate=false'
//        }
//    }
}
