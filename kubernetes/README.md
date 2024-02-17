# Follow the instructions below to run the application in Kubernetes
## Tools needed
To run the application in Kubernetes you need a couple of tools:
- **Minikube** is used to run Kubernetes. You can install it following their [official guide](https://minikube.sigs.k8s.io/docs/start/) in the **installation** section.
- **Kompose** is used to convert a docker-compose.yml file to all the .yamls files needed by kubernetes. You can install it following their [official guide](https://kompose.io/installation/).
## Starting the app in Kubernetes
To start the app run the following commands:

### 1. If the .yamls files needed by kubernetes are not present then run:
```bash
mkdir kubeFiles && cd kubeFiles && kompose convert -f ../docker-compose.yml
```
### 2. Start the cluster
```bash
minkube start
```
### 3. Apply the configurations to the pods
```bash
kubectl apply -f .
```
### 4. Check the status of all pods
```bash
kubectl get pods
```
_If some pods have error of type ``CrashLoopBackOff`` you just need to wait some more. Is usually means that a service is running but the respective DB is not so a connection can't be established._
### 5. Expose the ports needed:
You need to run:
```bash
kubectl port-forward svc/frontend 8080:8080 --address='0.0.0.0'
```
and run:
```bash
kubectl port-forward svc/frontend 4200:4200
```

