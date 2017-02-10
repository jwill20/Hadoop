package com.ibm.sae;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class TweetGeneratorThread implements Runnable
{
	/******************************************************/
	/* Class: TweetGeneratorThread                        */
	/*                                                    */
	/* Purpose: Main control point.                       */
	/*    1. Creates thread to create Twitter data.       */
	/*                                                    */
	/******************************************************/
	private ParameterManager pm = new ParameterManager();
	protected boolean runFlag;
	protected Thread t;
	private PrintWriter pw = null;
	private Double threadId = null;
	private int checkGuardPercentage = 0;
	private int serviceRepsPercentage = 0;
	private int onlineBankingPercentage = 0;
	private int billPayPercentage = 0;
	private int intRatePercentage = 0;
	
	/******************************************************/
	/* Constructor: TweetGeneratorThread                  */
	/*                                                    */
	/* Purpose: Set up percentages and run flag.          */
	/*                                                    */
	/******************************************************/
	public TweetGeneratorThread(boolean runFlag)
	{
		this.runFlag = runFlag;

		checkGuardPercentage = new Integer(pm.getCheckGuardPercentage());
		serviceRepsPercentage = new Integer(pm.getServiceRepsPercentage());
		onlineBankingPercentage = new Integer(pm.getOnlineBankingPercentage());
		billPayPercentage = new Integer(pm.getBillPayPercentage());
		intRatePercentage = new Integer(pm.getIntRatePercentage());

		t = new Thread(this,"TweetGeneratorThread");
	}

	@Override
	/******************************************************/
	/* Run the thread.                                    */
	/******************************************************/
	public void run()
	{
		if (pm.getLoggingFlag()) Utils.writeToLog(t.getName() + ": thread ready", pm);
		
		try
		{
			pw = new PrintWriter(
				 new BufferedWriter(
				 new FileWriter(
					 TweetConstants.TWEET_OUTPUT_FILE + t.getId() + TweetConstants.TWEET_OUTPUT_FILE_SFX)));
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		threadId = new Double(t.getId());
		
		while (this.runFlag)
		{
			/**************************************************************************/
			/* Write out sentiment based on the input percentages.                    */
			/**************************************************************************/
			for (int a = 0; a < checkGuardPercentage; a++)
			{
				pw.println(TweetConstants.TWEET_HEADER + TweetConstants.CHECK_GUARD_POS);
				pw.println(TweetConstants.TWEET_HEADER + TweetConstants.CHECK_GUARD_NEG);
			}
			for (int b = 0; b < serviceRepsPercentage; b++)
			{
				pw.println(TweetConstants.TWEET_HEADER + TweetConstants.SERVICE_REPS_POS);
				pw.println(TweetConstants.TWEET_HEADER + TweetConstants.SERVICE_REPS_NEG);
			}
			for (int c = 0; c < onlineBankingPercentage; c++)
			{
				pw.println(TweetConstants.TWEET_HEADER + TweetConstants.ONLINE_BANKING_POS);
				pw.println(TweetConstants.TWEET_HEADER + TweetConstants.ONLINE_BANKING_NEG);
			}
			for (int d = 0; d < billPayPercentage; d++)
			{
				pw.println(TweetConstants.TWEET_HEADER + TweetConstants.BILL_PAY_POS);
				pw.println(TweetConstants.TWEET_HEADER + TweetConstants.BILL_PAY_NEG);
			}
			for (int e = 0; e < intRatePercentage; e++)
			{
				pw.println(TweetConstants.TWEET_HEADER + TweetConstants.INT_RATE_POS);
				pw.println(TweetConstants.TWEET_HEADER + TweetConstants.INT_RATE_NEG);
			}
		}
		
		pw.close();
		if (pm.getLoggingFlag()) Utils.writeToLog(threadId.toString() + ": Received shutdown message", pm);
	}

}
