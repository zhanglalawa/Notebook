package singleton;

/*
 * 使用synchroniaze关键字
 * 每次都同步，保证了多线程下的单例
 * 但是synchronized同步的代价很高
 * 这个锁很重
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
