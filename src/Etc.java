import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Etc {
	public static void cleanSample() throws IOException{
		Scanner reader = new Scanner(new File("sample.txt"));
		BufferedWriter bf = new BufferedWriter(new FileWriter("sampleclean.txt"));
		int line=0;
		while(reader.hasNext()){
			String word = reader.next();
			word.trim();
			word = removeUnwantedCharacters(word);
			bf.write(word+" ");
			if(line%100==0)
				bf.newLine();
			bf.write(word);
			line++;
		}	
	}
	
	public static String removeUnwantedCharacters(String word){
		//Replaces all unwanted characters in the string
		String newWord = word.replaceAll("[\\-\\+\\=\\.\\|\\^\\:\\.\\;\\â\\€\\!\\˜\\™\\*\\œ\\,\\?\\_\\(\\[\\]\"\\$\\”\\#\\)]", "");
		//IN PROGRESS: remove ? from string
		newWord = newWord.replace("\\?", "");
		return newWord;
	}
	
	public static void main(String[] args) throws IOException{
		cleanSample();
	}
}
