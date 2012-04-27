#!/bin/bash
################################################################################
# Compiles the NRPSAdapter wsdl and copies the files into the path
#
# @author   Alexander Willner (willner@cs.uni-bonn.de)
# @version  $Id: generateJaxbClasses.sh 2177 2008-02-12 12:50:39Z zimmerm2@cs.uni-bonn.de $
################################################################################


#chmod +x $3/bin/*.sh >> $4/generator.log 2>&1

rm -rf $1/org/opennaas/extension/idb/serviceinterface/databinding/jaxb/
mkdir $1/org/opennaas/extension/idb/serviceinterface/databinding/jaxb/

chmod +x $3/bin/xjc.sh

$3/bin/xjc.sh -extension -no-header -d $1 -p org.opennaas.extensions.idb.serviceinterface.databinding.jaxb $2/Reservation-Types.xsd $2/Topology-Types.xsd $2/Notification-Types.xsd >> $4/generator.log 2>&1
if [ "$?" != "0" ]; then
  echo "failed. (please press enter to see the log file)";
  read
  less $4/generator.log
  exit;
fi

rm -rf $1/org/opennaas/extension/idb/serviceinterface/databinding/jaxb/exceptions
mkdir $1/org/opennaas/extension/idb/serviceinterface/databinding/jaxb/exceptions

echo $5

perl $5/generateFaultExceptions.pl $1/org/opennaas/extension/idb/serviceinterface/databinding/jaxb $1/org/opennaas/extension/idb/serviceinterface/databinding/jaxb/exceptions
perl $5/makeJaxbClassesClonable.pl $1/org/opennaas/extension/idb/serviceinterface/databinding/jaxb
#rm $1/org/opennaas/extension/idb/serviceinterface/databinding/jaxb/*.bak* $1/org/opennaas/extension/idb/serviceinterface/databinding/jaxb/exceptions/*.bak*
