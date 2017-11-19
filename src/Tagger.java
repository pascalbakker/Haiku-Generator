import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javassist.bytecode.stackmap.TypeData.ClassName;
import opennlp.tools.*;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.parser.Cons;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.uima.postag.POSTagger;


//Noun,adjective,verb,coordinating conjunction,adverb,verb pretense
public class Tagger {
	public static String DICTIONARY = "dictionary.txt";
	private static final Logger LOGGER = Logger.getLogger( ClassName.class.getName() );

	/** Writes the word class of a dictionary to "wordclass.txt"
	 *  
	 * @throws IOException
	 */
	public static void tagWordClass() throws IOException{
		//Read words from dictionary
		File dictFile = new File(DICTIONARY);
		Scanner dictionaryReader = new Scanner(dictFile);
		//Objects to print to file 
		BufferedWriter bw = new BufferedWriter(new FileWriter("wordclass.txt"));
		//Variables	
		POSModel model = new POSModelLoader().load(new File("en-pos-maxent.bin"));
		POSTaggerME tagger = new POSTaggerME(model);
		//Read words and add word class to WordClass.txt
		String[] tag;
		while(dictionaryReader.hasNext()){
			//Read word
			String nextLine = dictionaryReader.next();
			nextLine.trim();
			//Make it an array
			String [] aWord =  WhitespaceTokenizer.INSTANCE
					.tokenize(nextLine);
			//Tag it
			tag = tagger.tag(aWord);
			//Write to file
			bw.write(tag[0]);
			bw.newLine();
		}
		//Close
		bw.close();
		dictionaryReader.close();
	}
	
	/** Writes the number of syllables in a word to "wordsyllables.txt"
	 * 
	 * @throws IOException
	 */
	public static void tagSyllables() throws IOException{
		LOGGER.log(Level.FINE, "Initializing syllable");
		
		//Scanner 
		File dictFile = new File(DICTIONARY);
		Scanner dictionaryReader = new Scanner(dictFile);
		//Writer
		BufferedWriter bf = new BufferedWriter(new FileWriter("wordsyllables.txt"));
		//Syllable pattern matcher
		Pattern p = Pattern.compile("[aeiouy]+[^$e(,.:;!?)]");
		//Read every word and print its syllables
		Matcher matcher;
		int syllableCount;
		//Parse through ever word in dictionary
		while(dictionaryReader.hasNext()){
			syllableCount = 0;
			String dictionaryWord = dictionaryReader.next();
			matcher = p.matcher(dictionaryWord);
			//Find syllables in a word
			while(matcher.find()){
				syllableCount++;
			}
			
			bf.write(Integer.toString(syllableCount));
			bf.newLine();
		}
		//Close
		bf.close();
		dictionaryReader.close();
		LOGGER.log(Level.FINE, "Tagging complete. Location "+"wordsyllables.txt");
	}
	
	public static void main(String[] args) throws IOException{
		tagWordClass();
		tagSyllables();
	}
}
