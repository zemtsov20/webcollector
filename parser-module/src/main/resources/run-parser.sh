##!/bin/bash
#
#JAR=surfer-bot-`pwd | sed 's/[^\/]*\///g'`.jar
#
##Alexey@ZOLENENOK2 /cygdrive/c/Users/Alexey/IdeaProjects/mailextract/parser-module/src/main/resources
##$ ls
##run-parser.sh
##
##Alexey@ZOLENENOK2 /cygdrive/c/Users/Alexey/IdeaProjects/mailextract/parser-module/src/main/resources
##$ dos2unix.exe run-parser.sh
##dos2unix: converting file run-parser.sh to Unix format...
##
##Alexey@ZOLENENOK2 /cygdrive/c/Users/Alexey/IdeaProjects/mailextract/parser-module/src/main/resources
##$ ./run-parser.sh
##cat: proxies.txt: No such file or directory
#
#
#PROXY_PORT=34512
#
#DB_USER=prod
#DB_PASS=prod
#DB_NAME=prod
#
#server_port=22000
#
#for PROXY_HOST in `cat proxies.txt`; do
#  server_port=$((server_port+1))
#  d=`date +%F`
#  java \
#  -Dserver.port=$server_port \
#  -Dproxy.server=$PROXY_HOST \
#  -Dproxy.port=$PROXY_PORT \
#  -Ddb.user=$DB_USER \
#  -Ddb.pass=$DB_PASS \
#  -Ddb.name=$DB_NAME \
#  -jar $JAR >> bot-$d-$server_port-$PROXY_HOST.log 2>&1 &
#done
