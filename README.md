TomcatAutoLoginValve
=========================

Enables Auto Logging in for Basic Authentication with Tomcat.

Originally based on the code found at http://code.google.com/p/jugile-web2/source/browse/trunk/src/org/jugile/tomcat/AutoLoginValve.java?r=2
And refer to https://github.com/k-int/TomcatBasicAutoLoginValve/archive/TomcatBasicAutoLoginValve-1.1.tar.gz

The jar file needs to reside in the tomcat/lib directory and not the lib directory of your webapp.

This tomcat valve allows you to auto login either by specifying a username and password in the server.xml 

For this valve to kick in you need to add the following to your server.xml,
either under the Context or Host elements for where you want it to take effect.
the jar file needs to live in your tomcat/lib directory

<!--
<Valve className="org.apache.catalina.valves.AutoLoginValve" 
	   trustedIpAddresseses="<comma separated value of trusted ip addresses>"
   	   username="<username>"
   	   password="<password for username>"
	   role="<role for username>"
	   debug="<true or false>"/>
-->

The trustedIpAddresseses only needs to be the start of the address if you want to allow all addresses in that range,
eg. If you trust all addresses that begin with 192.168.1 then you only need to set trustedIpAddresseses to "192.168.1"

How to use? Example
===================

i) Get the source
wget https://github.com/lokechenlin/tomcat-auto-login-valve/archive/1.0.0.tar.gz 
tar -vxf 1.0.0.tar.gz
mv 1.0.0 TomcatAutoLoginValve

ii) Compile the source code
cd TomcatAutoLoginValve/
mkdir classes
javac -classpath /opt/apache-tomcat-7.0.56/lib/catalina.jar:/opt/apache-tomcat-7.0.56/lib/servlet-api.jar -d classes src/main/java/org/apache/catalina/valves/AutoLoginValve.java

iii) Compile the jar file
cd classes
jar -cvf auto-login-valve.jar org

iv) Move the file to tomcat/lib folder
sudo cp auto-login-valve.jar /opt/apache-tomcat-7.0.56/lib/

v) Edit the server.xml
sudo vi /opt/apache-tomcat-7.0.56/conf/server.xml
	# Add under <host>
	<Valve className="org.apache.catalina.valves.AutoLoginValve" trustedIpAddresses="xx.xx.xx" username="xxx" password="xxx" role="xxx" debug="true" />

vi) Restart server
sudo /etc/init.d/tomcat restart
