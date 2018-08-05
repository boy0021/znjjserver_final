package model;

public class Station {
	/**站号 id*/
	public int stationid;
	/**路线id*/
	public int roadid;
	/**下一站的id*/
	public int nextStationid;
	/**纬度*/
	public double lat;
	/**经度*/
	public double lon;
	/**中文名字*/
	public String name;
	/**二维码的字符串*/
	public String twoDimensionalCode;
	
	@Override
	public String toString() {
		return "Station [lat=" + lat + ", lon=" + lon + ", name=" + name
				+ ", nextStationid=" + nextStationid + ", roadid=" + roadid
				+ ", stationid=" + stationid + ", twoDimensionalCode="
				+ twoDimensionalCode + "]";
	}
}
