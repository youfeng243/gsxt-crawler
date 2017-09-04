#!/usr/bin/env bash

project=gsxt-crawler
main_class=com.haizhi.GsxtServer

check() {
    pid=`ps -ef | grep java | grep "$1" | grep "$2" | awk '{print $2}'`
    if [ -z "${pid}" ]; then
        exit
    fi

    echo ${pid}
}

if [ $# == 2 ]; then
    echo $(check $1 $2)
else
    echo $(check ${project} ${main_class})
fi