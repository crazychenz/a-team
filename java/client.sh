#!/bin/sh

java \
  -Dlog4j.configurationFile=log4j2.xml \
  -DlogFilename=client.log \
  -cp dist/clueless.jar ClientRun $@
