package splayTreeDictionary;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * Point defined by integer Cartesian coordinates.
 *
 * This just stores the two endpoints as plain int coordinates,
 * and as public attributes -- not encapsulated at all.
 */
class Point implements Comparable<Point> {

    /** Cartesian coordinates are just plain public attributes. */
    public int x, y;

    /** Ctor for a point, given its x, y coordinates. */
    public Point(int xx, int yy) {
        x = xx;
        y = yy;
    }

    /** Copy of a point. */
    public Point(Point p) {
        this(p.x, p.y);
    }

    /** Comparison is lexicographical: vertical first, horizontal second. */
    public int compareTo(Point p) {
        final int dy = p.y - y;
        return 0 == dy ? p.x - x : dy;
    }

    /** Stringify in a very plain comma-separated format. */
    public String toString() {
        return "" + x + ", " + y;
    }
}


/**
 * Node of the tree.  It's a standard BST node except for the parent ptr.
 * There are some helper methods here specific to the application.
 */
class Node {

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

}



/**
 * Splay tree (BST invented by Sleator and Tarjan).
 */
class SplayTree
{
	//root instance variable
		Node root;

		//constructor
		public SplayTree(){
			//initialize the root variable to NULL 
			root = null;
		}

		/** Portray tree as a string.  Optional but recommended. */
		/*
		 * Description: 
		 * A toString method in the class Splaytree is written for debugging purpose
		 * I chose an inorder traversal of the tree because it is easiest to understand
		 * (at least for me). I simply just passed this method to an inOrder helper
		 * method, which will product the string representation of the SPLAY tree
		 */
		public String toString()
		{
			//calls helper method. pass in an empty string and node ROOT
			return inOrder("", root);
		}

		/*
		 * Description: 
		 * helper method that will return the string representation of an inorder 
		 * traversal of a splay tree. Again, I chose an inOrder traversal just because it
		 * is more understandable for me, however, obviously, this can be modified to a
		 * preorder or an postorder traversal if necessary. 
		 * 
		 * This is a recursive method. What we do is we first travesal LEFT, until we hit
		 * a null left node. Then we concatenate into our string (called string). If we have
		 * traversed to the left-most node, we then do the same to the right-most. This is, again, 
		 * recursive, so we check Left, Data, Right, at each node. 
		 */
		private String inOrder(String string, Node head) {
			//traverse to left-most node on the tree
			if (head.left != null) 
				//recursively call the inOrder method
				string = inOrder(string, head.left);

			//reach a node where there is no more LEFT
			//thus adds the satellite data into our string
			string += " " + head.satellite;

			//traverse to the right nodes on the tree
			if (head.right != null) 
				//recursively call the inOrder method
				string = inOrder(string, head.right);

			//trims just in case there is an extra spaces
			return string.trim();
		}


		/**
		 * Search tree for key k.  Return its satellite data if found,
		 * and splay the found node.
		 * If not found, return null, and splay the would-be parent node.
		 */
		/*
		 * Description: 
		 * This method will search for the desired KEY. For this algorithm, 
		 * we call the private helper method SEARCH to return the place of the 
		 * desired node. If we can't find it, we return null and splay the would-be
		 * parent. But if we do find it, we return the node, which will possess the
		 * satellite data that we want to return. 
		 */
		public String lookup(Point key)
		{
			//returns the node.satellite, which should return the satellite data
			//of that key POINT.
			try{
				return search(key).satellite;
			}catch(Exception e){
				return null; //null check to prevent nullpointerexception
			}
			
		}

		/*
		 * Description: 
		 * This private helper method will search for the desired KEY. For this algorithm, 
		 * we set the ref to the root value and traverse the tree. Because a splay tree is a 
		 * BST, we can use the key value and compare it to the ref value. If it is less, we go left
		 * else we go right. If we ever get key == ref.key, that means we found the desired node
		 * and we can return the node. If we don't find it, based on the specs, we just return null;
		 */
		private Node search(Point key) {
			//sets ref node to root
			Node ref = root;

			//while loop to traverse the tree
			while(ref != null){
				//parent = ref.parent;
				//go right since key is larger than the ref value
				if(key.compareTo(ref.record) < 0){
					ref = ref.right;
				}

				//go left since key is smaller than the ref value
				else if (key.compareTo(ref.record) > 0){
					ref = ref.left;
				}

				//finds the ref.record ==  key! Returns ref node
				else{
					//splay(ref);
					return ref;
				}
			}
			
			//splay(parent);

			//can't find it, thus return null
			return null;
		}

