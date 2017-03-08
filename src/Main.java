import java.util.ArrayList;

import processing.core.PImage;

public class Main {
    public static final String PDF_PATH = "/omrtest.pdf";
    public static OpticalMarkReader markReader = new OpticalMarkReader();
    public static int[][] statistics;
    public static void main(String[] args) {
	System.out.println("Welcome!  I will now auto-score your pdf!");
	System.out.println("Loading file..." + PDF_PATH);
	ArrayList<PImage> images = PDFHelper.getPImagesFromPdf(PDF_PATH);

	System.out.println("Scoring all pages...");
	scoreAllPages(images);

	System.out.println("Complete!");
	// Optional: add a saveResults() method to save answers to a csv file
    }

    /***
     * Score all pages in list, using index 0 as the key.
     * 
     * NOTE: YOU MAY CHANGE THE RETURN TYPE SO YOU RETURN SOMETHING IF YOU'D
     * LIKE
     * 
     * @param images
     *            List of images corresponding to each page of original pdf
     */
    private static void scoreAllPages(ArrayList<PImage> images) {
    	statistics=new int[images.size()][4];
    	ArrayList<AnswerSheet> scoredSheets = new ArrayList<AnswerSheet>();

	// Score the first page as the key
    	AnswerSheet key = markReader.processPageImage(images.get(0));
		calculateStatistics(key, key, 0);


    	for (int i = 1; i < images.size(); i++) {
    		PImage image = images.get(i);
    		AnswerSheet answers = markReader.processPageImage(image);
    		calculateStatistics(answers, key, i);
    	}
    }
    private static void calculateStatistics(AnswerSheet answers, AnswerSheet key, int arrRow){
    	int numCorrect=0, numIncorrect=0;
    	double percentCorrect=0, percentIncorrect=0;
    	int numAnswers=answers.getNumQuestions();
    	for(int i=0; i< numAnswers; i++)
    		if(answers.getAnswer(i).equals(key.getAnswer(i)))
    			numCorrect++;
    	numIncorrect=numAnswers-numCorrect;
    	percentCorrect=((double)numCorrect/(double)numAnswers)*100;
    	percentIncorrect=100-percentCorrect;
    	statistics[arrRow][0]=numCorrect;
    	statistics[arrRow][1]=numIncorrect;
    	statistics[arrRow][2]=(int) percentCorrect;
    	statistics[arrRow][3]=(int) percentIncorrect;
    }
    private static void saveResults(){
    	
    }
    
//    private void Form1_Load(object sender, EventArgs e){
//       double[][] data = new double[10000][650];
//       using (StreamWriter outfile = new StreamWriter(@"C:\Temp\test.csv"))
//       {
//          for (int x = 0; x < 10000; x++)
//          {
//             string content = "";
//             for (int y = 0; y < 650; y++)
//             {
//                content += data[x][y].ToString("0.00") + ";";
//             }
//             outfile.WriteLine(content);
//          }
//       }
//    }
    
    
}
