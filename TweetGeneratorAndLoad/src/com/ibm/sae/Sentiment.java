package com.ibm.sae;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import com.ibm.sae.Utils;

public class Sentiment
{
	/*****************************************************************************/
	/*                                                                           */
	/* Class: SentimentMapper                                                    */
	/*                                                                           */
	/* Input: Twitter data in JSON format. Tweets about Big Bank.                */
	/*                                                                           */	
	/* Output: Key = Feature with positive sentiment                             */
	/*         Value = Number 1 to be subsequently counted in Reduce step.       */
	/*                                                                           */	
	/* Description: Takes in a row of Twitter data. Initially scans for a feature*/
	/*      such as online banking and then scans for positive sentiment. The K2 */
	/*      key will be the feature.                                             */
	/*                                                                           */
	/*****************************************************************************/
	public static class SentimentMapper extends Mapper<Object, Text, Text, IntWritable>
	{
		private Text twitterEntry = new Text();
		private IntWritable count = new IntWritable(1);
		private String inputEntry;

		public void map(Object key, Text value, Context context)
				throws IOException, InterruptedException
		{
			inputEntry = value.toString();
			
			/*************************************************************************/
			/* Find the positive sentiment and the feature.  Make feature the key    */
			/* and count of 1 the value.                                             */
			/*************************************************************************/
			if ((inputEntry.contains("like")) || (inputEntry.contains("love")) ||
				    (inputEntry.contains("great")) || (inputEntry.contains("terrific")) ||
				    (inputEntry.contains("wonderful")))
			{	
			   if ((inputEntry.contains("check guard")) || (inputEntry.contains("Check guard")))
			   {
				  twitterEntry.set("Check Guard");
				  context.write(twitterEntry,count);
			   }
			   else if ((inputEntry.contains("online banking")) || (inputEntry.contains("Online banking")))
			   {
				  twitterEntry.set("Online Banking");
				  context.write(twitterEntry,count);
			   }
			   else if ((inputEntry.contains("service reps")) || (inputEntry.contains("Service reps")))
			   {
				  twitterEntry.set("Service Reps");
				  context.write(twitterEntry,count);
			   }
			}   
		}
	}

	/*****************************************************************************/
	/*                                                                           */
	/* Class: SentimentReducer                                                   */
	/*                                                                           */
	/* Input: Key = Feature, Value = Count of 1 for each entry.                  */
	/*                                                                           */	
	/* Output: Key = Feature, Value = Count of positive sentiment.               */
	/*                                                                           */	
	/* Description: Counts the number of entries for each feature.               */
	/*                                                                           */
	/*****************************************************************************/
	public static class SentimentReducer extends Reducer<Text, IntWritable, Text, IntWritable>
	{
		private IntWritable result = new IntWritable();
		

		public void reduce(Text key, Iterable<IntWritable> values,
				Context context) throws IOException, InterruptedException
		{
		   int sum = 0;
		   Utils.writeToLog("sr");
		   for (IntWritable val : values)
		   {
		      sum++;
		   }
		
		   result.set(sum);
		   context.write(key,result);	
		}
	}

	public static void main(String[] args) throws Exception
	{
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		
		if (otherArgs.length != 2)
		{
			System.err.println("Usage: Sentiment <in> <out>");
			System.exit(2);
		}
		Job job = new Job(conf, "Sentiment Analysis");
		
		job.setJarByClass(Sentiment.class);
		job.setMapperClass(SentimentMapper.class);
		//job.setCombinerClass(SentimentReducer.class);
		job.setReducerClass(SentimentReducer.class);
		//job.setNumReduceTasks(0);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}

