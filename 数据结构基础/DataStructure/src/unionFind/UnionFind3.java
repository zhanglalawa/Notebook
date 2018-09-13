package unionFind;

/*
 * 基于树的大小进行优化
 * 让小树的根依附在大树根上
 * 避免了可能出现链表的情况
 */
public class UnionFind3 implements UF {
	private int[] parent;
	private int[] sz;		//sz[i]表示以i为根的集合中元素个数

	public UnionFind3(int size) {
		parent = new int[size];
		sz = new int[size];
		for (int i = 0; i < size; i++) {
			parent[i] = i;
			sz[i] = 1;
		}
	}

	// 复杂度是O（树的高度）
	private int find(int p) {
		if (parent[p] == p) {
			return p;
		}

		return find(parent[p]);
	}

	@Override
	public int getSize() {
		return parent.length;
	}

	@Override
	public boolean isConnected(int p, int q) {
		return find(p) == find(q);
	}

	//
	@Override
	public void unionElements(int p, int q) {
		int pRoot = find(p);
		int qRoot = find(q);

		if (pRoot == qRoot) {
			return;
		}

		if (sz[pRoot] >= sz[qRoot]) {
			sz[pRoot] += sz[qRoot];
			parent[qRoot] = pRoot;
		} else {
			sz[qRoot] += sz[pRoot];
			parent[pRoot] = qRoot;
		}
	}

}
