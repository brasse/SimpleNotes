#!/bin/bash

if [ -z $1 ]; then
    DB_FILE=notes.db
else
    DB_FILE=$1
fi

adb push $DB_FILE /data/data/com.github.simplenotes/databases
