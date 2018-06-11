import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class Main {

	public static void main(String[] args) {
		File directory = new File("./examples/video8-small part/");
		List<BufferedImage> movie = new ArrayList<BufferedImage>();
		for (int i = 1+53; i <= directory.listFiles().length+53; i++) {
			try {
				movie.add(ImageIO.read(new File(directory.getAbsolutePath()+"/seg-1_"+i+".bmp")));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		TrackingCells2D tracking = new TrackingCells2D(movie);
		tracking.trackCells();
//		tracking.drawContours();
		tracking.drawPaths();
	}

}
