package dichter.drawer;

import java.util.ArrayList;  
import java.util.Arrays;  
  




import android.app.Activity;  
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;  
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;  
import android.widget.CursorAdapter;
import android.widget.ListView;  
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
  
public class List extends ListActivity {  
    
  private ListView mainListView ;  
  private ArrayAdapter<String> listAdapter ;  
  private Intent intent;
  private DbConnector dbConnector;
  private CursorAdapter cursorAdapter; 
  private DrawerView dv;
  private Cursor copy;  
  /** Called when the activity is first created. */  
  @Override  
  public void onCreate(Bundle savedInstanceState) {  
    super.onCreate(savedInstanceState);  
   // setContentView(R.layout.listview);  
   intent = new Intent(this, DrawerActivity.class);
    
    mainListView =getListView();
    mainListView.setOnItemClickListener(ListListener);
    String[] from = new String[] { "name"};
    int[] to = new int[] { R.id.paintTextView };
    cursorAdapter = new SimpleCursorAdapter(
       List.this, R.layout.listtext, null, from, to);
    setListAdapter(cursorAdapter); // set contactView's adapter
    
    String[] planets = new String[] { "Mercury", "Venus", "Earth", "Mars",  
                                      "Jupiter", "Saturn", "Uranus", "Neptune"};    
    ArrayList<String> planetList = new ArrayList<String>();  
    planetList.addAll( Arrays.asList(planets) );  
      
  
   listAdapter = new ArrayAdapter<String>(this, R.layout.listtext);  
   registerForContextMenu(mainListView);

   
    
    
  }  
  @Override
  protected void onResume() 
  {
     super.onResume(); 
     DbConnector dbConnector = 
    	        new DbConnector(List.this);
     dbConnector.open();
     Cursor result=dbConnector.getAllPaints();
     cursorAdapter.changeCursor(result);
     dbConnector.close();
      // create new GetContactsTask and execute it 
     // new GetPaintTask().execute((Object[]) null);
   } // end method onResume

  
  // performs database query outside GUI thread
  private class GetPaintTask extends AsyncTask<Object, Object, Cursor> 
  {
     DbConnector dbConnector = 
        new DbConnector(List.this);

     // perform the database access
     @Override
     protected Cursor doInBackground(Object... params)
     {
        dbConnector.open();
           Cursor result=dbConnector.getAllPaints();
      
        return result;
     } // end method doInBackground

     // use the Cursor returned from the doInBackground method
     @Override
     protected void onPostExecute(Cursor result)
     {
    	cursorAdapter.changeCursor(result);
        dbConnector.close();
   
     } // end method onPostExecute
  } // end class GetContactsTask
    

 


@Override
public boolean onCreateOptionsMenu(Menu menu) 
{
   super.onCreateOptionsMenu(menu); // call super's method

   // add options to menu GROUP_ID and ORDER set to Menu.NONE
   menu.add(Menu.NONE, 0, Menu.NONE,"New Image");


   return true; // options menu creation was handled
} // end onCreateOptionsMenu



public boolean onOptionsItemSelected(MenuItem item) 
{
   // switch based on the MenuItem id
   switch (item.getItemId()) 
   {
      case 0:
    	  startActivity(intent);	
         return true; // consume the menu event
      
   } // end switch
   
   return super.onOptionsItemSelected(item); // call super's method
} //

OnItemClickListener ListListener = new OnItemClickListener() 
{
   @Override
   public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
      long arg3) 
   {
	   
	         DbConnector dbConnector = 
	     	        new DbConnector(List.this);
	      dbConnector.open();
	      Cursor result=dbConnector.getOnePaint(arg3);
	      result.moveToNext();

	      dbConnector.close();
	      intent.putExtra("time", result.getString(3));
	      startActivity(intent);
   } // end method onItemClick
}; // end viewContactListener

@Override
public void onCreateContextMenu(ContextMenu menu, View v,
    ContextMenuInfo menuInfo) {
  if (v.getId()==R.id.mainListView) {
    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
    menu.setHeaderTitle("hello");
    String[] menuItems = {"test"};
    for (int i = 0; i<menuItems.length; i++) {
      menu.add(Menu.NONE, i, i, menuItems[i]);
    }
  }
}


}


