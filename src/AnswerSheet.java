import java.util.ArrayList;

/***
 * A class to represent a set of answers from a page
 */
public class AnswerSheet {
	private ArrayList<String> answers;
	
	public AnswerSheet(){
		answers=new ArrayList<String>();
	}

	public ArrayList<String> getAnswers() {
		return answers;
	}

	public void setAnswers(ArrayList<String> answers) {
		this.answers = answers;
	}
	
	public void addAnswer(String answer){
		answers.add(answer);
	}
	
//	public void removeAnswer(String answer){
//		for(int i=0; i<answers.size(); i++)
//			if(answer.equals(answers.get(i)))
//				answers.remove(i);
//	}
}
