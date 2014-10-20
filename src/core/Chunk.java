package core;

import java.util.UUID;

public class Chunk {
	UUID itemID;
	int chunkId;
	String data;
	
	public Chunk (UUID itemID, int chunkId, String data) {
		this.itemID = itemID;
		this.chunkId = chunkId;
		this.data = data;
	}
}