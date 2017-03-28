#!/bin/bash

#export JAVA_HOME="/opt/java/jdk1.5.0_16"
export JAVA_HOME="/opt/java/jdk1.6.0_31"
export MAVEN_OPTS=-Xmx512m
echo ""
echo "JAVA_HOME is '$JAVA_HOME'"

#TARGET_DIR="/home/ghost/soft/java/tomcatdm-6.0.20/webapps/webtop"
PROJECT_NAME="URT STATS    "


checkInterrupt(){
    ST=$?
    if [ $ST -eq 130 ] ; then
        echo ""
        echo ""
        echo "BUILD INTERRUPTED."
        echo ""
        echo "" 
        exit 1
    fi
}

checkErrors() {
    ST=$?
    if [ $ST -eq 130 ] ; then
        echo ""
        echo ""
        echo "BUILD INTERRUPTED."
        echo ""
        echo "" 
        exit 1
    elif [ $ST -ne 0 ] ; then
        notify-send -u critical "$PROJECT_NAME: BUILD FAILED WITH ERROR!!!"
        echo "    !!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
        echo "    !! $PROJECT_NAME           !!"
        echo "    !! BUILD FAILED WITH ERROR !!"
        echo "    !!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
        exit 1
    else
        echo "    !!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
        echo "    !!    $PROJECT_NAME        !!" 
        echo "    !!    BUILD SUCCESSFULL    !!"
        echo "    !!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
    fi
}

echo ""
echo "----------------------------------------"
echo "> FULL PROJECT BUILD STARTED"
echo "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
echo ""

echo "> BUILDING PROJECT..."
echo ""

if [ "$1" = "clean" ] ; then
    echo ""
    echo "  CLEAN before build requested"
   echo ""
    mvn clean && mvn install -Dmaven.test.skip=true
else
    mvn install -Dmaven.test.skip=true
fi

checkErrors

notify-send -u normal "$PROJECT_NAME: BUILD SUCCESSFULL"


echo "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
echo "> FULL PROJECT BUILD FINISHED"
echo "========================================"
echo ""
echo "end time:"
date
echo "////////////////////////////////////////"
echo ""
echo ""
