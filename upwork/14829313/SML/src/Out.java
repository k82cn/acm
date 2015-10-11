
public class Out extends Instruction {

	@Override
	public void execute() {
		System.out.println(this.machine.getRegister(this.result));
	}

}
