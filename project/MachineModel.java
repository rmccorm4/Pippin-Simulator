package project;

import java.util.Map;
import java.util.TreeMap;

public class MachineModel 
{
	//Parameters needed in TreeMap?
	public final Map<Integer, Instruction> INSTRUCTIONS = new TreeMap<>();
	private CPU cpu = new CPU();
	private Memory memory = new Memory();
	private HaltCallback callback;
	Code code = new Code();
	Job[] jobs = new Job[2];
	private Job currentJob;
	
	public MachineModel()
	{
		this(() -> System.exit(0));
	}
	
	//Should we do anything to check that arg is int?
	public MachineModel(HaltCallback callback)
	{
		this.callback = callback;
		//instantiate jobs elements to new Jobs in machineModel constructor
		jobs[0] = new Job();
		jobs[1] = new Job();
		currentJob = jobs[0];
		jobs[0].setStartcodeIndex(0);
		jobs[0].setStartmemoryIndex(0);
		jobs[1].setStartcodeIndex(code.CODE_MAX/4);
		jobs[1].setStartmemoryIndex(Memory.DATA_SIZE/2);
		
		
		//INSTRUCTION_MAP entry for "NOP"
        INSTRUCTIONS.put(0x0, arg -> {
            cpu.incrementIP();
        });
        
		//INSTRUCTION_MAP entry for "LODI"
        INSTRUCTIONS.put(0x1, arg -> {
        	cpu.setAccumulator(arg);
            cpu.incrementIP();
        });
        
      //INSTRUCTION_MAP entry for "LOD"
        INSTRUCTIONS.put(0x2, arg -> {
        	int arg1 = memory.getData(cpu.getMemoryBase()+arg);
        	cpu.setAccumulator(arg1);
            cpu.incrementIP();
        });
        
      //INSTRUCTION_MAP entry for "LODN"
        INSTRUCTIONS.put(0x3, arg -> {
            int arg1 = memory.getData(cpu.getMemoryBase()+arg);
            int arg2 = memory.getData(cpu.getMemoryBase()+arg1);
            cpu.setAccumulator(arg2);
            cpu.incrementIP();
        });
        
        //INSTRUCTION_MAP entry for "STO"
        INSTRUCTIONS.put(0x4, arg -> {
        	int accu = cpu.getAccumulator();
        	memory.setData(cpu.getMemoryBase()+arg, accu);
            cpu.incrementIP();
        });
        
        //INSTRUCTION_MAP entry for "STON"
        INSTRUCTIONS.put(0x5, arg -> {
        	int accu = cpu.getAccumulator();
        	int memoryVal1 = memory.getData(cpu.getMemoryBase()+arg);
        	memory.setData(cpu.getMemoryBase()+memoryVal1, accu);
            cpu.incrementIP();
        });
        
      //INSTRUCTION_MAP entry for "JMPI"
        INSTRUCTIONS.put(0x6, arg -> {
        	cpu.setInstructionPointer(cpu.getInstructionPointer()+arg);
        });
        
      //INSTRUCTION_MAP entry for "JUMP"
        INSTRUCTIONS.put(0x7, arg -> {
        	int arg1 = cpu.getMemoryBase()+arg;
        	cpu.setInstructionPointer(cpu.getInstructionPointer()+memory.getData(arg1));
        });
        
      //INSTRUCTION_MAP entry for "JMZI"
        INSTRUCTIONS.put(0x8, arg -> {
        	if(cpu.getAccumulator() == 0)
        	{
        		cpu.setInstructionPointer(cpu.getInstructionPointer()+arg);
        	}
        	else
        	{
                cpu.incrementIP();
        	}
        });
		
      //INSTRUCTION_MAP entry for "JMPZ"
        INSTRUCTIONS.put(0x9, arg -> {
        	if(cpu.getAccumulator() == 0)
        	{
        		int arg1 = cpu.getMemoryBase()+arg;
            	cpu.setInstructionPointer(cpu.getInstructionPointer()+memory.getData(arg1));        	}
        	else
        	{
                cpu.incrementIP();
        	}
        });
        
		//INSTRUCTION_MAP entry for "ADDI"
        INSTRUCTIONS.put(0xA, arg -> {
            cpu.setAccumulator(cpu.getAccumulator() + arg);
            cpu.incrementIP();
        });

        //INSTRUCTION_MAP entry for "ADD"
        INSTRUCTIONS.put(0xB, arg -> {
            int arg1 = memory.getData(cpu.getMemoryBase()+arg);
            cpu.setAccumulator(cpu.getAccumulator() + arg1);
            cpu.incrementIP();
        });

        //INSTRUCTION_MAP entry for "ADDN"
        INSTRUCTIONS.put(0xC, arg -> {
            int arg1 = memory.getData(cpu.getMemoryBase()+arg);
            int arg2 = memory.getData(cpu.getMemoryBase()+arg1);
            cpu.setAccumulator(cpu.getAccumulator() + arg2);
            cpu.incrementIP();
        });
        
      //INSTRUCTION_MAP entry for "SUBI"
        INSTRUCTIONS.put(0xD, arg -> {
            cpu.setAccumulator(cpu.getAccumulator() - arg);
            cpu.incrementIP();
        });
        
      //INSTRUCTION_MAP entry for "SUB"
        INSTRUCTIONS.put(0xE, arg -> {
        	//arg = offset from memory location
            int arg1 = memory.getData(cpu.getMemoryBase()+arg);
            cpu.setAccumulator(cpu.getAccumulator() - arg1);
            cpu.incrementIP();
        });
        
      //INSTRUCTION_MAP entry for "SUBN"
        INSTRUCTIONS.put(0xF, arg -> {
            int arg1 = memory.getData(cpu.getMemoryBase()+arg);
            int arg2 = memory.getData(cpu.getMemoryBase()+arg1);
            cpu.setAccumulator(cpu.getAccumulator() - arg2);
            cpu.incrementIP();
        });
        
      //INSTRUCTION_MAP entry for "MULI"
        INSTRUCTIONS.put(0x10, arg -> {
            cpu.setAccumulator(cpu.getAccumulator() * arg);
            cpu.incrementIP();
        });
        
      //INSTRUCTION_MAP entry for "MUL"
        INSTRUCTIONS.put(0x11, arg -> {
        	//arg = offset from memory location
            int arg1 = memory.getData(cpu.getMemoryBase()+arg);
            cpu.setAccumulator(cpu.getAccumulator() * arg1);
            cpu.incrementIP();
        });
        
      //INSTRUCTION_MAP entry for "MULN"
        INSTRUCTIONS.put(0x12, arg -> {
            int arg1 = memory.getData(cpu.getMemoryBase()+arg);
            int arg2 = memory.getData(cpu.getMemoryBase()+arg1);
            cpu.setAccumulator(cpu.getAccumulator() * arg2);
            cpu.incrementIP();
        });
        
      //INSTRUCTION_MAP entry for "DIVI"
        INSTRUCTIONS.put(0x13, arg -> {
        	if(arg != 0)
        	{
        		cpu.setAccumulator(cpu.getAccumulator() / arg);
        		cpu.incrementIP();
        	}
        	else
        	{
        		throw new DivideByZeroException("Can't divide by zero!");
        	}
        });
        
      //INSTRUCTION_MAP entry for "DIV"
        INSTRUCTIONS.put(0x14, arg -> {
        	//arg = offset from memory location
            int arg1 = memory.getData(cpu.getMemoryBase()+arg);
            if(arg1 != 0)
            {
            	cpu.setAccumulator(cpu.getAccumulator() / arg1);
            	cpu.incrementIP();
            }
            else
        	{
        		throw new DivideByZeroException("Can't divide by zero!");
        	}
        });
        
      //INSTRUCTION_MAP entry for "DIVN"
        INSTRUCTIONS.put(0x15, arg -> {
            int arg1 = memory.getData(cpu.getMemoryBase()+arg);
            int arg2 = memory.getData(cpu.getMemoryBase()+arg1);
            if(arg2 != 0)
            {
	            cpu.setAccumulator(cpu.getAccumulator() / arg2);
	            cpu.incrementIP();
            }
            else
        	{
        		throw new DivideByZeroException("Can't divide by zero!");
        	}
        });
        
      //INSTRUCTION_MAP entry for "ANDI"
        INSTRUCTIONS.put(0x16, arg -> {
        	if(cpu.getAccumulator() != 0 && arg != 0)
        	{
        		cpu.setAccumulator(1);
        	}
        	else
        	{
        		cpu.setAccumulator(0);
        	}
        	cpu.incrementIP();
        });
        
      //INSTRUCTION_MAP entry for "AND"
        INSTRUCTIONS.put(0x17, arg -> {
        	if(cpu.getAccumulator() != 0 && memory.getData(cpu.getMemoryBase()+arg) != 0)
        	{
        		cpu.setAccumulator(1);
        	}
        	else
        	{
        		cpu.setAccumulator(0);
        	}
        	cpu.incrementIP();
        });
        
      //INSTRUCTION_MAP entry for "NOT"
        INSTRUCTIONS.put(0x18, arg -> {
        	if(cpu.getAccumulator() != 0)
        	{
        		cpu.setAccumulator(0);
        	}
        	else
        	{
        		cpu.setAccumulator(1);
        	}
        	cpu.incrementIP();
        });
        
      //INSTRUCTION_MAP entry for "CMPL"
        INSTRUCTIONS.put(0x19, arg -> {
        	if(memory.getData(cpu.getMemoryBase()+arg) < 0)
        	{
        		cpu.setAccumulator(1);
        	}
        	else
        	{
        		cpu.setAccumulator(0);
        	}
        	cpu.incrementIP();
        });
        
        //INSTRUCTION_MAP entry for "CMPZ"
        INSTRUCTIONS.put(0x1A, arg -> {
        	if(memory.getData(cpu.getMemoryBase()+arg) == 0)
        	{
        		cpu.setAccumulator(1);
        	}
        	else
        	{
        		cpu.setAccumulator(0);
        	}
        	cpu.incrementIP();
        });
        
      //INSTRUCTION_MAP entry for "HALT"
        INSTRUCTIONS.put(0x1F, arg -> {
        	callback.halt();
        });
        
        //INSTRUCTION MAP entry for "JMPN"
        INSTRUCTIONS.put(0x1B, arg -> {
        	int target = memory.getData(cpu.getMemoryBase() + arg);
        	cpu.setInstructionPointer(currentJob.getStartcodeIndex()+target);
        });
	}
	
