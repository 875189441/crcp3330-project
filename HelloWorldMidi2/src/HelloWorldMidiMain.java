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
	MarkovG<Integer> p2 = new MarkovG<Integer>();
	MarkovG<Double> r2 = new MarkovG<Double>();
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

		p2.setTokens(midiNotes.getPitchArray());
		r2.setTokens(midiNotes.getRhythmArray());
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
	public void unitTest2() {

		p2.generate();
		r2.generate();
		System.out.println(p2.genarray());
		System.out.println(r2.genarray());
		player.setMelody(p2.genarray());
		player.setRhythm(r2.genarray());

	}

//Unit Test three 
	public void unitTest3() {

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
