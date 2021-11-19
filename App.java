
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import org.junit.*;
import org.junit.jupiter.api.DisplayName;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.awt.*;
import java.awt.event.*;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;


class WordsObj {
	String wordBank;
	int wordCount;

	public WordsObj(String wordBank, int wordCount) {
		this.wordBank = wordBank;
		this.wordCount = wordCount;
	}

	/**
	 * Retrieves the string for this WordsObj.
	 * @param toString(
	 * @return String
	 */
	public String getWordBank() {return this.wordBank;}

	/**
	 * Sets the word of this WordObj.
	 */
	public void setWordBank(String word) {this.wordBank = word;}

	/**
	 * Retrieves the number of this WordsObj.
	 * @return int
	 */
	public int getWordCount() {return this.wordCount;}

	/**
	 * Sets the number of this WordsObj
	 */
	public void setWordCount(int number) {this.wordCount = number;}


	/**
	 * @return String
	 */
	public String toString() {return this.wordBank + " " + this.wordCount;}
}

/**
 * Used at the end of wordCount() to sort by wordCount of the WordObj object class
 */
class SortCount implements Comparator<WordsObj> {
	public int compare(WordsObj a, WordsObj b){return b.wordCount - a.wordCount;}
}

//  <=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=->
//<=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=->
//  <=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=->

public class App {

	/**
	* Opens a window for the user, containing a button and a window for actions as well as results of those actions.
	* <p>
	* Button: Runs fileRead() that is the param for a wordCount(). This will set the text for the label.
	* <p>
	* Label: Starts empty untill the Button is pressed. After it will be populated with the WordObj[] that is toString'ed at the end of wordCount().
	*/
	static void GUI() {

		JFrame frame = new JFrame("Word Count GUI App");
		JPanel panel = new JPanel();
		JTextArea label1 = new JTextArea ("");
		JButton button = new JButton("Start Count");
		GroupLayout layout = new GroupLayout(panel);

		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					label1.setText(wordCount(fileRead("\\C:\\Users\\Nicolo Perrelli\\Desktop\\poemText.txt")));
					//more stuff to make GUI look nice
				}
				catch (FileNotFoundException e1) { e1.printStackTrace(); }
			}
		});

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000,1000);
		frame.add(panel);
		panel.setLayout(layout);
		button.setBounds(10, 10, 100, 50);
		panel.add(button);
		label1.setBounds(10, 70, 970, 900);
		label1.setLineWrap(true);
		label1.setWrapStyleWord(true);
		label1.setFont(new Font("Serif", Font.ITALIC, 18));
		panel.add(label1);

		frame.setVisible(true);
	}

	@Test
	@DisplayName("GUI Tests")
	//TESTS ON GUI ELEMENTS
	public void testGUI() {
		//a robot or a @before and find way to test elements
		//i have not made the gui a class so it is harder to test this way
	}


	/**
	 * <code>fileRead()</code> takes in a file path (relative or not) to a
	 * txt file (or another readable file). With this file it then makes
	 * an <code>ArrayList</code> to split words by spaces and other word
	 * breaks. Once these words are broken up this function will return an
	 * unsorted array still containing some non-alpha numeric characters.
	 *
	 * @param filePath
	 * @return String[]
	 * @throws FileNotFoundException
	 */
	static String[] fileRead(String filePath) throws FileNotFoundException {
		//move data from .txt file
		Scanner sc = new Scanner(new File(filePath));
		List<String> lines = new ArrayList<String>();

		while (sc.hasNext()) {
			//for cleaning up edge casses
			String splitIn = sc.next().replaceAll("—", " ");

			//take edge case into an array for seperate inserts with checks to filter out nulls and " "'s
			if (splitIn.contains(" ")) {
				String[] splitWords = splitIn.split(" ", 2);
				if (!splitWords[0].isEmpty()) {lines.add(splitWords[0]);}
				if (!splitWords[1].isEmpty()) {lines.add(splitWords[1]);}}
			else if (!splitIn.isEmpty()) {lines.add(splitIn);}
			else{System.out.println("SOMETHING DIDN'T FIT");}
		}

		String[] str = lines.toArray(new String[0]);
		return str;
	}

	@Test(expected = FileNotFoundException.class)
	@DisplayName("File is not empty")
	public void testFileReadErr() throws FileNotFoundException {
		//test FNF Exception
		fileRead("\\C:\\Users\\Not A Real Name\\Desktop\\BadFile.txt");
	}

	@Test
	@DisplayName("File reads correctly")
	public void testFileRead() throws FileNotFoundException {
		String[] testString1 = {"Yes", "No", "Yes;"};//new line and "-"s
		String[] testString2 = {"No.", "Yes", "Spaces", "Lines"};//Iregular Lines and Spaces.
		assertArrayEquals(testString1, fileRead("\\C:\\Users\\Nicolo Perrelli\\Desktop\\TestFile1.txt"));
		assertArrayEquals(testString2, fileRead("\\C:\\Users\\Nicolo Perrelli\\Desktop\\TestFile2.txt"));
	}


	/**
	 * Takes in an array of strings that contains no empty strings,
	 * but may contain non-alphanumeric characters. <code>wordCount()</code> will
	 * loop through the <code>String[]</code> making a List<WordObj> of a word
	 * and a count of how many times it appears in the text, clearing
	 * the words of non-alphanumeric characters. Finaly sorting the
	 * List<WordObj> by the WordObj's wordCount.
	 * 
	 * @param String[]
	 * @return String
	 */
	static String wordCount(String[] str) {

		Boolean newWordbBoolean;
		String word;
		List<WordsObj> wb = new ArrayList<WordsObj>();

		for (int i = 0; i < str.length; i++) {
			// Grab Word
			word = str[i];

			// Scrub Word of Non-alphanumeric
			word = word.replaceAll("[^a-zA-Z0-9]", "");

			//set bool to true till proven otherwise for this word.
			newWordbBoolean=true;
			for (WordsObj toss: wb) {

				if (toss.getWordBank().equals(word)) {
					newWordbBoolean=false;
					toss.setWordCount(toss.getWordCount()+1);
					break;
				}
			}

			//if word is new add it and reset the key
			if (newWordbBoolean) {
				WordsObj newThing = new WordsObj(word,1);
				wb.add(newThing);
			}
		}
		Collections.sort(wb, new SortCount());
		return (wb.toString());
	}

	@Test
	@DisplayName("Test wordCount()")
	public void testWordCount() {
		String[] testString1 = {"yes", "no", "yes"};//quantity and placment1
		String[] testString2 = {"no", "yes"};//quantity and placment2
		String[] testString3 = {"yes", "yes", "yes"};//all the same
		String[] testString4 = {"", "", ""};//array of empty
		String[] testString5 = {};//empty array
		String[] testString6 = {"no.", "'yes'"};//Character Scrub

		assertEquals("[yes 2, no 1]", wordCount(testString1));
		assertEquals("[no 1, yes 1]", wordCount(testString2));
		assertEquals("[yes 3]", wordCount(testString3));
		assertEquals("[ 3]", wordCount(testString4));
		assertEquals("[]", wordCount(testString5));
		assertEquals("[no 1, yes 1]", wordCount(testString6));
	}


	public static void main(String[] args) {
		GUI();
	};
}


