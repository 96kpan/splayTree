package splayTreeDictionary;

/**
 * Node of the tree.  It's a standard BST node except for the parent ptr.
 * There are some helper methods here specific to the application.
 */
public class Node {

	public Point record;
	public String satellite;
	public Node left, right, parent;

	//just another constructor I added if there is no information about left, right, and parent nodes
	//calls the constructor that contains left, right, and parent parameters
	public Node(Point rec, String sat)
	{
		this(rec, sat, null, null, null);
	}

	//just another constructor I added if there is information about left, right, and parent nodes
	//sets values
	public Node(Point rec, String sat, Node left, Node right, Node parent)
	{
		record = rec;
		satellite = sat;
		this.left = left;
		this.right = right;
		this.parent = parent;
	}

	public String toString()
	{
		return "record = " + record + ", satellite = " + satellite;
	}

	// add more methods as appropriate
}
