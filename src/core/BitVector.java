package core;

import java.io.Serializable;

public class BitVector implements Serializable {
	long data;
	public BitVector(long data) {
		this.data = data;
	}
	
	public void setBit(int bit) {
		data = data | 1 << bit;
	}
	
	public void unsetBit(int bit) {
		data = data & ~(1 << bit); 
	}
	
}