		/**
		 * Insert a new record.
		 * First we do a search for the key.
		 * If the search fails, we insert a new record.
		 * Otherwise we update the satellite data with sat.
		 * Splay the new, or altered, node.
		 */
		/*
		 * Description: 
		 * Algorithm was already written out by Dr. Predoehl above. 
		 * So first, we search in the tree for the key value. If we can't
		 * find it, then we insert the value. 
		 * But if the key already exists, we just update the sat value. 
		 */
		public void insert_record(Point key, String sat)
		{
			//sets values. ref is root and the parentNode is null because root has
			//no parent
			Node ref = root;
			Node parentNode = null;

			//search the key
			while(ref != null){
				//throughout entire process, updates the parent of ref
				//so parent can still be accurately used
				parentNode = ref;

				//compares key values for insertion
				if(key.compareTo(parentNode.record) < 0)
					ref = ref.right;
				else if(key.compareTo(parentNode.record) > 0)
					ref = ref.left;
				else{
					ref.satellite = sat;
					return;
				}
			}

			//makes new node, insert data
			ref = new Node(key, sat);
			//throughout entire process, updates the parent of ref
			ref.parent = parentNode;

			//if parent is null, then ref is a root node
			if(parentNode == null){
				root = ref;
			}

			//else move right from parent is the key is bigger,
			//BST behavior
			else if(key.compareTo(parentNode.record) < 0){
				parentNode.right = ref;
			}

			//else move left from parent is the key is smaller,
			//BST behavior
			else if(key.compareTo(parentNode.record) > 0){
				parentNode.left = ref;
			}
			
			else{
				ref.satellite = sat;
				return;
			}
				

			//splay the new or altered node
			splay(ref);
		}

		/*
		 * Description:
		 * The head poncho of the SplayTree class. 
		 * What the splay does it is moves desired node, x, to the root of the splay tree. 
		 * Through reorganizing the structure of the tree while still keeping the organization
		 * of the BST and splay, there are THREE factors to consider
		 * 
		 * (wikipedia)
		 * FACTOR 1: Whether x is the left or right child of its parent node, p,
		 * FACTOR 2: Whether p is the root or not, and if not
		 * FACTOR 3: Whether p is the left or right child of its parent, g (the grandparent of x).
		 */
		private void splay(Node ref) {
			//null checks
			while(ref.parent != null){
				Node parent = ref.parent;
				Node gparent = parent.parent;
				
				//null checks
				/*ZIG STEP
				 * When the ref is the root, we rotate the tree. Comparing the ref with the parent's left
				 * and right nodes, we insert it to the correct location
				 */
				if(gparent == null){
					if(ref == parent.left)
						rotateLeft(ref, parent);

					else
						rotateRight(ref, parent);
				
				}
				
				/*
				 * ZIG-ZIG 
				 * parent is not the root and ref && parent are both right or both left children. 
				 * tree is rotated on the grandparnet-parent edge. and then rotated again on the 
				 * parent-ref edge. ref will now be the root of the tree. 
				 * or 
				 * ZIG-ZAG
				 * parent is not the root and ref && parent are are one left and one right child. 
				 * tree is rotated on the parent-ref edge. ref will now be the root of the tree.then it
				 * is rotated on the grandparent-ref edge to make it go in order
				 */
				else{

					if(ref == parent.left){
						if(parent == gparent.left){
							//ZIG-ZIG -> based on comparison key values
							rotateLeft(parent, gparent);
							rotateLeft(ref, parent);


						}
						else{
							//ZIG-ZAG -> based on comparison key values
							rotateLeft(ref, ref.parent);
							rotateRight(ref, ref.parent);
						}
					}

					//ref == parent.right
					else{
						if(parent == gparent.left){
							//ZIG-ZAG-> based on comparison key values
							rotateRight(ref, ref.parent);
							rotateLeft(ref, ref.parent);
						}
						else{
							//ZIG-ZIG -> based on comparison key values
							rotateRight(parent, gparent);
							rotateRight(ref, parent);
						}
					}
				}	
			}

			//sets root to ref since we already shifted the graphs
			root = ref;
		}

		/*
		 * Description: 
		 * Rotates tree and makes right child to the parent.This private
		 * helper method is necessary for the SPLAY method because it rotates the
		 * tree is a nice easy way. 
		 * Because we have 3 pointers (left, right, and parent), at each step
		 * we need to make sure to update the values
		 */
		private void rotateRight(Node ref, Node parent) {
			
			if(ref == null || parent == null || parent.right != ref || ref.parent != parent){
				return;
			}

			//null checks
			if(parent.parent != null){
				//sets the parent node's parent (gparent)'s left child (aunt/uncle?)
				//to the ref's value
				if(parent == parent.parent.left)
					parent.parent.left = ref;

				//sets the parent node's parent (gparent)'s right child (aunt/uncle?)
				//to the ref's value
				else
					parent.parent.right = ref;
			}

			//null checks
			//sets the parent value to ref.left's parents
			//sets left, not right due to BST comparison of Point values
			if(ref.left != null)
				ref.left.parent = parent;

			//sets, updates references of 3 pointers
			ref.parent = parent.parent;
			parent.parent = ref;
			parent.right = ref.left;
			ref.left = parent;

		}

