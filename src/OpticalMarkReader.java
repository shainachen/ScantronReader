import processing.core.PImage;

/***
 * Class to perform image processing for optical mark reading
 * 
 */
public class OpticalMarkReader {
	private static final int FIRST_X = 127, FIRST_Y = 471, BUBBLE_HEIGHT = 18, BUBBLE_WIDTH = 18, HORIZONATAL_SPACE = 37, VERTICAL_SPACE = 37;
	private static final int  numQuestions = 100, numAnswers = 5, numQuestionsInCol = 25;

	/***
	 * Method to do optical mark reading on page image.  Return an AnswerSheet object representing the page answers.
	 * @param image
	 * @return
	 */
	public AnswerSheet processPageImage(PImage image) {
		image.filter(PImage.GRAY);
		AnswerSheet answerSheet = new AnswerSheet();
		
		for(int i = 0; i < numQuestions; i++){
			int  darkestIndex = 0, darkestVal = Integer.MAX_VALUE;
			int row = i % numQuestionsInCol;
			
			for(int j = 0; j < numAnswers; j++){
				int val = valSumAt(FIRST_Y + row * (VERTICAL_SPACE), FIRST_X + j * (HORIZONATAL_SPACE), image);
				if (val < darkestVal){
					darkestVal = val;
					darkestIndex = j;
				}
			}
			answerSheet.addAnswer(darkestIndex);
		}
		
		for (int i = 0; i < numQuestions; i++)
    		System.out.println("Problem number: " + (i + 1) + "--> " + answerSheet.getAnswer(i));
		return answerSheet;
	}
	
	private int valSumAt(int row, int col, PImage image) {
		int sum = 0;
		for(int i = row; i < row+BUBBLE_HEIGHT; i++){
			for(int j = col; j < col+BUBBLE_WIDTH; j++){
				sum += getPixelAt(i, j, image);
			}
		}
		return sum;
	}

	public static int getPixelAt(int row, int col, PImage image){
		image.loadPixels();
		int index = row * image.width + col;
		return image.pixels[index] & 255;
	}
}