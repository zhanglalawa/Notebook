package characters;

import weapon_class.SwordBehavior;

public class King extends Character {

	public King() {
		//ª µ€”√±¶Ω£
		weaponBehavior = new SwordBehavior();
	}
	
	@Override
	public void display() {
		System.out.println("I am the King!");
		fight();	
	}

}
