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
	 * ɾ����rootΪ�������������ڵ�
	 * ����ɾ�����ڵ��������ĸ�
	 * ɾ���Ĺ�����һֱ�����ߣ��ҵ����ҽڵ㣬�øýڵ������ֱ�Ӵ�����
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
	 * ɾ����rootΪ���������е���С�ڵ�
	 * ����ɾ����С�ڵ��������ĸ�
	 * ɾ���Ĺ�����һֱ�����ߣ��ҵ�����ڵ㣬�øýڵ���Һ��Ӵ�����
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
	 * ɾ����rootΪ���ڵ��������ֵΪe�Ľڵ�
	 * ����ɾ��Ŀ�Ľڵ��������ĸ�
	 * ɾ���Ĺ��̾��Ǹ���e��ֵ�͵�ǰ�ڵ��ֵ�ıȽϽ��
	 * �����������ȥ�ߣ�ֱ���ҵ�Ŀ�Ľڵ�
	 * ��ʱ�������������
	 * 1.������Ϊ�գ��Ǿ�ֱ���������Һ���ȥ��������ڵ�
	 * 2.������Ϊ�գ��Ǿ�ֱ������������ȥ��������ڵ�
	 * 3.������������Ϊ�գ����������ĺ�̽ڵ�successor��
	 * Ҳ�����������е���С�ڵ㣬������ڵ�����������������
	 * ǰ���ڵ㣬�������е����ڵ㣬������ڵ�����������,��Ҫ
	 * �ǵİѺ�̣�ǰ�����ڵ�ɾ������
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
		
		//������������Ϊ��,�ҵ���̽ڵ㡪���������е���Сֵ
		Node successor = getMin(root.right);
		
		successor.right = removeMin(root.right);
		successor.left = root.left;
		
		root.left = null;
		root.right = null;
		
		
		return successor;
	}
}
