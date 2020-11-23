
import java.util.ArrayList;
public class Tree<T> extends ProbabilityGenerator<T> {
	
	Node<T> root = new Node<T>();
	int L = 3;
	double pMin;// = 0.15;
	int sampleSet = 0;
	int r;
	int totalInputTokens;
	Tree()
	{
		pMin = 0.15;
		r = 1;
		totalInputTokens = 1;
	}
	public void train() {
		for(int i = 1; i <= L; i++){
			for(int j = 0; j <= newTokens.size() - i; j++) {
				ArrayList<T> curSequence = new ArrayList<T>();//cursequence creat for each times
				for(int p = 0; p < i; p++) {//get sequence
						curSequence.add(newTokens.get(j+p));
					}
				//create new node with cursequence
					Node<T> nood = new Node<T>(curSequence);
					nood.hasSeqAtEndOfDataset = true;
					root.addNode(nood);	//add nood
				}
			
			}
		sampleSet = newTokens.size();
		root.pMinElim(sampleSet, pMin);
		root.rElimination( r, null );
	}
	public ArrayList<T> generate(int length)
	{
		return (ArrayList<T>) root.generate(new ArrayList(), length);
	}
	public void print() {
		
			root.print();
	}
	public void clearT() {
		root.clear();
		root=new Node<T>();
		
	}
}
