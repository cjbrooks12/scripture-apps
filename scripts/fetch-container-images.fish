#!/usr/local/bin/fish

# Manually pull images without SSL verification to get around stupid company proxies
podman pull eclipse-temurin:21-jre           --tls-verify=false
podman pull flyway/flyway:latest             --tls-verify=false
podman pull quay.io/keycloak/keycloak:latest --tls-verify=false
podman pull openfga/openfga:latest           --tls-verify=false
podman pull postgres:latest                  --tls-verify=false
podman pull bitnami/redis:latest             --tls-verify=false
podman pull pierrezemb/gostatic:latest       --tls-verify=false
