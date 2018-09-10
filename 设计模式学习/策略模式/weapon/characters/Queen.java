package characters;

import weapon_class.KnifeBehavior;

public class Queen extends Character{
	public Queen() {
		//ª ∫Û”√–°µ∂
		weaponBehavior = new KnifeBehavior();
	}
	
	@Override
	public void display() {
		System.out.println("I am the queen!");
		fight();
	}

}
