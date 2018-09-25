package singleton;

/*
 * ʹ��synchroniaze�ؼ���
 * ÿ�ζ�ͬ������֤�˶��߳��µĵ���
 * ����synchronizedͬ���Ĵ��ۺܸ�
 * ���������
 */
public class SingleTon2 {
	private static SingleTon2 uniqueInstance;
	private SingleTon2() {}
	
	public static synchronized SingleTon2 getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new SingleTon2();
		}
		return uniqueInstance;
	}
}
