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

	// 向BST中添加新元素e
	public void add(E e) {
		/*
		 * if (root == null) { root = new Node(e); } else { add(root, e); }
		 */

		treeRoot = add(treeRoot, e);
	}

	// 第一种递归写法，显得十分臃肿
	private void addFirstEdition(Node root, E e) {
		// 递归终止条件

		// 如果节点值相等 认为是一个节点，就不再递归了
		// 剩下的两种情况是如果递归到底
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

	// 第二种递归写法 方法返回加入后的新的二分搜索树的根
	// 我们让方法进入下一个节点
	// 如果这个节点就是空的，那就新建一个节点返回它 递归结束
	// 如果不为空，那就需要继续递归
	// 要么大要么小，要么等于 等于不作处理
	// 最后把本节点return回去。
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
	
	// 非递归写法
	private Node addNotRecursively(Node root, E e) {
		if (root == null) {
			size++;
			return new Node(e);
		}

		Node prev = root;
		while (e.compareTo(prev.e) != 0) {
			if (e.compareTo(prev.e) > 0) {
				if (prev.right != null) {
					prev = prev.right;
				} else {
					prev.right = new Node(e);
					size++;
					break;
				}
			} else {
				if (prev.left != null) {
					prev = prev.left;
				} else {
					prev.left = new Node(e);
					size++;
					break;
				}
			}
		}

		return root;
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

	// 先序遍历
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

	// 中序遍历的结果实质上是树里面的顺序排列
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

	// 后序遍历
	private void postOrder(Node root) {
		if (root == null) {
			return;
		}

		postOrder(root.left);
		postOrder(root.right);
		System.out.println(root.e);
	}

	// 非递归写法，实际上是用一个栈去模拟系统栈
	// 先把树根压栈
	// 访问完一个节点
	// 依次把节点的右子树和左子树压入栈中
	// 注意一定是先压右再压左！
	// 直到最后栈空为止
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

	// 层序遍历
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

	// 只需要一直往右走走到尽头就可以获取最大值
	public E getMaxNotRecursively() {
		Node node = treeRoot;
		while (node.right != null) {
			node = node.right;
		}

		return node.e;
	}

	// 只需要一直往左走
	public E getMinNotRecursively() {
		Node node = treeRoot;
		while (node.left != null) {
			node = node.left;
		}
		return node.e;
	}

	// 下面是递归写法
	public E getMax() {
		if (size == 0) {
			throw new IllegalArgumentException("BST is empty");
		}
		return getMax(treeRoot).e;
	}

	public Node getMax(Node root) {
		if (root.right == null) {
			return root;
		}

		return getMax(root.right);
	}

	public E getMin() {
		if (size == 0) {
			throw new IllegalArgumentException("BST is empty");
		}
		return getMin(treeRoot).e;
	}

	public Node getMin(Node root) {
		if (root.left == null) {
			return root;
		}

		return getMin(root.left);
	}

	// 从二分搜索树中删除最小结点
	// 返回删最小值
	public E removeMin() {
		E ret = getMin();
		treeRoot = removeMin(treeRoot);
		return ret;
	}

	// 从二分搜索树中删除最小节点
	// 返回删除结点之后的二分搜索树的根
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

	// 从二分搜索树中删除最大结点
	// 返回最大值
	public E removeMax() {
		E ret = getMax();
		treeRoot = removeMax(treeRoot);
		return ret;
	}

	// 从二分搜索树中删除最大结点
	// 返回删之后的二分搜索树的根
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

	// 删除指定值的节点
	public void removeNode(E e) {
		treeRoot = removeNode(treeRoot, e);
	}

	// 删除以root为根的树中值为e的节点
	// 并且返回删除后的树的根节点
	private Node removeNode(Node root, E e) {
		if (root == null) {
			return null;
		}
		
		if (e.compareTo(root.e) > 0) {
			root.right = removeNode(root.right, e);
			return root;
		}

		if (e.compareTo(root.e) < 0) {
			root.left = removeNode(root.left, e);
			return root;
		}

		// 下面是如果值和节点的值相等，那就要进行删除操作了

		// 最简单的两种情况
		// 如果左孩子为空，就获取它的右孩子，让要删除的这个节点和整棵树脱离关系
		// 然后返回它的右孩子
		if (root.left == null) {
			Node rightNode = root.right;
			root.right = null;
			size--;
			return rightNode;
		}

		// 如果有孩子为空，则同理，这里和我们removeMax removeMin 极其相似
		if (root.right == null) {
			Node leftNode = root.left;
			root.left = null;
			size--;
			return leftNode;
		}

		// 最后是左右孩子都不为空，这是最麻烦的一种
		// 我们需要找到待删除节点的右子树中的最小节点（又或者说是待删除节点左子树的最大结点）
		// 只有上面两种结点是符合要求的，我们用他们来替换这个待删除节点
		
		Node successor = getMin(root.right);//这里我们找右子树的最小节点，就是替换者
		successor.left = root.left;//替换当然是让这个最小节点的左孩子等于原来节点的左孩子，右孩子等于原来节点的右孩子，左孩子直接赋值
		//右孩子我们当然是需要得到已经删除掉这个替换着的右子树啊，所以有用到我们的removeMin方法
		successor.right = removeMin(root.right);
		
		//当然我们还需要把原来的节点和我们的二叉树脱离关系
		root.left = null;
		root.right = null;
		
		//最后还有一个问题，我们是否需要维护size--，答曰不用，因为removeMin里面已经进行了删除操作了！我们的工作只是替换，所以不能再次进行size--
		return successor;
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
		int[] nums = { 3, 6, 8, 4, 2 };
		for (int num : nums) {
			binarySearchTree.add(num);
		}
		System.out.println(binarySearchTree);
		// binarySearchTree.preOrderNotRecursively(binarySearchTree.treeRoot);
		System.out.println(binarySearchTree.getMax());
		System.out.println(binarySearchTree.getMin());

	}

}
