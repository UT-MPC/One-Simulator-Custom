package core;

import java.io.Serializable;

public class Item implements Serializable {
	public String name;
	public int chunkSize;
	public BitVector bv;
	
	public Item (String name, int chunkSize, BitVector bv) {
		this.name = name;
		this.chunkSize = chunkSize;
		this.bv = bv;
	}
}
