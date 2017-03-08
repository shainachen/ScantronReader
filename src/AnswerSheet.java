import java.util.ArrayList;

/***
 * A class to represent a set of answers from a page
 */
public class AnswerSheet {
	
	private ArrayList<String> answers;
	
	public AnswerSheet(){
		answers= new ArrayList<String>();
	}
	
	public void addAnswer(int answer){
		if (answer == 0) answers.add("a");
		else if (answer == 1) answers.add("b");
		else if (answer == 2) answers.add("c");
		else if (answer == 3) answers.add("d");
		else if (answer == 4) answers.add("e");
	}
	
	public String getAnswer(int index){
		return answers.get(index);
	}
	
	public void addAnswer(String answer){
		answers.add(answer);
	}

	public void print(){
		for(int i = 0; i < answers.size(); i++)
			System.out.print(answers.get(i) + " ");
		System.out.println();
	}

	public int getNumQuestions() {
		return answers.size();
	}
}