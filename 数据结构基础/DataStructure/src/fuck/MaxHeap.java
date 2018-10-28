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
	
	//������캯����Ӧ�Ĺ��̽�Heapify
	public MaxHeap(E[] array) {
		//�Ȱ�����Ū����
		data = new Array<>(array);
		//�ӵ�һ����Ҷ�ӽڵ㿪ʼ shiftDown
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
			//��һ���ҵ����Һ����и������һ��
			if(j + 1 < size() && data.get(j+1).compareTo(data.get(j))>0) {
				j = rightChild(index);
			}
			//j���ڴ���Ĳ����������ң�������������
			//�����ǰ�ڵ�����Һ��Ӹ�����Ǹ���Ҫ���Ǿ�˵��������ҪshiftDown�ˣ������ٽ���
			if (data.get(index).compareTo(data.get(j))>0) {
				break;
			}
			
			//��������ѭ��û������ȥ���Ǿ�˵���϶��Ǻ��ӱ�����ģ����������ң�ֻ��jjjjjj������һ�¼���
			data.swap(index, j);
			
			//�ٰ�ָ��Ų��j����
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
