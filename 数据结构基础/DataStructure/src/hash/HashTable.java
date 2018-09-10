package hash;


import java.util.TreeMap;

public class HashTable<K, V> {
	private TreeMap<K, V>[] hashtable;
	private int size;
	private int M;

	public HashTable(int M) {
		hashtable = new TreeMap[M];
		for(int i = 0; i < M; i++) {
			hashtable[i] = new TreeMap<>();
		}
	}

	public HashTable() {
		this(97);
	}

	private int hash(K key) {
		return (key.hashCode() & 0x7fffffff) % M;
	}

	public int getSize() {
		return size;
	}

	public void add(K key, V value) {
		TreeMap<K, V> map = hashtable[hash(key)];
		if (!map.containsKey(key)) {
			map.put(key, value);
			size++;
		}else {
			map.put(key, value);
		}
	}

	public V remove(K key) {
		/*return hashtable[hash(key)].remove(key);因为要维护size，所以一定要判断一下有没有这个节点*/
		TreeMap<K, V> map = hashtable[hash(key)];
		V ret = null;
		if (map.containsKey(key)) {
			ret = map.remove(key);
			size--;
		}
		return ret;
	}

	public boolean contains(K key) {
		return hashtable[hash(key)].containsKey(key);
	}

	public V get(K key) {
		return hashtable[hash(key)].get(key);
	}
}
