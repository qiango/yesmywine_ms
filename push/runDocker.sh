#!/bin/bash

gradle build

dockerImageName=mall-push
dockerContainerName=mall-push-c
dockerContainerPort=8212
dockerPath=/home/hz/file


docker stop $dockerContainerName
docker rm $dockerContainerName
docker rmi $dockerImageName

docker build -t $dockerImageName .

docker run -e JAVA_OPTS='-Xmx512m' --name $dockerContainerName -it -v /opt:/opt -p ${dockerContainerPort}:8080 -d $dockerImageName
