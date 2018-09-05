package linkedList;

public class LinkedList<E> {
	private class Node {
		public E e;
		public Node next;

		public Node(E e, Node next) {
			this.e = e;
			this.next = next;
		}

		public Node(E e) {
			this(e, null);
		}

		public Node() {
			this(null, null);
		}

		@Override
		public String toString() {
			return e.toString();
		}
	}

	private Node dummyHead;
	private int size;

	public LinkedList() {
		dummyHead = new Node();
		size = 0;
	}

	public int getSize() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public void add(int index, E e) {
		if (index < 0 || index > size) {
			throw new IllegalArgumentException("Add failed. Illegal index.");
		}
		Node newNode = new Node(e);
		Node prev = dummyHead;
		for (int i = 0; i < index; i++) {
			prev = prev.next;
		}

		newNode.next = prev.next;
		prev.next = newNode;
		size++;
	}

	public void addFirst(E e) {
		add(0, e);
	}

	public void addLast(E e) {
		add(size, e);
	}

	public E get(int index) {
		if (index < 0 || index >= size) {
			throw new IllegalArgumentException("Get failed. Illegal index.");
		}
		Node curr = dummyHead.next;
		for (int i = 0; i < index; i++) {
			curr = curr.next;
		}
		return curr.e;
	}

	public E getFirst() {
		return get(0);
	}

	public E getLast() {
		return get(size - 1);
	}

	public void set(int index, E e) {
		if (index < 0 || index >= size) {
			throw new IllegalArgumentException("Set failed. Illegal index.");
		}
		Node curr = dummyHead.next;
		for (int i = 0; i < index; i++) {
			curr = curr.next;
		}

		curr.e = e;
	}

	public boolean contains(E e) {
		Node node = dummyHead;
		while (node.next != null) {
			node = node.next;
			if (node.e.equals(e)) {
				return true;
			}
		}
		return false;
	}

	public E remove(int index) {
		if (index < 0 || index >= size) {
			throw new IllegalArgumentException("Remove failed. Illegal index.");
		}

		Node prev = dummyHead;
		for (int i = 0; i < index; i++) {
			prev = prev.next;
		}
		/*
		 * E ret = prev.next.e; prev.next = prev.next.next;
		 */
		Node retNode = prev.next;
		prev.next = retNode.next;
		retNode.next = null;// 这样和注释都可以达到目的，但是上面有内存泄漏的可能
		size--;
		return retNode.e;
	}

	public E removeFirst() {
		return remove(0);
	}

	public E removeLast() {
		return remove(size - 1);
	}

	public void removeElement(E e) {
		Node prev = dummyHead;
		Node curr;
		while (prev.next != null) {
			curr = prev.next;
			if (curr.e.equals(e)) {
				prev.next = curr.next;
				curr.next = null;
				return;
			}
			prev = curr;
		}

	}

	@Override
	public String toString() {
		StringBuilder res = new StringBuilder();

		Node cur = dummyHead.next;
		while (cur != null) {
			res.append(cur + "->");
			cur = cur.next;
		}
		res.append("NULL");

		return res.toString();
	}

	public static void main(String args[]) {
		LinkedList<Integer> test = new LinkedList<>();
		for (int i = 1; i <= 10; i++) {
			test.addLast(i);
		}

		System.out.println(test);
		test.addFirst(0);
		test.addLast(0);

		System.out.println(test);
		test.removeFirst();
		System.out.println(test);
		test.removeLast();
		System.out.println(test);
		test.removeElement(2);
		System.out.println(test);
	}
}
