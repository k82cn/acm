import java.util.HashMap;
import java.util.Map;

public class Translator {

	private static Map<String, String> instructionClasses = new HashMap<String, String>();

	static {
		instructionClasses.put("add", "Add");
		instructionClasses.put("sub", "Sub");
		instructionClasses.put("mul", "Mul");
		instructionClasses.put("div", "Div");
		instructionClasses.put("lin", "Lin");
		instructionClasses.put("out", "Out");
		instructionClasses.put("bnz", "Bnz");
	}

	public static Instruction instruction(String ins, Machine machine)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {

		String[] args = ins.split(" ");

		Instruction instruction = (Instruction) Class.forName(
				instructionClasses.get(args[1])).newInstance();

		instruction.setMachine(machine);

		instruction.setLabel(args[0]);
		instruction.setOperator(args[1]);

		for (int i = 2; i < args.length; i++) {
			instruction.addArgument(args[i]);
		}

		instruction.parseArgs();

		return instruction;
	}

}
