#!/bin/bash

# exit when anything fails
set -euo pipefail

BASE_DIR=/var/opt/services/telegram-bot-klootviool

echo "removing old logback file"
rm ${BASE_DIR}/logback.xml

echo "copying new logback file"
cp target/classes/logback-server.xml ${BASE_DIR}/logback.xml

echo "removing old jar"
rm ${BASE_DIR}/telegram-bot-klootviool.jar

echo "ensure jar is executable"
chmod +x target/*.jar

echo "copying new jar to /var/opt/services/opt/services/telegram-bot-klootviool"
cp target/telegram-bot-klootviool-0.0.1-SNAPSHOT.jar ${BASE_DIR}/telegram-bot-klootviool.jar

echo "restarting the service"
sudo systemctl restart telegram-bot-klootviool.service

echo "sleeping and then trying to send deploy message from the service"
ret=0
i=1
while [ ${i} -le 10 -a "${ret}" -ne "200" ]
do
	echo "waiting for 15 secs"
	sleep 15
	ret=`curl -s -o /dev/null -w "%{http_code}" -XPOST http://localhost:8101/message/-320932775/deployed_new_version`
	echo "return code: ${ret}"
	let i=${i}+1
done
