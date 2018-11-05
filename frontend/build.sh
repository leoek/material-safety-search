#!/bin/bash

if [[ "$1" != "" || "$BUILD_NUMBER" != "" ]] ; then
     if [[ "$1" != "" ]]; then
        BUILD_NUMBER="$1"
     fi
else
    echo "a build number is required."
    exit 1
fi

versionFile=./src/config/index.js
packageVersion=$(awk '/version/{gsub(/("|",)/,"",$2);print $2};' $versionFile)

. ./gitvars.sh

if [ "$BUILD_NUMBER" != "" ]; then
    echo "replacing BUILD_NUMBER with $BUILD_NUMBER"
    BUILD_NUMBER_TO_REPLACE="REPLACE_WITH_BUILD_NUMBER"
    BUILD_NUMBER_esc=$(echo "$BUILD_NUMBER" | sed -e 's/[\/&]/\\&/g');
    sed -i "s/$BUILD_NUMBER_TO_REPLACE/$BUILD_NUMBER_esc/g" ./public/index.html
fi

#check build status
if [ "$?" -gt "0" ]; then
    exit 1
fi

baseTag="materialsafetysearch/private"
tagName="frontend"
newTag="$baseTag:$tagName-$packageVersion-$BUILD_NUMBER"

echo "Building $newTag"
docker build -t "$newTag" .
#check build status
if [ "$?" -gt "0" ]; then
    exit 1
fi
docker push $newTag

if [ "$git_esc" != "" ]; then
    docker tag $newTag "$baseTag:$tagName-$git_esc"
    docker push "$baseTag:$tagName-$git_esc"
fi

if [ "$git_first" == "develop" ]; then
    docker tag $newTag "$baseTag:$tagName-next"
    docker push "$baseTag:$tagName-next"
fi

if [ "$git_first" == "master" ]; then
    docker tag $newTag "$baseTag:$tagName-stable"
    docker push "$baseTag:$tagName-stable"
fi