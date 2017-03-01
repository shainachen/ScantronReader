import processing.core.PImage;

/***
 * Class to perform image processing for optical mark reading
 * 
 */
public class OpticalMarkReader {
	/***
	 * Method to do optical mark reading on page image.  Return an AnswerSheet object representing the page answers.
	 * @param image
	 * @return
	 */
	public AnswerSheet processPageImage(PImage image) {
		AnswerSheet a=new AnswerSheet();
		
		
		return null;
	}
	
	public int getPixelAt(int row, int col, PImage image){
		image.loadPixels();
		int index= row*image.width+col;
		return image.pixels[index];
	}
	
	public short avgGreyValue(PImage image){
		short avgVal=0;
		for(int row=0; row<image.height; row++)
			for(int col=0; col<image.width; col++)
				avgVal+=getPixelAt(row, col, image);
		return (short) (avgVal/(image.height*image.width));
	}
}
