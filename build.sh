#!/bin/bash

if [[ "$1" != "" || "$BUILD_NUMBER" != "" ]] ; then
     if [[ "$1" != "" ]]; then
        BUILD_NUMBER="$1"
     fi
else
    echo "a build number is required."
    exit 1
fi

packageVersion=$(./getVersion.sh)

baseTag="leoek/material-safety-search"
newTag="$baseTag:$packageVersion-$BUILD_NUMBER"

echo "Building $newTag"
docker build -t "$newTag" .
