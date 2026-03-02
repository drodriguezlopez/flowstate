# Jenkins Agents on Kubernetes: Complete Setup Guide

https://www.jenkins.io/doc/book/installing/kubernetes/

Running Jenkins agents dynamically inside a Kubernetes cluster is one of the best ways to scale your CI/CD infrastructure. Instead of keeping machines running all the time, Jenkins creates a Pod for each job, executes the task, and destroys the Pod when finished.

Below is a step-by-step guide to set up Jenkins agents on Kubernetes:

---

## 1. Prerequisites

* A running **Kubernetes** cluster
* **Jenkins** installed (inside or outside the cluster)
* Admin access to Jenkins for plugin installation

## 2. Install the Kubernetes Plugin

To enable Jenkins to communicate with Kubernetes, install the official plugin:

1. Go to **Manage Jenkins** > **Plugins** > **Available plugins**
2. Search for **"Kubernetes"**
3. Install it and restart Jenkins if required

## 3. Configure the Cloud in Jenkins

This is where you connect Jenkins to your Kubernetes cluster:

1. Go to **Manage Jenkins** > **Nodes and Clouds** > **Clouds**
2. Click **New cloud** and select **Kubernetes**. Give it a name (e.g., `k0s-cluster`)
3. Configure cluster details:
   * **Kubernetes URL:** If Jenkins runs inside the cluster, use `https://kubernetes.default.svc`
   * **Kubernetes Namespace:** The namespace where agents will run (e.g., `jenkins`)
   * **Credentials:** If Jenkins is outside the cluster, add a Kubernetes Service Account token

## 4. Define the Pod Template

Within the cloud configuration, define how the agent Pod will look:

1. Find the **Pod Templates** section
2. **Name:** `jenkins-agent`
3. **Labels:** This is the label you will use in your Pipelines (e.g., `k0s-agent`)
4. **Containers:** Add a container
   * By default, the container is named `jnlp`
   * Use the image: `jenkins/inbound-agent:latest`

---

## 5. Pipeline Example (Jenkinsfile)

Once the infrastructure is ready, test it with a sample pipeline. The following code tells Jenkins to use an agent with the label `k0s-agent`:

[Pipeline example](./pipeline.groovy) 
