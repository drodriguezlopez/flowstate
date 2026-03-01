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
    }

    post {
        success {
            echo "Compilación completada. Archivo generado en target/"
        }
    }
}