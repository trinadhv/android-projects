package dichter.drawer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;


public class DbConnector
{
   // database name
   private static final String DATABASE_NAME = "Drawer";
   private SQLiteDatabase database; // database object
   private DatabaseOpenHelper databaseOpenHelper;
   private Context context;// database helper
   private String savedPath;
 
   // public constructor for DatabaseConnector
   public DbConnector(Context context) 
   {
      // create a new DatabaseOpenHelper
	   this.context=context;
      databaseOpenHelper = 
         new DatabaseOpenHelper(context, DATABASE_NAME, null, 1);
    
   } // end DatabaseConnector constructor

   // open the database connection
   public void open() throws SQLException 
   {
      // create or open a database for reading/writing
      database = databaseOpenHelper.getWritableDatabase();
   } // end method open

   // close the database connection
   public void close() 
   {
      if (database != null)
         database.close(); // close the database connection
   } // end method close

   // inserts a new contact in the database
   public void insertPaint(String name,String path,long time) 
   {
      ContentValues newPaint = new ContentValues();
     
      newPaint.put("name",name);
      newPaint.put("path",path);
      newPaint.put("time",time);
      
 
      open(); // open the database
      database.insert("Drawer", null, newPaint);
      Log.e("QQQ",name);
      Log.e("QQQ",path);
      Log.e("QQQ",Long.toString(time));
      close(); // close the database
   } // end method insertContact

   // inserts a new contact in the database
   public void updatePaint(long id,String name,String path) 
   {
      ContentValues editPaint = new ContentValues();
 
      editPaint.put("name",name);
      editPaint.put("path",path);

      open(); // open the database
      database.update("Drawer", editPaint, "_id=" + id, null);
      close(); // close the database
   } // end method updateContact

   // return a Cursor with all contact information in the database
   public Cursor getAllPaints() 
   {
    return database.query("Drawer", new String[] {"_id", "name","path","time"}, 
         null, null, null, null, "name");
	 


   } // end method getAllContacts

   // get a Cursor containing all information about the contact specified
   // by the given id
   public Cursor getOnePaint(long id) 
   {
     /* return database.query(
         "Drawer", null, "_id=" + id, null, null, null, null);
         */
	   Cursor c=database.query("Drawer", null, "_id=" + id, null, null, null, null);
	   return c;
	 
   } // end method getOnContact

   // delete the contact specified by the given String name
   public void deletePaint(long id) 
   {
      open(); // open the database
      database.delete("Drawer", "_id=" + id, null);
      close(); // close the database
   } // end method deleteContact
   
   private class DatabaseOpenHelper extends SQLiteOpenHelper 
   {
      // public constructor
      public DatabaseOpenHelper(Context context, String name,
         CursorFactory factory, int version) 
      {
         super(context, name, factory, version);
      } // end DatabaseOpenHelper constructor

      // creates the contacts table when the database is created
      @Override
      public void onCreate(SQLiteDatabase db) 
      {
         // query to create a new table named contacts
         String createQuery = "CREATE TABLE Drawer" +
            "(_id integer primary key autoincrement," +
            "name TEXT,path TEXT,time LONG);";
                  
         db.execSQL(createQuery);
         Log.e("QQQ","Database Connected");// execute the query
      } // end method onCreate

      @Override
      public void onUpgrade(SQLiteDatabase db, int oldVersion, 
          int newVersion) 
      {
      } // end method onUpgrade
   } // end class DatabaseOpenHelper
   
public void savePath(String path)
{
savedPath=path;	
}
} // end class DatabaseConnector


