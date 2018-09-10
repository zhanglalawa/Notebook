package behavior_class;

import behavior_interface.QuackBehavior;

public class MuteQuack implements QuackBehavior {

	@Override
	public void quack() {
		System.out.println("É¶½Ð¶¼²»»á");
	}

}
