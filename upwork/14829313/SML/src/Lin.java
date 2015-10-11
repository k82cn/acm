
public class Lin extends Instruction {

	@Override
	public void execute() {
		this.machine.setRegister(result, s1);
	}

}
