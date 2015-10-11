
public class Div extends Instruction {

	@Override
	public void execute() {
		int r1 = this.machine.getRegister(s1);
		int r2 = this.machine.getRegister(s2);
		super.machine.setRegister(super.result, r1 / r2);
	}

}
