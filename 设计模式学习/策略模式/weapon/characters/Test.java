package characters;

import weapon_class.AxeBehavior;

public class Test {

	public static void main(String[] args) {
		//����
		King king = new King();
		king.display();
		
		//�ʺ�
		Queen queen = new Queen();
		queen.display();
		
		//������ʼ�ø����ˣ�
		king.setWeaponBehavior(new AxeBehavior());
		king.display();
	}

}
