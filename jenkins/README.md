# Jenkins local para CI/CD

## Construir imagen personalizada

```bash
docker build -t jenkins-ms-pedidos:latest /Users/joelampuero/Documents/Learning/Tecsup/ms/jenkins
```

## Levantar Jenkins con Docker y kubectl

```bash
docker run -d \
  --name jenkins-ms-pedidos \
  -p 8080:8080 \
  -p 50000:50000 \
  -v jenkins_home:/var/jenkins_home \
  -v /var/run/docker.sock:/var/run/docker.sock \
  -v $HOME/.kube:/var/jenkins_home/.kube \
  -v /Users/joelampuero/Documents/Learning/Tecsup/ms:/workspace/ms \
  jenkins-ms-pedidos:latest
```

## Verificar herramientas

```bash
docker exec -it jenkins-ms-pedidos sh -c "git --version"
docker exec -it jenkins-ms-pedidos sh -c "docker --version"
docker exec -it jenkins-ms-pedidos sh -c "kubectl version --client"
```

## Obtener password inicial

```bash
docker exec -it jenkins-ms-pedidos cat /var/jenkins_home/secrets/initialAdminPassword
```

## Notas

- El socket Docker montado permite que Jenkins use el Docker del host.
- El volumen `$HOME/.kube` permite que Jenkins use tu contexto actual de Kubernetes.
- El volumen `/workspace/ms` deja el repo disponible dentro del contenedor si luego quieres usarlo en jobs locales.
