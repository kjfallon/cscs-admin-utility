###Clustered Security Configuration Service Admin Utility

This is a Java command line utility that implements the Copycat client (http://atomix.io/copycat/) to add/update/delete keys in a CSCS clustered KVM state machine.  This administration tool is to be used from a trusted host outside of the manged cluster to sign and update KVPs.  The key value is a tuple of the data and a SHA256withRSA signature of the data. Nodes running the Clustered Security Configuration Service validate the signature with a public key. 

Setting path
-------------
$ export CLASSPATH="/opt/cscs-admin-utility/clustered-security-configuration-service-admin-utility-1.0.jar:/opt/cscs-admin-utility"

Setting keys
-------------
$ /opt/java/bin/java -cp $CLASSPATH edu.syr.eecs.cis.cscs.util.CscsUtility -action add -host 172.30.0.157:5000 -key domain1.yum.centos7.repo.name -value "demoRepo"
Result: The key domain1.yum.centos7.repo.name was set to demoRepo
Raw key value tuple with both data and signature base 64 encoded is:
ZGVtb1JlcG8=,jpr072vbJ9lBMjaCAXNb2/o1J525gz4c16p+jwes0q+lEcRJSfD1ZdOHZG0JN9qpW3NB4MJ3kmEisPP9c5yfmxRivZMSXhbOvzDuH0OQSTmq8bsU/jadJyD615kpusigS//9iRD9VCB9dEho7RZiNVlCwChBJhXMaOYlizZOkZI=

$ /opt/java/bin/java -cp $CLASSPATH edu.syr.eecs.cis.cscs.util.CscsUtility -action add -host 172.30.0.157:5000 -key domain1.yum.centos7.repo.baseurl -value "https://mirror.syr.edu/centos/"
Result: The key domain1.yum.centos7.repo.baseurl was set to https://mirror.syr.edu/centos/
Raw key value tuple with both data and signature base 64 encoded is:
aHR0cHM6Ly9taXJyb3Iuc3lyLmVkdS9jZW50b3Mv,VXvAodFgzeE4xEMDQof+od7s1GpzgVX+/WJLT65T8fjwe6SXzQmbRD6nNWxFeAspHuDJ1MRE7lZtQlsjzSjFS9im4EJbgA0cNgJTyXSAscYblslqLLztljEh9qIyYSYcm+zJhIrtDZvCY+hm8eri98fN+fgQD8ZkiGUl07GBXEk=

$ /opt/java/bin/java -cp $CLASSPATH edu.syr.eecs.cis.cscs.util.CscsUtility -action add -host 172.30.0.157:5000 -key domain1.yum.centos7.repo.enabled -value "0"
Result: The key domain1.yum.centos7.repo.enabled was set to 0
Raw key value tuple with both data and signature base 64 encoded is:
MA==,Qu6lLzq26FG1h1JDmrS5LKw7Eo0rMvcVelL1r5jMQnUrZD47t7RjY2+dSV/K51bMBOaWkmedxUOZny3EmlGuRCmVsv4rhBLIIxtQOPnF1Tv0f9lRRWgfJHJiwMrwpehu/NSUiM7BTNp0/pu3BPkqwPwnguagJLCOh5uiOmodSD8=


Querying keys
--------------
$ /opt/java/bin/java -cp $CLASSPATH edu.syr.eecs.cis.cscs.util.CscsUtility -action query -host 172.30.0.72:5000 -key domain1.yum.centos7.repo.name
Result: Key domain1.yum.centos7.repo.name has a value of demoRepo
Result: Signature validation is: true

$ /opt/java/bin/java -cp $CLASSPATH edu.syr.eecs.cis.cscs.util.CscsUtility -action query -host 172.30.0.72:5000 -key domain1.yum.centos7.repo.baseurl
Result: Key domain1.yum.centos7.repo.baseurl has a value of https://mirror.syr.edu/centos/
Result: Signature validation is: true

$ /opt/java/bin/java -cp $CLASSPATH edu.syr.eecs.cis.cscs.util.CscsUtility -action query -host 172.30.0.72:5000 -key domain1.yum.centos7.repo.enabled
Result: Key domain1.yum.centos7.repo.enabled has a value of 0
Result: Signature validation is: true 
