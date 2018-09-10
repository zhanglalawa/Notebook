package weapon_class;

import weapon_interface.WeaponBehavior;

public class AxeBehavior implements WeaponBehavior {

	@Override
	public void useWeapon() {
		System.out.println("hack with the axe");
	}

}
