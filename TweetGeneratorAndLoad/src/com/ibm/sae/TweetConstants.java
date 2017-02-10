package com.ibm.sae;

public class TweetConstants
{
	public static final int MAX_NUM_THREADS = 10;
	public static final String TWEET_OUTPUT_FILE = "tweetOutput";
	public static final String TWEET_OUTPUT_FILE_SFX = ".txt";

	/****************************************************/
	/* Twitter tweet - This header is 73 bytes. Tweet   */
	/*     text is 30-40 bytes.                         */
	/****************************************************/
	public static final String TWEET_HEADER = 
		"query:Big+Bank,from_user:userId,id:548383,source:http://twitter.com,text,";
	
	/****************************************************/
	/* Positive sentiment                               */
	/*    love, great, terrific, like, fantastic        */
	/****************************************************/
	public static final String CHECK_GUARD_POS    = "I love check guard from Big Bank";
	public static final String ONLINE_BANKING_POS = "online banking is great at Big Bank";
	public static final String SERVICE_REPS_POS   = "The service reps at Big Bank are terrific";
	public static final String BILL_PAY_POS       = "I like automatic bill pay from Big Bank";
	public static final String INT_RATE_POS       = "Big Bank interest rates are fantastic";

	/****************************************************/
	/* Negative sentiment                               */
	/*    horrible, hate, bad, awful, terrible          */
	/****************************************************/
	
	public static final String CHECK_GUARD_NEG    = "check guard is horrible from Big Bank";
	public static final String ONLINE_BANKING_NEG = "I hate online banking from Big Bank";
	public static final String SERVICE_REPS_NEG   = "The service reps at Big Bank are really bad";
	public static final String BILL_PAY_NEG       = "automatic bill pay from Big Bank is awful";
	public static final String INT_RATE_NEG       = "Big Bank interest rates are terrible";
	
	/****************************************************/
	/* MongoDB constants                                */
	/****************************************************/
	public static final String MDB_HOSTNAME = "localhost";
	public static final int MDB_PORT = 27017;
	public static final String MDB_DBNAME = "vwapDB";
	public static final String MDB_COLLNAME = "vwapColl";

}
