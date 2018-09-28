#!/bin/bash

started="false"

while [ "$started" == "false" ]; do

    SOLR_HTTP_STATUS=$(curl -s -o /dev/null -I -w '%{http_code}' http://solr:8983/solr/admin/cores?action=STATUS)
    if [ "$SOLR_HTTP_STATUS" -eq '200' ]; then
        echo "$0: Solr seems to be up and running."
        started="true"
    else
        echo "$0: Current SOLR_HTTP_STATUS: ${SOLR_HTTP_STATUS}. Solr is not running or ready. Checking again in 10s"
        sleep 10
    fi

done

echo "$0: Starting the Application now."
java -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=prod -jar /opt/app.jar