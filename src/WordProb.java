
public class WordProb {
	public String word;
	public int prob;
	WordProb(String word, int prob){
		this.word=word;
		this.prob=prob;
	}
	
	public void updateProb(int newProb){
		prob=newProb;
	}
}
