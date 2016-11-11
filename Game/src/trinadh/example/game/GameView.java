package trinadh.example.game;


import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.SensorEvent;

import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Vibrator;


	
	  public class GameView extends SurfaceView implements SurfaceHolder.Callback {
		  
			 private GameThread gameThread;
			 private Activity activity;
		     private int ballradius;
			 public int ball1x,ball1y,ball2x,ball2y,holeX,holeY;
			 public int ballVelocity,screenHeight,screenWidth;
			 public int chosenBackground;
			 public int newx,newy;
			 private Paint paint;
			 private Bitmap ballImage,bg,blocker;
			 private SoundPool soundPool;
			 private Map<Integer,Integer> soundMap;
			 
			 private int blocker1x,blocker1y;
			 private int blocker2x,blocker2y;
			 private int blocker3x,blocker3y;
		
			
		
			 private Vibrator v;
			 public boolean gameOver;
			 public String gameStatus;
		
	 	    
		    public GameView(Context context,AttributeSet attrs) {
		    	
		    	
			super(context,attrs);
			activity=(Activity)context;
			getHolder().addCallback(this);
			 paint = new Paint(); 
			 ballradius=25;		
			 v = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
			 ball1x=1;
			 ball1y=1;
			 ball2x=2;
			 ball2y=3;
			 holeX=200;
			 holeY=200;
		     blocker1x=500;
		     blocker1y=500;
		     blocker2x=500;
		     blocker2y=500;
		     blocker3x=500;
		     blocker3y=500;
			 soundPool=new SoundPool(1,AudioManager.STREAM_MUSIC,0);
			 soundMap=new HashMap<Integer,Integer>();
			 soundMap.put(1, soundPool.load(context,R.raw.pocket,1));
			 chosenBackground=2;
			 ballVelocity=1;
			 ballImage=BitmapFactory.decodeResource(getResources(), R.drawable.ball);
			  blocker=BitmapFactory.decodeResource(getResources(), R.drawable.woodblocker);
		}
		     
		     @Override
	         protected void onSizeChanged(int w,int h,int oldw,int oldh) {
		       screenWidth=w;
		       screenHeight=h;
		       blocker1x=0;
		       blocker1y=h-200;
		       blocker2x=w-300;
		       blocker2y=h/2;
		       blocker3x=0;
		       blocker3y=h/3-20;
		       
			   newGame();  
            
	    
		     }
		     
		  public void newGame()
		  {
			   ball1x=screenWidth/2;
		       ball1y=screenHeight-30;
		       ball2x=screenWidth/4;
		       ball2y=screenHeight-30;
			   holeX=ball1x*2-ball1x/3;
			   holeY=screenHeight/4;
			   newx=0;
			   newy=0;
			
			  
			   ballImage=BitmapFactory.decodeResource(getResources(), R.drawable.ball);
			   switch(chosenBackground)
			   {
			   case 0: bg=BitmapFactory.decodeResource(getResources(), R.drawable.background);
			           break;
			   case 1: bg=BitmapFactory.decodeResource(getResources(), R.drawable.metal);
	           break;
			   case 2: bg=BitmapFactory.decodeResource(getResources(), R.drawable.metal_1);
	           break;
			   
			   }
					    	 		  
		  }
	   
	         public  void onDraw(Canvas canvas) {
	            
	        
	            paint.setStyle(Paint.Style.FILL);
	            paint.setColor(Color.WHITE);
	            canvas.drawRect(0, 0,canvas.getWidth(),canvas.getHeight(), paint);
	            canvas.drawBitmap(bg, 0, 0, paint);
	            canvas.drawBitmap(blocker, blocker1x,blocker1y, paint);
	            canvas.drawBitmap(blocker, blocker2x,blocker2y, paint);
	            canvas.drawBitmap(blocker, blocker3x,blocker3y, paint);
	            
	            paint.setColor(Color.BLACK);
	            canvas.drawCircle( holeX, holeY, ballradius+4, paint);
	            canvas.drawBitmap(ballImage, ball1x-25, ball1y-25, paint);
	           
	           
       
	        }
	         
	         
	      
 public void setBall1XY(SensorEvent e)
 {
	
	 if(!gameOver)
	 {
	 newx=(int) (-1*e.values[0]*7);
	 newy=(int) (e.values[1]*7);
	 
	
	 
	 if(ball1x>25&&ball1x<screenWidth-ballradius)
	 {
		 ball1x+=newx*ballVelocity;		
		 
	 }
	 if(ball1y>25&&ball1y<screenHeight-ballradius)
	 {
		 ball1y+=newy*ballVelocity;
		 
	 }
	  blocker1();
	  blocker2();
	  blocker3();
if(ball1x<=25)
{
	ball1x=25;
	v.vibrate(20);  
  if(newx>0)
  {
	  
	  ball1x+=newx*ballVelocity;
  }
}
  if(ball1x>=screenWidth-25)
  {
	  ball1x=screenWidth-25;
	  v.vibrate(20);  
    if(newx<0)
    {
  	  
    	ball1x+=newx*ballVelocity;
    }
  }
  


if(ball1y<=25)
{
	 ball1y=25;
	v.vibrate(20);  
  if(newy>0)
  {
	  
	  ball1y+=newy*ballVelocity;
  }
}
  if(ball1y>=screenHeight-25)
  {
	  ball1y=screenHeight-25;
	  v.vibrate(20);  
    if(newy<0)
    {
  	  
    	ball1y+=newy*ballVelocity;
    }
  }
  

  //if ball near hole
  if((ball1x>holeX-15&&ball1x<holeX+15)&&(ball1y>holeY-15&&ball1y<holeY+15))
  {
	  ball1x=holeX;
	  ball1y=holeY;
	  soundPool.play(soundMap.get(1), 1,1,0, 0, 1f);
	//  v.vibrate(100); 
	  gameStatus="Ball in the Hole";
      gameOver=true;
      
     

  
  }
  
	 }
	 
	 else
	 {
		 //if gameOver=true
	
	     
	 }
  
}
 
 
 



 public void blocker1()
 {
	 
	if(ball1x<=310&&(ball1y>blocker1y&&ball1y<blocker1y+75))		
	{
		
		 ball1y=blocker1y+75;
		 v.vibrate(50);
		 gameStatus="You lost";
		 gameOver=true;
	}
	
	if(ball1x<=310&&(ball1y>blocker1y-25&&ball1y<blocker1y+75))		
	{
		
		 ball1y=blocker1y-25;
		 v.vibrate(20);
		 gameStatus="You lost";
		 gameOver=true;
	}
	
	if((ball1y>=blocker1y-25&&ball1y<=blocker1y+75)&&(ball1x<325&&ball1x>=275))
	{
		ball1x=325;
		 v.vibrate(20);
		 gameStatus="You lost";
		 gameOver=true;
		
	}
	

	}//blocker1
 
 public void blocker2()

 {
	 if(ball1x>=blocker2x-25&&(ball1y>blocker2y&&ball1y<blocker2y+75))		
		{
			
			 ball1y=blocker2y+75;
			 v.vibrate(50);
			 gameStatus="You lost";
			 gameOver=true;
		}
		
		if(ball1x>=blocker2x-25&&(ball1y>blocker2y-25&&ball1y<blocker2y+75))		
		{
			
			 ball1y=blocker2y-25;
			 v.vibrate(20);
			 gameStatus="You lost";
			 gameOver=true;
		}
		
		if((ball1y>=blocker2y-25&&ball1y<=blocker2y+75)&&(ball1x>blocker2x-25&&ball1x<=blocker2x+25))
		{
			ball1x=blocker2x-25;
			 v.vibrate(20);
			 gameStatus="You lost";
			 gameOver=true;
			
		} 
	 
 }
	
	 
 public void blocker3()
 {
		if(ball1x<=310&&(ball1y>blocker3y&&ball1y<blocker3y+75))		
		{
			
			 ball1y=blocker3y+75;
			 v.vibrate(50);
			 gameStatus="You lost";
			 gameOver=true;
		}
		
		if(ball1x<=310&&(ball1y>blocker3y-25&&ball1y<blocker3y+75))		
		{
			
			 ball1y=blocker3y-25;
			 v.vibrate(20);
			 gameStatus="You lost";
			 gameOver=true;
		}
		
		if((ball1y>=blocker3y-25&&ball1y<=blocker3y+75)&&(ball1x<325&&ball1x>=275))
		{
			ball1x=325;
			 v.vibrate(20);
			 gameStatus="You lost";
			 gameOver=true;
			
		}
			 
	 
 }

	public void stopGame()
	{
		if(gameThread!=null)
		{
			
			gameThread.setRunning(false);
		}
		
	}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				gameThread =new GameThread(holder);
				gameThread.setRunning(true);
				gameThread.start();		
				
			}
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
			boolean retry=true;
			gameThread.setRunning(false);
			
			while(retry)
			{
				try
				{
					gameThread.join();
					retry=false;
					
					
				}
				catch(InterruptedException e)
				{
					
					 
				}//catch
				
			}
				
			}

		
		public void releaseResources()
		{
			
			soundPool.release();
			soundPool=null;
		}
			
			
			private class GameThread extends Thread
			{
				
				private SurfaceHolder surfaceHolder;
				public boolean threadIsRunning=true;
				
				public GameThread(SurfaceHolder holder)
				{
					surfaceHolder=holder;
					setName("GameThread");
					
				}
				
				public void setRunning(boolean running)
				{
					threadIsRunning=running;
				}
				@Override
				 public void run()
				{
					
					Canvas canvas=null;
					
					while(threadIsRunning)
					{
						try
						{
							canvas=surfaceHolder.lockCanvas(null);
							synchronized(surfaceHolder)
							{

						    draw(canvas);
								
							}
							
						}
						finally
						{
							if(canvas!=null)
							{
								surfaceHolder.unlockCanvasAndPost(canvas);
								
							}
							
						}//finally
						
					}//while
				}//run
				
				
			}//thread class
	     }

