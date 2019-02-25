#!/bin/bash

gradle build

dockerImageName=mall-user
dockerContainerName=mall-user-c
dockerContainerPort=8214


docker stop $dockerContainerName
docker rm $dockerContainerName
docker rmi $dockerImageName

docker build -t $dockerImageName .

docker run -e JAVA_OPTS='-Xmx512m' --name $dockerContainerName -it  -p ${dockerContainerPort}:8080 -d $dockerImageName


