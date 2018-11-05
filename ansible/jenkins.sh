#!/bin/bash
. ./gitvars.sh

if [ "$git_first" != "develop" ] && [ "$git_first" != "master" ]; then
    ONLY_FRONTEND="true"
	echo "SET ONLY_FRONTEND $ONLY_FRONTEND"
fi

if [ "$ONLY_FRONTEND" == "true"  ]; then
  echo "Deploying Frontend $FRONTEND_TAG"
  ./local-deployment.sh "only-frontend" "$FRONTEND_TAG"
else
  if [ "$RESET_VOLUMES" == "true" ]; then
      ./local-deployment.sh "reset-volumes"
  else
      ./local-deployment.sh
  fi
fi