package com.ibm.sae;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class ClickStream
{
	/*****************************************************************************/
	/*                                                                           */
	/* Class: ClickStreamMapper                                                  */
	/*                                                                           */
	/* Input: Web log from Friendly Foods Web server                             */
	/*                                                                           */	
	/* Output: Key = IP address, Value = Comma separated timestamp and URL       */
	/*                                                                           */	
	/* Description: Takes in a row of the web log, grabs the IP address for the  */
	/*      key and writes out the timestamp and url as a comma separated value. */
	/*                                                                           */
	/*****************************************************************************/
	public static class ClickStreamMapper extends Mapper<Object, Text, Text, Text>
	{
		private Text logEntry = new Text();
		private Text ipAddr = new Text();
		private String inputEntry;
		private String outputString;

		public void map(Object key, Text value, Context context)
				throws IOException, InterruptedException
		{
			outputString = "";
			StringTokenizer itr = new StringTokenizer(value.toString());
			
			/********************************************/
			/* Grab ip address                          */
			/********************************************/
			ipAddr.set(itr.nextToken());

			while (itr.hasMoreTokens())
			{
				/****************************************/
				/* Find timestamp and friendlyfoods URL */
				/* and build output value string.       */
				/****************************************/
				inputEntry = itr.nextToken();
				if (inputEntry.contains(":"))
				{
					outputString = outputString + inputEntry + ",";
				}
				else if (inputEntry.contains("www.friendlyfoods"))
				{
					outputString = outputString + inputEntry;
				}
			}
			logEntry.set(outputString);
			context.write(ipAddr, logEntry);
		}
	}

	/*****************************************************************************/
	/*                                                                           */
	/* Class: ClickStreamReducer                                                 */
	/*                                                                           */
	/* Input: Key = IP address, Value = Comma separated timestamp and URL        */
	/*                                                                           */	
	/* Output: Key = IP address, Value = Last URL touched by customer in session */
	/*                                                                           */	
	/*                                                                           */
	/*****************************************************************************/
	public static class ClickStreamReducer extends Reducer<Text, Text, Text, Text>
	{
		private Text url = new Text();
		private SimpleDateFormat theDateFmt = new SimpleDateFormat("hh:mm:ss");
		private Date inputDate;
		private Date currentDate;
		boolean foundJsp;

		public void reduce(Text key, Iterable<Text> values,
				Context context) throws IOException, InterruptedException
		{
			currentDate = new Date(0);
			foundJsp = false;
			
			for (Text val : values)
			{
				/****************************************/
				/* Check if the customer landed on the  */
				/* addItem.jsp during this session.     */
				/****************************************/
				if (val.find("addItem.jsp") > 0)
				{
					foundJsp = true;
				}
				StringTokenizer itr = new StringTokenizer(val.toString(),",");
				String timeStamp = itr.nextToken();
				
				try
				{
					/****************************************/
					/* Here we want to find the last thing  */
					/* the customer did at the site by time.*/
					/****************************************/
					inputDate = (Date)theDateFmt.parse(timeStamp.toString());
					if (inputDate.after(currentDate))
					{
						currentDate = inputDate;
						url.set(itr.nextToken());
					}
				}
				catch (ParseException e)
				{
					System.out.println("Exception thrown parsing date");
					e.printStackTrace();
				}
				
			}
			/****************************************/
			/* Only write out record if the customer*/
			/* landed on the addItem.jsp page.      */
			/****************************************/
			if (foundJsp)
			{
				context.write(key,url);	
			}
			
		}
	}

	public static void main(String[] args) throws Exception
	{
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		
		if (otherArgs.length != 2)
		{
			System.err.println("Usage: ClickStream <in> <out>");
			System.exit(2);
		}
		
		Job job = new Job(conf, "Click Stream Analysis");
		job.setJarByClass(ClickStream.class);
		job.setMapperClass(ClickStreamMapper.class);
		//job.setCombinerClass(ClickStreamReducer.class);
		job.setReducerClass(ClickStreamReducer.class);
		job.setNumReduceTasks(2);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
