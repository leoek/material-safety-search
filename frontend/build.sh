#!/bin/bash

if [[ "$1" != "" || "$BUILD_NUMBER" != "" ]] ; then
     if [[ "$1" != "" ]]; then
        BUILD_NUMBER="$1"
     fi
else
    echo "a build number is required."
    exit 1
fi

versionFile=./package.json
packageVersion=$(awk '/version/{gsub(/("|",)/,"",$2);print $2};' $versionFile)

baseTag="materialsafetysearch/private"
tagName="frontend"
newTag="$baseTag:$tagName-$packageVersion-$BUILD_NUMBER"

echo "Building $newTag"
docker build -t "$newTag" .
docker push $newTag

docker tag $newTag "$baseTag:$tagName-next"
docker push "$baseTag:$tagName-next"