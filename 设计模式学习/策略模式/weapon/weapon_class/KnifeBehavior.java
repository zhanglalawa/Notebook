package weapon_class;

import weapon_interface.WeaponBehavior;

public class KnifeBehavior implements WeaponBehavior{
	
	@Override
	public void useWeapon() {
		System.out.println("stab with the knife");
	}

}
