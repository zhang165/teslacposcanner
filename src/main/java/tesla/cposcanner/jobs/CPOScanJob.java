package tesla.cposcanner.jobs;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.mail.EmailException;

import lombok.extern.slf4j.Slf4j;
import tesla.cposcanner.emailController.EmailController;
import tesla.cposcanner.models.TeslaModel;
import tesla.cposcanner.parser.TeslaParser;
import tesla.cposcanner.scanner.WebScanner;

/**
 * Scans Tesla.com for CPO Vehicles
 * @author zhang165
 *
 */
@Slf4j
public class CPOScanJob implements Runnable{
	private static final String API_STRING = "https://www.tesla.com/cpo_tool/ajax?exteriors=all&model=MODEL_S&priceRange=0%2C200000&city=null&state=null&country=US";
	private static final String DRIVE_TRAIN = "DV4W";
	private static final int MAX_PRICE = 70000;
	
	private static final String FILENAME = "src/main/resources/data.txt";
	
	private final WebScanner scanner;
	private final TeslaParser parser;
	private final EmailController emailController;
	private final Map<String,TeslaModel> map;
	
	public CPOScanJob(final WebScanner scanner, final TeslaParser parser, 
			final EmailController emailController, final Map<String,TeslaModel> map){
		this.scanner = scanner;
		this.parser = parser;
		this.map = map;
		this.emailController = emailController;
	}
	
	@Override
	public void run() {
		log.info("Collecting data from: {}",API_STRING);
		String result = "";
		boolean isSuccessful = false;
		try {
			result = scanner.scan(API_STRING);
			isSuccessful = true;
		} catch (IOException e) {
			log.error("IOException: {}",e.getMessage());
		}
		
		if(!isSuccessful) return;
		
		try {
			final List<TeslaModel> list = parser.parse(result);
			final StringBuilder sb = new StringBuilder();
			for(TeslaModel model:list){
				if(model.getDriveTrain().equals(DRIVE_TRAIN) && model.getUsedVehiclePrice() < MAX_PRICE && !map.containsKey(model.getVin())){
					map.put(model.getVin(), model);
					
					sb.setLength(0);
					sb.append("Price: "); sb.append(model.getUsedVehiclePrice()); sb.append("\n");
					sb.append("Year: "); sb.append(model.getYear()); sb.append("\n");
					sb.append("Battery: "); sb.append(model.getBattery()); sb.append("\n");
					sb.append("URL: "); sb.append("https://www.tesla.com/preowned/"); sb.append(model.getVin()); sb.append("\n");
					log.info(sb.toString());
					
					try(BufferedWriter out = new BufferedWriter(new FileWriter(FILENAME))){
						out.append(sb.toString());
					}
					
					emailController.sendEmail(sb.toString());
				}
			}
		} catch (IOException | EmailException e) {
			log.error("Exception: {}",e.getMessage());
			e.printStackTrace();
		}
	}
}
