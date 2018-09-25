package singleton;

/*
 * 最朴素的实现方法
 * 但是存在多线程的bug
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
