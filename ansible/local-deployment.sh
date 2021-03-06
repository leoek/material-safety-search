#!/bin/bash

ansible_role="playbook-deployment.yml"
defaults_yaml_path="roles/docker-mss-deployment-local/defaults/main.yml"
if [ "$1" == "reset-volumes" ]; then
    ansible_role="playbook-deployment-reset-volumes.yml"
fi
if [ "$1" == "only-frontend" ]; then
    . ./gitvars.sh
    if [ "$2" != "" ]; then
        TO_BE_BUILD="$2"
    else
        TO_BE_BUILD="$git_esc"
    fi
    ansible_role="playbook-deployfrontend.yml"
    defaults_yaml_path="roles/docker-mss-deployfrontend-local/defaults/main.yml"
    # Replace stuff according to this build
    MSS_PATH_TO_REPLACE="mss_path: /srv/docker/mss/frontends"
    MSS_PATH_TO_REPLACE_esc=$(echo "$MSS_PATH_TO_REPLACE" | sed -e 's/[\/&]/\\&/g');
    mssPath="mss_path: /srv/docker/mss/frontends/frontend_$TO_BE_BUILD"
    mssPath_esc=$(echo "$mssPath" | sed -e 's/[\/&]/\\&/g');
    sed -i -e "s/$MSS_PATH_TO_REPLACE_esc/$mssPath_esc/g" $defaults_yaml_path
    DOMAIN_PREFIX_TO_REPLACE="mss_domain_prefix: frontend"
    DOMAIN_PREFIX_TO_REPLACE_esc=$(echo "$DOMAIN_PREFIX_TO_REPLACE" | sed -e 's/[\/&]/\\&/g');
    mssDomainPrefix_temp=$(echo "$TO_BE_BUILD" | sed -e 's/_//g')
    mssDomainPrefix="mss_domain_prefix: $mssDomainPrefix_temp"
    mssDomainPrefix_esc=$(echo "$mssDomainPrefix" | sed -e 's/[\/&]/\\&/g');
    sed -i -e "s/$DOMAIN_PREFIX_TO_REPLACE_esc/$mssDomainPrefix_esc/g" $defaults_yaml_path
    ansible_role="playbook-deployfrontend.yml"
    IMAGE_SUFFIX_TO_REPLACE="image: materialsafetysearch/private:frontend-next"
    IMAGE_SUFFIX_TO_REPLACE_esc=$(echo "$IMAGE_SUFFIX_TO_REPLACE" | sed -e 's/[\/&]/\\&/g');
    mssImageSuffix="image: materialsafetysearch/private:frontend-$TO_BE_BUILD"
    mssImageSuffix_esc=$(echo "$mssImageSuffix" | sed -e 's/[\/&]/\\&/g');
    sed -i -e "s/$IMAGE_SUFFIX_TO_REPLACE_esc/$mssImageSuffix_esc/g" roles/docker-mss-deployfrontend-local/templates/docker-compose.yml
fi

ip=$(docker network inspect --format='{{range .IPAM.Config}}{{.Gateway}}{{end}}' nginx-proxy | awk -F '/' 'NR==1{print $1}')
echo "net1 ansible_host=$ip" > hosts-deployment
echo "[mss]" >> hosts-deployment
echo "net1" >> hosts-deployment

cat hosts-deployment

DOCKER_PW_TO_REPLACE="MSS_DEPLOY_DOCKER_HUB_PW_TO_BE_REPLACE"
dockerPw_esc=$(echo "$dockerPw" | sed -e 's/[\/&]/\\&/g');
sed -i -e "s/$DOCKER_PW_TO_REPLACE/$dockerPw_esc/g" $defaults_yaml_path

DOCKER_USER_TO_REPLACE="MSS_DEPLOY_DOCKER_HUB_USER_TO_BE_REPLACE"
dockerUser_esc=$(echo "$dockerUser" | sed -e 's/[\/&]/\\&/g');
sed -i -e "s/$DOCKER_USER_TO_REPLACE/$dockerUser_esc/g" $defaults_yaml_path

docker pull leoek/ansible:2.4-did
docker run --rm -v $(pwd):/ansible/playbooks \
	-v /srv/docker/mss:/srv/docker/mss \
    -v  /var/run/docker.sock:/var/run/docker.sock:ro \
    leoek/ansible:next $ansible_role -i hosts-deployment --connection=local
