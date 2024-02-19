#!/bin/bash

DEPLOY_LOG_PATH="/home/ec2-user/deploy.log"
DEPLOY_PATH="/home/ec2-user/Mour"
DOCKER_CONTAINER_NAME="application"

START_TIME=$(date)
echo "> ##### START [$START_TIME] #####" >> $DEPLOY_LOG_PATH

cd $DEPLOY_PATH
echo "> 0. 배포 위치 = $DEPLOY_PATH" >> $DEPLOY_LOG_PATH

CURRENT_RUNNING_PID=$(sudo docker container ls -aq -f name=$DOCKER_CONTAINER_NAME)
echo "> 1. 현재 실행중인 WAS Docker Container PID = $CURRENT_RUNNING_PID" >> $DEPLOY_LOG_PATH

if [ -z $CURRENT_RUNNING_PID ]
then
  echo "> 2-1. 현재 실행중인 WAS Docker Container가 없습니다" >> $DEPLOY_LOG_PATH
else
  echo "> 2-2-1. 현재 실행중인 WAS Docker Container Stop & Remove = $CURRENT_RUNNING_PID" >> $DEPLOY_LOG_PATH
  echo "> 2-2-2. sudo docker stop $CURRENT_RUNNING_PID" >> $DEPLOY_LOG_PATH
  sudo docker stop $CURRENT_RUNNING_PID
  echo "> 2-2-3. sudo docker rm $CURRENT_RUNNING_PID" >> $DEPLOY_LOG_PATH
  sudo docker rm $CURRENT_RUNNING_PID
  sleep 5
fi

DOCKER_IMAGE_FILE_PATH="/home/ec2-user/Mour/DockerImage.txt"
DOCKER_IMAGE_NAME=$(cat $DOCKER_IMAGE_FILE_PATH)
DOCKER_COMPOSE_NAME="docker-compose-application.yml"

echo "> 3. Docker Image = $DOCKER_IMAGE_NAME" >> $DEPLOY_LOG_PATH

# docker-compose 파일 생성
echo "
version: '3'
services:
  application:
    container_name: $DOCKER_CONTAINER_NAME
    image: $DOCKER_IMAGE_NAME
    ports:
      - 8080:8080
    volumes:
      - $DEPLOY_PATH/logs:/app/logs
" > $DOCKER_COMPOSE_NAME

echo "> 4. docker-compose 실행 = $DOCKER_IMAGE_NAME, $DOCKER_COMPOSE_NAME" >> $DEPLOY_LOG_PATH
docker-compose -f $DOCKER_COMPOSE_NAME up -d

NEW_RUNNING_PID=$(sudo docker container ls -q -f name=$DOCKER_CONTAINER_NAME)
echo "> 5. 새로 실행된 WAS Docker Container PID = $NEW_RUNNING_PID" >> $DEPLOY_LOG_PATH

echo -e "> ##### END #####\n" >> $DEPLOY_LOG_PATH
