package fuck;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Stack;

public class BinarySearchTree<E extends Comparable<E>> {
	class Node{
		public Node left,right;
		public E e;
		
		public Node(E e){
			this.e = e;
			left = null;
			right = null;
		}
	}
	
	private Node treeRoot;
	private int size;
	
	public BinarySearchTree() {
		treeRoot = null;
		size = 0;
	}
	
	public int size() {
		return size;
	}
	
	public boolean isEmpty() {
		return size == 0;
	}
	
	public void add(E e) {
		treeRoot = add(treeRoot, e);
	}
	
	private Node add(Node root, E e) {
		if (root == null) {
			size++;
			return new Node(e);
		}
		
		if (root.e.compareTo(e) == 0) {
			return root;
		}
		
		if (e.compareTo(root.e)>0) {
			root.right = add(root.right, e);
		}else {
			root.left = add(root.left, e);
		}
		
		return root;
	}
	
	public boolean contains(E e) {
		return contains(treeRoot,e);
	}
	
	private boolean contains(Node root, E e) {
		if (root == null) {
			return false;
		}
		
		if (root.e.compareTo(e)==0) {
			return true;
		}
		
		if (e.compareTo(root.e) > 0) {
			return contains(root.right,e);
		} else {
			return contains(root.left, e);
		}
	}
	
	public void preOrder() {
		System.out.println("preOrder:");
		preOrder(treeRoot);
	}
	
	private void preOrder(Node root) {
		if (root == null) {
			return ;
		}
		
		System.out.println(root.e);
		preOrder(root.left);
		preOrder(root.right);
	}
	
	private void preOrderNotRecursively(Node root) {
		if (root == null) {
			return ;
		}
		
		Stack<Node> stack = new Stack<>();
		
		stack.push(root);
		
		while(!stack.isEmpty()) {
			Node node = stack.pop();
			
			System.out.println(node.e);
			
			if (node.right != null) {
				stack.push(node.right);
			}
			
			if (node.left != null) {
				stack.push(node.left);
			}
		}
	}
	
	public void inOrder() {
		System.out.println("inOrder:");
		inOrder(treeRoot);
	}
	
	private void inOrder(Node root) {
		if (root == null) {
			return ;
		}
		
		inOrder(root.left);
		System.out.println(root.e);
		inOrder(root.right);	
	}
	
	public void postOrder() {
		System.out.println("postOrder:");
		postOrder(treeRoot);
	}
	
	private void postOrder(Node root) {
		if (root == null) {
			return ;
		}
		
		postOrder(root.left);
		postOrder(root.right);
		System.out.println(root.e);
	}
	
	
	public void levelOrder() {
		System.out.println("leverOrder:");
		levelOrder(treeRoot);
	}
	
	private void levelOrder(Node root) {
		if (root == null) {
			return ;
		}
		
		Queue<Node> queue = new LinkedList<>();
		
		queue.offer(root);
		
		while(!queue.isEmpty()) {
			Node node = queue.poll();
			
			System.out.println(node.e);
			
			if (node.left != null) {
				queue.offer(node.left);
			}
			
			if (node.right != null) {
				queue.offer(node.right);
			}
		}
	}
	
	public E getMax() {
		if (isEmpty()) {
			throw new NullPointerException();
		}
		return getMax(treeRoot).e;
	}
	private Node getMax(Node root) {
		if (root.right == null) {
			return root;
		}
		
		return getMax(root.right);
	}
	
	private Node getMaxNotRecursively(Node root) {
		if (root == null) {
			return null;
		}
		
		while(root.right != null) {
			root = root.right;
		}
		
		return root;
	}
	
	public E getMin() {
		if (isEmpty()) {
			throw new NullPointerException();
		}
		return getMin(treeRoot).e;
	}
	
	private Node getMin(Node root) {
		if(root.left == null) {
			return root;
		}
		
		return getMin(root.left);
	}
	
	private Node getMinNotRecursively(Node root) {
		if (root == null) {
			return null;
		}
		
		while(root.left != null) {
			root = root.left;
		}
		
		return root;
	}
	
	public E removeMax() {
		E ret = getMax();
		treeRoot = removeMax(treeRoot);
		return ret;
	}
	
	/*
	 * 删除以root为根的子树的最大节点
	 * 返回删除最大节点后的子树的根
	 * 删除的过程是一直往右走，找到最右节点，用该节点的左孩子直接代替它
	 */
	private Node removeMax(Node root) {
		if (root.right == null) {
			Node leftNode = root.left;
			root.left = null;
			return leftNode;
		}
		
		root.right = removeMax(root.right);
		return root;
	}
	
	public E removeMin() {
		E ret = getMin();
		treeRoot = removeMin(treeRoot);
		return ret;
	}
	
	/*
	 * 删除以root为根的子树中的最小节点
	 * 返回删除最小节点后的子树的根
	 * 删除的过程是一直往左走，找到最左节点，用该节点的右孩子代替它
	 */
	private Node removeMin(Node root) {
		if (root.left == null) {
			Node rightNode = root.right;
			root.right = null;
			return rightNode;
		}
		
		root.left = removeMin(root.left);
		return root;
	}
	
	public void removeNode(E e) {
		treeRoot = removeNode(treeRoot, e);
	}
	
	/*
	 * 删除以root为根节点的子树中值为e的节点
	 * 返回删除目的节点后的子树的根
	 * 删除的过程就是根据e的值和当前节点的值的比较结果
	 * 向左或者向右去走，直到找到目的节点
	 * 这时候有三种情况。
	 * 1.左子树为空，那就直接用它的右孩子去代替这个节点
	 * 2.右子树为空，那就直接用它的左孩子去代替这个节点
	 * 3.左右子树都不为空，我们找它的后继节点successor，
	 * 也就是右子树中的最小节点，用这个节点来代替它（或者找
	 * 前驱节点，左子树中的最大节点，用这个节点来代替它）,还要
	 * 记的把后继（前驱）节点删除掉。
	 */
	private Node removeNode(Node root, E e) {
		
		if (root == null) {
			return null;
		}
		
		if (e.compareTo(root.e)>0) {
			root.right = removeNode(root.right,e);
			return root;
		}
		
		if (e.compareTo(root.e)<0) {
			root.left = removeNode(root.left, e);
			return root;
		}
		
		if (root.left == null) {
			Node rightNode = root.right;
			root.right = null;
			size--;
			return rightNode;
		}
		
		if (root.right == null) {
			Node leftNode = root.left;
			root.left = null;
			size--;
			return leftNode;
		}
		
		//左右子树都不为空,找到后继节点——右子树中的最小值
		Node successor = getMin(root.right);
		
		successor.right = removeMin(root.right);
		successor.left = root.left;
		
		root.left = null;
		root.right = null;
		
		
		return successor;
	}
}
