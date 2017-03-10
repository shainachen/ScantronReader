import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import processing.core.PImage;

public class Main {
    public static final String PDF_PATH = "/omrtest.pdf";
    public static OpticalMarkReader markReader = new OpticalMarkReader();
    private static int[][] statistics;
	private static int[][] itemAnalysis;
	private static String[] statTitles={"page number", "num correct", "num incorrect", "% correct", "% incorrect"};
	private static String[] statRowTitles={"answer key", "1", "2", "3", "4", "5", "6"};
	private static String[] itemTitles={"problem #", "# students that got it wrong", "% students that got it wrong", "percentages beyond acceptable % of students who got it wrong (>50%)"};
	private static int percentWrongThreshold=50;

    public static void main(String[] args) {
	System.out.println("Welcome!  I will now auto-score your pdf!");
	System.out.println("Loading file..." + PDF_PATH);
	ArrayList<PImage> images = PDFHelper.getPImagesFromPdf(PDF_PATH);

	System.out.println("Scoring all pages...");
	scoreAllPages(images);

	System.out.println("Complete!");
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
//    	ArrayList<AnswerSheet> scoredSheets = new ArrayList<AnswerSheet>();

    	// Score the first page as the key
    	AnswerSheet key = markReader.processPageImage(images.get(0));
    	itemAnalysis=new int[key.getNumQuestions()][4];

		calculateStatistics(key, key, 0);
		
		//calculate statistics for each page after the answer key
    	for (int i = 1; i < images.size(); i++) {
    		PImage image = images.get(i);
    		AnswerSheet answers = markReader.processPageImage(image);
    		calculateStatistics(answers, key, i);
    	}
    	
    	//calculate percent students who got a problem wrong
    	for(int i=0; i<itemAnalysis.length; i++){
    		itemAnalysis[i][2]=(itemAnalysis[i][1]*100)/(images.size()-1);
    		if(itemAnalysis[i][2]>percentWrongThreshold)
    			itemAnalysis[i][3]=itemAnalysis[i][2];
    	}
    	
    	
    	String stats=saveResults(statTitles, statRowTitles, intToString(statistics), ", ");
    	String items=saveResults(itemTitles, intToString(itemAnalysis), ", ");

    	CSVData.writeDataToFile("E:\\Shaina\\11th grade\\AP Comp Sci\\scantronStats.csv", stats);
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
    			System.out.println("Problem "+(i+1)+" is incorrect. ("+answers.getAnswer(i)+", "+key.getAnswer(i)+")");
    			itemAnalysis[i][1]+=1;
    		}
    	}
    	numIncorrect=numAnswers-numCorrect;
    	percentCorrect=((double)numCorrect/(double)numAnswers)*100;
    	System.out.println("percent correct: "+percentCorrect);
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
    	
    private static String saveResults(String[] firstCol, String[][] in, String delim){
            String out = "";
            for(int i=0; i<firstCol.length; i++){
            	out+=(firstCol[i]);
            	if(i!=firstCol.length-1)
        			out += delim;
            }
            out+=("\n");
            for(int x = 0;x < in.length;x++){
            	for(int y = 0;y < in[x].length;y++){
            		if(y==in[x].length-1){
            			if(!in[x][y].equals("0"))
            				out+=(in[x][y]);
            		}
            		else{
            			out += (in[x][y]);
            		}
            		if(y!=in[x].length-1)
            			out += delim;
            	}
            	out += ("\n");
            }
            return(out);
       }
    
    private static String saveResults(String[] firstCol, String[] firstRow, String[][] in, String delim){
    	 String out = "";
         for(int i=0; i<firstCol.length; i++){
         	out+=(firstCol[i]);
         	if(i!=firstCol.length-1)
     			out += delim;
         }
         out+=("\n");
         for(int x = 0;x < in.length;x++){
         	for(int y = 0;y < in[x].length;y++){
         		if(y==0){
         			out+=(firstRow[x]);
         			out += delim;
         		}
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
