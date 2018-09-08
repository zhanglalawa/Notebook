package map;

public class BSTMap<K extends Comparable<K>, V> implements Map<K, V> {

	private class Node {
		public Node left, right;
		public K key;
		public V value;

		public Node(K key, V value) {
			this.key = key;
			this.value = value;
			left = null;
			right = null;
		}

	}

	private Node treeRoot;
	private int size;

	public BSTMap() {
		treeRoot = null;
		size = 0;
	}

	@Override
	public void add(K key, V value) {
		treeRoot = add(treeRoot, key, value);
	}

	// ����µļ�ֵ��
	// ��������½ڵ�֮�����ĸ�
	private Node add(Node root, K key, V value) {
		if (root == null) {
			size++;
			return new Node(key, value);
		}

		if (key.compareTo(root.key) > 0) {
			root.right = add(root.right, key, value);
		} else if (key.compareTo(root.key) < 0) {
			root.left = add(root.left, key, value);
		} else {
			root.value = value;
		}
		return root;
	}

	@Override
	public V remove(K key) {
		Node node = getNode(treeRoot, key);
		if (node == null) {
			throw new IllegalArgumentException("No such node");
		}
		treeRoot = remove(treeRoot, key);
		return node.value;
	}

	// ɾ��ָ��key�Ľڵ�
	// ����ɾ��ָ���ڵ������ĸ�
	public Node remove(Node root, K key) {
		if (root == null) {
			throw new IllegalArgumentException("No such node");
		}
		if (key.compareTo(root.key) > 0) {
			root.right = remove(root.right, key);
			return root;
		}else if (key.compareTo(root.key)<0) {
			root.left = remove(root.left, key);
			return root;
		}else {
			if (root.right == null) {
				Node leftNode = root.left;
				root.left = null;
				return leftNode;
			}
			
			if (root.left ==null) {
				Node rightNode = root.right;
				root.right = null;
				return rightNode;
			}
			
			
			Node successor = getMin(root.right);
			successor.right = removeMin(root.right);
			successor.left = root.left;
			root.left = root.right = null;
			return successor;
		}
		
	}

	// ɾ����rootΪ�������е���С�ڵ�
	// ����ɾ���ڵ���µĶ����������ĸ�
	private Node removeMin(Node root) {
		if (root.left == null) {
			Node rightNode = root.right;
			root.right = null;
			size--;
			return rightNode;
		}

		root.left = removeMin(root.left);
		return root;
	}

	// ɾ����rootΪ�������е����ڵ�
	// ����ɾ���ڵ���µĶ����������ĸ�
	private Node removeMax(Node root) {
		if (root.right == null) {
			Node leftNode = root.left;
			root.left = null;
			size--;
			return leftNode;
		}

		root.right = removeMax(root.right);
		return root;
	}

	private Node getMin(Node root) {
		if (root.left == null) {
			return root;
		}

		return getMin(root.left);
	}

	private Node getMax(Node root) {
		if (root.right == null) {
			return root;
		}

		return getMax(root.right);
	}

	@Override
	public boolean contains(K key) {
		return getNode(treeRoot, key) == null;
	}

	@Override
	public V get(K key) {
		Node node = getNode(treeRoot, key);
		return node == null ? null : node.value;
	}

	@Override
	public void set(K key, V newValue) {
		Node node = getNode(treeRoot, key);
		if (node == null) {
			throw new IllegalArgumentException("no such node");
		}
		node.value = newValue;
	}

	@Override
	public int getSize() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	private Node getNode(Node node, K key) {
		if (node == null) {
			return null;
		}

		if (key.compareTo(node.key) == 0) {
			return node;
		}

		if (key.compareTo(node.key) > 0) {
			node.right = getNode(node.right, key);
		} else {
			node.left = getNode(node.left, key);
		}
		return node;
	}
}
