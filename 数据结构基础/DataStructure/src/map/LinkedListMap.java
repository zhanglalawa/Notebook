package map;

/*
 * �ײ�Ϊ�����һ��ӳ��
 * ��������Ҫ��һ��ֵ����������֮ǰʵ�ֵ�LinkedList�ǲ����õ�
 * �������¶���һ�½ڵ�����ݾͺ���
 * ������·������ͬ��
 */
public class LinkedListMap<K, V> implements Map<K, V> {

	private class Node {
		public K key;
		public V value;
		public Node next;

		public Node(K key, V value, Node next) {
			this.key = key;
			this.value = value;
			this.next = next;
		}

		public Node(K key) {
			this(key, null, null);
		}

		public Node() {
			this(null, null, null);
		}

		@Override
		public String toString() {
			return key.toString() + ":" + value.toString();
		}
	}

	// ������Ȼ��Ҫһ����ͷ���
	private Node dummyHead;
	private int size;

	// �����ǂ�key��Ӧ��Node
	private Node getNode(K key) {
		Node curr = dummyHead.next;
		while (curr != null) {
			if (curr.key.equals(key)) {
				return curr;
			}
			curr = curr.next;
		}
		return null;
	}

	public LinkedListMap() {
		dummyHead = new Node();
		size = 0;
	}

	@Override
	public int getSize() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	// ��ֱֵ����ӵ�������ͷ
	@Override
	public void add(K key, V value) {
		Node node = getNode(key);
		if (node == null) {
			dummyHead.next = new Node(key, value, dummyHead.next);
			size++;
		} else {
			node.value = value;
		}
	}

	@Override
	public V remove(K key) {
		Node prev = dummyHead;
		while (prev.next != null) {
			if (prev.next.key.equals(key)) {
				Node curr = prev.next;
				prev.next = curr.next;
				curr.next = null;
				size--;
				return curr.value;
			}
			prev = prev.next;
		}
		return null;
	}

	@Override
	public boolean contains(K key) {
		return getNode(key) != null;
	}

	@Override
	public V get(K key) {
		Node node = getNode(key);
		return node != null ? node.value : null;
	}

	@Override
	public void set(K key, V newValue) {
		Node node = getNode(key);
		if (node == null) {
			throw new IllegalArgumentException("No such key in this map");
		} else {
			node.value = newValue;
		}
	}
	
	public static void main(String[] args){
		Map<String, String> fuck = new LinkedListMap<>();
		fuck.add("badboy", "lala");
		fuck.add("badgirl", "hahah");
		fuck.add("fuck","xmj");
		
		System.out.println(fuck.getSize());
		System.out.println(fuck.get("fuck"));
		fuck.set("badboy", "zw");
		System.out.println(fuck.get("badboy"));
		System.out.println(fuck.contains("fuck"));
		System.out.println(fuck.get("badgirl"));
		fuck.remove("badgirl");
		System.out.println(fuck.get("badgirl"));
	}

	
}
