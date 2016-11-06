package splayTreeDictionary;

/*
 * Splay tree (BST invented by Sleator and Tarjan).
 * 
 * Katie Pan
 * CSC 345 Splay Tree
 * Project 4 -> Splay Tree Dictionary
 * Due Monday Nov 14, 2016
 * 
 * A splay tree is a self-adjusting binary search tree with the additional property 
 * that recently accessed elements are quick to access again. It performs basic operations 
 * such as insertion, look-up and removal in O(log n) time.
 * 
 * Using the operation, SPLAY, it rearranges the tree so the desired element that we want, say x, 
 * is placed at the root of the splay tree. 
 * 
 * This class is implemented in the CityRoster class. Has the basic operations of printing
 * out the toString (inorder format), insertion, search, and deletion. A more detailed algorithm 
 * for each method will be explained prior to each method. In this project, we are asked to implement 
 * a Dictionary ADT using a SPLAYTREE. We will store a key (in a Point object, thus coordinates) and 
 * a value (in a String object). 
 * 
 * Splay trees have advantages and disadvantages. For example, because of the method of "splay"ing,
 * the desired node can be accessed easily because it is at the root. However, it is also bad because
 * lets just say the desire node you want to remove is the largest or the smallest, that means the tree
 * can be reorganized as a linkedlist, providing a linear runtime
 */
public class SplayTree
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
				splay(ref);
				return ref;
			}
		}

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
			else
				ref = ref.left;
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
		else
			parentNode.left = ref;

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
			//null checks
			if(gparent != null){

				if(ref == parent.left){
					if(parent == gparent.left){
						//ZIG-ZIG -> based on comparison key values
						leftparent(parent, gparent);
						leftparent(ref, parent);


					}
					else{
						//ZIG-ZAG -> based on comparison key values
						leftparent(ref, ref.parent);
						rightRotate(ref, ref.parent);
					}
				}

				//ref == parent.right
				else{
					if(parent == gparent.left){
						//ZIG-ZAG-> based on comparison key values
						rightRotate(ref, ref.parent);
						leftparent(ref, ref.parent);
					}
					else{
						//ZIG-ZIG -> based on comparison key values
						rightRotate(parent, gparent);
						rightRotate(ref, parent);
					}
				}

			}

			/*ZIG STEP
			 * When the ref is the root, we rotate the tree. Comparing the ref with the parent's left
			 * and right nodes, we insert it to the correct location
			 */
			else{

				if(ref == parent.left)
					leftparent(ref, parent);

				else
					rightRotate(ref, parent);
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
	private void rightRotate(Node ref, Node parent) {

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
	private void leftparent(Node ref, Node parent) {
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
		Node deleteNode = search(key);

		//calls private helper method removeNode on deleteNode
		removeNode(deleteNode);
		
		try{
			//returns the satellite data of the deleted node. 
			return deleteNode.satellite;
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
			splay(ref.left); //splays the new root

		}

		//ref.left is null, thus we move the ref.right as root
		else if(ref.right != null){
			ref.right.parent = null;
			splay(ref.right); //splays the new root
		}

		//ref.right is null, thus we move the ref.left as root
		else if(ref.left != null){
			ref.left.parent = null;
			splay(ref.left); //splays the new root
		}

		//if both ref.left and ref.right is null, thus 
		//it is a root node and we simply set the root as null
		else
			root = null;

	}
}
