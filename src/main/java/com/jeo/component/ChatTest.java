package com.jeo.component;

import org.jivesoftware.whack.ExternalComponentManager;
import org.xmpp.component.ComponentException;

public class ChatTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ExternalComponentManager mgr = new ExternalComponentManager(  
                "192.168.1.105", 5275);  
        mgr.setSecretKey("chat", "chatKey");  
        try {  
            mgr.addComponent("chat", new ChatComponent());  
        } catch (ComponentException e) {  
        	e.printStackTrace();
            System.exit(-1);  
        }  
        // daemon  
        while (true)  
            try {  
                Thread.sleep(10000);  
            } catch (Exception e) {  
            }  

	}

}
