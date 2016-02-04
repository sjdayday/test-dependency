package org.test;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class FrameCountingInputStream extends FileInputStream
{
	private byte[] test;
	private byte[] single;
	private int minimum = 625; 
	private int frames = 0; 
	private int index = 0;
	private int where;
	private int lastHeader;
	private int nextHeader;
	private int length; 
	public FrameCountingInputStream(File file) throws FileNotFoundException
	{
		super(file); 
		test = new byte[]{0x00,0x00,0x00};
		single = new byte[1];
		where = 0; 
		lastHeader = 0; 
		nextHeader = 0; 
		minimum = 0;
		length = 0; 
	}
	@Override
	public int read() throws IOException
	{
		index = super.read(single);
		if (index != -1)
		{
			if (foundHeader()) 
			{
				minimum = 0; 
				frames++; 
				lastHeader = nextHeader; 
				nextHeader = where; 
				length = nextHeader - lastHeader; 
				if ((length != 627) && (length != 626))
				{
					System.out.println(frames+": non-standard header of length "+length+" found at "+where);
				}
			}
		}
		minimum++; 
		where++;
		return index;
	}
	
	
	public int frames()
	{
		return frames; 
	}
	private  boolean foundHeader()
	{
		test[0] = test[1];
		test[1] = test[2];
		test[2] = single[0];
			if ((test[0] & 0xff) != 0xff) return false;
			if ((test[1] & 0xff) != 0xfb) return false;
			if (((test[2] & 0xff) == 0xb2) || ((test[2] & 0xff) == 0xb0)) 
				if (minimum > 625)
				{
					return true;
				}
		return false; 
	}

}
