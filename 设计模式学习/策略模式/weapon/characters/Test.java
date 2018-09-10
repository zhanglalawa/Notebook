package characters;

import weapon_class.AxeBehavior;

public class Test {

	public static void main(String[] args) {
		//国王
		King king = new King();
		king.display();
		
		//皇后
		Queen queen = new Queen();
		queen.display();
		
		//国王开始用斧子了！
		king.setWeaponBehavior(new AxeBehavior());
		king.display();
	}

}
