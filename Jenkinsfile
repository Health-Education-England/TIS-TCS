@Library('utils@master')_

def utils = new hee.tis.utils()

node {

    def service = "tcs"

    deleteDir()

    stage('Checkout Git Repo') {
      checkout scm
    }

    env.GIT_COMMIT=sh(returnStdout: true, script: "git log -n 1 --pretty=format:'%h'").trim()
    def mvn = "${tool 'Maven 3.3.9'}/bin/mvn"
    def workspace = pwd()
    def parent_workspace = pwd()
    def repository = "${env.GIT_COMMIT}".split("TIS-")[-1].split(".git")[0]
    def buildNumber = env.BUILD_NUMBER
    def buildVersion = env.GIT_COMMIT
    def imageName = ""
    def imageVersionTag = ""
    boolean isService = false

    println "[Jenkinsfile INFO] Commit Hash is ${GIT_COMMIT}"

    if (fileExists("$workspace/$service-service/pom.xml")) {
        workspace = "$workspace/$service-service"
        env.WORKSPACE= workspace
        sh 'cd "$workspace"'
        isService = true
    }

    try {

        milestone 1


        stage('Build') {
          sh "'${mvn}' clean install -DskipTests"
        }

        stage('Unit Tests') {
          withCredentials([usernamePassword(credentialsId: 'AZUREBLOBTISDEVCRED', usernameVariable: 'BLOBACCOUNTNAME', passwordVariable: 'BLOBACCOUNTKEY')]) {
            env.CLOUD_BLOB_ACCOUNT_NAME = "${BLOBACCOUNTNAME}"
            env.CLOUD_BLOB_ACCOUNT_KEY = "${BLOBACCOUNTKEY}"
            env.CLOUD_BLOB_CONTAINER_NAME = "document-manager"
          }
          try {
            sh "'${mvn}' clean test"
          } finally {
            junit '**/target/surefire-reports/TEST-*.xml'
          }
        }

        stage('Analyze Quality') {
          withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
            if (env.CHANGE_ID) {
              sh "'${mvn}' sonar:sonar -Dsonar.login='${SONAR_TOKEN}' -Dsonar.pullrequest.key=$env.CHANGE_ID"
            } else {
              sh "'${mvn}' sonar:sonar -Dsonar.login='${SONAR_TOKEN}' -Dsonar.branch.name=$env.BRANCH_NAME"
            }
          }
        }

        stage('Analyze Quality in OpenJDK 11 Docker container') {
          withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
            def workspace = pwd()
            try {
              def buildIp=sh(returnStdout: true, script: "echo \$(ip -4 addr show docker0 | grep -Po 'inet \\K[\\d.]+')").trim()

              println "the buildIp is $buildIp"
                if (env.CHANGE_ID) {
                  sh "docker run --rm --cpus='0.75' --memory='3g' -v $workspace:$workspace -w $workspace -v /var/run/docker.sock:/var/run/docker.sock -e DOCKER_HOST_IP=$buildIp maven:3-openjdk-11 $workspace/mvn sonar:sonar -Dsonar.login='${SONAR_TOKEN}' -Dsonar.pullrequest.key=$env.CHANGE_ID"
                } else {
                  sh "docker run --rm --cpus='0.75' --memory='3g' -v $workspace:$workspace -w $workspace -v /var/run/docker.sock:/var/run/docker.sock -e DOCKER_HOST_IP=$buildIp maven:3-openjdk-11 $workspace/mvn sonar:sonar -Dsonar.login='${SONAR_TOKEN}' -Dsonar.branch.name=$env.BRANCH_NAME"
              }
            }
         }
      }
    } catch (hudson.AbortException ae) {
      // We do nothing for Aborts.
    } catch (err) {
      currentBuild.result = 'FAILURE'
      throw err
    }
}
