package com.ibm.sae;

public class TweetGenerator
{
	private static ParameterManager pm = new ParameterManager();
	private int numberOfThreads;
	private TweetGeneratorThread[] tgt = new TweetGeneratorThread[TweetConstants.MAX_NUM_THREADS];
	
	public static void main(String[] args)
	{
		if (pm.getLoggingFlag()) Utils.writeToLog("Begin", pm);
		TweetGenerator tg = new TweetGenerator();
		
        /********************************************/
		/* Validate the number of threads to start. */
		/********************************************/
		if (!tg.validateInput(pm.getThreadCount()))
		{
			Utils.writeToLog("Error: Input not valid.  Number of threads is invalid.", pm);
			return;
		}
		
		tg.numberOfThreads = new Integer(pm.getThreadCount());
		
		if (pm.getLoggingFlag()) Utils.writeToLog("Number of threads to start = " + tg.numberOfThreads, pm);
		
		try
		{
			/******************************************/
			/* Start all threads.                     */
			/******************************************/
			for (int a = 0; a < tg.numberOfThreads; a++)
			{
				tg.tgt[a] = new TweetGeneratorThread(true);
				tg.tgt[a].t.start();
			}

			/******************************************/
			/* This is effectively the time to run    */
			/* the threads.                           */
			/******************************************/
			Thread.sleep(new Long(pm.getRuntime()));
			
			/******************************************/
			/* Send flag to stop threads.             */
			/******************************************/
			for (int b = 0; b < tg.numberOfThreads; b++) 
			{
				tg.tgt[b].runFlag = false;
			}
			
			/******************************************/
			/* Wait for threads to complete.          */
			/******************************************/
			for (int c = 0; c < tg.numberOfThreads; c++) 
			{
				tg.tgt[c].t.join();
			}
			
			if (pm.getLoggingFlag()) Utils.writeToLog("All threads complete", pm);
		} 
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
	}
    /******************************************************/
    /* Method: validateInput                              */
    /*                                                    */
    /* Purpose: Makes sure the input is a positive int.   */
    /******************************************************/
    public boolean validateInput(String numberOfThreads)
    {
    	int threadCount = new Integer(numberOfThreads);
    	if ((threadCount > 0) && (threadCount < 11))
    	{
    		return true;
    	}
    	
    	return false;
    }

}
