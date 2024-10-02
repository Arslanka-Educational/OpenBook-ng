#!/bin/bash

docker compose -f ./core-catalog/docker-compose.yaml down -v
docker compose -f ./core-booking/docker-compose.yaml down -v

docker compose -f ./core-catalog/docker-compose.yaml up --build -d
docker compose -f ./core-booking/docker-compose.yaml up --build -d