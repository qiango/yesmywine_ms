#!/bin/bash

gradle build

dockerImageName=mall-activity
dockerContainerName=mall-activity-c
dockerContainerPort=8201


docker stop $dockerContainerName
docker rm $dockerContainerName
docker rmi $dockerImageName

docker build -t $dockerImageName .

docker run -e JAVA_OPTS='-Xmx512m' --name $dockerContainerName -it -p ${dockerContainerPort}:8080 -d $dockerImageName

