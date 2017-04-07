package tesla.cposcanner.props;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class EmailProperties extends DefaultHandler{
	private final String emailContextFileName = "email_context.xml";
	
	public final static String senderAddressPropertyName = "senderAddress";
	public final static String receiverAddressPropertyName = "receiverAddress";
	public final static String passwordPropertyName = "senderPassword";
	public final static String hostPropertyName = "hostName";
	public final static String SSLPropertyName = "setSSLOnConnect";
	public final static String setStartTLSName = "setStartTLS";
	
	private String senderAddress = "";
	private String receiverAddress = "";
	private String password = "";
	private String host = "";
	private String SSL = "";
	private String TLS = "";
	
	private boolean isSenderAddress = false;
	private boolean isReceiverAddress = false;
	private boolean isPassword = false;
	private boolean isHost = false;
	private boolean isSSL = false;
	private boolean isTLS = false;

	private Properties props;

	public EmailProperties(){
		try {
			generateProperties();
		} catch (ParserConfigurationException | SAXException e) {
			log.error("DBPropertiesDAO failed to parse: {}",emailContextFileName);
			e.printStackTrace();
		} catch (IOException e) {
			log.error("Failed to locate file: {}",emailContextFileName);
			e.printStackTrace();
		}
	}	
	
	public void generateProperties() throws ParserConfigurationException, SAXException, IOException{
		props = new Properties();
		initParser();
	}
	
	private void initParser() throws IOException, SAXException, ParserConfigurationException{
	    SAXParserFactory spf = SAXParserFactory.newInstance();
	    spf.setNamespaceAware(true);
	    SAXParser saxParser = spf.newSAXParser();
	    XMLReader xmlReader = saxParser.getXMLReader();
	    xmlReader.setContentHandler(this);
	    ClassLoader classLoader = getClass().getClassLoader();
	    InputStream stream = classLoader.getResourceAsStream(emailContextFileName);    
	    Reader reader = new InputStreamReader(stream,"UTF-8");
	    InputSource is = new InputSource(reader);
	    xmlReader.parse(is);
	}
	
	public void startElement(String uri, String localName, String qName, Attributes arg3) throws SAXException {
		if (qName.equalsIgnoreCase(passwordPropertyName)) {
			isPassword = true;
		}else if (qName.equalsIgnoreCase(senderAddressPropertyName)) {
			isSenderAddress = true;
		}else if (qName.equalsIgnoreCase(receiverAddressPropertyName)) {
			isReceiverAddress = true;
		}else if (qName.equalsIgnoreCase(hostPropertyName)) {
			isHost = true;
		}else if (qName.equalsIgnoreCase(emailContextFileName)) {
			//Ignore - this is the root element
		}else if (qName.equalsIgnoreCase(setStartTLSName)) {
			isTLS = true;
		}else{ 
			isSSL = true;
		}
	}
	
	public void characters(char ch[], int start, int length) throws SAXException {
		if (isSenderAddress) {
			senderAddress = new String(ch, start, length);
			isSenderAddress = false;
		}
		
		if (isReceiverAddress) {
			receiverAddress = new String(ch, start, length);
			isReceiverAddress = false;
		}
		
		if (isPassword) {
			password = new String(ch, start, length);
			isPassword = false;
		}

		if (isHost) {
			host = new String(ch, start, length);
			isHost = false;
		}

		if (isSSL) {
			SSL = new String(ch, start, length);
			isSSL = false;
		}
		if (isTLS) {
			TLS = new String(ch, start, length);
			isTLS = false;
		}
	}
	
	public void endDocument(){
		if(senderAddress.equals("")){
			log.error("Unable to parse sender email address from email_context.xml. Cannot send emails");
		}
		if(receiverAddress.equals("")){
			log.error("Unable to parse receiver email address from email_context.xml. Cannot send emails");
		}
		if(password.equals("")){
			log.error("Unable to parse password from email_context.xml. Cannot send emails");		
		}
		if(host.equals("")){
			log.error("Unable to parse host value from email_context.xml. Cannot send emails");
		}
		if(SSL.equals("")){
			log.error("Unable to parse SSL value from email_context.xml. Cannot send emails");
		}
		if(TLS.equals("")){
			log.error("Unable to parse TLS value from email_context.xml. Cannot send emails");
		}
	}
	
	public boolean getIsSSL(){
		return isSSL;
	}
	
	public boolean getIsTLS(){
		return isTLS;
	}
	
}
