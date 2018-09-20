#!/bin/bash
ip=$(docker network inspect --format='{{range .IPAM.Config}}{{.Gateway}}{{end}}' nginx-proxy | awk -F '/' 'NR==1{print $1}')
echo "net1 ansible_host=$ip" > hosts-deployment
echo "[mss]" >> hosts-deployment
echo "net1" >> hosts-deployment

cat hosts-deployment

docker pull leoek/ansible:2.4-did
docker run --rm -v $(pwd):/ansible/playbooks \
	-v /srv/docker/swt:/srv/docker/swt \
    -v  /var/run/docker.sock:/var/run/docker.sock:ro \
    leoek/ansible:next playbook-deployment.yml -i hosts-mss-deployment --connection=local
