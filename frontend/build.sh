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

if [ "$BUILD_NUMBER" != "" ]; then
    echo "replacing BUILD_NUMBER with $BUILD_NUMBER"
    BUILD_NUMBER_TO_REPLACE="REPLACE_WITH_BUILD_NUMEBR"
    BUILD_NUMBER_esc=$(echo "$BUILD_NUMBER" | sed -e 's/[\/&]/\\&/g');
    sed -i "s/$BUILD_NUMBER_TO_REPLACE/$BUILD_NUMBER_esc/g" ./public/index.html
fi

baseTag="materialsafetysearch/private"
tagName="frontend"
newTag="$baseTag:$tagName-$packageVersion-$BUILD_NUMBER"

echo "Building $newTag"
docker build -t "$newTag" .
docker push $newTag

docker tag $newTag "$baseTag:$tagName-next"
docker push "$baseTag:$tagName-next"