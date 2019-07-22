#!/usr/bin/env bash

BASE_DIR=/var/opt/services/telegram-bot-klootviool
CONF_DIR=${BASE_DIR}/config

echo "removing old logback file"
rm ${CONF_DIR}/logback.xml

echo "copying new logback file"
cp target/classes/logback-server.xml ${CONF_DIR}/logback.xml

echo "removing old jar"
rm ${BASE_URL}/telegram-bot-klootviool-0.0.1-SNAPSHOT.jar

echo "ensure jar is executable"
chmod +x target/telegram-bot-klootviool-0.0.1-SNAPSHOT.jar

echo "copying new jar to /var/opt/services/opt/services/telegram-bot-klootviool"
cp target/telegram-bot-klootviool-0.0.1-SNAPSHOT.jar ${BASE_URL}/

echo "restarting the service"
sudo systemctl restart telegram-bot-klootviool.service
