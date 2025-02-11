pipeline {
    agent any

    stages {
        stage("GIT Clone") {
            steps {
                git url : "https://github.com/Han-minshik/Project.git",
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
