package behavior_class;

import behavior_interface.FlyBehavior;

public class FlyWithWings implements FlyBehavior{

	@Override
	public void fly() {
		System.out.println("I can fly with wings!");
	}

}
