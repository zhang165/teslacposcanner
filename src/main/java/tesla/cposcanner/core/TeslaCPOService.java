package tesla.cposcanner.core;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import lombok.extern.slf4j.Slf4j;
import tesla.cposcanner.emailController.EmailController;
import tesla.cposcanner.jobs.CPOScanJob;
import tesla.cposcanner.models.TeslaModel;
import tesla.cposcanner.parser.TeslaParser;
import tesla.cposcanner.scanner.WebScanner;

/**
 * Main service which schedules jobs
 * @author zhang165
 *
 */
@Slf4j
@Singleton
public class TeslaCPOService {
	@SuppressWarnings("unused")
	private final WebScanner webScanner;
	private final ScheduledJobExecutorService scheduledExecutor;
	private final WebScanner scanner;
	private final TeslaParser parser;
	private final EmailController emailController;
	
	// TODO: extract this from xml
	private static final String API_STRING_US = "https://www.tesla.com/cpo_tool/ajax?exteriors=all&model=MODEL_S&priceRange=0%2C200000&city=null&state=null&country=US";
	private static final String URL_REDIRECT_US = "https://www.tesla.com/preowned/";
	
	private static final String API_STRING_CA = "https://www.tesla.com/cpo_tool/ajax?exteriors=all&model=MODEL_S&priceRange=0%2C200000&city=null&state=null&country=CA";
	private static final String URL_REDIRECT_CA = "https://www.tesla.com/en_CA/preowned/";
	
	private static final double US_CAD_EXCHANGE_RATE = 1.34;
	private static final int MAX_PRICE_US = 73000;
	private static final int MAX_PRICE_CA = (int)(MAX_PRICE_US*US_CAD_EXCHANGE_RATE);
	
	private static final String DRIVE_TRAIN = "DV4W";
	
	@Inject
	public TeslaCPOService(final WebScanner webscanner, final ScheduledJobExecutorService scheduledExecutor,
			final WebScanner scanner, final TeslaParser parser, final EmailController emailController){
		this.webScanner = webscanner;
		this.scheduledExecutor = scheduledExecutor;
		this.scanner = scanner;
		this.parser = parser;
		this.emailController = emailController;
	}
	
	public void start() throws IOException{
		log.info("Scheduling CPO scan job every {} minutes",ScheduledJobExecutorService.UPDATE_TIME/60);
		final Map<String, TeslaModel> map = new ConcurrentHashMap<String,TeslaModel>();
		scheduledExecutor.schedule(new CPOScanJob(scanner, parser, emailController, map, 
				API_STRING_US, URL_REDIRECT_US, DRIVE_TRAIN, MAX_PRICE_US));
		scheduledExecutor.schedule(new CPOScanJob(scanner, parser, emailController, map, 
				API_STRING_CA, URL_REDIRECT_CA, DRIVE_TRAIN, MAX_PRICE_CA));
	}
}
