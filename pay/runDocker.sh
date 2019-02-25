#!/bin/bash

gradle build

dockerImageName=mall-pay
dockerContainerName=mall-pay-c
dockerContainerPort=8211


docker stop $dockerContainerName
docker rm $dockerContainerName
docker rmi $dockerImageName

docker build -t $dockerImageName .

docker run -e JAVA_OPTS='-Xmx512m' --name $dockerContainerName -it -v /opt:/opt -p ${dockerContainerPort}:8080 -d $dockerImageName
