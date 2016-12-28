package project;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Loader {
	public static String load(MachineModel model, File file, int codeOffset, int memoryOffset) {
		int codeSize = 0;
		if (model == null || file == null) {
			return null;
		}
		try (Scanner input = new Scanner(file)) {
			boolean incode = true;
			while (input.hasNextLine()) {
				String line = input.nextLine();
				Scanner parser = new Scanner(line);
				int opcode = parser.nextInt(16);
				if (opcode == -1 && incode) {
					incode = false;
				}
				else if (opcode != -1 && incode) {
					int arg = parser.nextInt(16);
					model.setCode(codeOffset+codeSize, opcode, arg);
					codeSize++;
					
				}
				else if (!incode) {
					int arg = parser.nextInt(16);
					model.setData(opcode+memoryOffset, arg);
					parser.close();
				}
			}
			return "" + codeSize;
		} catch (FileNotFoundException e) {
			return "File " + file.getName() + " Not Found";
		} catch (ArrayIndexOutOfBoundsException e) {
			return "Array Index " + e.getMessage();
		} catch (NoSuchElementException e) {
			return "From Scanner: NoSuchElementException";
		}
	}
	public static void main(String[] args) {
		MachineModel model = new MachineModel();
		String s = Loader.load(model, new File("factorial.pexe"),100,200);
		for(int i = 100; i < 100+Integer.parseInt(s); i++) {
			System.out.println(model.getCode().getText(i));			
		}
		System.out.println(200 + " " + model.getData(200));
	}
}
