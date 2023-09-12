import java.awt.Color;

public class Coloring {
	
    public static Color lerp4Colors(int value, Color color1,Color color2,Color color3,Color color4) {
        double percent = value / (double)Main.max;
        if (percent < 0.5)
        	return lerp(color1,color2,percent*2);
        else
        	return lerp(color2,color3,(percent - 0.5f)*2);
    }
    
    public static Color lerpRainbow(int value) {
        double percent = value / (double)Main.max;
        if (percent < 0.10)
        	return lerp(Color.black,Color.blue,percent*10);
        else if (percent < 0.25)
        	return lerp(Color.blue,Color.red,(percent)*4);
        else if (percent < 0.5)
        	return lerp(Color.red,Color.yellow,(percent-.25)*4);
        else if (percent < 0.75)
        	return lerp(Color.yellow,Color.green,(percent-.5)*4);
        else
        	return lerp(Color.green,Color.blue,(percent-.75)*2);
    }
    
    public static Color lerp(Color color1, Color color2, double percent) {
        int red = (int) (color1.getRed() * (1 - percent) + color2.getRed() * percent);
        int green = (int) (color1.getGreen() * (1 - percent) + color2.getGreen() * percent);
        int blue = (int) (color1.getBlue() * (1 - percent) + color2.getBlue() * percent);
        return new Color(red, green, blue);
    }
    
}
