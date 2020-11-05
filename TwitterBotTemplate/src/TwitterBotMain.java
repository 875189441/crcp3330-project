/* Programmer: Adam Wu
 
 */

import processing.core.*;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import com.jaunt.JauntException;

//This class serves as a template for creating twitterbots and demonstrates string tokenizing and web scraping and the use of the 
//twitter API
public class TwitterBotMain extends PApplet {

	private ArrayList<String> tokens;
	private static String HEYER_TWITTER_URL = "https://twitter.com/GeorgetteRomanc"; // this is mine, you should use
																						// yours
	private static int TWITTER_CHAR_LIMIT = 140; // I understand this has changed... but forget limit

	// useful constant strings -- for instance if you want to make sure your tweet
	// ends on a space or ending punctuation, etc.
	private static final String fPUNCTUATION = "\",.!?;:()/\\";
	private static final String fENDPUNCTUATION = ".!?;,";
	private static final String fREALENDPUNCTUATION = ".!?";

	private static final String fWHITESPACE = "\t\r\n ";

	// example twitter hastag search term
	private static final String fPASSIVEAGG = "passiveaggressive";
	private static final String fCOMMA = ",";
	ProbabilityGenerator<String> pitchGenerator = new ProbabilityGenerator<String>();
	ProbabilityGenerator<String> ryhthemGenerator = new ProbabilityGenerator<String>();
	MarkovG<String> p2 = new MarkovG<String>();
	MarkovG<String> r2 = new MarkovG<String>();

	// handles twitter api
	MarkovG<String> trainTweet = new MarkovG<String>();
	MarkovG<String> To = new MarkovG<String>();
	TwitterInteraction tweet = new TwitterInteraction();;
	ArrayList<String> genTweet = new ArrayList<String>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PApplet.main("TwitterBotMain"); // Not really using processing functionality but ya know, you _could_. UI not
										// required.

	}

	public void settings() {
		size(300, 300); // dummy window

	};

	public void setup() {
		tweet = new TwitterInteraction();

//NOTE: everything starts uncommented. Comment out the calls that you would like to try and use.

		loadNovel("data/The Grand Sophy excerpt.txt"); // TODO: must train from another source
//		println("Token size:"+tokens.size());

		// TODO: train an AI algorithm (eg, Markov Chain) and generate text for markov
		// chain status

		// can train on twitter statuses -- note: in your code I would put this part in
		// a separate function
		// but anyhow, here is an example of searrching twitter hashtag. You have to pay
		// $$ to the man to get more results. :(
		// see TwitterInteraction class
//		ArrayList<String> tweetResults = tweet.searchForTweets("John Cage");
//		for (int i = 0; i < tweetResults.size(); i++) {
//				println(tweetResults.get(i)); //just prints out the results for now
//		}

		// Make sure within Twitter limits (used to be 140 but now is more?)
		// String status = "OMG testing again";
		// tweet.updateTwitter(status);

		// prints the text content of the sites that come up with the google search of
		// dogs
//		//you may use this content to train your AI too
		// Scraper scraper = new Scraper();
		// ArrayList<String> results;
		// try {
		// results = scraper.scrapeGoogleResults("dogs");

		// print your results
		// System.out.println(results);
//			
		// scraper.scrape("http://google.com", "dogs"); //see class documentation
//
		// } catch (JauntException e) {
		// TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

	// this loads the novel 'The Grand Sophy' given a path p -- but really will load
	// any file.
	void loadNovel(String p) {
		String filePath = getPath(p);
		Path path = Paths.get(filePath);
		tokens = new ArrayList<String>();

		try {
			List<String> lines = Files.readAllLines(path);

			for (int i = 0; i < lines.size(); i++) {

				TextTokenizer tokenizer = new TextTokenizer(lines.get(i));
				ArrayList<String> t = tokenizer.parseSearchText();
				for (int j = t.size() - 1; j >= 0; j--) {
					if (t.get(j).contains(","))

						t.remove(j);
				}
				tokens.addAll(t);
			}

		} catch (Exception e) {
			e.printStackTrace();
			println("Oopsie! We had a problem reading a file!");
		}
	}

	void printTokens() {
		for (int i = 0; i < tokens.size(); i++)
			print(tokens.get(i) + " ");
	}

	// get the relative file path
	String getPath(String path) {

		String filePath = "";
		try {
			filePath = URLDecoder.decode(getClass().getResource(path).getPath(), "UTF-8");

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filePath;
	}

	public void draw() {
		// ellipse(width / 2, height / 2, second(), second());
text("press 1 to create sentence",40,100);
	}

	public void keyPressed() {
		String a="";
		if (key == '1') {
			String status = "";
			
			trainTweet.setTokens(tokens);

			trainTweet.train(tokens);
			trainTweet.normalize();

			trainTweet.generate(trainTweet.generateToken());
			status = status + trainTweet.genarray();

	
	a=status.replaceAll(",", "");

			System.out.println(a);
		}
	

	}

	public void unitTest1() {
		
		trainTweet.setTokens(tokens);
		trainTweet.setTokens(tokens);
		System.out.println("------Pitches Probability:-------");
		trainTweet.train();
		trainTweet.print(1);
		System.out.println("------------------------------");
		System.out.println("------Rhythm Probability:-------");
		trainTweet.train();
		trainTweet.print(1);
		System.out.println("------------------------------");

	}

//Unit Test Two
	public void unitTest2() {

		pitchGenerator.generate();
		ryhthemGenerator.generate();
		System.out.println(pitchGenerator.genarray());
		System.out.println(ryhthemGenerator.genarray());

	}

//Unit Test three 
	public void unitTest3() {

		for (int i = 0; i < 1000; i++) {
			
			trainTweet.generate(trainTweet.generateToken());
			trainTweet.setTokens(trainTweet.genarray());
			trainTweet.train();
			trainTweet.generate(trainTweet.generateToken());
			trainTweet.setTokens(trainTweet.genarray());
			trainTweet.train();

		}
		System.out.println("------Pitches Probability:-------");
		trainTweet.print(1000);
		System.out.println("------Rhythm Probability:-------");
		trainTweet.print(1000);

	}
	public void unitTest1M() {

		p2.setTokens(tokens);
		r2.setTokens(tokens);
		System.out.println("------Pitches Transition Table:-------");
		p2.train();
		p2.normalize();
		p2.print();
		System.out.println("------------------------------");
		System.out.println("------Rhythm Transition Table:-------");
		r2.train();
		r2.normalize();
		r2.print();
		System.out.println("------------------------------");

	}

//Unit Test Two
	public void unitTest2M() {

		p2.generate();
		r2.generate();
		System.out.println(p2.genarray());
		System.out.println(r2.genarray());
		

	}

//Unit Test three 
	public void unitTest3M() {

		for (int i = 0; i < 1000; i++) {
			p2.generate(p2.generateToken());
			p2.setTokens(p2.genarray());
			p2.train();
			r2.generate(r2.generateToken());
			r2.setTokens(r2.genarray());
			r2.train();

		}
		p2.normalize();
		r2.normalize();
		System.out.println("Pitches Transition Table ");
		p2.print();
		System.out.println("------------------------------");
		System.out.println("Rhythm Transition Table ");
		r2.print();
		System.out.println("------------------------------");
	}
}
