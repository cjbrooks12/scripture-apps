#!/usr/local/bin/fish

# build all containers in the project
./gradlew :services:api:assemble :services:api:buildFatJar
podman build -t api ./services/api/

./gradlew :services:site:hugoBuild
podman build -t site ./services/site/
