package eccang.zookeeper_server.entity;

import java.io.Serializable;

public class UrlMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	private int number;
	private String url;
	private int type;
	private int statu;

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getStatu() {
		return statu;
	}

	public void setStatu(int statu) {
		this.statu = statu;
	}

	@Override
	public String toString() {
		return "UrlMessage [number=" + number + ", url=" + url + ", type=" + type + ", statu=" + statu + "]";
	}
}
