package com.jeo.component;


import org.eclipse.jetty.util.log.Log;
import org.xmpp.component.Component;
import org.xmpp.component.ComponentException;
import org.xmpp.component.ComponentManager;
import org.xmpp.packet.JID;
import org.xmpp.packet.Packet;

public class ChatComponent implements Component{
	private static final String name="chat";
	private static final String description="实现多人通过组件在区域内聊天";
	private JID jid;
	private ComponentManager comMgr;
	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public void processPacket(Packet packet) {
		System.out.println("processPacket:"+packet.toXML());
		Log.info(packet.toXML());
		
	}

	public void initialize(JID jid, ComponentManager componentManager)
			throws ComponentException {
		this.jid=jid;
		this.comMgr=componentManager;
	}

	public void start() {
		System.out.println("component start");
		Log.info("component start");
		
	}

	public void shutdown() {
		System.out.println("component shutdown");
		Log.info("component shutdown");
		
	}

}
