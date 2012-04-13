#!/bin/sh

    find_package_root() {
        find $1/src/main/java/ -type d \( ! -links 3 -or -exec sh -c "find {} -maxdepth 1 -type f | grep -q ''" \; \)  -print -quit
    }

    package2path() {
        echo $1 | sed -e 's:\.:/:g'
    }

    path2package() {
        echo $1 | sed -e "s:.*/src/.*/java/::" -e "s:/:.:g"
    }

    move() {
        if [ -d $1 ]; then
            mkdir -p $2
            git mv $1/* $2/
        fi
    }

    replace_package() {
        find * -type f -exec sed -i -e "/artifactId/ n" -e "s:$1\([;.*]\):$2\1:g" {} \;
    }

    # The script can run in two modes:
    #
    # - Either a BASE directory and a package PREFIX is provided as
    #   arguments. In this case the script will proposed a rename map.
    #
    # - Or the script can read a rename map from stdin and perform the
    #   renaming.
    #

    if [ $# -eq 2 ]; then
        BASE=$1
        PREFIX=$2

        for DIR in $BASE/*; do
            if [ -d $DIR ]; then
                OLD=$(path2package $(find_package_root $DIR))
                NEW=$PREFIX.$(basename $DIR)
                echo $DIR $OLD $NEW
            fi
        done | sort -r --key=2,2 | column -t
    else
        while read DIR OLDP NEWP; do
            OLD=$(package2path $OLDP)
            NEW=$(package2path $NEWP)

            move $DIR/src/main/java/$OLD/ $DIR/src/main/java/$NEW
            move $DIR/src/test/java/$OLD/ $DIR/src/test/java/$NEW

            replace_package $OLDP $NEWP

            echo "$OLDP -> $NEWP"
        done
    fi
