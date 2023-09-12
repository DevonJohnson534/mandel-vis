import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/*
COLOR TEST SETTINGS:
static int nx   = 1000;
static int ny   = 700;

max:   130    power: 2    size: 0.001
center coords: -0.77568377,0.13646737 */
/*
DEFAULT SETTINGS:
static int nx   = 1000;
static int ny   = 700;

max:   100    power: 2    size: 3
center coords: -0.5,0 
*/
public class Main {
	//========================================================
	//IT ONLY WORKS RIGHT IF THREADCOUNT DIVIDES EVENLY INTO NX
	static int threadCount = 1000;
	
    //create nx-by-ny image
    static int nx   = 1000;
    static int ny   = 700;
    static double aspectRatio = (double) nx / ny;
    
    static int power = 2; //exponent of z(default 2)
    
    static int max = 100;   // maximum number of iterations
    static double deltaMax = 10; //gets added/subtracted
    
    static double size = 3; //zoom (default 1.5)
    static double deltaSize = .8; //gets multiplied/divided

    //starting coords
    static double xc =  -0.5;
    static double yc = 0;
    
    static Picture picture = new Picture(nx, ny);
    //========================================================
	public static void main(String[] args) {
		ControlPanel.main(args);
	    Update();
    	picture.frame.addKeyListener(new KeyAdapter() {
    		  public void keyPressed(KeyEvent e) {
    			  int key = e.getKeyCode();
    			  switch (key) {
    			    //change power
    			  		case KeyEvent.VK_OPEN_BRACKET:
    			  			System.out.println("-----pow down-----");
    			  			power -= 1;
    			  			Update();
    			  			break;
    			  		case KeyEvent.VK_CLOSE_BRACKET:
    			  			System.out.println("-----pow up-----");
    			  			power += 1;
    			  			Update();
    			  			break;
    				//change max
    			  		case KeyEvent.VK_SEMICOLON:
    			  			System.out.println("-----max decrease-----");
				  	        max-=deltaMax;
				  	      Update();
    			  			break;
    			  		case KeyEvent.VK_QUOTE:
    			  			System.out.println("-----max increase-----");
    			  	        max+=deltaMax;
    			  	        Update();
			  				break;
		    		//zoom
    			  		case KeyEvent.VK_COMMA:
    			  			System.out.println("-----zoom out-----");
				  	        size /= deltaSize;
				  	        Update();
				  	        break;
    			  		case KeyEvent.VK_PERIOD:
    			  			System.out.println("-----zoom in-----");
    			  	        size *= deltaSize;
    			  	        Update();
    			  	        break;
    			  	//move
    			  		case KeyEvent.VK_A:
    			  			System.out.println("-----left-----");
    			  			xc -= 0.1*size;
    			  			Update();
    			  			break;
    			  		case KeyEvent.VK_D:
    			  			System.out.println("-----right-----");
    			  			xc += 0.1*size;
    			  			Update();
    			  			break;
    			  		case KeyEvent.VK_W:
    			  			System.out.println("-----up-----");
    			  			yc -= 0.1*size;
    			  			Update();
    			  			break;
    			  		case KeyEvent.VK_S:
    			  			System.out.println("-----down-----");
    			  			yc += 0.1*size;
    			  			Update();
    			  			break;
    			  }
    		  }
    	});

	}
	public static void Update() {        
        MakeThreads();
        picture.show();
        ControlPanel.controlPanel.setFields(); //ok this is a bit jank but it works
        	//scientific notation is not displayed in control panel but still works in the variable
        	//(0.001216944576219103 is size boundary before 9.735556609752824E-4)
        //UNCOMMENT TO PRINT VARIABLE INFO
        	//System.out.println("max:   "+max+"    power: "+power+"    size: "+size);
        	//System.out.println("center coords: " + xc+","+yc);
        
    }
	private static void MakeThreads() {
		//to maintain aspect ratio and zoom center
        double zoomedHeight = size / aspectRatio;
        double xMin = xc - size / 2;
        double yMin = yc - zoomedHeight / 2;
        
        int pixelsPerThreadX = nx/threadCount;
        
        MandelThread.setStatics(pixelsPerThreadX, zoomedHeight, xMin, yMin);
        MandelThread[] threads = new MandelThread[threadCount+1]; //+1 is bc starts at 0
        
		for(int i=0;i<=threadCount;i++) {
			threads[i] = new MandelThread(i);
			threads[i].start();
		}
		
		//wait until all threads are finished.
	    for( int i = 0; i < threads.length; i ++ )
	    {
	        try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
	    }

	}
}
