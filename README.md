###Clustered Security Configuration Service Admin Utility

This is a command line utility that uses the Copycat client to add/update/delete keys in a CSCS clustered KVM state machine.

$ export CLASSPATH="/opt/cscs-admin-utility/clustered-security-configuration-service-admin-utility-1.0.jar:/opt/cscs-admin-utility"

$ /opt/java/bin/java -cp $CLASSPATH edu.syr.eecs.cis.cscs.util.CscsUtility -action add -host 172.30.0.157:5000 -key testkey2 -value 777777
The key testkey2 was set to 777777

$ /opt/java/bin/java -cp $CLASSPATH edu.syr.eecs.cis.cscs.util.CscsUtility -action query -host 172.30.0.157:5000 -key testkey2
Key testkey2 has a value of 777777
