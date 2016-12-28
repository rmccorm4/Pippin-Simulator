package project;

public class Code 
{
	public static final int CODE_MAX = 2048;
	int[] codeArray = new int[CODE_MAX];
	
	public int getOp(int i)
	{
		return codeArray[2*i];
	}
	public int getArg(int i)
	{
		return codeArray[2*i + 1];
	}
	public void clear(int start, int end)
	{
		for(int i = 2*start; i<2*end; i++)
		{
			codeArray[i] = 0;
		}
	}
	
	public String getText(int i)
	{
		String s1 = Integer.toHexString(codeArray[2*i]).toUpperCase();
		if(codeArray[2*i + 1] < 0)
		{
			String s2 = "-" + Integer.toHexString(-codeArray[2*i+1]).toUpperCase();
			return s1 + " " + s2;
		}
		
		String s2 = Integer.toHexString(codeArray[2*i+1]).toUpperCase();
		return s1 + " " + s2;
	}
	
	public void setCode(int i, int op, int arg)
	{
		codeArray[2*i] = op;
		codeArray[2*i + 1] = arg;
	}
	
	public String getHex(int i)
	{
		return Integer.toHexString(codeArray[2*i]).toUpperCase() + " " + Integer.toHexString(codeArray[2*i+1]).toUpperCase();
	}
	
	public String getDecimal(int i)
	{
		return InstructionMap.mnemonics.get(codeArray[2*i]) + " " + codeArray[2*i+1];
	}
}
