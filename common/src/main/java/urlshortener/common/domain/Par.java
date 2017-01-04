package urlshortener.common.domain;

public class Par{
	private String ip;
	private int numVeces;
	
	public Par(){}

	public Par(String ip, int numVeces){
		this.ip = ip;
		this.numVeces = numVeces;
	}
	
	public String getIp(){
		return ip;
	}
	
	public int getNumVeces(){
		return numVeces;
	}
	
	public void setNumVeces(int numVeces) {
		this.numVeces = numVeces;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
}