		/*
		 * Description: 
		 * Rotates tree and makes left child to the parent.This private
		 * helper method is necessary for the SPLAY method because it rotates the
		 * tree is a nice easy way. 
		 * Because we have 3 pointers (left, right, and parent), at each step
		 * we need to make sure to update the values
		 */
		private void rotateLeft(Node ref, Node parent) {
			
			if(ref == null || parent == null || parent.left != ref || ref.parent != parent){
				return;
			}
			
			//null checks
			if(parent.parent != null){
				//sets the parent node's parent (gparent)'s left child (aunt/uncle?)
				//to the ref's value
				if(parent == parent.parent.left)
					parent.parent.left = ref;

				//sets the parent node's parent (gparent)'s right child (aunt/uncle?)
				//to the ref's value
				else
					parent.parent.right = ref;
			}

			//null checks
			//sets the parent value to ref.right's parents
			//sets right, not right due to BST comparison of Point values
			if(ref.right != null)
				ref.right.parent = parent;

			//sets, updates references of 3 pointers
			ref.parent = parent.parent;
			parent.parent = ref;
			parent.left = ref.right;
			ref.right = parent;

		}

		/**
		 * Remove a record.
		 * Search for the key.  If not found, return null.
		 * If found, save the satellite data, remove the record, and splay
		 * appropriately.  Use one of the two following methods and update
		 * this comment to document which method you actually use.
		 *
		 *   Appropriate method 2:  splay twice.
		 *   Find the node u with the key.  Splay u, then immediately splay
		 *   the successor of u.  Finally, remove u by splicing.
		 *
		 * Return the satellite data from the deleted node.
		 */
		public String delete(Point key)
		{
			//searches for the node that we want to delete
			String delete = lookup(key);
			Node deleteNode = search(key);

			//calls private helper method removeNode on deleteNode
			removeNode(deleteNode);
			
			try{
				//returns the satellite data of the deleted node. 
				return delete;
			}
			catch(Exception e){
				//null check for nullpointerexception errors
				return null;
			}

			
		}

		/*
		 * Description:
		 * This private helper method will remove the desired node. We are using Method
		 * 2 as since we splay first to find the desired node and then according to whether
		 * it has a left or right child, it goes from there. 
		 */
		private void removeNode(Node ref) {
			
			//null check
			if(ref == null)
				return;

			splay(ref);

			//there are both left and right children
			if(ref.left != null && ref.right != null){
				Node l = ref.left;
				while(l.right != null)
					l = l.right;

				//moves the pointer
				l.right = ref.right;
				ref.right.parent = l;
				ref.left.parent = null;
				root = ref.left;
				//splay(ref.left); //splays the new root

			}

			//ref.left is null, thus we move the ref.right as root
			else if(ref.right != null){
				ref.right.parent = null;
				root = ref.right;
				//splay(ref.right); //splays the new root
			}

			//ref.right is null, thus we move the ref.left as root
			else if(ref.left != null){
				ref.left.parent = null;
				root = ref.left;
				//splay(ref.left); //splays the new root
			}

			//if both ref.left and ref.right is null, thus 
			//it is a root node and we simply set the root as null
			else
				root = null;
			

		}
}


public class CityRoster {

    /** Print an error message and exit. */
    public static void exit(String message) {
        System.err.println("Error: " + message);
        System.exit(1);
    }

    /** Driver state machine for the roster dictionary. */
    public static void main(String[] argv)
        throws java.io.FileNotFoundException, java.io.IOException
    {
        // Dictionary
        SplayTree roster = new SplayTree();

        // Read from standard input
        BufferedReader br = new BufferedReader(
                                new InputStreamReader(System.in));

        // State machine states
        final int    KEYWORD = 0, XINS = 1, YINS = 2, COLOR = 3,
                    XQRY = 4, YQRY = 5, XDEL = 6, YDEL = 7;
        int state = KEYWORD, xcoord = 0, ycoord = 0;

        // Loop through the lines of input, and divide each line at whitespace:
        for (String line = br.readLine(); line != null; line = br.readLine())
            for (StringTokenizer tz = new StringTokenizer(line);
                    tz.hasMoreTokens(); ) {

                // Next coordinates to be handled
                final String token = tz.nextToken();

                if (KEYWORD == state) {
                    if (0 == token.compareTo("insert"))
                        state = XINS;
                    else if (0 == token.compareTo("query"))
                        state = XQRY;
                    else if (0 == token.compareTo("delete"))
                        state = XDEL;
                    else if (0 == token.compareTo("debug_print")) // optional
                        System.out.println("" + roster);
                    else
                        exit("bad command " + token);
                } else if (XINS == state || XQRY == state || XDEL == state) {
                    xcoord = Integer.parseInt(token);
                    state += 1;
                } else if (YINS == state) {
                    ycoord = Integer.parseInt(token);
                    state = COLOR;
                } else if (COLOR == state) {
                    state = KEYWORD;
                    assert token.length() > 0;
                    roster.insert_record(new Point(xcoord, ycoord), token);
                } else if (YQRY == state) {
                    state = KEYWORD;
                    ycoord = Integer.parseInt(token);
                    String color = roster.lookup(new Point(xcoord, ycoord));
                    System.out.println("query: "
                        + (null == color ? "not found" : color));
                } else if (YDEL == state) {
                    state = KEYWORD;
                    ycoord = Integer.parseInt(token);
                    String color = roster.delete(new Point(xcoord, ycoord));
                    System.out.println("delete: "
                        + (null == color ? "not found" : color));
                } else
                    exit("bad state " + state);
            }
    }
}