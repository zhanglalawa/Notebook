package behavior_class;

import behavior_interface.FlyBehavior;

public class FlyNoWay implements FlyBehavior{

	@Override
	public void fly() {
		System.out.println("I can't fly!");
	}

}
