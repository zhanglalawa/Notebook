package hash;

import java.util.TreeMap;

public class HashTable<K, V> {
	private static final int upperTol = 10;
	private static final int lowerTol = 2;
	private static final int initCapacity = 7;
	private TreeMap<K, V>[] hashtable;
	private int size;
	private int M;

	public HashTable(int M) {
		hashtable = new TreeMap[M];
		for (int i = 0; i < M; i++) {
			hashtable[i] = new TreeMap<>();
		}
	}

	public HashTable() {
		this(initCapacity);
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

			if (size >= upperTol * M) {
				resize(2 * M);
			}
		} else {
			map.put(key, value);
		}
	}

	public V remove(K key) {
		/* return hashtable[hash(key)].remove(key);因为要维护size，所以一定要判断一下有没有这个节点 */
		TreeMap<K, V> map = hashtable[hash(key)];
		V ret = null;
		if (map.containsKey(key)) {
			ret = map.remove(key);
			size--;

			if (size < lowerTol * M && M / 2 >= initCapacity) {
				resize(M / 2);
			}
		}
		return ret;
	}

	public boolean contains(K key) {
		return hashtable[hash(key)].containsKey(key);
	}

	public V get(K key) {
		return hashtable[hash(key)].get(key);
	}
	
	private void resize(int newM) {
		TreeMap<K, V>[] newHashTable = new TreeMap[newM];
		
		for(int i = 0; i < newM; i++) {
			newHashTable[i] = new TreeMap<>();
		}
		
		int oldM = M;
		M = newM;//一定要记得把M更新了！
		
		for(int i = 0; i < oldM; i++) {
			TreeMap<K, V> map = hashtable[i];
			
			for(K key: map.keySet()) {
				newHashTable[hash(key)].put(key, map.get(key));
			}
		}
		
		hashtable = newHashTable;
	}
}
