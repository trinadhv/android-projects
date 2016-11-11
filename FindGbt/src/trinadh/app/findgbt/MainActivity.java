package trinadh.app.findgbt;



import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


public class MainActivity extends Activity {
	private Spinner routelist,towardsList,where;

   private	Integer busroute,to;

   private Button track,find,exit,schedule;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		routelist = (Spinner) findViewById(R.id.route_list);
		towardsList=(Spinner)findViewById(R.id.towards_list);
	
		track=(Button)findViewById(R.id.track);
		find=(Button)findViewById(R.id.button1);
		exit=(Button)findViewById(R.id.exit);
		schedule=(Button)findViewById(R.id.schedule);
		ArrayAdapter<CharSequence> routes = ArrayAdapter.createFromResource(this,
		        R.array.routes, android.R.layout.simple_spinner_item);
		busroute=1;
		routes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		routelist.setAdapter(routes);
		routelist.setOnItemSelectedListener(new BusRouteListener());
		towardsList.setOnItemSelectedListener(new TowardsListener());
	
		
		track.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	Intent myIntent = new Intent(MainActivity.this, BusTracker.class);
		   
		    	MainActivity.this.startActivity(myIntent);
		    }
		});
		
		
		find.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    
		  	
		    	Intent myIntent = new Intent(MainActivity.this, BusTimer.class);
				   myIntent.putExtra("route", busroute.toString());
				   myIntent.putExtra("to", to.toString());
		    	MainActivity.this.startActivity(myIntent);
		    }
		});
		
		schedule.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    
		  	
		    	Intent myIntent = new Intent(MainActivity.this, BusSchedule.class);
				
		    	MainActivity.this.startActivity(myIntent);
		    }
		});
		
		exit.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    
		  	
		    	 finish();
		         System.exit(0);
		    }
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	

	 class BusRouteListener  implements  OnItemSelectedListener {

		

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position,
				long id) {
			switch(position)
			{
			
			case 0:busroute=1;
				ArrayAdapter<CharSequence> adapter0 = ArrayAdapter.createFromResource(getBaseContext(),
				        R.array.route1, android.R.layout.simple_spinner_item);
				towardsList.setAdapter(adapter0);
	            break;
			case 1: 
			        busroute=9;
			ArrayAdapter<CharSequence> adaptercl = ArrayAdapter.createFromResource(getBaseContext(),
			        R.array.routecl, android.R.layout.simple_spinner_item);
			towardsList.setAdapter(adaptercl);
            break;
            
			case 2:ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(getBaseContext(),
			        R.array.route3, android.R.layout.simple_spinner_item);
			towardsList.setAdapter(adapter3);
			busroute=14;
	        break;
			case 3:
				ArrayAdapter<CharSequence> adapter4= ArrayAdapter.createFromResource(getBaseContext(),
			        R.array.route4, android.R.layout.simple_spinner_item);
			towardsList.setAdapter(adapter4);
			busroute=15;
      
	        break;
			case 4:ArrayAdapter<CharSequence> adapter5 = ArrayAdapter.createFromResource(getBaseContext(),
			        R.array.route5, android.R.layout.simple_spinner_item);
			towardsList.setAdapter(adapter5);
			busroute=23;
      
	        break;
			case 5:ArrayAdapter<CharSequence> adapter6 = ArrayAdapter.createFromResource(getBaseContext(),
			        R.array.route6, android.R.layout.simple_spinner_item);
			towardsList.setAdapter(adapter6);
			busroute=16;
	        break;
			case 6:ArrayAdapter<CharSequence> adapter7 = ArrayAdapter.createFromResource(getBaseContext(),
			        R.array.route7, android.R.layout.simple_spinner_item);
			towardsList.setAdapter(adapter7);
			busroute=17;
	        break;
			case 7:ArrayAdapter<CharSequence> adapter8 = ArrayAdapter.createFromResource(getBaseContext(),
			        R.array.route8, android.R.layout.simple_spinner_item);
			towardsList.setAdapter(adapter8);
			busroute=18;
	        break;
			case 8:ArrayAdapter<CharSequence> adapter9 = ArrayAdapter.createFromResource(getBaseContext(),
			        R.array.route9, android.R.layout.simple_spinner_item);
			towardsList.setAdapter(adapter9);
			busroute=19;
	        break;
			case 9:ArrayAdapter<CharSequence> adapter10 = ArrayAdapter.createFromResource(getBaseContext(),
			        R.array.route10, android.R.layout.simple_spinner_item);
			towardsList.setAdapter(adapter10);
			busroute=2;
	        break;
			case 10:ArrayAdapter<CharSequence> adapter13 = ArrayAdapter.createFromResource(getBaseContext(),
			        R.array.route13, android.R.layout.simple_spinner_item);
			towardsList.setAdapter(adapter13);
			busroute=3;
	        break;
			case 11:ArrayAdapter<CharSequence> adapter14 = ArrayAdapter.createFromResource(getBaseContext(),
			        R.array.route14, android.R.layout.simple_spinner_item);
			towardsList.setAdapter(adapter14);
			busroute=4;
	        break;
			case 12:ArrayAdapter<CharSequence> adapter15 = ArrayAdapter.createFromResource(getBaseContext(),
			        R.array.route15, android.R.layout.simple_spinner_item);
			towardsList.setAdapter(adapter15);
			busroute=5;
	        break;
			case 13:ArrayAdapter<CharSequence> adapter16 = ArrayAdapter.createFromResource(getBaseContext(),
			        R.array.route16, android.R.layout.simple_spinner_item);
			towardsList.setAdapter(adapter16);
			busroute=22;
	        break;
			case 14:ArrayAdapter<CharSequence> adapter17 = ArrayAdapter.createFromResource(getBaseContext(),
			        R.array.route17, android.R.layout.simple_spinner_item);
			towardsList.setAdapter(adapter17);
			busroute=6;
	        break;
			case 15:ArrayAdapter<CharSequence> adapter19x = ArrayAdapter.createFromResource(getBaseContext(),
			        R.array.route19x, android.R.layout.simple_spinner_item);
			towardsList.setAdapter(adapter19x);
			busroute=8;
			
	        break;
			case 16:ArrayAdapter<CharSequence> adapter20 = ArrayAdapter.createFromResource(getBaseContext(),
			        R.array.route20, android.R.layout.simple_spinner_item);
			towardsList.setAdapter(adapter20);
			busroute=10;
	        break;
			case 17:ArrayAdapter<CharSequence> adapter22x = ArrayAdapter.createFromResource(getBaseContext(),
			        R.array.route22x, android.R.layout.simple_spinner_item);
			towardsList.setAdapter(adapter22x);
			busroute=12;
	        break;
		
			case 19:ArrayAdapter<CharSequence> adapter23 = ArrayAdapter.createFromResource(getBaseContext(),
			        R.array.route23, android.R.layout.simple_spinner_item);
			towardsList.setAdapter(adapter23);
			busroute=13;
	        break;
	
			
			}
			
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
	}
	 
	 
	 
	 

	 class TowardsListener  implements  OnItemSelectedListener {

		

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position,
				long id) {
		to=position+1;
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
	}
	 
	 
	 


}

