#!/bin/bash
docker login --username "$dockerUser" --password "$dockerPw"

./build.sh

docker logout