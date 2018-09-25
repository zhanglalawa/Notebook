package command;

/*
 * 命令接口
 * 含有执行和撤销方法
 */
public interface Command {
	public void execute();
	public void undo();
}
