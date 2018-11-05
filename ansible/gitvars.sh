#!/bin/bash

if [ "$GIT_BRANCH" != "" ]; then
    git_branch="$GIT_BRANCH"
else
    git_branch=$(git rev-parse --abbrev-ref HEAD)
fi
echo "Detected git branch $git_branch"
if [[ "$GIT_BRANCH" != *"origin"* ]]; then
    git_first=$(echo "$git_branch" | awk -F/ '{print $1}')
    git_esc=$(echo "$git_branch" | awk -F/ '{print $1"_"$2}')
else
    git_first=$(echo "$git_branch" | awk -F/ '{print $2}')
    git_esc=$(echo "$git_branch" | awk -F/ '{print $2"_"$3}')
fi
echo "Escaped git branch $git_esc"