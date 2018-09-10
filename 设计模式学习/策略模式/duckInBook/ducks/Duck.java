package ducks;

import behavior_interface.FlyBehavior;
import behavior_interface.QuackBehavior;

public abstract class Duck {
	//������ϡ����ü̳У�
	FlyBehavior flyBehavior;
	QuackBehavior quackBehavior;
	
	public Duck() {}
	
	public abstract void display();
	
	// ��������������ȥ�ѷ��кͽ�ί�и���Ϊ��
	public void performFly() {
		flyBehavior.fly();
	}
	
	public void performQuack() {
		quackBehavior.quack();
	}

	//����ͨ������ķ�����ʱ��̬�ı�Ѽ�ӵ���Ϊ���������ڹ���������ȥʵ����
	public void setFlyBehavior(FlyBehavior flyBehavior) {
		this.flyBehavior = flyBehavior;
	}

	public void setQuackBehavior(QuackBehavior quackBehavior) {
		this.quackBehavior = quackBehavior;
	}
	
	
}
