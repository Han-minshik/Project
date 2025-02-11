pipeline {
    agent any

    stages {
        stage("GIT Clone") {
            steps {
                git url : "https://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@github.com/Han-minshik/Project.git",
                branch: 'main',
                credentialsId: 'hanminshik'
            }
        }
//         stage("Package"){
//             steps{
//                 sh "chmod 777 mvnw"
//                 sh "./mvnw package"
//             }
//         }
    }
}
