//Adam Wu 

import processing.core.*;
import java.util.*;

//importing the JMusic stuff
import jm.music.data.*;
import jm.JMC;
import jm.util.*;
import jm.midi.*;

import java.io.UnsupportedEncodingException;
import java.net.*;

//make sure this class name matches your file name, if not fix.
public class HelloWorldMidiMain extends PApplet {

	MelodyPlayer player; // play a midi sequence
	MidiFileToNotes midiNotes; // read a midi file

	ProbabilityGenerator<Integer> pitchGenerator = new ProbabilityGenerator<Integer>();
	ProbabilityGenerator<Double> ryhthemGenerator = new ProbabilityGenerator<Double>();

	boolean play = false;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PApplet.main("HelloWorldMidiMain"); // change this to match above class & file name

	}

	// setting the window size to 300x300
	public void settings() {
		size(300, 300);

	}

	// doing all the setup stuff
	public void setup() {
		fill(120, 50, 240);

		// returns a url
		String filePath = getPath("mid/MaryHadALittleLamb.mid");
		// playMidiFile(filePath);

		midiNotes = new MidiFileToNotes(filePath); 
//		// which line to read in --> this object only reads one line (or ie, voice or ie, one instrument)'s worth of data from the file
		midiNotes.setWhichLine(0);

		player = new MelodyPlayer(this, 100.0f);
		player.setup();
		player.setMelody(midiNotes.getPitchArray()); // instead of setting Melody, send it to ProbabilityGen?
		player.setRhythm(midiNotes.getRhythmArray());

	}

	public void draw() {
		text("Press 1,2,3 for Unit Test 1,2,3", 40, 100);
		player.play(); // play each note in the sequence -- the player will determine whether is time
						// for a note onset

	}

	// this finds the absolute path of a file
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

	void playMidiFile(String filename) {
		Score theScore = new Score("Temporary score");
		Read.midi(theScore, filename);
		Play.midi(theScore);
	}

	

	public void mouseClicked() {

	}

	// Unit Test One 
	public void unitTest1() {
		ArrayList<Integer> pitchInput = midiNotes.getPitchArray();

		ArrayList<Double> rhythmInput = midiNotes.getRhythmArray();

		pitchGenerator.setTokens(pitchInput);
		ryhthemGenerator.setTokens(rhythmInput);
		System.out.println("------Pitches Probability:-------");
		pitchGenerator.train();
		pitchGenerator.print(1);
		System.out.println("------------------------------");
		System.out.println("------Rhythm Probability:-------");
		ryhthemGenerator.train();
		ryhthemGenerator.print(1);
		System.out.println("------------------------------");

	}
//Unit Test Two
	public void unitTest2() {

		pitchGenerator.generate();
		ryhthemGenerator.generate();
		System.out.println(pitchGenerator.genarray());
		System.out.println(ryhthemGenerator.genarray());
		player.setMelody(pitchGenerator.genarray());
		player.setRhythm(ryhthemGenerator.genarray());

	}
//Unit Test three 
	public void unitTest3() {

		for (int i = 0; i < 1000; i++) {

			pitchGenerator.generate();
			pitchGenerator.setTokens(pitchGenerator.genarray());
			pitchGenerator.train();
			ryhthemGenerator.generate();
			ryhthemGenerator.setTokens(ryhthemGenerator.genarray());
			ryhthemGenerator.train();

		}
		System.out.println("------Pitches Probability:-------");
		pitchGenerator.print(1000);
		System.out.println("------Rhythm Probability:-------");
		ryhthemGenerator.print(1000);

	}
	public void keyPressed() {
		if (key == ' ') {
			player.reset();
			println("Melody started!");
		}
		if (key == '1') {
			unitTest1();
		}
		if (key == '2') {
			unitTest2();
		}
		if (key == '3') {
			unitTest3();
		}
		if (key == 'P') {
			if (play == false) {
				play = true;
			} else {
				play = false;
			}
		}

	}


}
