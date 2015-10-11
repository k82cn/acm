public class Bnz extends Instruction {

	@Override
	public void execute() {
		// the counter will be updated in updateCounter()
	}
	
	@Override
	public void updateCounter()
	{
		int r = this.machine.getRegister(result);
		if (r != 0) {
			this.machine.setCounter(target);
		}
		else
		{
			super.updateCounter();
		}
	}

}
