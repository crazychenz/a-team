#!/bin/sh

# TODO: Fix this
#  -Djava.util.logging.manager=org.apache.logging.log4j.jul.LogManager \

java -cp dist/clueless.jar \
  -Dlog4j.configurationFile=log4j2.xml \
  -DlogFilename=clitester.log \
  CLITester $@
