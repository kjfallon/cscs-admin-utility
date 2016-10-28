#! /bin/sh

CSCSUTILITY_PROPFILE="cscs-utility.properties"
export CSCSUTILITY_PROPFILE

CLASSPATH="/opt/cscs-admin-utility/clustered-security-configuration-service-admin-utility-1.0.jar:/opt/cscs-admin-utility"

/opt/java/bin/java -Xmx128m -cp $CLASSPATH edu.syr.eecs.cis.cscs.util.CscsUtility