package core;

import java.io.Serializable;
import java.util.Vector;

public class Item implements Serializable {
	public String name;
	public int size;
	public BitVector bv;
	
	public Item (String name, int size, BitVector bv) {
		this.name = name;
		this.size = size;
		this.bv = bv;
	}
}
