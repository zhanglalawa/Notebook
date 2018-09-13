package unionFind;
/*
 * 在find过程中 路径亚索
 */
public class UnionFind5 implements UF{
	private int[] parent;
	private int[] rank;	//rank[i]表示以i为根的集合所表示的树的高度

	public UnionFind5(int size) {
		parent = new int[size];
		rank = new int[size];
		for (int i = 0; i < size; i++) {
			parent[i] = i;
			rank[i] = 1;
		}
	}

	// 复杂度是O（树的高度）
	private int find(int p) {
		parent[p] = parent[parent[p]];
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

	//根据树的高度去union 矮树附到高树上面 高树的高度更新的情形仅仅存在于两树高度相等时候
	@Override
	public void unionElements(int p, int q) {
		int pRoot = find(p);
		int qRoot = find(q);

		if (pRoot == qRoot) {
			return;
		}

		if (rank[pRoot] >= rank[qRoot]) {
			parent[qRoot] = pRoot;
			if (rank[qRoot] + 1 > rank[pRoot]) {
				rank[pRoot] = rank[qRoot] + 1;
			}
		} else {
			parent[pRoot] = qRoot;
		}
	}
}
