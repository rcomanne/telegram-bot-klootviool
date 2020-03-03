#!/bin/bash

function wrap() {
    echo "#========================================#"
    echo "# $1 #"
    echo "#========================================#"
}

# exit when anything fails
set -euo pipefail

BASE_DIR=/var/opt/services/telegram-bot-klootviool

wrap "copying new logback file"
cp target/classes/logback-server.xml ${BASE_DIR}/logback.xml

wrap "ensure jar is executable"
chmod +x target/*.jar

wrap "copying new jar to /var/opt/services/opt/services/telegram-bot-klootviool"
cp target/*.jar ${BASE_DIR}/telegram-bot-klootviool.jar

wrap "restarting the service"
sudo systemctl restart telegram-bot-klootviool.service
