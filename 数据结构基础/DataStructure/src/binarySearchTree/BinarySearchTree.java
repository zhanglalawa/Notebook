package binarySearchTree;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class BinarySearchTree<E extends Comparable<E>> {
	class Node {
		public Node left, right;
		public E e;

		public Node(E e) {
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

	// ��BST�������Ԫ��e
	public void add(E e) {
		/*
		 * if (root == null) { root = new Node(e); } else { add(root, e); }
		 */

		treeRoot = add(treeRoot, e);
	}

	// ��һ�ֵݹ�д�����Ե�ʮ��ӷ��
	private void addFirstEdition(Node root, E e) {
		// �ݹ���ֹ����

		// ����ڵ�ֵ��� ��Ϊ��һ���ڵ㣬�Ͳ��ٵݹ���
		// ʣ�µ��������������ݹ鵽��
		if (root.e.equals(e)) {
			return;
		} else if (e.compareTo(root.e) > 0 && root.right == null) {
			root.right = new Node(e);
			size++;
			return;
		} else if (e.compareTo(root.e) < 0 && root.left == null) {
			root.left = new Node(e);
			size++;
			return;
		}

		if (e.compareTo(root.e) > 0) {
			addFirstEdition(root.right, e);
		} else {
			addFirstEdition(root.left, e);
		}
	}

	// �ڶ��ֵݹ�д��
	// �����÷���������һ���ڵ�
	// �������ڵ���ǿյģ��Ǿ��½�һ���ڵ㷵���� �ݹ����
	// �����Ϊ�գ��Ǿ���Ҫ�����ݹ�
	// Ҫô��ҪôС��Ҫô���� ���ڲ�������
	// ���ѱ��ڵ�return��ȥ��
	private Node add(Node root, E e) {
		if (root == null) {
			size++;
			return new Node(e);
		}

		if (e.compareTo(root.e) > 0) {
			root.right = add(root.right, e);
		} else if (e.compareTo(root.e) < 0) {
			root.left = add(root.left, e);
		}
		return root;
	}

	// �ǵݹ�д��
	private void addNotRecursively(Node root, E e) {
		Node curr = root;
		while (curr != null && e.compareTo(curr.e) != 0) {
			if (e.compareTo(curr.e) > 0) {
				curr = curr.right;
			} else {
				curr = curr.left;
			}
		}
		if (curr == null) {
			curr = new Node(e);
			size++;
		}
	}

	public boolean contains(E e) {
		return contains(treeRoot, e);
	}

	private boolean contains(Node root, E e) {
		if (root == null) {
			return false;
		}

		if (e.compareTo(root.e) == 0) {
			return true;
		} else if (e.compareTo(root.e) > 0) {
			return contains(root.right, e);
		} else {
			return contains(root.left, e);
		}
	}

	public void preOrder() {
		preOrder(treeRoot);
	}

	// �������
	private void preOrder(Node root) {
		if (root == null) {
			return;
		}

		System.out.println(root.e);
		preOrder(root.left);
		preOrder(root.right);
	}

	public void inOrder() {
		inOrder(treeRoot);
	}

	// ��������Ľ��ʵ�������������˳������
	private void inOrder(Node root) {
		if (root == null) {
			return;
		}

		inOrder(root.left);
		System.out.println(root.e);
		inOrder(root.right);
	}

	public void postOrder() {
		postOrder(treeRoot);
	}

	// �������
	private void postOrder(Node root) {
		if (root == null) {
			return;
		}

		postOrder(root.left);
		postOrder(root.right);
		System.out.println(root.e);
	}

	// �ǵݹ�д����ʵ��������һ��ջȥģ��ϵͳջ
	// �Ȱ�����ѹջ
	// ������һ���ڵ�
	// ���ΰѽڵ����������������ѹ��ջ��
	// ע��һ������ѹ����ѹ��
	// ֱ�����ջ��Ϊֹ
	private void preOrderNotRecursively(Node root) {
		Stack<Node> stack = new Stack<>();
		stack.push(root);

		while (!stack.isEmpty()) {
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

	public void levelOrder() {
		levelOrder(treeRoot);
	}

	// �������
	private void levelOrder(Node treeRoot) {
		Queue<Node> queue = new LinkedList<>();
		queue.offer(treeRoot);
		while (!queue.isEmpty()) {
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

	// ֻ��Ҫһֱ�������ߵ���ͷ�Ϳ��Ի�ȡ���ֵ
	public E getMaxNotRecursively() {
		Node node = treeRoot;
		while (node.right != null) {
			node = node.right;
		}

		return node.e;
	}

	// ֻ��Ҫһֱ������
	public E getMinNotRecursively() {
		Node node = treeRoot;
		while (node.left != null) {
			node = node.left;
		}
		return node.e;
	}

	public E getMax() {
		return getMax(treeRoot);
	}
	public E getMax(Node root) {
		if (root.right == null) {
			return root.e;
		}
		
		return getMax(root.right);
	}
	
	public E getMin() {
		return getMin(treeRoot);
	}
	public E getMin(Node root) {
		if (root.left == null) {
			return root.e;
		}
		
		return getMin(root.left);
	}
	public String toString() {
		StringBuilder res = new StringBuilder();
		generateBSTString(treeRoot, 0, res);
		return res.toString();
	}

	private void generateBSTString(Node root, int depth, StringBuilder res) {
		if (root == null) {
			res.append(generateBSTString(depth) + "null\n");
			return;
		}

		res.append(generateBSTString(depth) + root.e + '\n');
		generateBSTString(root.left, depth + 1, res);
		generateBSTString(root.right, depth + 1, res);
	}

	private String generateBSTString(int depth) {
		StringBuilder res = new StringBuilder();
		for (int i = 0; i < depth; i++) {
			res.append('-');
		}
		return res.toString();
	}

	public static void main(String args[]) {
		BinarySearchTree<Integer> binarySearchTree = new BinarySearchTree<>();
		int[] nums = { 5, 3, 6, 8, 4, 2 };
		for (int num : nums) {
			binarySearchTree.add(num);
		}
		// binarySearchTree.preOrderNotRecursively(binarySearchTree.treeRoot);
		System.out.println(binarySearchTree.getMax());
		System.out.println(binarySearchTree.getMin());
		System.out.println(binarySearchTree);
	}

}
