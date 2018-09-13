package unionFind;
/*
 * ��find������ ·������
 */
public class UnionFind5 implements UF{
	private int[] parent;
	private int[] rank;	//rank[i]��ʾ��iΪ���ļ�������ʾ�����ĸ߶�

	public UnionFind5(int size) {
		parent = new int[size];
		rank = new int[size];
		for (int i = 0; i < size; i++) {
			parent[i] = i;
			rank[i] = 1;
		}
	}

	// ���Ӷ���O�����ĸ߶ȣ�
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

	//�������ĸ߶�ȥunion ���������������� �����ĸ߶ȸ��µ����ν��������������߶����ʱ��
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
