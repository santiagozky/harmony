#!/bin/bash
HELP=false
SVN=https://svn.uva.netherlight.nl/projects/phosphorus-wp1/modules
USER=`whoami`@cs.uni-bonn.de
LOGMESSAGE="Tagged a stable version"
SVNTAG=Stable-`date +"%Y%m%d"`
MODULES=( \
adapter/dummy \
adapter/argon \
#adapter/thin \
adapter/uclp \
common/serviceinterface \
common/security \
common/ant \
common/utils \
idb \
ui/reservationcli \
ui/topologygui \
ui/webgui \
)


while [ 1 -le $# ]
do
    case "$1" in
        "--username")
                        USER=$2 ;;
        "--svn")
                        SVN=$2 ;;
        "--message")
                        LOGMESSAGE=$2 ;;
        "--tag")
                        SVNTAG=$2 ;;
       "-h")
                        HELP=true ;;
             *) ;;
        esac
        shift
done

if [ "$HELP" == "true" ]; then
  echo "Usage: $0 <options>";
  echo "";
  echo "--username <username>          Change user name";
  echo "--svn <repository>             Change repository";
  echo "--message <commit message>     Change message";
  echo "--tag <subversion tag name>    Change tag name";
  echo "-h                             This help";
  echo "";
  echo "Mail bug reports and suggestions to <willner@cs.uni-bonn.de>.";
  exit;
  echo 
fi


clear
echo "Using the following configuration:"
echo "----------------------------------"
echo "Repository : $SVN";
echo "Message    : $LOGMESSAGE";
echo "Tag        : $SVNTAG";
echo "User       : $USER";
echo "";
echo "To change these values run $0 -h";
echo "";
echo "(press CTRL+C to cancel or ENTER to continue)";
read

for ((i=0;i<${#MODULES[*]};i++)); do
   module="${MODULES[${i}]}";
   svn copy $SVN/$module/trunk $SVN/$module/tags/$SVNTAG --username $USER -m "$LOGMESSAGE"
done

