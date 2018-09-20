#!/bin/bash
ip=$(docker network inspect --format='{{range .IPAM.Config}}{{.Gateway}}{{end}}' nginx-proxy | awk -F '/' 'NR==1{print $1}')
echo "net1 ansible_host=$ip" > hosts-deployment
echo "[mss]" >> hosts-deployment
echo "net1" >> hosts-deployment

cat hosts-deployment

DOCKER_PW_TO_REPLACE="MSS_DEPLOY_DOCKER_HUB_PW_TO_BE_REPLACE"
dockerPw_esc=$(echo "$dockerPw" | sed -e 's/[\/&]/\\&/g');
sed -i -e "s/$DOCKER_PW_TO_REPLACE/$dockerPw_esc/g" roles/docker-mss-deployment-local/defaults/main.yml

DOCKER_USER_TO_REPLACE="MSS_DEPLOY_DOCKER_HUB_USER_TO_BE_REPLACE"
dockerUser_esc=$(echo "$dockerUser" | sed -e 's/[\/&]/\\&/g');
sed -i -e "s/$DOCKER_USER_TO_REPLACE/$dockerUser_esc/g" roles/docker-mss-deployment-local/defaults/main.yml

docker pull leoek/ansible:2.4-did
docker run --rm -v $(pwd):/ansible/playbooks \
	-v /srv/docker/swt:/srv/docker/swt \
    -v  /var/run/docker.sock:/var/run/docker.sock:ro \
    leoek/ansible:next playbook-deployment.yml -i hosts-deployment --connection=local
