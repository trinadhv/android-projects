package dichter.drawer;

import dichter.drawer.R;























import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.HashMap;












import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.text.Editable;
import android.util.AttributeSet;
import android.util.Config;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

// the main screen that is painted
public class DrawerView extends View 
{
   // used to determine whether user moved a finger enough to draw again   
   private static final float TOUCH_TOLERANCE = 10;


   private Bitmap bitmap; // drawing area for display or saving
   private Canvas bitmapCanvas; // used to draw on bitmap
   private Paint paintScreen; // use to draw bitmap onto screen
   private Paint paintLine; // used to draw lines onto bitmap
   private HashMap<Integer, Path> pathMap; // current Paths being drawn
   private HashMap<Integer, Point> previousPointMap; // current Points
   private boolean isImage;
   private DbConnector dbConnector;
   public String iPath;


   public DrawerView(Context context, AttributeSet attrs) 
   {
      super(context, attrs); // pass context to View's constructor
     
      paintScreen = new Paint(); // used to display bitmap onto screen
   Log.e("log","Drawer View class");
      // set the initial display settings for the painted line
      paintLine = new Paint();
      paintLine.setAntiAlias(true); // smooth edges of drawn line
      paintLine.setColor(Color.BLACK); // default color is black
      paintLine.setStyle(Paint.Style.STROKE); // solid line
      paintLine.setStrokeWidth(5); // set the default line width
      paintLine.setStrokeCap(Paint.Cap.ROUND); // rounded line ends
      pathMap = new HashMap<Integer, Path>();
      previousPointMap = new HashMap<Integer, Point>();
      isImage=false;
      dbConnector=new DbConnector(context);

   } 

   // Method onSizeChanged creates BitMap and Canvas after app displays
   @Override
   public void onSizeChanged(int w, int h, int oldW, int oldH)
   {
	   
	   if(!isImage)
	   {
      bitmap = Bitmap.createBitmap(getWidth(), getHeight(), 
         Bitmap.Config.ARGB_8888);		   
      bitmapCanvas = new Canvas(bitmap);
     bitmap.eraseColor(Color.WHITE);  
	   }//erase the BitMap with white
	   else
		   bitmap=getBitmap();
   } // end method onSizeChanged
   
   public void setBitmap(String path)
   {
	   // dbConnector.savePath(path);
	    Log.e("setBitmap",path);
	   String imagePath="/storage/sdcard0/DCIM/Camera/316269878617.jpg";
	   Log.e("setBitmap",imagePath);
	   bitmap=Bitmap.createBitmap(BitmapFactory.decodeFile(path));
	  
	   bitmap=convertToMutable(bitmap);
	   bitmapCanvas = new Canvas(bitmap);
	   isImage=true;
	 invalidate();
   }
   
   public Bitmap getBitmap()
   {
	   
	   
	   return bitmap;
   }
   public static Bitmap convertToMutable(Bitmap imgIn) {
	    try {
	     
	        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "temp.tmp");

	        
	        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");

	      
	        int width = imgIn.getWidth();
	        int height = imgIn.getHeight();
	        android.graphics.Bitmap.Config type = imgIn.getConfig();

	      
	        FileChannel channel = randomAccessFile.getChannel();
	        MappedByteBuffer map = channel.map(MapMode.READ_WRITE, 0, imgIn.getRowBytes()*height);
	        imgIn.copyPixelsToBuffer(map);
	      
	        imgIn.recycle();
	        System.gc();

	       
	        imgIn = Bitmap.createBitmap(width, height, type);
	        map.position(0);
	        //load it back from temporary 
	        imgIn.copyPixelsFromBuffer(map);
	        //close the temporary file and channel , then delete that also
	        channel.close();
	        randomAccessFile.close();

	        // delete the temp file
	        file.delete();

	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } 

