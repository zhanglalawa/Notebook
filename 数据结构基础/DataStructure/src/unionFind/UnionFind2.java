package unionFind;

public class UnionFind2 implements UF{
	private int[] parent;
	
	public UnionFind2(int size) {
		parent = new int[size];
		
		for(int i = 0; i < size; i++) {
			parent[i]= i;
		}
	}
	
	//���Ӷ���O�����ĸ߶ȣ�
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

	//�ֱ��ҵ������� ��Ȼ���һ��������Ϊ��һ����
	@Override
	public void unionElements(int p, int q) {
		int pRoot = find(p);
		int qRoot = find(q);
		
		if (pRoot == qRoot) {
			return;
		}
		
		parent[pRoot] = qRoot;
	}
	
	
}
