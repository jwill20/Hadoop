package com.ibm.sae;

import java.net.UnknownHostException;
import java.util.Set;
import com.mongodb.*;

public class TestMongo
{
	public static void main(String[] args)
	{
		try
		{
			System.out.println("Begin");

			/******************************************/
			/* Connect to database and collection     */
			/******************************************/
			Mongo m = new Mongo(TweetConstants.MDB_HOSTNAME,
		            TweetConstants.MDB_PORT);
            DB db = m.getDB(TweetConstants.MDB_DBNAME);
            DBCollection coll = db.getCollection(TweetConstants.MDB_COLLNAME);
			
			/******************************************/
			/* Get all collections in this database   */
			/******************************************/
			Set<String> colls = db.getCollectionNames();
			for (String s : colls) 
			{
			    System.out.println(s);
			}
			
			/*		        
	        //coll.remove(newDoc);
	        System.out.println("Print all that have a type of database");
	        DBCursor cursor = coll.find(newDoc);

	        */
			
			BasicDBObject newDoc = new BasicDBObject();
	        newDoc.put("ticker", "IBM");
	        DBCursor cur = coll.find(newDoc);

	        // loop over the cursor and display the retrieved result
	        int a = 0;
			while (cur.hasNext()) 
			{
				System.out.print(a + " ");
				System.out.println(cur.next());
				a++;
			}
	        
			System.out.println("");
			System.out.println("Now print everything");

            cur = coll.find();
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
		} 
		catch (MongoException e)
		{
			e.printStackTrace();
		}
	
	}

}
