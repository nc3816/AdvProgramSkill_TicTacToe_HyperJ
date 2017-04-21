#!/bin/sh

# Build a HyperJ system
# James Heliotis
# Rochester Institute of Technology
# January 2013

if [ $# -lt 1 ]; then
    echo Usage: `basename $0` program-name 1>&2
    echo For a description of the script: `basename $0` help 1>&2
    exit 1
fi

if echo $1 | grep -iq help; then
    echo HOW TO USE `basename $0`
    echo
    cat << ENDIT
To run this script, you must be in the directory that is the root
of all your local packages. The HyperJ hyperspace (.hs), concern
mappings (.cm), and hypermodule (.hm) files must all have the same
name (before the suffix) and exist in the current directory.
The name of the hypermodule must also be that same name.

When this script is executed, all class files will be erased and
the java code will be recompiled. Then HyperJ will be used to weave
the code together according to the configuration files. The
result is put in a subdirectory named the same as the hypermodule.

You must change to that directory to execute your program.

LIMITATION: no nested packages are allowed.

ENDIT
    exit 0
fi

if [ -z "$HYPERJ_DIR" ]; then
    # HYPERJ_DIR="$PWD"
    HYPERJ_DIR=`dirname $0`
    echo Expecting to find jar files in $HYPERJ_DIR
fi

echo "Removing old classes"
/bin/rm */*.class
echo "Compiling */*.java"
echo javac \
          -d . \
          -cp $HYPERJ_DIR/hyperj.jar:$CLASSPATH \
          -Xlint:unchecked \
          */*.java
if javac \
          -d . \
          -cp $HYPERJ_DIR/hyperj.jar:$CLASSPATH \
          -Xlint:unchecked \
          */*.java; then
    progname=$1
    shift
    echo "Removing directory $progname"
    /bin/rm -rf $progname
    echo "Merging"
    echo java -classpath .:$HYPERJ_DIR/hyperj.jar:$HYPERJ_DIR/rt.jar com.ibm.hyperj.hyperj $* \
        -hyperspace ${progname}.hs \
        -concerns ${progname}.cm \
        -hypermodules ${progname}.hm
    java -classpath .:$HYPERJ_DIR/hyperj.jar:$HYPERJ_DIR/rt.jar com.ibm.hyperj.hyperj $* \
        -hyperspace ${progname}.hs \
        -concerns ${progname}.cm \
        -hypermodules ${progname}.hm
fi

