#!/bin/bash

set -euo pipefail

NAMESPACE="pedidos-dev"
LOG_DIR="/tmp/ms-port-forward"

mkdir -p "$LOG_DIR"

kubectl port-forward -n "$NAMESPACE" svc/usuarios 8082:8082 >"$LOG_DIR/usuarios.log" 2>&1 &
kubectl port-forward -n "$NAMESPACE" svc/catalogo 8083:8083 >"$LOG_DIR/catalogo.log" 2>&1 &
kubectl port-forward -n "$NAMESPACE" svc/orders 8084:8084 >"$LOG_DIR/orders.log" 2>&1 &
kubectl port-forward -n "$NAMESPACE" svc/payments 8085:8085 >"$LOG_DIR/payments.log" 2>&1 &
kubectl port-forward -n "$NAMESPACE" svc/entregas 8086:8086 >"$LOG_DIR/entregas.log" 2>&1 &

echo "Port-forward activos para usuarios, catalogo, orders, payments y entregas."
echo "Logs disponibles en: $LOG_DIR"
echo "Para detenerlos: pkill -f 'kubectl port-forward -n $NAMESPACE'"
