package command;

import receiver.CeilingFan;

public class CeilingFanMediumCommand implements Command{
	private CeilingFan ceilingFan;
	//用一个变量来保存上一个状态，实现撤销
	private int prevSpeed;
	
	public CeilingFanMediumCommand(CeilingFan ceilingFan) {
		this.ceilingFan = ceilingFan;
	}
	
	@Override
	public void execute() {
		prevSpeed = ceilingFan.getSpeed();
		ceilingFan.medium();
	}

	@Override
	public void undo() {
		if (prevSpeed == CeilingFan.HIGH) {
			ceilingFan.high();
		}else if (prevSpeed == CeilingFan.MEDIUM) {
			ceilingFan.medium();
		}else if(prevSpeed == CeilingFan.LOW) {
			ceilingFan.low();
		}else if (prevSpeed == CeilingFan.OFF) {
			ceilingFan.off();
		}
	}

}