	    return imgIn;
	}
   // clear the painting
   public void clear()
   {
      pathMap.clear(); // remove all paths
      previousPointMap.clear(); // remove all previous points
      if(isImage)
      {
       isImage=false;
       bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);	
	   bitmap.eraseColor(Color.WHITE);  
	   bitmapCanvas = new Canvas(bitmap);
        
      }
      else
      {
    		
    	   bitmap.eraseColor(Color.WHITE);  
      }
      // clear the bitmap 
      invalidate(); // refresh the screen
   } // end method clear
   
   // set the painted line's color
   public void setDrawingColor(int color) 
   {
      paintLine.setColor(color);
   } // end method setDrawingColor

   // return the painted line's color
   public int getDrawingColor() 
   {
      return paintLine.getColor();
   } // end method getDrawingColor

   // set the painted line's width
   public void setLineWidth(int width) 
   {
      paintLine.setStrokeWidth(width);
   } // end method setLineWidth

   // return the painted line's width
   public int getLineWidth() 
   {
      return (int) paintLine.getStrokeWidth();
   } // end method getLineWidth

   // called each time this View is drawn
   @Override
   protected void onDraw(Canvas canvas) 
   {
      // draw the background screen
	
      canvas.drawBitmap(bitmap, 0, 0, paintScreen);
	
      // for each path currently being drawn
      for (Integer key : pathMap.keySet()) 
         canvas.drawPath(pathMap.get(key), paintLine); // draw line
   } // end method onDraw

   // handle touch event
   @Override
   public boolean onTouchEvent(MotionEvent event) 
   {

     // get the event type and the ID of the pointer that caused the event
      int action = event.getActionMasked(); // event type 
      int actionIndex = event.getActionIndex(); // pointer (i.e., finger)
      
      Log.e("ACTION INDEX: ", "" + event.getActionIndex());
      
      // determine which type of action the given MotionEvent 
      // represents, then call the corresponding handling method
      if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN) 
      {
    	  if (action == MotionEvent.ACTION_DOWN) 
    	    Log.e("ACTION_DOWN: ", "" + event.getPointerId(actionIndex));
    	  else
    		Log.e("ACTION_POINTER_DOWN: ", "" + event.getPointerId(actionIndex));
    	 
          touchStarted(event.getX(actionIndex), event.getY(actionIndex), event.getPointerId(actionIndex));
    	 
      } // end if
      else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP) 
      {   	  
    	  if (action == MotionEvent.ACTION_UP) 
      	    Log.e("ACTION_UP: ", "" + event.getPointerId(actionIndex));
      	  else
      		Log.e("ACTION_POINTER_UP: ", "" + event.getPointerId(actionIndex));
    	           
    	  touchEnded(event.getPointerId(actionIndex));
      } // end else if
      else 
      {
         touchMoved(event); 
      } // end else
      
      invalidate(); // redraw
      return true; // consume the touch event
   } // end method onTouchEvent

   // called when the user touches the screen
   private void touchStarted(float x, float y, int lineID) 
   {
      Path path; // used to store the path for the given touch id
      Point point; // used to store the last point in path

      // if there is already a path for lineID
      if (pathMap.containsKey(lineID)) 
      {
         path = pathMap.get(lineID); // get the Path
         path.reset(); // reset the Path because a new touch has started
         point = previousPointMap.get(lineID); // get Path's last point
      } // end if
      else 
      {
         path = new Path(); // create a new Path
         pathMap.put(lineID, path); // add the Path to Map
         point = new Point(); // create a new Point
         previousPointMap.put(lineID, point); // add the Point to the Map
      } // end else

      // move to the coordinates of the touch
      path.moveTo(x, y);
      point.x = (int) x;
      point.y = (int) y;
   } // end method touchStarted

   // called when the user drags along the screen
   private void touchMoved(MotionEvent event) 
   {
      // for each of the pointers in the given MotionEvent
      for (int i = 0; i < event.getPointerCount(); i++) 
      {
         // get the pointer ID and pointer index
         int pointerID = event.getPointerId(i);
         int pointerIndex = event.findPointerIndex(pointerID);
            
         // if there is a path associated with the pointer
         if (pathMap.containsKey(pointerID)) 
         {
            // get the new coordinates for the pointer
            float newX = event.getX(pointerIndex);
            float newY = event.getY(pointerIndex);
            
            // get the Path and previous Point associated with 
            // this pointer
            Path path = pathMap.get(pointerID);
            Point point = previousPointMap.get(pointerID);
            
            // calculate how far the user moved from the last update
            float deltaX = Math.abs(newX - point.x);
            float deltaY = Math.abs(newY - point.y);

            // if the distance is significant enough to matter
            if (deltaX >= TOUCH_TOLERANCE || deltaY >= TOUCH_TOLERANCE) 
            {
               // move the path to the new location
               path.quadTo(point.x, point.y, (newX + point.x) / 2, (newY + point.y) / 2);

               // store the new coordinates
               point.x = (int) newX;
               point.y = (int) newY;
            } // end if
         } // end if
      } // end for
   } // end method touchMoved

   // called when the user finishes a touch
   private void touchEnded(int lineID) 
   {
      Path path = pathMap.get(lineID); // get the corresponding Path
      bitmapCanvas.drawPath(path, paintLine); // draw to bitmapCanvas
      invalidate();
      path.reset(); // reset the Path
   } // end method touch_ended

   // save the current image to the Gallery
   public void saveImage(String name)
   {
      // use "DrawerView" followed by current time as the image file name
	   
	   

      // create a ContentValues and configure new image's data
  
      try 
      { 
    	  long time=System.currentTimeMillis();
ContentValues values = new ContentValues();
    	      values.put(Images.Media.DISPLAY_NAME, name);
    	      values.put(Images.Media.TITLE, name);
    	    
    	      values.put(Images.Media.DATE_ADDED,time);
    	      values.put(Images.Media.MIME_TYPE, "image/jpg");

    	      // get a Uri for the location to save the file
    	      Uri uri = getContext().getContentResolver().insert(
    	         Images.Media.EXTERNAL_CONTENT_URI, values);
    
         // get an OutputStream to uri
         OutputStream outStream = 
            getContext().getContentResolver().openOutputStream(uri);

         // copy the bitmap to the OutputStream
         bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);

         // flush and close the OutputStream
         outStream.flush(); // empty the buffer
         outStream.close(); // close the stream
   
    	//createDirectoryAndSaveFile(bitmap,name);
    	dbConnector.insertPaint(name,uri.toString(),time);  
         // display a message indicating that the image was saved
         Toast message = Toast.makeText(getContext(), 
           "Painting saved in Gallery", Toast.LENGTH_LONG);
         message.setGravity(Gravity.CENTER, message.getXOffset() / 2, 
            message.getYOffset() / 2);
         message.show(); // display the Toast
      } // end try
      catch (IOException ex) 
      {
         // display a message indicating that the image was saved
         Toast message = Toast.makeText(getContext(), 
            R.string.message_error_saving, Toast.LENGTH_SHORT);
         message.setGravity(Gravity.CENTER, message.getXOffset() / 2, 
            message.getYOffset() / 2);
         message.show(); // display the Toast
      }
   }
   
   
   public void getAllPaints()
   {
 	 dbConnector.getAllPaints(); 
 	 
   }
   
   
   
   private void createDirectoryAndSaveFile(Bitmap imageToSave, String fileName) {

	   Bitmap bbicon;

	    bbicon=imageToSave;
	
	    String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
	    OutputStream outStream = null;
	    File file = new File(extStorageDirectory, fileName+".jpg");
	    try {
	     outStream = new FileOutputStream(file);
	     bbicon.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
	     outStream.flush();
	     outStream.close();
	    }
	    catch(Exception e)
	    {}
   }
}
