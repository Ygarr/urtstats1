#!/bin/bash

IMPORT_DATA_FILE=$1

file_lines=($(cat "$IMPORT_DATA_FILE"))


checkErrors() {
    if [ "$?" -ne "0" ] ; then
        echo ""
        echo "The entire script stopped."
        echo ""
        exit 1
    fi
}

file_count=${#file_lines[*]}
idx=0

for file_path in ${file_lines[@]}; do
    idx=$[$idx+1]
    echo ""
    echo "Processing: $idx of $file_count : '$file_path'"
    ./urt-stats-admin.sh import "$file_path"
    checkErrors
    echo ""
    echo "Done"
    echo "-------------------------------------------------------------------------"
done
