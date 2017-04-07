package tesla.cposcanner.core;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import lombok.extern.slf4j.Slf4j;
import tesla.cposcanner.jobs.CPOScanJob;
import tesla.cposcanner.parser.TeslaParser;
import tesla.cposcanner.scanner.WebScanner;

@Slf4j
@Singleton
public class TeslaCPOService {
	@SuppressWarnings("unused")
	private final WebScanner webScanner;
	private final ScheduledJobExecutorService scheduledExecutor;
	private final WebScanner scanner;
	private final TeslaParser parser;
	
	@Inject
	public TeslaCPOService(final WebScanner webscanner, final ScheduledJobExecutorService scheduledExecutor,
			final WebScanner scanner, final TeslaParser parser){
		this.webScanner = webscanner;
		this.scheduledExecutor = scheduledExecutor;
		this.scanner = scanner;
		this.parser = parser;
	}
	
	public void start() throws IOException{
		log.info("Scheduling CPO scan job every 5 minutes");
		scheduledExecutor.schedule(new CPOScanJob(scanner, parser));
	}
}
