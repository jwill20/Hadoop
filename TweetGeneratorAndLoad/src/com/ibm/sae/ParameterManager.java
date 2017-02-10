package com.ibm.sae;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ParameterManager
{

	ResourceBundle rb;
	FileReader input;
	BufferedReader bufRead;
	Map<String,String> parmMap;
	String[] splits;
	String inputLine;
	
	public ParameterManager()
	{
		loadParameters();
	}
	
	private void loadParameters()
	{
		
		parmMap = new HashMap<String,String>();
		//parmMap.put("AuditFileName",SOFConstants.auditTrailFileName);
		
		//String propFileName = System.getProperty("PARAMETERS");
		String propFileName = "Parameters.properties";
		
		//System.out.println("Property file location is " + propFileName);
		
		try
		{
			input = new FileReader(propFileName);
			bufRead = new BufferedReader(input);
			
			inputLine = bufRead.readLine();
			
			while (inputLine != null)
			{
				splits = inputLine.split("=");
				parmMap.put(splits[0],splits[1]);
				inputLine = bufRead.readLine();
			}
		} 
		catch (FileNotFoundException e)
		{
			System.out.println("FileNotFoundException getting Parameter.properties");
			e.printStackTrace();
		} 
		catch (IOException e)
		{
			System.out.println("IOException reading Parameter.properties");
			e.printStackTrace();
		}
		catch (Exception e)
		{
			System.out.println("Exception reading Parameter.properties");
			e.printStackTrace();
		}

		//rb = ResourceBundle.getBundle(propFileName);
		//String hostAndPort = parmMap.get("Host1Name");
		//String[] sArray = hostAndPort.split("\\:");
		//host = sArray[0];
		//port = sArray[1];
	}

	public String getThreadCount()
	{
		return parmMap.get("ThreadCount");
	}

	public String getAuditFileName()
	{
		return parmMap.get("AuditFileName");
	}

	public Boolean getLoggingFlag()
	{
		Boolean flag = new Boolean(parmMap.get("LoggingFlag"));
		return flag;
	}
	
	public String getCheckGuardPercentage()
	{
		return parmMap.get("CheckGuardPercentage");
	}
	
	public String getIntRatePercentage()
	{
		return parmMap.get("IntRatePercentage");
	}
	
	public String getBillPayPercentage()
	{
		return parmMap.get("BillPayPercentage");
	}
	
	public String getServiceRepsPercentage()
	{
		return parmMap.get("ServiceRepsPercentage");
	}
	
	public String getOnlineBankingPercentage()
	{
		return parmMap.get("OnlineBankingPercentage");
	}
	
	public String getRuntime()
	{
		return parmMap.get("Runtime");
	}

	public static void main(String[] args)
	{
		ParameterManager pm = new ParameterManager();
		System.out.println(pm.getAuditFileName());
	}

}
