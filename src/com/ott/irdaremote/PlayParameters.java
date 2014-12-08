package com.ott.irdaremote;

public class PlayParameters {
	String boardname;
	String ir_address;
	String key;

	public PlayParameters(String boardname, String key, String ir_address) {
		super();
		this.boardname = boardname;
		this.ir_address = ir_address;
		this.key = key;
	}

	public String getIr_address() {
		return ir_address;
	}

	public void setIr_address(String ir_address) {
		this.ir_address = ir_address;
	}

	public String getBoardname() {
		return boardname;
	}

	public void setBoardname(String boardname) {
		this.boardname = boardname;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
