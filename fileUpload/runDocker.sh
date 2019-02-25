#!/bin/bash

gradle build

dockerImageName=mall-fileupload
dockerContainerName=mall-fileupload-c
dockerContainerPort=8205
dockerPath=/home/hz/file


docker stop $dockerContainerName
docker rm $dockerContainerName
docker rmi $dockerImageName

docker build -t $dockerImageName .

docker run -e JAVA_OPTS='-Xmx512m' --name $dockerContainerName -it -v /var/ftp:/var/ftp -p ${dockerContainerPort}:8080 -d $dockerImageName
