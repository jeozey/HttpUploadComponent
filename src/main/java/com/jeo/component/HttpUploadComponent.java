package com.jeo.component;

import org.dom4j.Element;
import org.eclipse.jetty.util.log.Log;
import org.xmpp.component.AbstractComponent;
import org.xmpp.component.ComponentException;
import org.xmpp.component.ComponentManager;
import org.xmpp.component.ComponentManagerFactory;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;
import org.xmpp.packet.Presence;

public class HttpUploadComponent extends AbstractComponent {
	private static final String name = "upload";
	private static final String description = "Http Upload Component";
	private JID jid;
	private String urlPut,urlGet;
	private long fileMax = 5242880;
	private ComponentManager comMgr;
	
	public HttpUploadComponent(String urlPut,String urlGet,long fileMax){
		this.urlPut = urlPut;
		this.urlGet = urlGet;
		this.fileMax = fileMax;
	}
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	protected IQ handleDiscoInfo(IQ iq) {
		System.out.println("handleDiscoInfo:" + iq.toXML());

		final IQ replyPacket = IQ.createResultIQ(iq);
		final Element responseElement = replyPacket.setChildElement("query",
				NAMESPACE_DISCO_INFO);

		// identity
		responseElement.addElement("identity")
				.addAttribute("category", "store").addAttribute("type", "file")
				.addAttribute("name", getName());
		// features
		responseElement.addElement("feature").addAttribute("var",
				"urn:xmpp:http:upload");
		//http://blog.csdn.net/yougou_sully/article/details/6565060
		Element xElement = responseElement.addElement("x","jabber:x:data").addAttribute("type", "result");
		Element filedElement = xElement.addElement("field")
				.addAttribute("var", "FORM_TYPE")
				.addAttribute("type", "hidden");
		filedElement.addElement("value").addText("urn:xmpp:http:upload");

		Element filedElement1 = xElement.addElement("field").addAttribute(
				"var", "max-file-size");
		filedElement1.addElement("value").addText(""+fileMax);

		return replyPacket;
	}
	
	@Override
	protected IQ handleIQGet(IQ iq) throws Exception {
		System.out.println("handleDiscoInfo:" + iq.toXML());

		final IQ replyPacket = IQ.createResultIQ(iq);
		final Element slotElement = replyPacket.setChildElement("slot", 
				"urn:xmpp:http:upload");
		String str = ""+System.currentTimeMillis()+"_"+iq.getChildElement().elementText("filename");
		slotElement.addElement("put").addText(urlPut+str);
		slotElement.addElement("get").addText(urlGet+str);
		return replyPacket;

	}

	
	@Override
	protected void handleIQResult(IQ iq) {
		System.out.println("handleIQResult:" + iq.toXML());
		super.handleIQResult(iq);
	}

	@Override
	protected void handleIQError(IQ iq) {
		System.out.println("handleIQError:" + iq.toXML());
		super.handleIQError(iq);
	}

	@Override
	protected IQ handleIQSet(IQ iq) throws Exception {
		System.out.println("handleIQSet:" + iq.toXML());
		return super.handleIQSet(iq);
	}

	@Override
	protected IQ handleDiscoItems(IQ iq) {
		System.out.println("handleDiscoItems:" + iq.toXML());
		return super.handleDiscoItems(iq);
	}

	@Override
	protected IQ handlePing(IQ iq) {
		System.out.println("handlePing:" + iq.toXML());
		return super.handlePing(iq);
	}

	@Override
	protected IQ handleLastActivity(IQ iq) {
		System.out.println("handleLastActivity:" + iq.toXML());
		return super.handleLastActivity(iq);
	}

	@Override
	protected IQ handleEntityTime(IQ iq) {
		// TODO Auto-generated method stub
		return super.handleEntityTime(iq);
	}

	@Override
	protected void handlePresence(Presence presence) {
		// TODO Auto-generated method stub
		super.handlePresence(presence);
	}

	@Override
	protected void handleMessage(Message message) {
		System.out.println("handleMessage:" + message.toXML());
		// TODO Auto-generated method stub
		super.handleMessage(message);
	}

}
