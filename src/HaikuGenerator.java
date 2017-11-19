import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HaikuGenerator {
	//Add 1 to the desired number of syllables per line.(Ex. if you want 5 syllables set to 6W)
	public static final int[] syllableCount = {6,8,6};
	HashMap<String,List<WordProb>> markov;
	
	public HaikuGenerator(File learningFile) throws FileNotFoundException{
		if(!learningFile.exists())
			throw new IllegalArgumentException("File must exist!");
		ProbabilityGenerator pg = new ProbabilityGenerator(learningFile);
		pg.generateProbability();
		markov = pg.getProbabilties();
	}
	
	public String generateHaiku(){
		Stack<String> haiku = new Stack<String>();
		List<String> keys      = new ArrayList<String>(markov.keySet());
		ArrayList<String> firstline=new ArrayList<String>(),secondline = new ArrayList<String>(),thirdline=new ArrayList<String>();
		while(true){
			String currentWord="";
			Random r = new Random();
			int sum=0;
			List<WordProb> list = null;
			String chosenWord;
			while(sum!=syllableCount[0]-1){
				sum=0;
				//First line
				currentWord = keys.get( r.nextInt(keys.size()) );	
				firstline = DFS(currentWord,syllableCount[0],true,0);
				if(firstline.isEmpty()) continue;
				list = markov.get(firstline.get(firstline.size()-1));
				for(int i=0;i<firstline.size();i++){
					sum+=syllableCount(firstline.get(i));
				}
			}
			
			sum=0;
			//Second line
			while(sum!=syllableCount[1]-1){
				sum=0;
				chosenWord = list.get(r.nextInt(list.size())).word;
				secondline = DFS(chosenWord,syllableCount[1],false,list.size());
				if(secondline.isEmpty()) continue;
				list = markov.get(secondline.get(secondline.size()-1));
				sum=0;
				for(int i=0;i<secondline.size();i++){
					sum+=syllableCount(secondline.get(i));
				}
			}
			//Third line
			while(sum!=syllableCount[2]-1){
				sum=0;
			    chosenWord = list.get(r.nextInt(list.size())).word;
				thirdline = DFS(chosenWord,syllableCount[2],false,list.size());
				sum=0;
				for(int i=0;i<thirdline.size();i++){
					sum+=syllableCount(thirdline.get(i));
				}
			}
			//If not haiku redo
			if(!(firstline.isEmpty()||secondline.isEmpty()||thirdline.isEmpty()))
				break;
		}
		String sentence="";
		//Print the haiku
		//First line
		for(String word: firstline){
			System.out.print(word+" ");
			sentence+=word+" ";
		}
		sentence+="\n";
		System.out.println();
		//Second line
		for(String word: secondline){
			System.out.print(word+" ");
			sentence+=word+" ";
		}
		sentence+="\n";
		System.out.println();
		//Third line
		for(String word: thirdline){
			System.out.print(word+" ");
			sentence+=word+" ";
		}
		sentence+="\n";
		System.out.println();
		return sentence;
	}
	
	public ArrayList<String> DFS(String starting,int desiredSyllables, boolean isStart,int numofpaths){
		//If not start of haiku, set starting word to a neighbor of the starting word
		if(!isStart){
			Random r = new Random();
			List<String> keys = new ArrayList<String>(markov.keySet());
			starting = keys.get( r.nextInt(keys.size()) );	
		}
		
		int numSyllables = 0;
		WordProb startingWP = new WordProb(starting,0);
		ArrayList<String> sentence = new ArrayList<String>();
		HashMap<String,Boolean> visited = new HashMap<String,Boolean>();
		//Add all words to visited
		for(String key:markov.keySet())
			visited.put(key, false);
		
		
		Stack<WordProb> stack = new Stack<WordProb>();
		stack.push(startingWP);
		numSyllables+=syllableCount(startingWP.word);
		
		//Find a path that is desired Syllables long using DFS
		while(!stack.isEmpty()){
			WordProb element = stack.pop();
			visited.put(element.word, true);
			int wordSyl = syllableCount(element.word);
			
			//if more than desired syllable, print
			if(numSyllables+wordSyl>desiredSyllables){
				continue;
			}else{
				sentence.add(element.word);
				numSyllables+=wordSyl;
			}
			
			//If x syllables, print result
			if(numSyllables+wordSyl==desiredSyllables){
				break;
			}
			
			List<WordProb> wordList = markov.get(element.word);
			for(int i=0;i<wordList.size();i++){
				WordProb wp = wordList.get(i);
				if(wp!=null&&!visited.get(wp.word))
					stack.push(wordList.get(i));
			}
		}
		
		//If less than, redo
		
		//boolean islastupper = !sentence.get(sentence.size()-1).equals(sentence.get(sentence.size()-1).toLowerCase());
		if(numSyllables<desiredSyllables&&numofpaths>0||sentence.size()<1){
			try{
				DFS(starting,desiredSyllables,isStart,numofpaths-1);
			}catch(StackOverflowError sofe){
				System.out.println(sofe+" "+numofpaths+"");
			}
		}
		
		return sentence;
	}	
	
	/** Finds the number of syllables in a word or phrase
	 * 
	 * @param word String to parse through
	 * @return an integer.
	 */
	public int syllableCount(String word){
		word = word.toLowerCase();
		//Syllable pattern matcher
		Pattern p = Pattern.compile("[aeiouy]+[^$e(,.:;!?)]");
		if(word.length()==2||word.length()==1)
			return 1;
		//Read every word and print its syllables
		Matcher matcher;
		int syllableCount;
		//Parse through ever word in dictionary
		syllableCount = 0;
		matcher = p.matcher(word);
		//Find syllables in a word
		while(matcher.find()){
			syllableCount++;
		}
		if(syllableCount==0)
			return 1;
		
		if(word.subSequence(word.length()-1, word.length()-1).equals("y")&&word.length()>2)
			syllableCount++;
		return syllableCount;
	}
	
	/** Change the file that the haiku program learns from
	 * 
	 * @param link file location
	 * @throws FileNotFoundException
	 */
	public void setNewFile(String link) throws FileNotFoundException{
		File learningFile = new File(link);
		if(!learningFile.exists())
			throw new IllegalArgumentException("File must exist!");
		ProbabilityGenerator pg = new ProbabilityGenerator(learningFile);
		pg.generateProbability();
		markov.clear();
		markov = pg.getProbabilties();
	}
	
	public static void main(String[] args) throws FileNotFoundException{
		HaikuGenerator hg = new HaikuGenerator(new File("trump.txt"));
		Random r = new Random();
		List<String> keys  = new ArrayList<String>(hg.markov.keySet());
		String currentWord = keys.get( r.nextInt(keys.size()) );
		hg.generateHaiku();
	}
}
