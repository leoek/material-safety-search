#!/bin/bash
GRADLE_PATH=./build.gradle
GRADLE_FIELD="version = "
VERSION=$(grep "$GRADLE_FIELD" $GRADLE_PATH | awk '{print substr($3, 2, length($3)-2)}')
echo $VERSION