	//check
	public Instruction get(int opCode)
	{
		return INSTRUCTIONS.get(opCode);
	}
	
	//Delegating Methods
	
	//check
	public int getAccumulator()
	{
		return cpu.getAccumulator();
	}
	//check
	public int getInstructionPointer()
	{
		return cpu.getInstructionPointer();
	}
	//check
	public int getMemoryBase()
	{
		return cpu.getMemoryBase();
	}
	//check
	public void setAccumulator(int accumulator) {
		cpu.setAccumulator(accumulator);
	}
	//check
	public void setInstructionPointer(int instructionPointer) {
		cpu.setInstructionPointer(instructionPointer);
	}
	//check
	public void setMemoryBase(int memoryBase) {
		cpu.setMemoryBase(memoryBase);
	}
	//check
	int[] getData() 
	{
		return memory.getData();
	}
	//check
	int getData(int index)
	{
		return memory.getData(index);
	}
	//check
	void setData(int index, int value)
	{
		memory.setData(index, value);
	}
	//check
	public void setCode(int i, int op, int arg)
	{
		code.setCode(i, op, arg);
	}
	//check
	public Code getCode()
	{
		return code;
	}
	//check
	public Job getCurrentJob() {
		return currentJob;
	}
	//check
	public void setJob(int i) {
		if(i != 0 && i != 1)
		{
			throw new IllegalArgumentException();
		}
		currentJob.setCurrentAcc(cpu.getAccumulator());
		currentJob.setCurrentIP(cpu.getInstructionPointer());
		currentJob = jobs[i];
		cpu.setAccumulator(currentJob.getCurrentAcc());
		cpu.setInstructionPointer(currentJob.getCurrentIP());
		cpu.setMemoryBase(currentJob.getStartmemoryIndex());
	}
	//check
	public int getChangedIndex()
	{
		return memory.getChangedIndex();
	}
	//check
	public States getCurrentState()
	{
		return currentJob.getCurrentState();
	}
	//check
	public void setCurrentState(States currentState)
	{
		currentJob.setCurrentState(currentState);
	}
	//check
	public void step()
	{
		try
		{
			int ip = cpu.getInstructionPointer();
			if(ip<currentJob.getStartcodeIndex() || ip >= currentJob.getStartcodeIndex()+currentJob.getCodeSize())
			{
				throw new CodeAccessException("instruction pointer out of range!");
			}
			
			int opcode = code.getOp(ip);
			int arg = code.getArg(ip);
			get(opcode).execute(arg);
		}
		catch(Exception e)
		{
			callback.halt();
			throw e;
		}
	}
	//check
	public void clearJob()
	{
		memory.clear(currentJob.getStartmemoryIndex(), currentJob.getStartmemoryIndex()+memory.DATA_SIZE/2);
		code.clear(currentJob.getStartcodeIndex(), currentJob.getCodeSize());
		cpu.setAccumulator(0);
		cpu.setInstructionPointer(currentJob.getStartcodeIndex());
		currentJob.reset();
	}
	
	
}
