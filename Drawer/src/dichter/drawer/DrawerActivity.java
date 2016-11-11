package dichter.drawer;

import dichter.drawer.R;

import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class DrawerActivity extends Activity
{
   private DrawerView drawerView; // drawing View
   private SensorManager sensorManager; // monitors accelerometer
   private float acceleration; // acceleration
   private float currentAcceleration; // current acceleration
   private float lastAcceleration; // last acceleration
   private AtomicBoolean dialogIsVisible = new AtomicBoolean(); // false


   // create menu ids for each menu option 
   private static final int COLOR_MENU_ID = Menu.FIRST;
   private static final int WIDTH_MENU_ID = Menu.FIRST + 1;
   private static final int ERASE_MENU_ID = Menu.FIRST + 2;
   private static final int CLEAR_MENU_ID = Menu.FIRST + 3;
   private static final int SAVE_MENU_ID = Menu.FIRST + 4;
   private static final int LOAD_IMAGE_ID = Menu.FIRST + 5;
   private static int RESULT_LOAD_IMAGE = 1;
   private String imagePath;
   private Intent i;
   
   // value used to determine whether user shook the device to erase
   private static final int ACCELERATION_THRESHOLD = 15000;
   
   // variable that refers to a Choose Color or Choose Line Width dialog
   private Dialog currentDialog; 

   // called when this Activity is loaded
   @Override
   protected void onCreate(Bundle savedInstanceState) 
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main); // inflate the layout
      
      Log.e("QQQ", "Entering onCreate"); 

      drawerView = (DrawerView) findViewById(R.id.drawerView);
      
      // initialize acceleration values
      acceleration = 0.00f; 
      currentAcceleration = SensorManager.GRAVITY_EARTH;    // 9.8 m/sec^2
      lastAcceleration = SensorManager.GRAVITY_EARTH;    
     
      enableAccelerometerListening(); // listen for shake
      i=getIntent();
      if(i.getExtras()!=null)
      {
    	  
    	  Log.e("intent",i.getStringExtra("time")); 
		  //drawerView.setBitmap(i.getStringExtra("time").toString());
    	 // setImage();
		  // loadImage();  
    	 
      }
      
   } // end method onCreate
   
   public void setImage()
   {
	   
	   drawerView.setBitmap("/storage/sdcard0/DCIM/Camera/"+getIntent().getStringExtra("time")+".jpg");
   }

   // when app is sent to the background, stop listening for sensor events
   @Override
   protected void onPause()
   {
      super.onPause();
      disableAccelerometerListening(); // don't listen for shake 
   } // end method onPause
 
   // enable listening for accelerometer events
   private void enableAccelerometerListening()
   {
      // initialize the SensorManager
      sensorManager = 
         (SensorManager) getSystemService(Context.SENSOR_SERVICE);
      sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 
         SensorManager.SENSOR_DELAY_NORMAL);
   } // end method enableAccelerometerListening
   
   // disable listening for accelerometer events
   private void disableAccelerometerListening()
   {
      // stop listening for sensor events
      if (sensorManager != null) 
      {         
    	  	sensorManager.unregisterListener(
            sensorEventListener, 
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));         
         	sensorManager = null;     
      } // end if
   } // end method disableAccelerometerListening

   // event handler for accelerometer events
   private SensorEventListener sensorEventListener = new SensorEventListener()
      {
         // use accelerometer to determine whether user shook device 
         @Override
         public void onSensorChanged(SensorEvent event)
         {  
            // ensure that other dialogs are not displayed
            if (!dialogIsVisible.get())
            {
               // get x, y, and z values for the SensorEvent
               float x = event.values[0];
               float y = event.values[1];
               float z = event.values[2];
      
               // save previous acceleration value
               lastAcceleration = currentAcceleration;
      
               // calculate the current acceleration
               currentAcceleration = x * x + y * y + z * z;
      
               // calculate the change in acceleration
               acceleration = currentAcceleration * 
                  (currentAcceleration - lastAcceleration);
      
               // if the acceleration is above a certain threshold
               if (acceleration > ACCELERATION_THRESHOLD)
               {
                  // create a new AlertDialog Builder
                  AlertDialog.Builder builder = 
                     new AlertDialog.Builder(DrawerActivity.this);
                
                  // set the AlertDialog's message 
                  builder.setMessage(R.string.message_erase);
                  builder.setCancelable(true);
                
                  // add Erase Button
                  builder.setPositiveButton(R.string.button_erase,
                     new DialogInterface.OnClickListener() 
                     {
                        public void onClick(DialogInterface dialog, int id) 
                        {
                          dialogIsVisible.set(false);
                          drawerView.clear(); // clear the screen
                        } // end method onClick
                     } // end anonymous inner class
                  ); // end call to setPositiveButton
               
                  // add Cancel Button
                  builder.setNegativeButton(R.string.button_cancel,
                     new DialogInterface.OnClickListener() 
                     {
                        public void onClick(DialogInterface dialog, int id) 
                        {
                          dialogIsVisible.set(false);
                          dialog.cancel(); // dismiss the dialog
                        } // end method onClick
                     } // end anonymous inner class
                  ); // end call to setNegativeButton
      
                  dialogIsVisible.set(true); // dialog is on the screen
                  builder.show(); // display the dialog
               } // end if
            } // end if
         } // end method onSensorChanged
      
         // required method of interface SensorEventListener
         @Override
         public void onAccuracyChanged(Sensor sensor, int accuracy)
         {
         } // end method onAccuracyChanged
      }; // end anonymous inner class 
   
   // displays configuration options in menu
   @Override
   public boolean onCreateOptionsMenu(Menu menu) 
   {
      super.onCreateOptionsMenu(menu); // call super's method

      // add options to menu GROUP_ID and ORDER set to Menu.NONE
      menu.add(Menu.NONE, COLOR_MENU_ID, Menu.NONE, R.string.menuitem_color);
      menu.add(Menu.NONE, WIDTH_MENU_ID, Menu.NONE, R.string.menuitem_line_width);
      menu.add(Menu.NONE, ERASE_MENU_ID, Menu.NONE, R.string.menuitem_erase);
      menu.add(Menu.NONE, CLEAR_MENU_ID, Menu.NONE, R.string.menuitem_clear);
      menu.add(Menu.NONE, SAVE_MENU_ID, Menu.NONE,  R.string.menuitem_save_image);
      menu.add(Menu.NONE, LOAD_IMAGE_ID, Menu.NONE, "Load image");
      

      return true; // options menu creation was handled
   } // end onCreateOptionsMenu

   // handle choice from options menu
   @Override
   public boolean onOptionsItemSelected(MenuItem item) 
   {
      // switch based on the MenuItem id
      switch (item.getItemId()) 
      {
         case COLOR_MENU_ID:
            showColorDialog(); // display color selection dialog
            return true; // consume the menu event
         case WIDTH_MENU_ID:
            showLineWidthDialog(); // display line thickness dialog
            return true; // consume the menu event
         case ERASE_MENU_ID:
            drawerView.setDrawingColor(Color.WHITE); // line color white
            return true; // consume the menu event
         case CLEAR_MENU_ID:
            drawerView.clear(); // clear drawerView
            return true; // consume the menu event
         case SAVE_MENU_ID:     
            showSaveDialog(); // save the current images
            return true; // consume the menu event
         case LOAD_IMAGE_ID:     
           loadImage(); // save the current images
             return true;   
      } // end switch
      
      return super.onOptionsItemSelected(item); // call super's method
   } // end method onOptionsItemSelected
     
   public void loadImage()
   {
	   
	  Intent i1 = new Intent(
               Intent.ACTION_PICK,
               android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
	 
      Log.e("log","reached load image");
       startActivityForResult(i1, RESULT_LOAD_IMAGE);
   }
   

   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       super.onActivityResult(requestCode, resultCode, data);

       if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
           Uri selectedImage = data.getData();
           String[] filePathColumn = { MediaStore.Images.Media.DATA };

           Cursor cursor = getContentResolver().query(selectedImage,
                   filePathColumn, null, null, null);
           cursor.moveToFirst();

           int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
          String picturePath = cursor.getString(columnIndex);
         
           cursor.close();

		         Log.e("log","almost there");
        drawerView.setBitmap(picturePath);
          Log.e("log",picturePath);

       }


   }
   // display a dialog for selecting color
   
   private void showSaveDialog()
   {
	      currentDialog = new Dialog(this);
	      currentDialog.setContentView(R.layout.savename);
	      currentDialog.setTitle("Give a name");
	      currentDialog.setCancelable(true); 
	      currentDialog.show();
	   
	     Button saveButton=(Button)currentDialog.findViewById(R.id.saveButton);
	saveButton.setOnClickListener(saveButtonListener);
	   
   }
   
   private OnClickListener saveButtonListener = new OnClickListener() 
   {
      @Override
      public void onClick(View v) 
      {
       
    	  EditText name=(EditText)currentDialog.findViewById(R.id.saveName);
    	 
    	  
    	  if(name.getText().toString().length()==0)
    	  {
    		  
    	 
    	   currentDialog.setTitle("Please enter a name");
    	 
    	  }
    	  else
    	  {
    		  drawerView.saveImage(name.getText().toString());
    		  dialogIsVisible.set(false); // dialog is not on the screen
          currentDialog.dismiss(); // hide the dialog
          currentDialog = null;
          } //
      } // end method onClick
   }; // end saveNameListener
   private void showColorDialog()
   {
      // create the dialog and inflate its content
      currentDialog = new Dialog(this);
      currentDialog.setContentView(R.layout.color_dialog);
      currentDialog.setTitle(R.string.title_color_dialog);
      currentDialog.setCancelable(true);
      
      // get the color SeekBars and set their onChange listeners
      final SeekBar alphaSeekBar = 
         (SeekBar) currentDialog.findViewById(R.id.alphaSeekBar);
      final SeekBar redSeekBar = 
         (SeekBar) currentDialog.findViewById(R.id.redSeekBar);
      final SeekBar greenSeekBar = 
         (SeekBar) currentDialog.findViewById(R.id.greenSeekBar);
      final SeekBar blueSeekBar = 
         (SeekBar) currentDialog.findViewById(R.id.blueSeekBar);

      // register SeekBar event listeners
      // colorSeekBarChangedListener defined below
      
      alphaSeekBar.setOnSeekBarChangeListener(colorSeekBarChangedListener);
      redSeekBar.setOnSeekBarChangeListener(colorSeekBarChangedListener);
      greenSeekBar.setOnSeekBarChangeListener(colorSeekBarChangedListener);
      blueSeekBar.setOnSeekBarChangeListener(colorSeekBarChangedListener);
     
      // use current drawing color to set SeekBar values
      final int color = drawerView.getDrawingColor();
      alphaSeekBar.setProgress(Color.alpha(color));
      redSeekBar.setProgress(Color.red(color));
      greenSeekBar.setProgress(Color.green(color));
      blueSeekBar.setProgress(Color.blue(color));        
      
      // set the Set Color Button's onClickListener
      // setColorButtonListener defined below
      
      Button setColorButton = (Button) currentDialog.findViewById(R.id.setColorButton);
      setColorButton.setOnClickListener(setColorButtonListener);
 
      dialogIsVisible.set(true); // dialog is on the screen
      currentDialog.show(); // show the dialog
   } // end method showColorDialog
   
   // OnSeekBarChangeListener for the SeekBars in the color dialog
   private OnSeekBarChangeListener colorSeekBarChangedListener = new OnSeekBarChangeListener() 
   {
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) 
      {
         // get the SeekBars and the colorView LinearLayout
         SeekBar alphaSeekBar = (SeekBar) currentDialog.findViewById(R.id.alphaSeekBar);
         SeekBar redSeekBar   = (SeekBar) currentDialog.findViewById(R.id.redSeekBar);
         SeekBar greenSeekBar = (SeekBar) currentDialog.findViewById(R.id.greenSeekBar);
         SeekBar blueSeekBar  = (SeekBar) currentDialog.findViewById(R.id.blueSeekBar);
         
         // The colorView displays the color as it is being selected by the Seekbars
         View colorView = (View) currentDialog.findViewById(R.id.colorView);

         // display the current color
         colorView.setBackgroundColor(Color.argb(alphaSeekBar.getProgress(), redSeekBar.getProgress(), 
        		 					          greenSeekBar.getProgress(), blueSeekBar.getProgress()));
      } // end method onProgressChanged
      
      // required method of interface OnSeekBarChangeListener; start of seekbar movement
      @Override
      public void onStartTrackingTouch(SeekBar seekBar) 
      {
      } 
      
      // required method of interface OnSeekBarChangeListener; end of seekbar movement
      @Override
      public void onStopTrackingTouch(SeekBar seekBar) 
      {
      } 
   }; // end colorSeekBarChanged
   
   // OnClickListener for the color dialog's Set Color Button
   private OnClickListener setColorButtonListener = new OnClickListener() 
   {
      @Override
      public void onClick(View v) 
      {
         // get the color SeekBars
         SeekBar alphaSeekBar = (SeekBar) currentDialog.findViewById(R.id.alphaSeekBar);
         SeekBar redSeekBar   = (SeekBar) currentDialog.findViewById(R.id.redSeekBar);
         SeekBar greenSeekBar = (SeekBar) currentDialog.findViewById(R.id.greenSeekBar);
         SeekBar blueSeekBar =  (SeekBar) currentDialog.findViewById(R.id.blueSeekBar);

         // set the line color
         drawerView.setDrawingColor(Color.argb(
            alphaSeekBar.getProgress(), redSeekBar.getProgress(), 
            greenSeekBar.getProgress(), blueSeekBar.getProgress()));
         dialogIsVisible.set(false); // dialog is not on the screen
         currentDialog.dismiss(); // hide the dialog
         currentDialog = null; // dialog no longer needed
      } // end method onClick
   }; // end setColorButtonListener
   
 
   
   // display a dialog for setting the line width
   private void showLineWidthDialog()
   {
      // create the dialog and inflate its content
      currentDialog = new Dialog(this);
      currentDialog.setContentView(R.layout.width_dialog);
      currentDialog.setTitle(R.string.title_line_width_dialog);
      currentDialog.setCancelable(true);
      
      // get widthSeekBar and configure it
      SeekBar widthSeekBar = (SeekBar) currentDialog.findViewById(R.id.widthSeekBar);
      widthSeekBar.setOnSeekBarChangeListener(widthSeekBarChangedListener);
      widthSeekBar.setProgress(drawerView.getLineWidth()); 
       
      // set the Set Line Width Button's onClickListener
      Button setLineWidthButton = 
         (Button) currentDialog.findViewById(R.id.widthDialogDoneButton);
      setLineWidthButton.setOnClickListener(setLineWidthButtonListener);
      
      dialogIsVisible.set(true); // dialog is on the screen
      currentDialog.show(); // show the dialog      
   } // end method showLineWidthDialog

   // OnSeekBarChangeListener for the SeekBar in the width dialog
   private OnSeekBarChangeListener widthSeekBarChangedListener = new OnSeekBarChangeListener() 
      {
	     // Bitmap.Config.ARGB_8888 stores each pixel in 4 bytes
         Bitmap bitmap = Bitmap.createBitmap(400, 100, Bitmap.Config.ARGB_8888);
         Canvas canvas = new Canvas(bitmap); // associate with Canvas
         
         @Override
         public void onProgressChanged(SeekBar seekBar, int value, boolean fromUser) 
         {  
            // get the ImageView
            ImageView widthImageView = (ImageView) currentDialog.findViewById(R.id.widthImageView);
            
            // configure a Paint object for the current SeekBar value
            Paint p = new Paint();
            p.setColor(drawerView.getDrawingColor());
            p.setStrokeCap(Paint.Cap.ROUND);
            p.setStrokeWidth(value);
            
            // fill bitmap with white and redraw the line
            bitmap.eraseColor(Color.WHITE);
            canvas.drawLine(30, 50, 370, 50, p);
            widthImageView.setImageBitmap(bitmap);
         } // end method onProgressChanged
   
         // required method of interface OnSeekBarChangeListener
         @Override
         public void onStartTrackingTouch(SeekBar seekBar) 
         {
         } // end method onStartTrackingTouch
   
         // required method of interface OnSeekBarChangeListener
         @Override
         public void onStopTrackingTouch(SeekBar seekBar) 
         {
         } // end method onStopTrackingTouch
      }; // end widthSeekBarChanged

   // OnClickListener for the line width dialog's Set Line Width Button
   private OnClickListener setLineWidthButtonListener = new OnClickListener() {
         @Override
         public void onClick(View v) 
         {
            // get the color SeekBars
            SeekBar widthSeekBar = (SeekBar) currentDialog.findViewById(R.id.widthSeekBar);
   
            // set the line color
            drawerView.setLineWidth(widthSeekBar.getProgress());
            dialogIsVisible.set(false); // dialog is not on the screen
            currentDialog.dismiss(); // hide the dialog
            currentDialog = null; // dialog no longer needed
         } 
      }; 
      
  public void setImage(String path)
  {
	  
	  drawerView.iPath=path;
  }
}