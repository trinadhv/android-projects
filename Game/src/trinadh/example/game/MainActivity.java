package trinadh.example.game;





import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.media.AudioManager;

import android.hardware.Sensor;  
import android.hardware.SensorEvent;  
import android.hardware.SensorEventListener;  
import android.hardware.SensorManager;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements SensorEventListener   {
   
	
	 private SensorManager sManager;  
	 private Sensor accelero;
	 private GameView gameView;
	 private TextView sensorText;
	 final Context context = this;
	


     @Override
     public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);
         setVolumeControlStream(AudioManager.STREAM_MUSIC);
       newGame();
         
          
        
     }
public void enableSensor()
{
	 sManager = (SensorManager) getSystemService(SENSOR_SERVICE);  
     accelero=sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	 sManager.registerListener(this, accelero,SensorManager.SENSOR_DELAY_NORMAL);
}

public void disableSensor()
{
	 sManager.unregisterListener(this);
}

	@Override
	   public void onSensorChanged(SensorEvent event) {
		/*Integer x,y,hx,hy;
		x=gameView.ball1x;
		y=(int)event.values[1];
		hx=gameView.holeX;
		hy=gameView.holeY;*/
		if(gameView.gameOver)
		{
		     disableSensor();
		     gameView.destroyDrawingCache();
		   
			showAlertDialogBox();
		}
		else
		{
		
		gameView.setBall1XY(event);	
		gameView.invalidate();
		}
		
		
	}
	
	public void newGame()
	{
		gameView=(GameView)findViewById(R.id.gameView);
        
     
        enableSensor();	
		
	}
	
	
	public void showAlertDialogBox()
	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);
 
		
			alertDialogBuilder.setTitle(gameView.gameStatus);
 
		
			alertDialogBuilder
				.setMessage("Want to play Again?")
				.setCancelable(false)
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
					
					
						gameView.gameOver=false;
					    gameView.ball1x=gameView.screenWidth/2;
					    gameView.ball1y=gameView.screenHeight-30;
					    gameView.ball2x=gameView.screenWidth/2;
					    gameView.ball2y=gameView.screenHeight-30;
					    gameView.holeX=gameView.ball1x*2-gameView.ball1x/3;
					    gameView.holeY=gameView.screenHeight/4;
						newGame();
					}
				  })
				.setNegativeButton("No",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
					
						disableSensor();
						
						MainActivity.this.finish();
					}
				});
 
			
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
	}
	
@Override
protected void onDestroy()
{
	
super.onDestroy();
gameView.releaseResources();
}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	
	


	 @Override
	   public boolean onCreateOptionsMenu(Menu menu)             
	   {            
	      super.onCreateOptionsMenu(menu);                        
	                                                              
	 
	      menu.add(Menu.NONE, 1, Menu.NONE, "BACKGROUND");             
	      menu.add(Menu.NONE,2, Menu.NONE, "DIFFICULTY");             
	                                                              
	      return true; // display the menu                        
	   }  // end method onCreateOptionsMenu                       


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	
		int id = item.getItemId();
		
		
		if (id ==1) {
		
		            // create a new AlertDialog Builder and set its title
		            AlertDialog.Builder choicesBuilder = 
		               new AlertDialog.Builder(this);         
		            choicesBuilder.setTitle("select background");
		         
		            // add possibleChoices's items to the Dialog and set the 
		            // behavior when one of the items is clicked
		            choicesBuilder.setItems(R.array.backgrounds,                    
		               new DialogInterface.OnClickListener()                    
		               {                                                        
		                  public void onClick(DialogInterface dialog, int item) 
		                  {                                                     
		                    gameView.chosenBackground=item; 
		                    newGame();
		                    gameView.newGame();// reset the quiz                     
		                  } // end method onClick                               
		               } // end anonymous inner class
		            );  // end call to setItems                             
		         
		            // create an AlertDialog from the Builder
		            AlertDialog choicesDialog = choicesBuilder.create();
		            choicesDialog.show(); // show the Dialog            
		            return true; 
		}//if close
		
		if (id ==2) {
			
            // create a new AlertDialog Builder and set its title
            AlertDialog.Builder choicesBuilder = 
               new AlertDialog.Builder(this);         
            choicesBuilder.setTitle("Choose Difficulty");
         
            // add possibleChoices's items to the Dialog and set the 
            // behavior when one of the items is clicked
            choicesBuilder.setItems(R.array.velocity,                    
               new DialogInterface.OnClickListener()                    
               {                                                        
                  public void onClick(DialogInterface dialog, int item) 
                  {                                                     
                    switch(item)
                    {
                    case 0 : gameView.ballVelocity=1;
                            break;
                    case 1: gameView.ballVelocity=3;
                            break;
                    case 2: gameView.ballVelocity=5;
                    break;         
        
                    }
                    newGame();
                    gameView.newGame();// reset the quiz                     
                  } // end method onClick                               
               } // end anonymous inner class
            );  // end call to setItems                             
         
            // create an AlertDialog from the Builder
            AlertDialog choicesDialog = choicesBuilder.create();
            choicesDialog.show(); // show the Dialog            
            return true; 
}//if close
		
		return super.onOptionsItemSelected(item);
	}
	
}