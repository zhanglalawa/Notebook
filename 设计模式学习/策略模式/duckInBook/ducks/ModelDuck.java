package ducks;

import behavior_class.FlyNoWay;
import behavior_class.FlyWithWings;
import behavior_class.Quack;

public class ModelDuck extends Duck {
	public ModelDuck() {
		//����������ģ��Ѽ���Դ��ų��ɣ����ҿ������ɽ�
		flyBehavior = new FlyWithWings();
		quackBehavior = new Quack();
	}
	
	@Override
	public void display() {
		System.out.println("I am a ModelDuck");
		performFly();
		performQuack();
	}

	//�������Ǿ������һ�������
	public static void main(String[] args) {
		Duck modelDuck = new ModelDuck();
		//չʾһ���Լ��ķ��кͽе�����
		modelDuck.display();
		
		//ͻȻ���ĳ����ˡ���
		modelDuck.setFlyBehavior(new FlyNoWay());
		modelDuck.display();
	}
}