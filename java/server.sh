#!/bin/sh

java \
  -Dlog4j.configurationFile=log4j2.xml \
  -DlogFilename=server.log \
  -jar dist/clueless.jar --enable-logger --server-only $@
