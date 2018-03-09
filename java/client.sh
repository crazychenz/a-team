#!/bin/sh

# TODO: Fix this
#  -Djava.util.logging.manager=org.apache.logging.log4j.jul.LogManager \

java -cp lib/*:dist/clueless.jar \
  -Dlog4j.configurationFile=log4j2.xml \
  -DlogFilename=client.log \
  ClientRun $@
