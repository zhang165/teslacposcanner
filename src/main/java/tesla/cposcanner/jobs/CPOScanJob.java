package tesla.cposcanner.jobs;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import tesla.cposcanner.scanner.WebScanner;

@Slf4j
public class CPOScanJob implements Runnable{
	private static final String API_STRING = "https://www.tesla.com/cpo_tool/ajax?exteriors=all&model=MODEL_S&priceRange=0%2C200000&city=null&state=null&country=US";
	
	private final WebScanner scanner;
	
	public CPOScanJob(WebScanner scanner){
		this.scanner = scanner;
	}
	
	@Override
	public void run() {
		String result = "";
		try {
			result = scanner.scan(API_STRING);
		} catch (IOException e) {
			log.error("IOException: {}",e.getMessage());
		}
		System.out.println(result);
	}
}
