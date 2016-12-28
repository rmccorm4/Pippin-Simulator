package project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Assembler {
	public static String assemble(File input, File output) {
		ArrayList<String> inText = new ArrayList<String>();
		ArrayList<String> code = new ArrayList<String>();
		ArrayList<String> data = new ArrayList<String>();
		try {
			Scanner scanner = new Scanner(input);
			//commenting this out
			//String line = scanner.nextLine();
			
			//Add all lines in file to ArrayList inText
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				inText.add(line);
				
			}
			
			scanner.close();
			
			String returnValue = "success";
			//Loop through all elements in inText up to second to last element
			//Search for first blank line
			int errorLine = -1;
			for (int i = 0; i < inText.size() - 1; i++) {
				if (inText.get(i).trim().length() == 0 && inText.get(i+1).trim().length() > 0) {
					returnValue = "Error: line " + (i+1) + " is a blank line"; 
					errorLine = i;
					break;
				}
			}
			
			//If error of blank line hasn't been found, upper limit of next loop is end of inText
			int j = inText.size();

			//If error of blank line has been found, upper limit of next loop is this line
			if (errorLine != -1) {
				j = errorLine;
			}
			//Search for white space error
			for (int i = 0; i < j; i++) {
				if (inText.get(i).trim().length() > 0) {
					if (inText.get(i).charAt(0) == ' ' || inText.get(i).charAt(0) == '\t') {
						returnValue = "Error: line " + (i+1) + " starts with white space";
						errorLine = i;
						break;
					}
				}
			}
			//If error of white space has been found, change upper limit of next loop to this line
			if (errorLine != -1) {
				j = errorLine;
			}
			//Search for non upper-case DATA error
			for (int i = 0; i < j; i++) {
				if (inText.get(i).trim().toUpperCase().equals("DATA")) {
					if (!(inText.get(i).trim().equals("DATA"))) {
						returnValue = "Error: line " + (i+1) + " does not have DATA in upper case";
						errorLine = i;
						break;
					}
				}
			}
			//If error of non upper case DATA has been found, upper limit of next loop is this line
			if (errorLine != -1) {
				j = errorLine;
			}
			
			//Copy all lines in inText, up to first error, into code and data ArrayLists
			boolean dataFound = false;
			for (int i = 0; i < j; i++) {
				if (inText.get(i).trim().equals("DATA")) {
					dataFound = true;
				}
				else if (!dataFound) {
					if (inText.get(i).trim().length() > 0) {
						code.add(inText.get(i).trim());
					}
				}
				//
				//RYAN CHANGED THIS, it originally said  code.add(inText.....)
				//dataFound is never becoming true!
				else if (dataFound) {
					if (inText.get(i).trim().length() > 0) {
						data.add(inText.get(i).trim());
					}
				}
			}
			//Create ArrayList to hold instructions if no errors are found
			ArrayList<String> outText = new ArrayList<String>();
			//Search for errors within code ArrayList AND add to outText if no errors are found
			for (int i = 0; i < code.size(); i++) {
				String[] parts = code.get(i).trim().split("\\s+");
				if (InstructionMap.sourceCodes.contains(parts[0].toUpperCase()) && !(InstructionMap.sourceCodes.contains(parts[0]))) {
					returnValue = "Error: line " + (i+1) + " does not have the instruction mnemonic in upper case";
					errorLine = i;
					break;
				}
				if (InstructionMap.noArgument.contains(parts[0]) && parts.length != 1) {
					returnValue = "Error: line " + (i+1) + " has an illegal argument";
					errorLine = i;
					break;
				}
				if (!(InstructionMap.noArgument.contains(parts[0]))) {
					if (parts.length >= 3) {
						returnValue = "Error: line " + (i+1) + " has more than one argument";
						errorLine = i;
						break;
					}
					else if (parts.length == 1) {
						 returnValue = "Error: line " + (i+1) + " is missing an argument";
						 errorLine = i;
						 break;
					}
				}
				if (parts.length == 2) {
					if (parts[1].startsWith("#")) {
						if (!(InstructionMap.immediateOK.contains(parts[0]))) {
							returnValue = "Error: line " + (i+1) + " instruction does not use immediate";
							errorLine = i;
							break;
						}
						else {
							int arg = 0;
							if (parts[0].equals("JUMP")) {
								 parts[0] = "JMPI";
							}
							else if (parts[0].equals("JMPZ")) {
								parts[0] = "JMZI";
							}
							else {
								parts[0] = parts[0] + "I";
							}
							//If number is a hexadecimal integer, do the normal processing of changing parts[0]
							try {
								parts[1] = parts[1].substring(1);
								arg = Integer.parseInt(parts[1],16);
							} catch (NumberFormatException e) {
								returnValue = "Error: line " + (i+1) 
									+ " does not have a numeric argument";
								errorLine = i;
								break;
							}
						}
					}
					else if (parts[1].startsWith("&")) {
						if (!(InstructionMap.indirectOK.contains(parts[0]))) {
							returnValue = "Error: line " + (i+1) + " instruction does not use indirect";
							errorLine = i;
							break;
						}
						else {
							int arg = 0; 
							try {
								parts[1] = parts[1].substring(1);
								arg = Integer.parseInt(parts[1],16);
								if (parts[0].equals("JUMP")) {
									parts[0] = "JMPN";
								}
								else {
									parts[0] = parts[0] + "N";
								}
							} catch (NumberFormatException e) {
								returnValue = "Error: line " + (i+1) 
									+ " does not have a numeric argument";
								errorLine = i;
								break;
							}
						}
					}
					else if (!(InstructionMap.opcode.containsKey(parts[0]))) {
						return "Error: line " + (i+1) + " bad mnemonic";
					}
				}
				int opcode = InstructionMap.opcode.get(parts[0]);
				if (parts.length == 1) {
					outText.add(Integer.toHexString(opcode).toUpperCase() + " 0");
				}
				else if (parts.length == 2) {
					outText.add(Integer.toHexString(opcode).toUpperCase() + " " + parts[1]);
				}
			}
			outText.add("-1");
				//Search for errors within data ArrayList
				for (int i = 0; i < data.size(); i++) {
					String[] parts = data.get(i).trim().split("\\s+");
					if (parts.length == 2) {
						int arg = 0; 
						try {
							arg = Integer.parseInt(parts[0],16); 
						} catch (NumberFormatException e) {
							if (errorLine > i+1+code.size() || errorLine == -1) {
								returnValue = "Error: line " + (i+2+code.size()) 
									+ " does not have only numeric arguments";
							}
							break;
						}
						try {
							arg = Integer.parseInt(parts[1],16); 
						} catch (NumberFormatException e) {
							if (errorLine > i+1+code.size() || errorLine == -1) {
								returnValue = "Error: line " + (i+2+code.size()) 
								+ " does not have only numeric arguments";
							}
							break;
						}
					}
					else if (parts.length == 1) {
						if (errorLine > i+1+code.size() || errorLine == -1) {
							returnValue = "Error: line " + (i+2+code.size()) + "only contains one piece of data";
						}
						break;
					}
					else if (parts.length > 2){
						if (errorLine > i+1+code.size() || errorLine == -1) {
							returnValue = "Error: line " + (i+2+code.size()) + " contains more than two pieces of data";
						}
						break;
					}
				}
				if (returnValue == "success") {
					outText.addAll(data);
				}
			PrintWriter out = new PrintWriter(output);
			
			for (String element: outText) {
				out.println(element);
			}
			
			out.close();
			return returnValue;
		} catch (FileNotFoundException e) {
			return "file " + input.getName() + " not found";
		}
	}
	public static void main(String[] args) {
		File infile = new File("21e.pasm");
		File outfile = new File("21e.pexe");
		System.out.println(assemble(infile, outfile));
	}
}
