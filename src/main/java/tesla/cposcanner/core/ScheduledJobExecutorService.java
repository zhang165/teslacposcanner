package tesla.cposcanner.core;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class ScheduledJobExecutorService extends ScheduledThreadPoolExecutor{
		private static final int CORE_POOL_SIZE = 8; // core # threads
		private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;
		private static final long START_DELAY = 0L;
		private static final long UPDATE_TIME = 300L; // update every 5 minutes
		
		private Runnable lastJob;
	
	    @Inject
		public ScheduledJobExecutorService() {
	    	super(CORE_POOL_SIZE);
		}

		/**
	 	* Schedules a job with a fixed delay; all thrown exceptions are caught in
	 	* the ScheduledFuture to be processed in afterExecute()
	 	* @return A scheduledfuture containing any thrown exceptions
	 	*/
		public ScheduledFuture<?> schedule(Runnable job) {
			lastJob = job;
	    	return super.scheduleWithFixedDelay(job, START_DELAY, UPDATE_TIME, TIME_UNIT);
		}
		
		public ScheduledFuture<?> scheduleWithTimer(Runnable job, long startDelay, long updateTime) {
			lastJob = job;
	    	return super.scheduleWithFixedDelay(job, startDelay, updateTime, TIME_UNIT);
		}

		@Override
		protected void afterExecute(Runnable r, Throwable t) {
	    	super.afterExecute(r, t);
	   	 
	    	Throwable thrown = t;
	   	 
	    	if (thrown == null && r instanceof ScheduledFuture<?>) {
	        	try {
	            	final ScheduledFuture<?> future = (ScheduledFuture<?>) r;
	            	if (future != null && future.isDone()) {
	                	future.get();
	            	}
	        	} catch (ExecutionException | InterruptedException ee) {
	            	thrown = ee.getCause();
	            	log.error("Execution Exception occurred: {}", ee);
	        	} 
	    	}
	   	 
	    	if (thrown != null) {
	        	scheduleWithFixedDelay(lastJob, START_DELAY, UPDATE_TIME, TIME_UNIT);
	    	}
		}
}