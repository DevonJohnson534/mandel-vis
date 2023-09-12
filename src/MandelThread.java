import java.awt.Color;

public class MandelThread extends Thread {
	//======================
	static int PixelsPerThreadX;
	static double ZoomedHeight, Xmin, Ymin;
	
	int threadNum;
	int startX;
	//======================
	public static void setStatics(int pixelsPerThreadX, double zoomedHeight, double xMin, double yMin) {
		PixelsPerThreadX = pixelsPerThreadX;
		ZoomedHeight = zoomedHeight;
		Xmin = xMin;
		Ymin = yMin;
	}
	
	public MandelThread(int threadNum) {
		this.threadNum = threadNum;
	}
	
	public void run(){
		startX = PixelsPerThreadX*threadNum;
		int endX = PixelsPerThreadX+startX;
		int endY = Main.ny;
		
		//System.out.println("thread: "+threadNum+" bounds: "+startX+","+endX+","+startY+","+endY); //debug
        for (int x = startX; x < endX; x++) {
            for (int y = 0; y < endY; y++) {
            	if (x>=Main.nx) //kind of a sloppy fix for the remainder problem but ok for now
            		break;
            	
                double x0 = Xmin + Main.size * x /Main.nx;
                double y0 = Ymin + ZoomedHeight * y /Main.ny;
                Complex z0 = new Complex(x0, y0);
                
                int iterations = Main.max - mand(z0, Main.max);
                Color color = Coloring.lerpRainbow(iterations);
                //Color color = Coloring.lerp4Colors(iterations,Color.black, Color.red, Color.yellow, Color.blue);
                //Color color = new Color(iterations, iterations, iterations); //greyscale color
                //Color color = Coloring.lerp(Color.red, Color.blue, (double)threadNum/Main.threadCount); //debug coloring to show thread
                Main.picture.set(x, y, color);
            }
        }
	}
	
    // return number of iterations to check if c = a + ib is in Mandelbrot set
    private int mand(Complex z0, int max) {
        Complex z = z0;
        for (int t = 0; t < max; t++) {
            if (z.abs() > 2.0) return t;
            z = z.pow(Main.power).plus(z0);
        }
        return max;
    }
}
