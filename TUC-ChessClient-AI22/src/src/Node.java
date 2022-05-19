package src;

import java.util.Hashtable;
import java.util.Vector;

public class Node {
	
    String[][] key;
    Node parent;
    Vector<Node> child ;
    Vector<String> moves;
    int N, Q;
    Hashtable<Node, String> actions;
    
	// Represents a node of an n-ary tree
	public Node(String[][] key, Node parent)	{
		this.key = key;
		this.parent = parent;
		child = new Vector<>();
		moves = new Vector<>();
		N = Q = 0;
		actions = new Hashtable<>();
	}

}