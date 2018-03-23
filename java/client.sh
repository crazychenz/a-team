#!/bin/sh

java \
  -Dlog4j.configurationFile=log4j2.xml \
  -DlogFilename=client.log \
  -jar dist/clueless.jar --enable-logger --cli-client $@
