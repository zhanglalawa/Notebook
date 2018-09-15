package heap;

import array.Array;

/*
 * ��Ҫ��ע����shiftUp��shiftDown����
 */
public class MaxHeap<E extends Comparable<E>> {
	private Array<E> data;

	public MaxHeap() {
		data = new Array<>();
	}

	public MaxHeap(int capacity) {
		data = new Array<>(capacity);
	}

	// Heapify�����һ����Ҷ�ӽڵ㵽��������shiftDown
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

	// ����index�����ĸ���
	public int parent(int index) {
		return (index - 1) / 2;
	}

	// �Һ���
	public int rightChild(int index) {
		return 2 * index + 2;
	}

	// ����
	public int leftChild(int index) {
		return 2 * index + 1;
	}

	// ��ӹ���������������������Ԫ�أ�Ȼ��Ϊ�˱��ֶѵ����ԣ�Ҫ����Ԫ�ؽ���shiftUp����
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

	// �ó�������ݵĹ������Ȱ�����Ϊ0���������ó�����Ȼ������һ�����������ݷ���0����Ȼ����·���0��������shiftDown
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
			 * �����е㸴���ˡ������Ȼ�ȡ�Һ��������ĸ���Ȼ���ٺʹ�ıȽϣ�����ȴ�Ļ����Ǿ�break������ȴ��С���Ǿ���swap�������߼������˺ܶ�
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
