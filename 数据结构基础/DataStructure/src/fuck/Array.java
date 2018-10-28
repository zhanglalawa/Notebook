package fuck;

import java.util.Objects;

import sun.nio.cs.ext.IBM037;


public class Array<E> {
	//底层还是用数组实现
	private E[] data;
	//size记录data中真实存在的元素个数
	private int size;
	
	public Array() {
		//默认实现给10个空间
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
		//这是Java不允许泛型数组，所以要Object数组强转
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
			throw new IllegalArgumentException("下标范围异常");
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
		//先保存下来要删除（返回）的值
		E ret = data[index];
		
		//然后从index开始把后一个元素赋值给前一个
		for(int i = index; i < size - 1; i++) {
			data[i] = data[i+1];
		}
		
		
		//最后一个元素赋为Null
		data[size - 1] = null;
		//别忘了维护size
		size --;
		
		
		//size减小之后如果到了四分之一长度，那就缩容为原来容量的一半，但是同时要注意 data.length/2不是0
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
	
	// 测试
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

