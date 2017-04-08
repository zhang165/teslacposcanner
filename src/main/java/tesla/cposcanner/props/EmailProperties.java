package tesla.cposcanner.props;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

/**
 * XML parser for email_context.xml
 * @author zhang165
 *
 */
@Data
@Slf4j
public class EmailProperties extends DefaultHandler{
	private static final String EMAIL_CONTEXT_FILE_NAME = "email_context.xml";
	
	public final static String SENDER_ADDRESS_NAME = "senderAddress";
	public final static String RECEIVER_ADDRESS_NAME = "receiverAddress";
	public final static String PASSWORD_NAME = "senderPassword";
	public final static String HOST_NAME = "hostName";
	public final static String SSL_NAME = "setSSLOnConnect";
	public final static String SET_START_TLS_NAME = "setStartTLS";
	
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
		props = new Properties();
		try {
			generateProperties();
		} catch (ParserConfigurationException | SAXException e) {
			log.error("Failed to parse: {}",EMAIL_CONTEXT_FILE_NAME);
			e.printStackTrace();
		} catch (IOException e) {
			log.error("Failed to locate file: {}",EMAIL_CONTEXT_FILE_NAME);
			e.printStackTrace();
		}
	}	
	
	private void generateProperties() throws ParserConfigurationException, SAXException, IOException{
		final SAXParserFactory spf = SAXParserFactory.newInstance();
	    spf.setNamespaceAware(true);
	    final SAXParser saxParser = spf.newSAXParser();
	    final XMLReader xmlReader = saxParser.getXMLReader();
	    xmlReader.setContentHandler(this);
	    final ClassLoader classLoader = getClass().getClassLoader();
	    final InputStream stream = classLoader.getResourceAsStream(EMAIL_CONTEXT_FILE_NAME);    
	    final InputSource is = new InputSource(new InputStreamReader(stream,"UTF-8"));
	    xmlReader.parse(is);
	}
	
	public void startElement(String uri, String localName, String qName, Attributes arg3) throws SAXException {
		if (qName.equalsIgnoreCase(PASSWORD_NAME)) {
			isPassword = true;
		}else if (qName.equalsIgnoreCase(SENDER_ADDRESS_NAME)) {
			isSenderAddress = true;
		}else if (qName.equalsIgnoreCase(RECEIVER_ADDRESS_NAME)) {
			isReceiverAddress = true;
		}else if (qName.equalsIgnoreCase(HOST_NAME)) {
			isHost = true;
		}else if (qName.equalsIgnoreCase(EMAIL_CONTEXT_FILE_NAME)) {
			//Ignore - this is the root element
		}else if (qName.equalsIgnoreCase(SET_START_TLS_NAME)) {
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
			log.error("Unable to parse sender email address from {}. Cannot send emails",EMAIL_CONTEXT_FILE_NAME);
		}
		if(receiverAddress.equals("")){
			log.error("Unable to parse receiver email address from {}. Cannot send emails", EMAIL_CONTEXT_FILE_NAME);
		}
		if(password.equals("")){
			log.error("Unable to parse password from {}. Cannot send emails",EMAIL_CONTEXT_FILE_NAME);		
		}
		if(host.equals("")){
			log.error("Unable to parse host value from {}. Cannot send emails",EMAIL_CONTEXT_FILE_NAME);
		}
		if(SSL.equals("")){
			log.error("Unable to parse SSL value from {}. Cannot send emails",EMAIL_CONTEXT_FILE_NAME);
		}
		if(TLS.equals("")){
			log.error("Unable to parse TLS value from {}. Cannot send emails",EMAIL_CONTEXT_FILE_NAME);
		}
	}
	
}
