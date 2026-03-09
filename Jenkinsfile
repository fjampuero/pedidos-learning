pipeline {
    agent any

    options {
        timestamps()
    }

    parameters {
        booleanParam(name: 'DEPLOY_TO_K8S', defaultValue: true, description: 'Desplegar en Kubernetes al finalizar el build')
        string(name: 'REGISTRY_URL', defaultValue: 'docker.io/TU_USUARIO', description: 'Registry donde se publicaran las imagenes')
        string(name: 'IMAGE_TAG', defaultValue: '', description: 'Tag de imagen. Si se deja vacio se usa BUILD_NUMBER')
    }

    environment {
        NAMESPACE = 'pedidos-dev'
        DOCKER_CREDENTIALS_ID = 'docker-registry-credentials'
        KUBECONFIG_CREDENTIALS_ID = 'kubeconfig-pedidos'
        REPO_ROOT = '/workspace/ms'
    }

    stages {
        stage('Init') {
            steps {
                script {
                    env.RESOLVED_IMAGE_TAG = params.IMAGE_TAG?.trim() ? params.IMAGE_TAG.trim() : env.BUILD_NUMBER
                }
                echo "Usando tag de imagen: ${env.RESOLVED_IMAGE_TAG}"
            }
        }

        stage('Compile') {
            parallel {
                stage('Usuarios') {
                    steps {
                        dir("${env.REPO_ROOT}/usuarios") {
                            sh './mvnw -DskipTests compile'
                        }
                    }
                }
                stage('Catalogo') {
                    steps {
                        dir("${env.REPO_ROOT}/catalogo") {
                            sh './mvnw -DskipTests compile'
                        }
                    }
                }
                stage('Orders') {
                    steps {
                        dir("${env.REPO_ROOT}/orders") {
                            sh './mvnw -DskipTests compile'
                        }
                    }
                }
                stage('Payments') {
                    steps {
                        dir("${env.REPO_ROOT}/payments") {
                            sh './mvnw -DskipTests compile'
                        }
                    }
                }
                stage('Entregas') {
                    steps {
                        dir("${env.REPO_ROOT}/entregas") {
                            sh './mvnw -DskipTests compile'
                        }
                    }
                }
            }
        }

        stage('Docker Build') {
            parallel {
                stage('Build Usuarios') {
                    steps {
                        sh 'docker build -t ${REGISTRY_URL}/usuarios:${RESOLVED_IMAGE_TAG} ${REPO_ROOT}/usuarios'
                    }
                }
                stage('Build Catalogo') {
                    steps {
                        sh 'docker build -t ${REGISTRY_URL}/catalogo:${RESOLVED_IMAGE_TAG} ${REPO_ROOT}/catalogo'
                    }
                }
                stage('Build Orders') {
                    steps {
                        sh 'docker build -t ${REGISTRY_URL}/orders:${RESOLVED_IMAGE_TAG} ${REPO_ROOT}/orders'
                    }
                }
                stage('Build Payments') {
                    steps {
                        sh 'docker build -t ${REGISTRY_URL}/payments:${RESOLVED_IMAGE_TAG} ${REPO_ROOT}/payments'
                    }
                }
                stage('Build Entregas') {
                    steps {
                        sh 'docker build -t ${REGISTRY_URL}/entregas:${RESOLVED_IMAGE_TAG} ${REPO_ROOT}/entregas'
                    }
                }
            }
        }

        stage('Docker Push') {
            steps {
                withCredentials([usernamePassword(credentialsId: env.DOCKER_CREDENTIALS_ID, usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                    sh '''
                        echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
                        docker push ${REGISTRY_URL}/usuarios:${RESOLVED_IMAGE_TAG}
                        docker push ${REGISTRY_URL}/catalogo:${RESOLVED_IMAGE_TAG}
                        docker push ${REGISTRY_URL}/orders:${RESOLVED_IMAGE_TAG}
                        docker push ${REGISTRY_URL}/payments:${RESOLVED_IMAGE_TAG}
                        docker push ${REGISTRY_URL}/entregas:${RESOLVED_IMAGE_TAG}
                        docker logout
                    '''
                }
            }
        }

        stage('Deploy Kubernetes') {
            when {
                expression { return params.DEPLOY_TO_K8S }
            }
            steps {
                withCredentials([file(credentialsId: env.KUBECONFIG_CREDENTIALS_ID, variable: 'KUBECONFIG')]) {
                    sh '''
                        kubectl apply -k ${REPO_ROOT}/k8s

                        kubectl set image deployment/usuarios usuarios=${REGISTRY_URL}/usuarios:${RESOLVED_IMAGE_TAG} -n ${NAMESPACE}
                        kubectl set image deployment/catalogo catalogo=${REGISTRY_URL}/catalogo:${RESOLVED_IMAGE_TAG} -n ${NAMESPACE}
                        kubectl set image deployment/orders orders=${REGISTRY_URL}/orders:${RESOLVED_IMAGE_TAG} -n ${NAMESPACE}
                        kubectl set image deployment/payments payments=${REGISTRY_URL}/payments:${RESOLVED_IMAGE_TAG} -n ${NAMESPACE}
                        kubectl set image deployment/entregas entregas=${REGISTRY_URL}/entregas:${RESOLVED_IMAGE_TAG} -n ${NAMESPACE}

                        kubectl rollout status deployment/usuarios -n ${NAMESPACE}
                        kubectl rollout status deployment/catalogo -n ${NAMESPACE}
                        kubectl rollout status deployment/orders -n ${NAMESPACE}
                        kubectl rollout status deployment/payments -n ${NAMESPACE}
                        kubectl rollout status deployment/entregas -n ${NAMESPACE}
                    '''
                }
            }
        }
    }

    post {
        success {
            echo "Pipeline completado correctamente con tag ${env.RESOLVED_IMAGE_TAG}"
        }
        failure {
            echo 'Pipeline fallido. Revisar logs del stage correspondiente.'
        }
    }
}
