#!/bin/bash
################################################################################
# Compiles the client and server from wsdl and copies the files into the path
#
# @author	Alexander Willner (willner@cs.uni-bonn.de)
# @version	$Id: wsdl2java.sh 2946 2008-05-06 13:28:17Z gassen@cs.uni-bonn.de $
################################################################################

chmod +x $MUSE_HOME/bin/*.sh >> log/generator.log 2>&1

perl $DIR_SCRIPTS/wsdl2java.pl $*

exit;

