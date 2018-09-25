package invoker;

import command.CeilingFanHighCommand;
import command.CeilingFanMediumCommand;
import command.CeilingFanOffCommand;
import receiver.CeilingFan;

public class RemoteLoader {
	public static void main(String[] args) {
		RemoteControl remoteControl = new RemoteControl();
		
		CeilingFan ceilingFan = new CeilingFan("Living Room");
		
		CeilingFanHighCommand ceilingFanHighCommand = new CeilingFanHighCommand(ceilingFan);
		CeilingFanMediumCommand ceilingFanMediumCommand = new CeilingFanMediumCommand(ceilingFan);
		CeilingFanOffCommand ceilingFanOffCommand = new CeilingFanOffCommand(ceilingFan);
		
		remoteControl.setCommand(0, ceilingFanMediumCommand, ceilingFanOffCommand);
		remoteControl.setCommand(1, ceilingFanHighCommand, ceilingFanOffCommand);
		
		remoteControl.onButtonWasPushed(0);
		remoteControl.offButtonWasPushed(0);
		remoteControl.undoButtonWasPushed();
		
		remoteControl.onButtonWasPushed(1);
		remoteControl.offButtonWasPushed(1);
		remoteControl.undoButtonWasPushed();
	}
}
