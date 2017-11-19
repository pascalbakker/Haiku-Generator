import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
public class ProbabilityGenerator {

	private HashMap<String,List<WordProb>> markov = new HashMap<String,List<WordProb>>();
	private File source;
	ProbabilityGenerator(File source){
		this.source=source;
	}
	
	/**
	 * Determines if word is an integer
	 * @param word
	 * @return True if number, false otherwise
	 */
	public static boolean isNumber(String word){
	    try { 
	        Integer.parseInt(word); 
	    }catch(NumberFormatException e){ 
	        return false; 
	    }catch(NullPointerException e){
	        return false;
	    }
	    return true;
	}
	
	public static boolean isAllUppercase(String word){
		char[] chararray= word.toCharArray();
		for(char c: chararray){
			char upper =Character.toUpperCase(c);
			if(upper!=c){
				return false;
			}
		}
		return true;
	}
	
	public void generateProbability() throws FileNotFoundException{
		Scanner reader = new Scanner(source);
		
		//Add the first words the list
		String oldWord=reader.next();
		String word=reader.next();
		List<WordProb> listprob = new ArrayList<WordProb>();
		listprob.add(new WordProb(word,1));
		markov.put(oldWord, listprob);
		oldWord=word;
		while(reader.hasNext()){
			word = reader.next();
			String tempword = word;
			word = word.replaceAll("[\\-\\+\\=\\.\\|\\^\\:\\.\\;\\â\\€\\!\\˜\\'\\™\\*\\œ\\,\\?\\_\\(\\[\\]\"\\$\\”\\#\\)]", "");
			word.toLowerCase();
			//Ignore numbers and uppercase words
			if(isNumber(word)||isAllUppercase(word)) continue;
			//Get the list of words that occur after the oldWord
			if(!markov.containsKey(oldWord))
				markov.put(oldWord, new ArrayList<WordProb>());
			//System.out.println(oldWord);
			List<WordProb> wordsAfterOldWord = markov.get(oldWord);
			//If contains list
			boolean containsWord=false;
			for(int i=0;i<wordsAfterOldWord.size();i++){
				//If in list, update number of occurences
				if(wordsAfterOldWord.get(i).word.equals(word)){
					int currentOccurences = wordsAfterOldWord.get(i).prob;
					wordsAfterOldWord.set(i, new WordProb(word,currentOccurences+1));
					markov.put(oldWord, wordsAfterOldWord);
					containsWord=true;
					break;
				}
			}
			if(!containsWord){
				wordsAfterOldWord.add(new WordProb(word,1));
				markov.put(oldWord, wordsAfterOldWord);
			}
			
			if(tempword.contains("?")||tempword.contains("!")||tempword.contains(".")){
				if(reader.hasNext())
					oldWord=reader.next();
				if(!markov.containsKey(word))
					markov.put(word, new ArrayList<WordProb>());
			}else{
				oldWord=word;
			}
			oldWord=word;

		}
	}
	
	public HashMap<String,List<WordProb>> getProbabilties(){
		return markov;
	}
	
	public void printMarkov(){
		for(String word:markov.keySet()){
			System.out.print(word+" ");
			for(WordProb wordProb: markov.get(word)){
				System.out.print(wordProb.word+": "+wordProb.prob+" ");
			}
			System.out.print("\n");
		}
	}
	
	public void writeHashMap(){}
	
	public WordProb generateRandomWithWord(String word){
		if(!markov.containsKey(word))
			throw new IllegalArgumentException("Error in getting random word: ");
		Random r = new Random();
		List<WordProb> list = markov.get(word);
		WordProb s = list.get(r.nextInt(list.size()));
		return s;
	}
	
	public String generateRandom(){
		Random r = new Random();
		List<String> keys = new ArrayList<String>(markov.keySet());
		String s = keys.get( r.nextInt(keys.size()) );	
		
		return s;
	}
	
	
	public String generateSentence(){
		String sentence="";
		List<String> keys      = new ArrayList<String>(markov.keySet());
		Random r = new Random();
		String currentWord = keys.get( r.nextInt(keys.size()) );
		sentence+=currentWord+" ";
		for(int i=0;i<15;i++){
			List<WordProb> wordProbList = new ArrayList<WordProb>(markov.get(currentWord));
			WordProb randomElement = wordProbList.get(r.nextInt(wordProbList.size()));
			currentWord=randomElement.word;
			sentence+=currentWord+" ";
		}
		
		return sentence;
	}
	
	public static void main(String[] args) throws FileNotFoundException{
		ProbabilityGenerator pg = new ProbabilityGenerator(new File("t8.shakespeare.txt"));
		pg.generateProbability();
		System.out.println(pg.generateSentence());
		String test = "hi!";
		System.out.println(test.contains("!"));
	}
	
	
}
