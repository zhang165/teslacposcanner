package tesla.cposcanner.jobs;

import java.io.IOException;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import tesla.cposcanner.models.TeslaModel;
import tesla.cposcanner.parser.TeslaParser;
import tesla.cposcanner.scanner.WebScanner;

@Slf4j
public class CPOScanJob implements Runnable{
	private static final String API_STRING = "https://www.tesla.com/cpo_tool/ajax?exteriors=all&model=MODEL_S&priceRange=0%2C200000&city=null&state=null&country=US";
	
	private final WebScanner scanner;
	private final TeslaParser parser;
	
	public CPOScanJob(WebScanner scanner, TeslaParser parser){
		this.scanner = scanner;
		this.parser = parser;
	}
	
	@Override
	public void run() {
		String result = "";
		boolean isSuccessful = false;
		try {
			result = scanner.scan(API_STRING);
			isSuccessful = true;
		} catch (IOException e) {
			log.error("IOException: {}",e.getMessage());
		}
		
		if(!isSuccessful) return;
		
		List<TeslaModel> list = parser.parse(result);
	}
}
