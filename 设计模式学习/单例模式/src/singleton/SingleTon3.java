package singleton;

/*
 * ֱ�Ӿ�̬��ʼ��
 * ��ȥ��ͬ��
 */
public class SingleTon3 {
	private static SingleTon3 uniqueInstance = new SingleTon3();
	
	private SingleTon3() {}
	
	public static SingleTon3 SingleTon3() {
		return uniqueInstance;
	}
}
