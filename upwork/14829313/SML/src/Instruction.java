import java.util.ArrayList;

public abstract class Instruction {

	private String label;
	private String operator;
	private ArrayList<String> arguments = new ArrayList<String>();

	protected Machine machine;

	protected int result;
	protected int s1;
	protected int s2;
	protected String target;

	public void parseArgs() {

		this.result = Integer.parseInt(this.getArgument(0));

		if (this.arguments.size() > 1) {
			try {
				this.s1 = Integer.parseInt(this.getArgument(1));
			} catch (Exception e) {
				this.target = this.arguments.get(1);
			}
		}

		if (this.arguments.size() > 2) {
			try {
				this.s2 = Integer.parseInt(this.getArgument(2));
			} catch (Exception e) {
				this.target = this.arguments.get(2);
			}
		}
	}

	public abstract void execute();

	public void updateCounter() {
		this.machine.updateCounter();
	}

	public void setMachine(Machine machine) {
		this.machine = machine;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getArgument(int i) {
		return this.arguments.get(i);
	}

	public void addArgument(String arg) {
		this.arguments.add(arg);
	}
}
