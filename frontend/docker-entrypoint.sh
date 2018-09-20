#!/bin/sh

if [ "$API_BASE_URL" != "" ]; then

    echo "$0: replacing API_BASE_URL with $API_BASE_URL"
    API_BASE_URL_TO_REPLACE="REPLACE_WITH_APIBASEURL"
    API_BASE_URL_esc=$(echo "$API_BASE_URL" | sed -e 's/[\/&]/\\&/g');
    sed -i -e "s/$API_BASE_URL_TO_REPLACE/$API_BASE_URL_esc/g" /usr/share/nginx/html/index.html

fi

# start nginx
nginx -g "daemon off;"