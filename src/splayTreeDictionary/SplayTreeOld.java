package splayTreeDictionary;

/**
 * Splay tree (BST invented by Sleator and Tarjan).
 */
public class SplayTreeOld
{
	// tbd
	Node root = null; 

	/** Portray tree as a string.  Optional but recommended. */
	public String toString()
	{
		//in order traversal
		String str = inOrder("", root);

		return str;
		// tbd
	}

	//helper method that will return the string representation of an inorder 
	//traversal of a splay tree
	private String inOrder(String string, Node head) {
		if (head.left != null) 
			string = inOrder(string, head.left);
		string += " " + head.satellite;
		if (head.right != null) 
			string = inOrder(string, head.right);

		return string.trim();
	}


	/**
	 * Search tree for key k.  Return its satellite data if found,
	 * and splay the found node.
	 * If not found, return null, and splay the would-be parent node.
	 */
	public String lookup(Point key)
	{
		// tbd
		String satelliteStr = search(root, key).satellite;
		return satelliteStr;
	}

	private Node search(Node ref, Point key) {
		//null checks to prevent a nullpointerexception
		if(ref == null)
			return null;

		//traversing the left subtree
		if(key.compareTo(ref.record) < 0){

			//if we reach a null, we have reached the end of the tree and the key desired key is not found. 
			//if it is not found, then we return NULL
			//we are done
			if(ref.left == null){
				return ref;
			}

			if(key.compareTo(ref.left.record) < 0){
				ref.left.left = search(ref.left.left, key);
				ref = rotateRight(ref);
			}
			else if(key.compareTo(ref.left.record) > 0){
				ref.left.right = search(ref.left.right, key);
				if(ref.left.right != null)
					ref.left = rotateLeft(ref.left);
			}

			if(ref.left == null)
				return ref;

			else
				return rotateRight(ref);
		}

		else if(key.compareTo(ref.record) > 0){
			//if we reach a null, we have reached the end of the tree and the key desired key is not found. 
			//if it is not found, then we return NULL
			//we are done
			if(ref.right == null){
				return ref;
			}

			if(key.compareTo(ref.right.record) < 0){
				ref.right.left = search(ref.right.left, key);
				if(ref.right.left != null)
					ref.right = rotateRight(ref.right);
			}
			else if(key.compareTo(ref.right.record) > 0){
				ref.right.right = search(ref.right.right, key);
				ref = rotateLeft(ref);
			}

			if(ref.right == null)
				return ref;

			else
				return rotateLeft(ref);

		}
		else
			return ref;

	}

	//rotate right
	private Node rotateRight(Node ref) {

		Node ref2 = ref.left;
		ref.left = ref2.right;
		ref2.right = ref;
		return ref2;
	}

	//rotate left
	private Node rotateLeft(Node ref) {

		Node ref2 = ref.right;
		ref.right = ref2.left;
		ref2.left = ref;
		return ref2;
	}

	/**
	 * Insert a new record.
	 * First we do a search for the key.
	 * If the search fails, we insert a new record.
	 * Otherwise we update the satellite data with sat.
	 * Splay the new, or altered, node.
	 */
	public void insert_record(Point key, String sat)
	{
		// tbd


		if(root == null){
			root = new Node(key, sat); 
			return;
		}

		root = search(root, key);
		System.out.println(this.toString());
		if(key.compareTo(root.record) > 0){
			System.out.println("right");
			Node newNode = new Node(key, sat);
			newNode.left = root.left;
			newNode.right = root;
			root.left = null;
			root = newNode;
		}

		else if(key.compareTo(root.record) < 0){
			System.out.println("left");
			Node newNode = new Node(key, sat);
			newNode.right = root.right;
			newNode.left = root;
			root.right = null;
			root = newNode;
		}

		else{
			root.satellite = sat;
		}
	}

	/**
	 * Remove a record.
	 * Search for the key.  If not found, return null.
	 * If found, save the satellite data, remove the record, and splay
	 * appropriately.  Use one of the two following methods and update
	 * this comment to document which method you actually use.
	 *
	 * Appropriate method 1:  splay the bereaved parent.
	 *   some node x will be deleted, so splay the parent of x.
	 * Appropriate method 2:  splay twice.
	 *   Find the node u with the key.  Splay u, then immediately splay
	 *   the successor of u.  Finally, remove u by splicing.
	 *
	 * Return the satellite data from the deleted node.
	 */
	public String delete(Point key)
	{
		// tbd
		if(root == null)
			return null;

		root = search(root, key);
		String data = root.satellite;

		if(key.compareTo(root.record) == 0){
			if(root.left == null)
				root = root.left;

			else{
				Node ref= root.right;
				root = root.left;
				search(root, key);
				root.right = ref;
			}
		}

		return data;
	}
}
