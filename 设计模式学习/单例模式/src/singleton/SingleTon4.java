package singleton;

/*
 * ˫�ؼ���
 * ֻ����ͻ�Ƶ�һ��if���֮��
 * �Ž���ͬ������
 * ������Ƶ��synchronized
 * ��Ҫע��volatile
 */

public class SingleTon4 {
	private volatile static SingleTon4 uniqueInstance;
	
	private SingleTon4() {}
	
	public static SingleTon4 getInstance() {
		if (uniqueInstance == null) {
			synchronized (SingleTon4.class) {
				if (uniqueInstance == null) {
					uniqueInstance = new SingleTon4();
				}
			}
		}
		return uniqueInstance;
	}
}
