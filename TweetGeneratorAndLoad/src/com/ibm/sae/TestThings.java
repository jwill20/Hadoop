package com.ibm.sae;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Writer;

public class TestThings
{

	public static void main(String[] args)
	{
		//int[][] tda = new int[4][4];
		System.out.println("Here we go");
		
		String abc = new String("abc");
		byte[] barray = abc.getBytes();
		String def = new String(barray);
		System.out.println("Length = " + barray.length);
		System.out.println(def);
		
		try
		{
			PrintWriter pw 
			   = new PrintWriter(
					   new BufferedWriter(
							   new FileWriter("generatedOutput")));
			
			pw.println("sentimentLine");
		} 
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		
		try
		{
			FileOutputStream out = new FileOutputStream("tmp");  // true means append
			ObjectOutput objOut = new ObjectOutputStream(out);
			objOut.write(barray);      // 1 bytes per character
			objOut.writeInt(254);      // 4 bytes
			objOut.writeInt(256);      // 4 bytes
			objOut.writeInt(255);      // 4 bytes
			objOut.writeInt('a');      // 4 bytes
			objOut.writeChar('b');     // 2 bytes
			objOut.writeBytes("XYZ");  // 1 byte per character
			objOut.flush();
			objOut.close();
			//objOut.writeChars("abc");
			
			FileInputStream in = new FileInputStream("tmp");
			ObjectInputStream objin = new ObjectInputStream(in);
			//String str = (String) objin.readObject();
			//int num = objin.readInt();
			int num = objin.available();  // returns # of bytes
			System.out.println("Here is loop");
			while (objin.available() != 0)
			{
				System.out.println("Here is data");
				num = objin.read();
				System.out.println(num);
			}
		} 
		catch (FileNotFoundException e)
		{
			System.out.println("file not found");
			e.printStackTrace();
		}
		catch (IOException e)
		{
			System.out.println("io exception");
			e.printStackTrace();
		}
		//
		//System.out.println(tda[1][1]);
		//for (int a = 1; a < tda[1].length; a++)
		//{
		//catch (ClassNotFoundException e)
		//{
			//System.out.println("class not found");
			//e.printStackTrace();
		//}
			
		//}

	}

}
