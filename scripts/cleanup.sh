#!/bin/bash

BASE_DIR=/var/opt/services/telegram-bot-klootviool

echo "removing old logback file"
rm ${BASE_DIR}/logback.xml

echo "removing old jar"
rm ${BASE_DIR}/*.jar
