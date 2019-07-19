#!/usr/bin/env bash

echo "removing old jar"
rm /var/opt/services/telegram-bot-klootviool/telegram-bot-klootviool-0.0.1-SNAPSHOT.jar

echo "ensure jar is executable"
chmod +x target/telegram-bot-klootviool-0.0.1-SNAPSHOT.jar

echo "copying new jar to /var/opt/services/opt/services/telegram-bot-klootviool"
cp target/telegram-bot-klootviool-0.0.1-SNAPSHOT.jar /var/opt/services/telegram-bot-klootviool/

echo "restarting the service"
sudo systemctl restart telegram-bot-klootviool.service
