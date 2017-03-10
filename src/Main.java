import java.util.ArrayList;

import processing.core.PImage;

public class Main {
    public static final String PDF_PATH = "/omrtest.pdf";
    public static OpticalMarkReader markReader = new OpticalMarkReader();
    public static int[][] statistics;
	private static int[][] itemAnalysis;
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
    	itemAnalysis=new int[key.getNumQuestions()][3];

		calculateStatistics(key, key, 0);


    	for (int i = 1; i < images.size(); i++) {
    		PImage image = images.get(i);
    		AnswerSheet answers = markReader.processPageImage(image);
    		calculateStatistics(answers, key, i);
    	}
    	
    	for(int i=0; i<itemAnalysis.length; i++){
    		itemAnalysis[i][2]=(itemAnalysis[i][1]*100)/(images.size()-1);
    		System.out.println("percent students that got it wrong: "+itemAnalysis[i][2]);
    	}
    	
    	String stats=saveResults(intToString(statistics), ", ");
    	String items=saveResults(intToString(itemAnalysis), ", ");

    	CSVData.writeDataToFile("E:\\Shaina\\11th grade\\AP Comp Sci\\scantronStats.csv" , stats);
    	CSVData.writeDataToFile("E:\\Shaina\\11th grade\\AP Comp Sci\\scantronItemAnalysis.csv", items);
    }
    private static void calculateStatistics(AnswerSheet answers, AnswerSheet key, int arrRow){
    	int numCorrect=0, numIncorrect=0;
    	double percentCorrect=0, percentIncorrect=0;
    	int numAnswers=answers.getNumQuestions();
    	for(int i=0; i< numAnswers; i++){
    		itemAnalysis[i][0]=i+1;
    		if(answers.getAnswer(i).equals(key.getAnswer(i))){
    			numCorrect++;
    		}
    		else{
    			itemAnalysis[i][1]+=1;
    		}
    	}
    	numIncorrect=numAnswers-numCorrect;
    	percentCorrect=((double)numCorrect/(double)numAnswers)*100;
    	percentIncorrect=100-percentCorrect;
    	statistics[arrRow][0]=numCorrect;
    	statistics[arrRow][1]=numIncorrect;
    	statistics[arrRow][2]=(int) percentCorrect;
    	statistics[arrRow][3]=(int) percentIncorrect;
    }
    private static String[][] intToString(int[][] arr){
    	String[][] a=new String[arr.length][arr[0].length];
    	for(int i=0; i<arr.length; i++)
    		for(int j=0; j<arr[0].length; j++)
    			a[i][j]=Integer.toString(arr[i][j]);	
    	return a;
    }
    	
    private static String saveResults(String[][] in, String delim){
            String out = "";
            for(int x = 0;x < in.length;x++){
            	for(int y = 0;y < in[x].length;y++){
            		out += (in[x][y]);
            		if(y!=in[x].length-1)
            			out += delim;
            	}
            	out += ("\n");
            }
            return(out);
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
