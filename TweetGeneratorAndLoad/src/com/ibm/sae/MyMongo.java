package com.ibm.sae;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.mongodb.MongoException;


public class MyMongo
{

	public static void main(String[] args)
	{
		try
		{
			System.out.println("Begin");

			/******************************************/
			/* Connect to database and collection     */
			/******************************************/
			Mongo m = new Mongo( "localhost" , 27017 );
			DB db = m.getDB( "kushlie" );
			DBCollection coll = db.getCollection("mycoll");
			
			List<String> myl = new ArrayList<String>();
			
			/******************************************/
			/* Get all collections in this database   */
			/******************************************/
			//Set<String> colls = db.getCollectionNames();
			//for (String s : colls) 
			//{
			//    System.out.println(s);
			//}
			
			/******************************************/
			/* Create a doc and place in collection   */
			/******************************************/
			BasicDBObject doc = new BasicDBObject();
	        doc.put("name", "MongoDB");
	        doc.put("type", "database");
	        doc.put("count", 1);

	        BasicDBObject innerDoc = new BasicDBObject();
	        innerDoc.put("x", 203);
	        innerDoc.put("y", 102);

	        doc.put("innerDoc", innerDoc);

	        coll.insert(doc);
	        
	        BasicDBObject newDoc = new BasicDBObject();
	        newDoc.put("type", "database");
	        
	        //coll.remove(newDoc);
	        System.out.println("Print all that have a type of database");
	        DBCursor cursor = coll.find(newDoc);
	        
			// loop over the cursor and display the retrieved result
	        int a = 0;
			while (cursor.hasNext()) 
			{
				System.out.print(a + " ");
				System.out.println(cursor.next());
				a++;
			}
	        
			System.out.println("");
			System.out.println("Now print everything");
			
            DBCursor cur = coll.find();
            int b = 0;
	        while(cur.hasNext()) 
	        {
				System.out.print(b + " ");
	            System.out.println(cur.next());
	            b++;
	        }
	        
	        //DBObject myDoc = coll.findOne();
	        //System.out.println(myDoc);
	        //myDoc = coll.find("{"_id": "4ffdd15e9fc108b9f8964580"}");
	        //System.out.println(myDoc);
	        
			System.out.println("End");
			
		} 
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		} catch (MongoException e)
		{
			e.printStackTrace();
		}
	
	}

}
