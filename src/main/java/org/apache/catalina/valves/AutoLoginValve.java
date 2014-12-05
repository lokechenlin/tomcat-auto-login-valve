package org.apache.catalina.valves;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.realm.GenericPrincipal;
import org.apache.catalina.valves.ValveBase;

/** 
 *  Allows auto login in tomcat so we can bypass the BASIC authentication, for specific IPs
 *  
 *  Based on the code at http://code.google.com/p/jugile-web2/source/browse/trunk/src/org/jugile/tomcat/AutoLoginValve.java?r=2
 *   & https://github.com/k-int/TomcatBasicAutoLoginValve/archive/TomcatBasicAutoLoginValve-1.1.tar.gz
 *     
 *  Entry in server.xml will look something like  
 *  <Valve className="org.apache.catalina.valves.AutoLoginValve" 
 *		trustedIpAddresseses="127.0.0.1" 
 *   	username="xyz" 
 *      password="abc" 
 *      role="" 
 *      debug="false"/>
 *  trustedIpAddresses can be a comma list of values
 *  trustedIpAddresses only needs to be the start of the address, 
 *  so if you want all addresses that begin with 192.168.1 to be trusted then you only need to set trustedIpAddresses to "192.168.1"
 *  
 */

public class AutoLoginValve extends ValveBase {

	private List<String> trustedIpAddresses = new ArrayList<String>();
	private String username;
    private String password;
	private String role;
    private Boolean debug = new Boolean(false);

    public AutoLoginValve() {
    }

    @Override
    public void invoke(final Request request, final Response response) 
             throws IOException, ServletException {

        final String remoteAddr = request.getRemoteAddr();
		final String forwarded = request.getHeader("X-Forwarded-For");

		final boolean isTrustedIp = isTrusted(remoteAddr) || isTrusted(forwarded);

		if (debug) {
			System.out.println(
				"remoteAddr: " + remoteAddr + 
				", forwarded-for: " + forwarded + 
				", trusted ip: " + trustedIpAddresses.toString() + 
				", isTrustedIp: " + isTrustedIp
			);			
		}
        
        if (isTrustedIp) {
         
            final List<String> roles = new ArrayList<String>();
            roles.add(role);
			
            final Principal principal = new GenericPrincipal(username, password, roles);
            request.setUserPrincipal(principal);
        }

        getNext().invoke(request, response);
    }

	private boolean isTrusted(String ipAddress) {

    	boolean trusted = false;
    	
        if (ipAddress != null) {

        	for (String trustedAddress : trustedIpAddresses) {

                if (ipAddress.startsWith(trustedAddress)) {
                	               	
					trusted = true;                
                	break;
                }
        	}
        }
        return(trusted);
    }

    public void setTrustedIpAddresses(final String trustedIpAddresses) {
		
		if ((trustedIpAddresses != null) && !trustedIpAddresses.isEmpty()) {

    		String [] stringArray = trustedIpAddresses.split(",");
    		for (String stringElement : stringArray) {

    			String trimmed = stringElement.trim();
    			if (!trimmed.isEmpty()) {

    				this.trustedIpAddresses.add(trimmed);
    			}
    		}
    	}
    }

	public void setUsername(final String username) {

    	if ((username != null) && !username.isEmpty()) {

	        this.username = username;
    	}
    }
 
    public void setPassword(final String password) {

    	if ((password != null) && !password.isEmpty()) {

    		this.password = password;
    	}
    }

	public void setRole(final String role) {

    	if ((role != null) && !role.isEmpty()) {

    		this.role = role;
    	}
    }
    
    public void setDebug(final Boolean debug) {

    	if (debug != null) {

    		this.debug = debug;
    	}
    }
}
