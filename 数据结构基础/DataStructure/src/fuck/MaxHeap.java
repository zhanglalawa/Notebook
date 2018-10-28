package fuck;

import java.util.PriorityQueue;

public class MaxHeap<E extends Comparable<E>> {
	private Array<E> data;
	
	public MaxHeap() {
		this.data = new Array<>();
	}
	
	public MaxHeap(int Capacity) {
		this.data = new Array<>(Capacity);
	}
	
	//这个构造函数对应的过程叫Heapify
	public MaxHeap(E[] array) {
		//先把数组弄进来
		data = new Array<>(array);
		//从第一个非叶子节点开始 shiftDown
		for(int i = parent(size() - 1); i >= 0; i--) {
			shiftDown(i);
		}
	}
	
	public boolean isEmpty() {
		return data.isEmpty();
	}
	public int size() {
		return data.getSize();
	}
	
	public void add(E element) {
		data.addLast(element);
		shiftUp(size()-1);
	}
	
	public E extractMax() {
		E max = findMax();
		data.swap(0, size()-1);
		data.removeLast();
		
		shiftDown(0);
		return max;
	}
	
	public E findMax() {
		return data.getFirst();
	}
	
	
	private void shiftDown(int index) {
		while(leftChild(index)< size()) {
			int j = leftChild(index);
			//第一步找到左右孩子中更大的那一个
			if(j + 1 < size() && data.get(j+1).compareTo(data.get(j))>0) {
				j = rightChild(index);
			}
			//j现在代表的不管是左还是右，反正都是最大的
			//如果当前节点比左右孩子更大的那个还要大，那就说明不再需要shiftDown了，不用再交换
			if (data.get(index).compareTo(data.get(j))>0) {
				break;
			}
			
			//如果上面的循环没有跳出去，那就说明肯定是孩子比他大的，不管左还是右，只管jjjjjj，交换一下即可
			data.swap(index, j);
			
			//再把指针挪到j上面
			index = j;
		}
	}
	
	private void shiftUp(int index) {
		while(parent(index) >= 0 && data.get(index).compareTo(data.get(parent(index)))>0) {
			data.swap(index, parent(index));
			index = parent(index);
		}
	}
	
	private int parent(int index) {
		return (index-1)/2;
	}
	
	private int leftChild(int index) {
		return 2 * index + 1;
	}
	
	private int rightChild(int index) {
		return 2 * index + 2;
	}
	
	
	
}
