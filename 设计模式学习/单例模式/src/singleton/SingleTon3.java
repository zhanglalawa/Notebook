package singleton;

/*
 * 直接静态初始化
 * 免去了同步
 */
public class SingleTon3 {
	private static SingleTon3 uniqueInstance = new SingleTon3();
	
	private SingleTon3() {}
	
	public static SingleTon3 SingleTon3() {
		return uniqueInstance;
	}
}
