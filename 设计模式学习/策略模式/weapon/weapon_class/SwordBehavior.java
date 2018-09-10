package weapon_class;

import weapon_interface.WeaponBehavior;

public class SwordBehavior implements WeaponBehavior{

	@Override
	public void useWeapon() {
		System.out.println("wave with the sword");
	}

}
