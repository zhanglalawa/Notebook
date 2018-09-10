package weapon_class;

import weapon_interface.WeaponBehavior;

public class BowAndArrowBehavior implements WeaponBehavior{

	@Override
	public void useWeapon() {
		System.out.print("shoot with the bow");
	}

}
