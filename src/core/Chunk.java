package core;

public class Chunk {
	public String itemId;
	public int chunkId;
	public int size;
    public String appId;
	
	public Chunk (String itemId, int chunkId, int size, String appId) {
		this.itemId = itemId;
		this.chunkId = chunkId;
		this.size = size;
        this.appId = appId;
	}

    public Chunk (Chunk other) {
        this.itemId = other.itemId;
        this.chunkId = other.chunkId;
        this.size = other.size;
        this.appId = other.appId;
    }
}