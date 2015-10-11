import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Machine {

	private int[] registers = new int[32];

	private int counter;

	private ArrayList<Instruction> instructions = new ArrayList<Instruction>();

	private Map<String, Integer> instructionIndex = new HashMap<String, Integer>();

	public Machine(String cmdFile) throws IOException {
		FileReader fr = new FileReader(cmdFile);
		BufferedReader br = new BufferedReader(fr);
		String cmd = null;
		while ((cmd = br.readLine()) != null) {
			Instruction ins;
			try {
				ins = Translator.instruction(cmd, this);
				instructions.add(ins);
				this.instructionIndex.put(ins.getLabel(),
						instructions.size() - 1);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		br.close();
		fr.close();
	}

	public void execute() {
		while (counter < instructions.size()) {
			Instruction ins = this.instructions.get(counter);
			ins.execute();
			ins.updateCounter();
		}
	}

	public void setRegister(int num, int val) {
		registers[num] = val;
	}

	public int getRegister(int num) {
		return registers[num];
	}

	public void updateCounter() {
		this.counter++;
	}

	public void setCounter(String label) {
		this.counter = this.instructionIndex.get(label);
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder("Registers:");
		
		for (int i = 0; i < this.registers.length; i++)
		{
			sb.append(" ").append(this.registers[i]);
		}
		
		return sb.toString();
	}
}
