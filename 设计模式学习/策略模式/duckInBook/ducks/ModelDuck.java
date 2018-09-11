package ducks;

import behavior_class.FlyNoWay;
import behavior_class.FlyWithWings;
import behavior_class.Quack;

public class ModelDuck extends Duck {
	public ModelDuck() {
		//构造器中让模特鸭可以带着翅膀飞，并且可以呱呱叫
		flyBehavior = new FlyWithWings();
		quackBehavior = new Quack();
	}
	
	@Override
	public void display() {
		System.out.println("I am a ModelDuck");
		performFly();
		performQuack();
	}

	//下面我们具体测试一下这个类
	public static void main(String[] args) {
		Duck modelDuck = new ModelDuck();
		//展示一下自己的飞行和叫的能力
		modelDuck.display();
		
		//突然它的翅膀断了。。
		modelDuck.setFlyBehavior(new FlyNoWay());
		modelDuck.display();
	}
}