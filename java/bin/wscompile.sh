#!/bin/sh

#
# Copyright 2004 Sun Microsystems, Inc. All rights reserved.
# SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
#


# -----------------------------------------------------------------------------
#
# Script for launching wscompile using the Launcher
#
# -----------------------------------------------------------------------------

# Resolve links - $0 may be a softlink
PRG="$0"

while [ -h "$PRG" ]; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '.*/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`/"$link"
  fi
done

# Get standard environment variables
PRGDIR=`dirname "$PRG"`
if [ -r "$PRGDIR"/../../jwsdp-shared/bin/setenv.sh ]; then
  . "$PRGDIR"/../../jwsdp-shared/bin/setenv.sh
fi

# Make sure prerequisite environment variables are set
if [ -z "$JAVA_HOME" ]; then
  echo "The JAVA_HOME environment variable is not defined"
  echo "This environment variable is needed to run this program"
  exit 1
fi

# Execute the Launcher using the "wscompile" target
exec "$JAVA_HOME"/bin/java -classpath "$PRGDIR":"$PRGDIR"/../../jwsdp-shared/bin LauncherBootstrap -verbose wscompile "$@"
