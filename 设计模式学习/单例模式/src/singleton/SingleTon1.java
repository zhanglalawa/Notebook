package singleton;

/*
 * �����ص�ʵ�ַ���
 * ���Ǵ��ڶ��̵߳�bug
 */
public class SingleTon1 {
	private static SingleTon1 uniqueInstance;
	
	private SingleTon1() {}
	
	public static SingleTon1 getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new SingleTon1();
		}
		return uniqueInstance;
	}
}
