package array;

public class Array<E> {
	private E[] data;
	private int size;

	public Array(int Capacity) {
		data = (E[]) new Object[Capacity];
		size = 0;
	}

	// �޲ι�������Ĭ��������10
	public Array() {
		this(10);
	}

	// �������
	public int getCapacity() {
		return data.length;
	}

	// ���Ŀǰ�����е�Ԫ����Ŀ
	public int getSize() {
		return size;
	}

	// �жϿ�
	public boolean isEmpty() {
		return size == 0;
	}

	// ��ȡindex��������ֵ
	public E get(int index) {
		if (index >= size || index < 0) {
			throw new IllegalArgumentException("Get failed. Require index >= 0 and index < size.");
		}
		return data[index];
	}

	// ���õ�index��������ֵ
	public void set(int index, E e) {
		if (index >= size || index < 0) {
			throw new IllegalArgumentException("Set failed. Require index >= 0 and index < size.");
		}

		data[index] = e;
	}

	// ����Ƿ���ĳԪ��
	public boolean contains(E e) {
		for (int i = 0; i < size; i++) {
			if (e.equals(data[i])) {
				return true;
			}
		}
		return false;
	}

	// ��ָ��index����������һ��Ԫ��
	public void add(int index, E e) {
		if (index > size || index < 0) {
			throw new IllegalArgumentException("Add failed. Require index >= 0 and index <= size.");
		}

		// ������˾�����
		if (size == data.length) {
			resize(2 * data.length);
		}

		// �����һ��λ�ÿ�ʼ�������Ϻ���Ԫ��
		for (int i = size; i > index; i--) {
			data[i] = data[i - 1];
		}

		data[index] = e;

		// �������˸�size++
		size++;
	}

	// ��β�����Ԫ��
	public void addLast(E e) {
		add(size, e);
	}

	// ��ͷ�����Ԫ��
	public void addFirst(E e) {
		add(0, e);
	}

	// ɾ��ָ��index��������Ԫ�ز��ҷ�����
	public E remove(int index) {
		if (index >= size || index < 0) {
			throw new IllegalArgumentException("Remove failed. Require index >= 0 and index < size.");
		}
		// ��ΪҪ��������������Ҫ��һ��
		E retElement = data[index];
		for (int i = index; i < size; i++) {
			data[i] = data[i + 1];
		}
		data[size] = null;// ��һ���ǹؼ��ķ�ֹ�ڴ�й©
		// ���Ҫ����size--
		size--;
		// ������������ʵ����������data.length���ķ�֮һ�ˣ�������һ�룬һ��Ҫ�ж�data.length����֮������0��
		if (size == data.length / 4 && data.length / 2 != 0) {
			resize(data.length / 2);
		}
		return retElement;
	}

	// ɾ�����һ��
	public E removeLast() {
		return remove(size - 1);
	}

	// ɾ����һ��
	public E removeFirst() {
		return remove(0);
	}

	// Ѱ��ָ��Ԫ�ز��ҷ�������
	public int find(E e) {
		int index;
		for (index = 0; index < size; index++) {
			if (e.equals(data[index])) {
				return index;
			}
		}
		// û���ҵ��Ļ�����-1
		return -1;
	}

	// ɾ��ĳ������Ԫ��
	public void removeElement(E e) {
		int index = find(e);
		if (index != -1) {
			remove(index);
		}
	}

	// ��дtoStringչʾ����
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