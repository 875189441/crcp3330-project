
import java.util.ArrayList;
public class Node<T> {
	
	ArrayList<T> tokenSequence;
	ArrayList<Node<T>> children = new ArrayList<Node<T>>();
	int count=1;
	
	public Node(ArrayList<T> tokenSequence) { // Constructor
		this.tokenSequence = tokenSequence;
	}
	public Node() { //constructor
		tokenSequence = new ArrayList<T>(); 
	}
	
	public boolean addNode(Node<T> node) {
		boolean found = false;
		int i = 0;
		if(tokenSequence.equals(node.tokenSequence)) {// if tokenSequence node == add node
			found = true;
			count++;
		}else if(amISuffix(node)||(tokenSequence.size()==0)) {
				while(!found && i < children.size()) {
					found = children.get(i).addNode(node);//add nood to children
					i++;
				}
			if(!found) {
				children.add(node);
				found = true;
			}
		}
		return found;
	}
	public boolean amISuffix(Node<T> node) {//use sublist fromindex to toinddex
		if((node.tokenSequence.size() - tokenSequence.size()) < 0)
			return false;		
		return tokenSequence.equals(node.tokenSequence.subList(node.tokenSequence.size() - this.tokenSequence.size(), node.tokenSequence.size()));
	}
	public boolean pMinElim(int total, double pmin) {
		boolean remove = false;
		float num = (count/(float)(total-(tokenSequence.size()-1)));//sequence have occur
		
		if(num <= pmin && tokenSequence.size() != 0) {
			remove = true;
		}else {
				for(int i = children.size()-1; i >= 0; i--) {
					if(children.get(i).pMinElim(total, pmin)) {
						children.remove(children.get(i));//do removing
					}
				}
				remove = false;
		}
		return remove; 
	}
	public void print() {//to print 
		System.out.println(tokenSequence); 
		for(int i = 0; i < children.size(); i++) 
			children.get(i).print(1);
	}
	public void clear() {//clear 
		tokenSequence.clear();
		for(int i = 0; i < children.size(); i++)
			children.get(i).clear();
		
	}
	public void print(int numspaces) {
		for(int i = 1; i <= numspaces; i++) {
			System.out.print(" ");
		}
		System.out.print("-->");
		System.out.println(tokenSequence);
		for(int j = 0; j < children.size(); j++) 
			children.get(j).print(numspaces + 1);
	}
	
	
}