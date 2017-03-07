import processing.core.PImage;

/***
 * Class to perform image processing for optical mark reading
 * 
 */
public class OpticalMarkReader {
    private static final int HEIGHT_OF_EACH_PROBLEM = 40;
    private static final int HEIGHT_FIRST_ROW = 465;
    private static final int MID_OF_IMAGE = 600;
    private static final int XI = 130;
    private static final int XF = 310;
    private static final int YI = 460;
    private static final int YF = 490;
    private static final int NUM_BUBBLES = 5;
    private static final int NUM_PROBLEMS = 100;
	private static final String[] answerLetters = { "a", "b", "c", "d", "e" };


    /***
     * Method to do optical mark reading on page image. Return an AnswerSheet
     * object representing the page answers.
     * 
     * @param image
     * @return
     */
    public AnswerSheet processPageImage(PImage image) {
    	image.filter(PImage.GRAY);
    	int xi = XI;
    	int xf = XF;
    	int yi = YI;
    	int yf = YF;
    	int[] problems = new int[NUM_PROBLEMS];
    	AnswerSheet a = new AnswerSheet();
    	for (int i = 0; i < NUM_PROBLEMS; i++) {
    		if (i != 0 && i % 25 == 0) {
    			xi += 283;
    			xf += 283;
    			yi = YI;
    			yf = YF;
    		}
    		problems[i] = determineBubble(xi, yi, xf, yf, NUM_BUBBLES, image);

    		yi += HEIGHT_OF_EACH_PROBLEM;
    		yf += HEIGHT_OF_EACH_PROBLEM;
    	}
    	for (int i = 0; i < problems.length; i++)
    		a.addAnswer(calculateAnswer(i % NUM_BUBBLES));
    	
    	for (int i = 0; i < NUM_PROBLEMS; i++)
    		System.out.println("Problem number: " + (i + 1) + "--> " + problems[i] + "--> "+ a.getAnswer(i));
    	
    	return a;
    }
    
    public String calculateAnswer(int index) {
		return answerLetters[index];
	}

    public static int getPixelAt(int row, int col, PImage image) {
    	image.loadPixels();
    	int color = image.pixels[(row * image.width) + col];
    	return color & 255;
    }

    public int determineBubble(int r1, int c1, int r2, int c2, int numBubbles, PImage image) {
    	int width = c2 - c1;
    	width /= numBubbles;
    	int currentIndex = 1;
    	int darkestIndex = 1;
    	int mostBlack = Integer.MAX_VALUE;
    	int currentBlack = Integer.MAX_VALUE;

    	for (int i = 0; i < numBubbles; i++) {
    		currentBlack=avgVal(width, r2-r1, image, r1, c1);
    		if (currentBlack < mostBlack) {
        		darkestIndex = currentIndex;
        		mostBlack = currentBlack;
        	}
    		c1+=width;
    		currentBlack = Integer.MAX_VALUE;
    		currentIndex++;
    	}
    	return darkestIndex;
    }
    
    public int avgVal(int recWidth, int recHeight, PImage image, int row, int col) {
		short avg = 0;
		for (int r = row; r < row + recHeight; r++)
			for (int c = col; c < col + recWidth; c++)
				avg += getPixelAt(r, c, image);
		// System.out.println("width: " + recWidth + " height: " + recHeight);
		return (short) (avg / (recWidth * recHeight));
	}
}
