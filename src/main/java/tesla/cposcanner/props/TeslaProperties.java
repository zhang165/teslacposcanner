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

@Data
@Slf4j
public class TeslaProperties extends DefaultHandler{
	private static final String TESLA_CONTEXT_FILE_NAME = "tesla_context.xml";
	
	public final static String MAX_PRICE_US_NAME = "maxPriceUS";
	public final static String EXCHANGE_RATE_NAME = "exchangeRate";
	public final static String DRIVE_TRAIN_NAME = "driveTrain";
	
	private Integer maxPriceUS = null;
	private Double exchangeRate = null;
	private String driveTrain = "";
	
	private boolean isMaxPriceUS = false;
	private boolean isExchangeRate = false;
	private boolean isDriveTrain = false;
	
	private Properties props;
	
	public TeslaProperties(){
		try {
			generateProperties();
		} catch (ParserConfigurationException | SAXException e) {
			log.error("Failed to parse: {}",TESLA_CONTEXT_FILE_NAME);
			e.printStackTrace();
		} catch (IOException e) {
			log.error("Failed to locate file: {}",TESLA_CONTEXT_FILE_NAME);
			e.printStackTrace();
		}
	}
	
	public void generateProperties() throws ParserConfigurationException, SAXException, IOException{
		final SAXParserFactory spf = SAXParserFactory.newInstance();
	    spf.setNamespaceAware(true);
	    final SAXParser saxParser = spf.newSAXParser();
	    final XMLReader xmlReader = saxParser.getXMLReader();
	    xmlReader.setContentHandler(this);
	    final ClassLoader classLoader = getClass().getClassLoader();
	    final InputStream stream = classLoader.getResourceAsStream(TESLA_CONTEXT_FILE_NAME);    
	    final InputSource is = new InputSource(new InputStreamReader(stream,"UTF-8"));
	    xmlReader.parse(is);
	}
	
	public void startElement(String uri, String localName, String qName, Attributes arg3) throws SAXException {
		if (qName.equalsIgnoreCase(MAX_PRICE_US_NAME)) {
			isMaxPriceUS = true;
		}else if (qName.equalsIgnoreCase(TESLA_CONTEXT_FILE_NAME)) {
			//Ignore - this is the root element
		}else if (qName.equalsIgnoreCase(EXCHANGE_RATE_NAME)) {
			isExchangeRate = true;
		}else if (qName.equalsIgnoreCase(DRIVE_TRAIN_NAME)){ 
			isDriveTrain = true;
		}
	}
	
	public void characters(char ch[], int start, int length) throws SAXException {
		if (isMaxPriceUS) {
			maxPriceUS = Integer.parseInt(new String(ch, start, length));
			isMaxPriceUS = false;
		}

		if (isExchangeRate) {
			exchangeRate = Double.parseDouble(new String(ch, start, length));
			isExchangeRate = false;
		}
		if (isDriveTrain) {
			driveTrain = new String(ch, start, length);
			isDriveTrain = false;
		}
	}
	
	public void endDocument(){
		if(maxPriceUS == null){
			log.error("Unabled to parse {} from: {}",MAX_PRICE_US_NAME,TESLA_CONTEXT_FILE_NAME);
		}
		if(exchangeRate == null){
			log.error("Unabled to parse {} from: {}",EXCHANGE_RATE_NAME,TESLA_CONTEXT_FILE_NAME);
		}
		if(driveTrain.equals("")){
			log.error("Unabled to parse {} from: {}",DRIVE_TRAIN_NAME,TESLA_CONTEXT_FILE_NAME);
		}
	}
	
}
