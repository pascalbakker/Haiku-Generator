
public class WordValue {
	private String wordClass;
	private String syllableCount;
	WordValue(String wordClass,String syllableCount){
		this.wordClass=wordClass;
		this.syllableCount=syllableCount;
	}
	
	public String getWordClass(){
		return wordClass;
	}
	
	public String getSyllableCount(){
		return syllableCount;
	}
}
