pipeline {
    agent {
        kubernetes {
            yaml '''
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: maven
    image: maven:3.9.6-eclipse-temurin-21
    command:
    - sleep
    args:
    - infinity
    resources:
      requests:
        cpu: "500m"
        memory: "1Gi"
      limits:
        cpu: "1000m"
        memory: "2Gi"
'''
        }
    }

    environment {
        // Tu registro en k0s
        REGISTRY_URL = "registry.rodriguezrodero.com"
    }

    stages {
        stage('Checkout Especial') {
            steps {
                container('maven') { // Puedes hacerlo dentro del contenedor de Maven
                    git url: 'https://github.com/drodriguezlopez/flowstate.git',
                            branch: 'develop'
                }
            }
        }
        stage('Build') {
            steps {
                container('maven') {
                    // -B: Batch mode para logs limpios
                    // -DskipTests: Opcional, dependiendo de tu flujo
                    sh 'mvn -B clean package -DskipTests'
                }
            }
        }

        stage('Deploy') {
            steps {
                container('maven') {
                    // -B: Batch mode para logs limpios
                    // -DskipTests: Opcional, dependiendo de tu flujo
                    sh 'mvn -B com.google.cloud.tools:jib-maven-plugin:3.5.1:build'
                }
            }
        }
    }

    post {
        success {
            echo "Compilación completada. Archivo generado en target/"
        }
    }
}