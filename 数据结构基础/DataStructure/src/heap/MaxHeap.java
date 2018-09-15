package heap;

import array.Array;

/*
 * 主要关注的是shiftUp和shiftDown操作
 */
public class MaxHeap<E extends Comparable<E>> {
	private Array<E> data;

	public MaxHeap() {
		data = new Array<>();
	}

	public MaxHeap(int capacity) {
		data = new Array<>(capacity);
	}

	// Heapify从最后一个非叶子节点到树根依次shiftDown
	// O(n)
	public MaxHeap(E[] array) {
		data = new Array<>(array);
		for (int i = parent(size() - 1); i >= 0; i--) {
			shiftDown(i);
		}
	}

	public int size() {
		return data.getSize();
	}

	public boolean isEmpty() {
		return data.isEmpty();
	}

	// 返回index索引的父亲
	public int parent(int index) {
		return (index - 1) / 2;
	}

	// 右孩子
	public int rightChild(int index) {
		return 2 * index + 2;
	}

	// 左孩子
	public int leftChild(int index) {
		return 2 * index + 1;
	}

	// 添加过程是先在数组最后添加新元素，然后为了保持堆的特性，要对新元素进行shiftUp操作
	public void add(E e) {
		data.addLast(e);
		shiftUp(size() - 1);
	}

	private void shiftUp(int index) {
		while (index > 0 && data.get(index).compareTo(data.get(parent(index))) > 0) {
			data.swap(index, parent(index));
			index = parent(index);
		}
	}

	public E findMax() {
		if (size() == 0) {
			throw new IllegalArgumentException("size = 0");
		}
		return data.get(0);
	}

	// 拿出最大数据的过程是先把索引为0处的数据拿出来，然后把最后一个索引的数据放在0处，然后给新放在0处的数据shiftDown
	public E extractMax() {
		E ret = findMax();
		data.swap(0, size() - 1);
		data.removeLast();

		shiftDown(0);
		return ret;
	}

	private void shiftDown(int index) {
		while (leftChild(index) < size()) {
			/*
			 * if(data.get(index).compareTo(data.get(leftChild(index)))<0) { if
			 * (rightChild(index)<size()&&data.get(leftChild(index)).compareTo(data.get(
			 * rightChild(index)))<0) { data.swap(index, rightChild(index)); index =
			 * rightChild(index); }else { data.swap(index, leftChild(index)); index =
			 * leftChild(index); } } else if (rightChild(index) < size() &&
			 * data.get(index).compareTo(data.get(rightChild(index)))<0) { data.swap(index,
			 * rightChild(index)); index = rightChild(index); } else { break; }
			 * 做的有点复杂了。可以先获取右孩子左孩子哪个大，然后再和大的比较，如果比大的还大，那就break，如果比大的小，那就再swap，这样逻辑清晰了很多
			 */

			int j = leftChild(index);
			if (j + 1 < size() && data.get(j + 1).compareTo(data.get(j)) > 0) {
				j = rightChild(index);// j++
			}

			if (data.get(index).compareTo(data.get(j)) > 0) {
				break;
			}

			data.swap(index, j);
			index = j;
		}
	}
}
