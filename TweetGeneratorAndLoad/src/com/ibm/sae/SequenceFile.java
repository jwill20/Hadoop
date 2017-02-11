package com.manning.hip.ch3.seqfile;

import com.manning.hip.ch3.StockPriceWritable;
import com.manning.hip.ch3.csv.CSVParser;
import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.DefaultCodec;

import java.io.File;
import java.io.IOException;

/*************************************************************/
/* SequenceFile Writer                                       */ 
/*************************************************************/
public class SequenceFileStockWriter 
{

  public static void main(String... args) throws IOException 
  {
    write(new File(args[0]), new Path(args[1]));
  }

  public static void write(File inputFile, Path outputPath) throws IOException 
  {
    Configuration conf = new Configuration();
    FileSystem fs = FileSystem.get(conf);

    SequenceFile.Writer writer = //<co id="ch03_comment_seqfile_write1"/>
        SequenceFile.createWriter(fs, conf, outputPath, Text.class,
            StockPriceWritable.class,
            SequenceFile.CompressionType.BLOCK,
            new DefaultCodec());
    try 
    {
      Text key = new Text();

      for (String line : FileUtils.readLines(inputFile))  //<co id="ch03_comment_seqfile_write2"/>
      {
        StockPriceWritable stock = StockPriceWritable.fromLine(line);
        key.set(stock.getSymbol());

        key.set(stock.getSymbol());
        writer.append(key,stock);        //<co id="ch03_comment_seqfile_write4"/>
      }
    } 
    finally 
    {
      writer.close();
    }
  }
}


/*************************************************************/
/* SequenceFile Reader                                       */ 
/*************************************************************/
public class SequenceFileStockReader 
{
  public static void main(String... args) throws IOException 
  {
    read(new Path(args[0]));
  }

  public static void read(Path inputPath) throws IOException 
  {
    Configuration conf = new Configuration();
    FileSystem fs = FileSystem.get(conf);

    SequenceFile.Reader reader = //<co id="ch03_comment_seqfile_read1"/>
        new SequenceFile.Reader(fs, inputPath, conf);

    try 
    {
      System.out.println(
          "Is block compressed = " + reader.isBlockCompressed());

      Text key = new Text();
      StockPriceWritable value = new StockPriceWritable();

      while (reader.next(key, value)) { //<co id="ch03_comment_seqfile_read2"/>
        System.out.println(key + "," + value);
      }
    } 
    finally 
    {
      reader.close();
    }
  }
}



/*************************************************************/
/* MapReduce Job                                             */
/*************************************************************/
package com.manning.hip.ch3.seqfile;

import com.manning.hip.ch3.StockPriceWritable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.DefaultCodec;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

public class SequenceFileStockMapReduce {
  public static void main(String... args) throws Exception {
    runJob(args[0], args[1]);
  }

  public static void runJob(String input,
                            String output)
      throws Exception {
    Configuration conf = new Configuration();
    Job job = new Job(conf);
    job.setJarByClass(SequenceFileStockMapReduce.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(StockPriceWritable.class);
    job.setInputFormatClass(
        SequenceFileInputFormat.class); //<co id="ch03_comment_seqfile_mr1"/>
    job.setOutputFormatClass(SequenceFileOutputFormat.class); //<co id="ch03_comment_seqfile_mr2"/>
    SequenceFileOutputFormat.setCompressOutput(job, true); //<co id="ch03_comment_seqfile_mr3"/>
    SequenceFileOutputFormat.setOutputCompressionType(job, //<co id="ch03_comment_seqfile_mr4"/>
        SequenceFile.CompressionType.BLOCK);
    SequenceFileOutputFormat.setOutputCompressorClass(job, //<co id="ch03_comment_seqfile_mr5"/>
        DefaultCodec.class);

    FileInputFormat.setInputPaths(job, new Path(input));
    Path outPath = new Path(output);
    FileOutputFormat.setOutputPath(job, outPath);
    outPath.getFileSystem(conf).delete(outPath, true);

    job.waitForCompletion(true);
  }
}












