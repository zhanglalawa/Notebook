package set;
/*
 * 我们基于二分搜索树实现一个集合
 */

import binarySearchTree.BinarySearchTree;

public class BSTSet<E extends Comparable<E>> implements Set<E> {

	private BinarySearchTree<E> bst;

	public BSTSet() {
		bst = new BinarySearchTree<>();
	}

	@Override
	public void add(E e) {
		bst.add(e);
	}

	@Override
	public void remove(E e) {
		bst.removeNode(e);
	}

	@Override
	public boolean contains(E e) {
		return bst.contains(e);
	}

	@Override
	public int getSize() {
		return bst.size();
	}

	@Override
	public boolean isEmpty() {
		return bst.isEmpty();
	}
	
	
}
