package singleton;

/*
 * 双重检锁
 * 只有在突破第一次if检测之后
 * 才进行同步操作
 * 避免了频繁synchronized
 * 还要注意volatile
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
