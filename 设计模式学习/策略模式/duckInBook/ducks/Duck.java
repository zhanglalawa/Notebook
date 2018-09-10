package ducks;

import behavior_interface.FlyBehavior;
import behavior_interface.QuackBehavior;

public abstract class Duck {
	//多用组合、少用继承！
	FlyBehavior flyBehavior;
	QuackBehavior quackBehavior;
	
	public Duck() {}
	
	public abstract void display();
	
	// 下面这两个方法去把飞行和叫委托给行为类
	public void performFly() {
		flyBehavior.fly();
	}
	
	public void performQuack() {
		quackBehavior.quack();
	}

	//可以通过下面的方法随时动态改变鸭子的行为，而不是在构造器里面去实例化
	public void setFlyBehavior(FlyBehavior flyBehavior) {
		this.flyBehavior = flyBehavior;
	}

	public void setQuackBehavior(QuackBehavior quackBehavior) {
		this.quackBehavior = quackBehavior;
	}
	
	
}
