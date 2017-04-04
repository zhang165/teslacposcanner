package tesla.cposcanner.core;

import java.io.IOException;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import tesla.cposcanner.scanner.WebScanner;

@Slf4j
public class TeslaCPOService {
	private final WebScanner webScanner;
	private final ScheduledJobExecutorService scheduledExecutor;
	
	@Inject
	public TeslaCPOService(final WebScanner webscanner, final ScheduledJobExecutorService scheduledExecutor){
		this.webScanner = webscanner;
		this.scheduledExecutor = scheduledExecutor;
	}
	
	public void start() throws IOException{
		webScanner.scan();
	}
}
