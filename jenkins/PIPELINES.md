# Pipelines por microservicio

## Archivos

- `Jenkinsfile.usuarios`
- `Jenkinsfile.catalogo`
- `Jenkinsfile.orders`
- `Jenkinsfile.payments`
- `Jenkinsfile.entregas`

## Jobs sugeridos en Jenkins

- `usuarios-cicd`
- `catalogo-cicd`
- `orders-cicd`
- `payments-cicd`
- `entregas-cicd`

## Configuracion de cada job

- Tipo: `Pipeline`
- Definition: `Pipeline script`
- Pegar el contenido del `Jenkinsfile` correspondiente

## Credenciales reutilizadas

- `docker-registry-credentials`
- `kubeconfig-pedidos`

## Parametros disponibles

- `DEPLOY_TO_K8S`
- `REGISTRY_URL`
- `IMAGE_TAG`

## Comportamiento

Cada pipeline:

1. compila solo su microservicio
2. construye solo su imagen Docker
3. hace push solo de esa imagen
4. actualiza solo su `Deployment` en Kubernetes
5. espera solo el rollout de ese microservicio
