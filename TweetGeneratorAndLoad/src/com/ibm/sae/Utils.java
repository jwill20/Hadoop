package com.ibm.sae;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils
{
	static DataOutputStream auditLog;
	static String auditLine;
	static String callingMethodName = null;
	static String callingClassName  = null;
	static SimpleDateFormat theDateFmt = null;
	
/*****************************************************************************/
/* Function: writeToLog                                                      */
/*                                                                           */
/* Description: Writes output message. Method is synchronized for a multi-   */
/*              threaded environment.                                        */	
/*                                                                           */
/*****************************************************************************/
public static synchronized void writeToLog(String message,ParameterManager pm)
{
	
	callingMethodName = Thread.currentThread().getStackTrace()[3].getMethodName();
	callingClassName  = sun.reflect.Reflection.getCallerClass(2).getSimpleName();

	try
	{
		auditLog = new DataOutputStream(new FileOutputStream(pm.getAuditFileName(), true));
		theDateFmt = new SimpleDateFormat("MM/dd HH:mm:ss.SSSS");
		auditLine = theDateFmt.format(new Date());
		auditLine = (auditLine + " " + callingClassName + ":" + callingMethodName + " " + message);
		auditLog.writeBytes(auditLine + "\n");
		System.out.println(auditLine);
		auditLog = null;
	} 
	catch (Exception e)
	{
		System.out.println("Utils:writeToLog ==> Exception thrown in writeToLog routine");
		System.out.println(e.getMessage());
		e.printStackTrace();
	}
}

}

