#!/bin/bash

data=`curl -u $JENKINS_USER:$JENKINS_PASS https://jenkins.faria.xyz/job/spacejava/job/master/lastSuccessfulBuild/api/json`
url=`echo $data | jq -r ".url"`
filename=`echo $data | jq -r  ".artifacts.[].relativePath | select(contains(\"plain\") | not)"`

download_url="${url}artifact/${filename}"

echo $download_url

curl -u $JENKINS_USER:$JENKINS_PASS $download_url --output build/space.jar

