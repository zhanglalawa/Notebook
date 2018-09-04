package array;

public class Array<E> {
	private E[] data;
	private int size;

	public Array(int Capacity) {
		data = (E[]) new Object[Capacity];
		size = 0;
	}

	// 无参构造器，默认容量是10
	public Array() {
		this(10);
	}

	// 获得容量
	public int getCapacity() {
		return data.length;
	}

	// 获得目前数组中的元素数目
	public int getSize() {
		return size;
	}

	// 判断空
	public boolean isEmpty() {
		return size == 0;
	}

	// 获取index索引处的值
	public E get(int index) {
		if (index >= size || index < 0) {
			throw new IllegalArgumentException("Get failed. Require index >= 0 and index < size.");
		}
		return data[index];
	}

	// 设置第index索引处的值
	public void set(int index, E e) {
		if (index >= size || index < 0) {
			throw new IllegalArgumentException("Set failed. Require index >= 0 and index < size.");
		}

		data[index] = e;
	}

	// 检查是否有某元素
	public boolean contains(E e) {
		for (int i = 0; i < size; i++) {
			if (e.equals(data[i])) {
				return true;
			}
		}
		return false;
	}

	// 在指定index索引处加入一个元素
	public void add(int index, E e) {
		if (index > size || index < 0) {
			throw new IllegalArgumentException("Add failed. Require index >= 0 and index <= size.");
		}

		// 如果满了就扩容
		if (size == data.length) {
			resize(2 * data.length);
		}

		// 从最后一个位置开始处理，不断后移元素
		for (int i = size; i > index; i--) {
			data[i] = data[i - 1];
		}

		data[index] = e;

		// 最后别忘了给size++
		size++;
	}

	// 在尾部添加元素
	public void addLast(E e) {
		add(size, e);
	}

	// 在头部添加元素
	public void addFirst(E e) {
		add(0, e);
	}

	// 删除指定index索引处的元素并且返回它
	public E remove(int index) {
		if (index >= size || index < 0) {
			throw new IllegalArgumentException("Remove failed. Require index >= 0 and index < size.");
		}
		// 因为要返回它，所以需要这一句
		E retElement = data[index];
		for (int i = index; i < size; i++) {
			data[i] = data[i + 1];
		}
		data[size] = null;// 这一步是关键的防止内存泄漏
		// 最后不要忘了size--
		size--;
		// 如果现在里面的实际数量到了data.length的四分之一了，就缩容一半，一定要判断data.length缩容之后不能是0！
		if (size == data.length / 4 && data.length / 2 != 0) {
			resize(data.length / 2);
		}
		return retElement;
	}

	// 删除最后一个
	public E removeLast() {
		return remove(size - 1);
	}

	// 删除第一个
	public E removeFirst() {
		return remove(0);
	}

	// 寻找指定元素并且返回索引
	public int find(E e) {
		int index;
		for (index = 0; index < size; index++) {
			if (e.equals(data[index])) {
				return index;
			}
		}
		// 没有找到的话返回-1
		return -1;
	}

	// 删除某个具体元素
	public void removeElement(E e) {
		int index = find(e);
		if (index != -1) {
			remove(index);
		}
	}

	// 重写toString展示数组
	@Override
	public String toString() {

		StringBuilder res = new StringBuilder();
		res.append(String.format("Array: size = %d , capacity = %d\n", size, data.length));
		res.append('[');
		for (int i = 0; i < size; i++) {
			res.append(data[i]);
			if (i != size - 1)
				res.append(", ");
		}
		res.append(']');
		return res.toString();
	}

	private void resize(int newCapacity) {

		E[] newArray = (E[]) new Object[newCapacity];
		for (int i = 0; i < size; i++) {
			newArray[i] = data[i];
		}

		data = newArray;
	}

	public static void main(String[] args) {

		        Array<Integer> arr = new Array<>();
		        for(int i = 0 ; i < 10 ; i ++)
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

		        for(int i = 0 ; i < 4 ; i ++){
		            arr.removeFirst();
		            System.out.println(arr);
		        }
	}
}