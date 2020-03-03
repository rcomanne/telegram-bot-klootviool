#!/bin/bash

BASE_DIR=/var/opt/services/telegram-bot-klootviool

echo "#==============================#"
echo "#       starting cleanup       #"

echo "#  removing old logback file   #"
rm ${BASE_DIR}/logback.xml

echo "#       removing old jar       #"
rm ${BASE_DIR}/*.jar

echo "#       cleanup complete       #"
echo "#==============================#"
