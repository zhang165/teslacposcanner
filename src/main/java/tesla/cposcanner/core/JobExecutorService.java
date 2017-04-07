package tesla.cposcanner.core;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;

/**
 * Manages threads and retries failed jobs
 */
@Slf4j
public class JobExecutorService extends ThreadPoolExecutor{
	private static final int CORE_POOL_SIZE = 8; // core # threads
	private static final int MAX_POOL_SIZE = 12; // max 12 threads
	private static final long KEEP_ALIVE_TIME = 5000L; // 
	private static final TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;
	
	@Inject
	public JobExecutorService(){
    	super(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TIME_UNIT, new LinkedBlockingQueue<Runnable>());
	}

	/**
	 * Executes a given task
 	* @return A Future containing any thrown exceptions
 	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Future execute(Callable task) {
    	return super.submit(task);
	}
	
	private void retry(){ // TODO: handle retrying tasks using exponential delay
		
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void afterExecute(Runnable r, Throwable t) {
    	super.afterExecute(r, t);
    	Throwable thrown = t;
   	 
    	if (thrown == null && r instanceof Future) {
        	try {
            	final Future future = (Future) r;
            	if (future != null && future.isDone()) {
                	future.get();
            	}
        	} catch (ExecutionException ee) {
            	thrown = ee.getCause();
            	log.error("Execution Exception occurred: ", ee);
        	} catch (CancellationException | InterruptedException ce) {
            	log.error("Another Exception occurred: ", ce);
        	}
    	}
   	 
    	if (thrown != null) {
        	retry();
    	}
	} 

}
