import java.util.ArrayList;

//ProbabilityGenerator
public class ProbabilityGenerator<T> {

	ArrayList<T> newTokens = new ArrayList<T>();
	 ArrayList<T> genarray = new ArrayList<T>();
	public void setTokens(ArrayList<T> input) {
		newTokens.clear(); // clear
		
		for (int i = 0; i < input.size(); i++)
			newTokens.add(input.get(i));
	}
	ArrayList<T> genarray() {
		return genarray;
	}
	// array alphabet
	 ArrayList<T> alphabet = new ArrayList<T>();
	// alphabet count
	 ArrayList<Integer> alphabetcount = new ArrayList<Integer>();
	// count for normalizing
	float probability;
	private ArrayList<Float> newProbability = new ArrayList<Float>();

	public void train() {// train class
		for (int i = 0; i < newTokens.size(); i++) {
			if (alphabet.contains(newTokens.get(i)) == false) {
				alphabet.add(newTokens.get(i));
				alphabetcount.add(1);

			} else {

				int Findex = alphabet.indexOf(newTokens.get(i)); // find index of token in alphabet
				alphabetcount.set(alphabet.indexOf(newTokens.get(i)), alphabetcount.get(Findex) + 1); // incremented																																																	
			}

		}
	}

	public void print(int times) {// print function
		
		for (int j = 0; j < alphabetcount.size(); j++) {
			probability = (float) alphabetcount.get(j) / (newTokens.size() * times);
			System.out.println(alphabet.get(j));
			System.out.println(probability);
			newProbability.add(probability);
		}
	}

	void generate() {
		genarray.clear();
		for (int i = 0; i < newTokens.size(); i++) {
			genarray.add(generateToken());
		}
	}

	public T generateToken() {// generate tokens 
		float randIndex = (float) Math.random();
		boolean found = false;
		int index = 0;
		float total = 0;

		while (!found && index < alphabetcount.size()) {
			total += newProbability.get(index);
			found = randIndex <= total;
			index++;
		}
		if (found)
			return alphabet.get(index - 1);
		else
			return alphabet.get(alphabet.size() - 1);
	}
}