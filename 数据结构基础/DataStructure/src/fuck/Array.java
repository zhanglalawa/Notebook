package fuck;

import java.util.Objects;

import sun.nio.cs.ext.IBM037;


public class Array<E> {
	//�ײ㻹��������ʵ��
	private E[] data;
	//size��¼data����ʵ���ڵ�Ԫ�ظ���
	private int size;
	
	public Array() {
		//Ĭ��ʵ�ָ�10���ռ�
		this(10);
	}
	
	public Array(E[] data) {
		this.data = (E[])new Object[data.length];
		for(int i = 0; i < data.length; i++) {
			this.data[i] = data[i];
		}
		size = data.length;
	}
	
	public Array(int Capacity) {
		//����Java�����������飬����ҪObject����ǿת
		data = (E[]) new Object[Capacity];
		size = 0;
	}
	
	public int getSize() {
		return size;
	}
	
	public int getCapacity() {
		return data.length;
	}
	
	public boolean isEmpty() {
		return size == 0;
	}
	
	public E get(int index) {
		checkIndexRange(index);
		return data[index];
	}
	
	public E getFirst() {
		return get(0);
	}
	
	public E getLast() {
		return get(size - 1);
	}
	
	
	private void checkIndexRange(int index) {
		if (index < 0 || index >= data.length) {
			throw new IllegalArgumentException("�±귶Χ�쳣");
		}
	}
	
	private void resize(int newCapacity) {
		E[] newArray = (E[]) new Object[newCapacity];
		
		for(int i = 0; i < size; i++) {
			newArray[i] = data[i];
		}
		
		data = newArray;
	}
	
	public boolean contains(E element) {
		for(int i = 0; i < size; i++) {
			if (Objects.equals(data[i], element)) {
				return true;
			}
		}
		
		return false;
	}
	
	public void add(int index, E element) {
		checkIndexRange(index);
		
		
		if (size == data.length) {
			resize(2 * size);
		}
		
		for(int i = size; i > index; i--) {
			data[i] = data[i-1];
		}
		
		data[index] = element;
		
		size ++;
	}

	public void addFirst(E element) {
		add(0, element);
	}
	
	public void addLast(E element) {
		add(size, element);
	}
	
	
	public int find(E element) {
		for(int i = 0; i < size; i++) {
			if (Objects.equals(data[i], element)) {
				return i;
			}
		}
		
		return -1;
	}
	
	public E remove(int index) {
		checkIndexRange(index);
		//�ȱ�������Ҫɾ�������أ���ֵ
		E ret = data[index];
		
		//Ȼ���index��ʼ�Ѻ�һ��Ԫ�ظ�ֵ��ǰһ��
		for(int i = index; i < size - 1; i++) {
			data[i] = data[i+1];
		}
		
		
		//���һ��Ԫ�ظ�ΪNull
		data[size - 1] = null;
		//������ά��size
		size --;
		
		
		//size��С֮����������ķ�֮һ���ȣ��Ǿ�����Ϊԭ��������һ�룬����ͬʱҪע�� data.length/2����0
		if (size == data.length/4 && data.length / 2 != 0) {
			resize(data.length / 2);
		}
		
		return ret;
	}
	
	public E removeFirst() {
		return remove(0);
	}
	
	public E removeLast() {
		return remove(size);
	}
	
	public E removeElement(E element) {
		int index = find(element);
		
		return remove(index);
	}
	
	public void set(int index, E element) {
		checkIndexRange(index);
		
		data[index] = element;
	}
	
	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer();
		ret.append("Array size = "+ size + " Capacity = " + data.length + ":[ ");
		
		for(int i = 0; i < size; i++) {
			ret.append("" + data[i]+ ' ');
		}
		
		ret.append("]");
		
		return ret.toString();
	}
	
	public void swap(int i, int j) {
		E tmp = data[i];
		data[i] = data[j];
		data[j] = tmp;
	}
	
	// ����
		public static void main(String[] args) {
			Array<Integer> arr = new Array<>();
			for (int i = 0; i < 10; i++)
				arr.addLast(i);
			System.out.println(arr);

			arr.add(1, 100);
			System.out.println(arr);

			arr.addFirst(-1);
			System.out.println(arr);

			arr.remove(2);
			System.out.println(arr);

			arr.removeElement(4);
			System.out.println(arr);

			arr.removeFirst();
			System.out.println(arr);

			for (int i = 0; i < 4; i++) {
				arr.removeFirst();
				System.out.println(arr);
			}
		}
}

