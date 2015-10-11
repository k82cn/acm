import java.io.FileReader;
import java.io.IOException;


public class SML {

	public static void main(String[] args) throws IOException {

		if (args.length != 1)
		{
			throw new IllegalArgumentException("Usage: java -cp . SML instruction_file");
		}
		
		System.out.println("The source code of program");
		System.out.println("================================================");
		char[] cbuf = new char[1024];
		FileReader fr = new FileReader(args[0]);
		fr.read(cbuf);
		System.out.print(cbuf);
		fr.close();
		
		System.out.println("\n");
		System.out.println("The output of program:");
		System.out.println("================================================");
		
		Machine machine = new Machine(args[0]);
		
		machine.execute();

		System.out.println("\nThe status of machine:");
		System.out.println("================================================");
		
		System.out.println(machine.toString());
		
	}

}
