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
	
	private static final String FILENAME = "src/main/resources/data.txt";
	
	private final WebScanner scanner;
	private final TeslaParser parser;
	private final EmailController emailController;
	private final Map<String,TeslaModel> map;
	
	private final String api;
	private final String redirect;
	private final String driveTrain;
	private final int maxPrice; 
	
	public CPOScanJob(final WebScanner scanner, final TeslaParser parser, 
			final EmailController emailController, final Map<String,TeslaModel> map,
			final String api, final String redirect, final String driveTrain,
			final int maxPrice){
		this.scanner = scanner;
		this.parser = parser;
		this.map = map;
		this.emailController = emailController;
		this.api = api;
		this.redirect = redirect;
		this.driveTrain = driveTrain;
		this.maxPrice = maxPrice;
	}
	
	@Override
	public void run() {
		log.info("Collecting data from: {}",api);
		String result = "";
		boolean isSuccessful = false;
		try {
			result = scanner.scan(api);
			isSuccessful = true;
		} catch (IOException e) {
			log.error("IOException: {}",e.getMessage());
			e.printStackTrace();
		}
		
		if(!isSuccessful) return;
		
		try {
			final List<TeslaModel> list = parser.parse(result);
			final StringBuilder sb = new StringBuilder();
			int match = 0;
			for(TeslaModel model:list){
				if(model.getDriveTrain().equals(driveTrain) && model.getUsedVehiclePrice() <= maxPrice && !map.containsKey(model.getVin())){
					match++;
					map.put(model.getVin(), model);
					
					sb.setLength(0);
					sb.append("Price: "); sb.append(model.getUsedVehiclePrice()); sb.append("\n");
					sb.append("Year: "); sb.append(model.getYear()); sb.append("\n");
					sb.append("Battery: "); sb.append(model.getBattery()); sb.append("\n");
					sb.append("URL: "); sb.append(redirect); sb.append(model.getVin()); sb.append("\n");
					log.info(sb.toString());
					
					try(BufferedWriter out = new BufferedWriter(new FileWriter(FILENAME))){
						out.append(sb.toString());
					}
					
					emailController.sendEmail(sb.toString());
				}
			}
			log.info("Found: {} CPO vehicles with {} matching critera",list.size(),match);
			log.info("Drive Train: {} and Price <= {}",driveTrain, maxPrice);
		} catch (IOException | EmailException e) {
			log.error("Exception: {}",e.getMessage());
			e.printStackTrace();
		}
	}
}
