#!/bin/bash

if [ "$GIT_BRANCH" != "" ]; then
    git_branch="$GIT_BRANCH"
else
    git_branch=$(git rev-parse --abbrev-ref HEAD)
fi
echo "Detected git branch $git_branch"
if [[ "$GIT_BRANCH" != *"origin"* ]]; then
    git_first=$(echo "$git_branch" | awk -F/ '{print $1}')
else
    git_first=$(echo "$git_branch" | awk -F/ '{print $2}')
fi
echo "detected first relevant git branch part $git_first"
if [[ "$git_branch" == *"feature"* ]]; then
    featureTag=$(echo "$git_branch" | sed -e 's/\//_/g' )
    BUILD_NUMBER="${BUILD_NUMBER}-${featureTag}"
    echo "new enhanced build number $BUILD_NUMBER"
fi