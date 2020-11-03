
import java.util.ArrayList;
import java.util.List;

public class MarkovOrder<T> extends MarkovG<T> {
	int orderM;
	ArrayList<ArrayList<T>> uniqueSequence = new ArrayList<ArrayList<T>>();
	ArrayList<T> container = new ArrayList<T>();
	ArrayList<Integer> getRow = new ArrayList<Integer>();
	ArrayList<Float> newProbability = new ArrayList<Float>();
	MarkovG<T> markG = new MarkovG<T>();
	ProbabilityGenerator<T> probG = new ProbabilityGenerator<T>();

	/*
	 * for i = orderM -1 to (i < size of the input - 1) do {
	 * 
	 * 1. Create the current sequence (eg. curSequence) of size orderM from the
	 * input Remember to start the index into the input at 0 (with this algorithm)
	 * a. add the previous tokens to a container (eg ArrayList). b. You may do this
	 * in a for-loop or use .subList() i.
	 * https://beginnersbook.com/2013/12/how-to-get-sublist-of-an-arraylist-with-
	 * example/
	 * 
	 * 2. Find curSequence in uniqueAlphabetSequences if curSequence is not found {
	 * 1. set rowIndex to the size of uniqueAlphabetSequences
	 * 
	 * 2. add the curSequence to uniqueAlphabetSequences
	 * 
	 * 3. add a new row to the transition table the size of the alphabet }
	 * 
	 * 3. Find the current next token (tokenIndex) { tokenIndex = the next index of
	 * the token in the alphabet (i+1)
	 * 
	 * if tokenIndex is not found in the alphabet { 1. tokenIndex = size of the
	 * alphabet 2. add the token to the alphabet 3. expand transitionTable
	 * horizontally } }
	 * 
	 * 4. Update the counts – since we started after the beginning, rowIndex will
	 * not be -1 a. Get the row using rowIndex b. Get the column using tokenIndex c.
	 * Add one to that value retrieved from the transition table }
	 */
	MarkovOrder() {
		super();
		orderM = 2;
	}

	MarkovOrder(int M) {
		super();
		orderM = M;

	}

	public void train(ArrayList<T> newTokens) {

		for (int i = orderM - 1; i < newTokens.size() - 1; i++) {
			int tokenIn = alphabet.indexOf(newTokens.get(i + 1));
			int RowIndex = uniqueSequence.indexOf(container);

			container = new ArrayList<T>(newTokens.subList(i - (orderM - 1), i - (orderM - 1) + orderM));
			if (RowIndex == -1) {
				RowIndex = uniqueSequence.size();
				uniqueSequence.add(container);
				ArrayList<Float> nRow = new ArrayList<Float>();// include expand
				for (int y = 0; y < alphabet.size(); y++) {
					nRow.add((float) 0);
				}
				transitionTable.add(nRow);

			}

			tokenIn = alphabet.indexOf(newTokens.get(i + 1));

			if (tokenIn == -1) {
				tokenIn = alphabet.size();
				alphabet.add(newTokens.get(i + 1));
				alphabet.add(newTokens.get(i + 1));
				for (int j = 0; j < transitionTable.size(); j++) {// include expand
					transitionTable.get(j).add((float) 0);
				}
			}

			if (RowIndex != -1) {
				ArrayList<Float> getRow = transitionTable.get(RowIndex);
				getRow.set(tokenIn, getRow.get(tokenIn) + 1);

			}

		}

	}

	public void print() {
		for (int i = 0; i < transitionTable.size(); i++) {
			prob(transitionTable.get(i), i);
			System.out.println(alphabet.get(i));
			System.out.println(newProbability);
		}
	}

//end class

	public void prob(ArrayList<Float> transitionTable, int index) {
		float total = 0;
		for (int i = 0; i < transitionTable.size(); i++) {
			total = (float) transitionTable.get(i) + total;
		}
		if (total == 0) {

			for (int i = 0; i < transitionTable.size(); i++) {
				newProbability.set(i, (float) 0);
			}
		} else {
			newProbability.clear();
			for (int j = 0; j < transitionTable.size(); j++) {
				newProbability.add(((float) transitionTable.get(j) / total));
			}
		}

	}

	/*
	 
	 */
	T generate(ArrayList<T> initSeq) {
		float sum = 0;
		float v = 0;
		T newTokens = null;
		int curIn = uniqueSequence.indexOf(initSeq);
		if (curIn == -1) {
			markG.generate();

		}
		if (curIn != -1) {
			ArrayList<Float> FindRow = transitionTable.get(curIn);
			for (int i = 0; i < FindRow.size(); i++) {
				sum = sum + FindRow.get(i);
				if (sum == 0) {
					probG.generate();
				}
			}

		}
		return newTokens;
	}

	ArrayList<T> generate(ArrayList<T> initSeq, int numTokensToGen) {
		ArrayList<T> output = new ArrayList<T>();
		T token;
		for (int i = 0; i <= numTokensToGen; i++) {
			token = generate(initSeq);
			initSeq.remove(0);
			initSeq.add(token);
			output.add(token);
			initSeq.remove(initSeq.size() - 1);

		}
		return output;
	}

}