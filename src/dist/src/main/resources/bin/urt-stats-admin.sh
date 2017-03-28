#!/bin/bash

CLASSPATH="../lib/*:../cfg/jdbc.properties:."
#echo "Classpath: $CLASSPATH"

java -classpath "$CLASSPATH" urt.stats.core.UrTStatsAdmin "$@"
