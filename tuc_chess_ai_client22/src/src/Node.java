package tuc_chess_ai_client22.src.src;

import java.util.Hashtable;
import java.util.Vector;

public class Node {
	
    String[][] key;
    Node parent;
    Vector<Node> child ;
    Vector<String> moves;
    int N, Q;
    Hashtable<Node, String> actions;
    
    boolean white; // If the node belongs to white. White has the value true, and false for black
    
	// Represents a node of an n-ary tree
	public Node(String[][] key, Node parent, boolean isWhite)	{
		this.key = key;
		this.parent = parent;
		child = new Vector<>();
		moves = new Vector<>();
		N = Q = 0;
		actions = new Hashtable<>();
		this.white = isWhite;
	}

}