

https://www.jenkins.io/doc/book/installing/kubernetes/


Configurar un agente de Jenkins (anteriormente llamado "slave") para que se ejecute dinámicamente dentro de un clúster de Kubernetes es una de las mejores formas de escalar tu CI/CD. En lugar de tener máquinas encendidas todo el tiempo, Jenkins crea un Pod, ejecuta la tarea y lo destruye al terminar.

Aquí tienes la guía paso a paso para lograrlo:

---

## 1. Requisitos Previos

* Un clúster de **Kubernetes** operativo.
* **Jenkins** instalado (dentro o fuera del clúster).
* Acceso al panel de administración de Jenkins con permisos para instalar plugins.

## 2. Instalar el Plugin de Kubernetes

Para que Jenkins sepa "hablar" con Kubernetes, necesitas el plugin oficial:

1. Ve a **Administrar Jenkins** > **Plugins** > **Available plugins**.
2. Busca **"Kubernetes"**.
3. Instálalo y reinicia Jenkins si es necesario.

## 3. Configurar la Nube (Cloud) en Jenkins

Aquí es donde conectas Jenkins con tu clúster:

1. Ve a **Administrar Jenkins** > **Nodes and Clouds** > **Clouds**.
2. Haz clic en **New cloud** y selecciona **Kubernetes**. Dale un nombre (ej. `k8s-cluster`).
3. Configura los detalles del clúster:
* **Kubernetes URL:** Si Jenkins está dentro del mismo clúster, usa `https://kubernetes.default.svc`.
* **Kubernetes Namespace:** El namespace donde se ejecutarán los agentes (ej. `jenkins`).
* **Credentials:** Si Jenkins está fuera del clúster, añade un Token de Service Account de Kubernetes.



## 4. Definir la Plantilla del Pod (Pod Template)

Dentro de la misma configuración de la nube, debes definir cómo será el agente:

1. Busca la sección **Pod Templates**.
2. **Name:** `jenkins-agent`.
3. **Labels:** Este es el nombre que usarás en tus Pipelines (ej. `k8s-agent`).
4. **Containers:** Añade un contenedor.
* Por defecto, el contenedor se llama `jnlp`.
* Usa la imagen: `jenkins/inbound-agent:latest`.



---

## 5. Ejemplo de Pipeline (Jenkinsfile)

Ahora que la infraestructura está lista, puedes probarla con un script. El siguiente código le dice a Jenkins que busque un agente con la etiqueta `k8s-agent`:

```groovy
pipeline {
    agent {
        kubernetes {
            label 'k8s-agent'
        }
    }
    stages {
        stage('Prueba en K8s') {
            steps {
                sh 'echo "Hola desde el pod de Kubernetes"'
                sh 'java -version'
            }
        }
    }
}

```

---

## Consideraciones de Seguridad (RBAC)

Para que Jenkins pueda crear pods, el **Service Account** que usa debe tener permisos. Si tienes errores de "Forbidden", aplica este archivo YAML en tu clúster:

```yaml
apiVersion: rbac.authorization.k8s.io/v1
kind: Role
metadata:
  name: jenkins-agent-role
  namespace: jenkins
rules:
- apiGroups: [""]
  resources: ["pods", "pods/exec", "pods/log"]
  verbs: ["get", "list", "watch", "create", "delete"]
---
apiVersion: rbac.authorization.k8s.io/v1
kind: RoleBinding
metadata:
  name: jenkins-agent-binding
  namespace: jenkins
subjects:
- kind: ServiceAccount
  name: default # O el nombre de tu cuenta de Jenkins
roleRef:
  kind: Role
  name: jenkins-agent-role
  apiGroup: rbac.authorization.k8s.io

```

---

### ¿Qué sigue?

Si necesitas herramientas específicas (como Docker, Python o Go) en tu agente, puedes personalizar la imagen del contenedor o añadir varios contenedores al mismo Pod Template.

¿Te gustaría que te ayude a crear un **Dockerfile personalizado** para tu agente de Jenkins?