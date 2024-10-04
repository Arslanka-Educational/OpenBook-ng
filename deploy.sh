#!/bin/bash

docker compose -f ./core-catalog/docker-compose.yaml down -v
docker compose -f ./core-booking/docker-compose.yaml down -v
docker compose -f ./gateway/docker-compose.yaml down -v
docker compose -f ./metrics/docker-compose.yaml down -v

docker compose -f ./core-catalog/docker-compose.yaml up --build
docker compose -f ./core-booking/docker-compose.yaml up --build
docker compose -f ./gateway/docker-compose.yaml up --build
docker compose -f ./metrics/docker-compose.yaml up --build