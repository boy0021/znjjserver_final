package model;

public class Station {
	/**վ�� id*/
	public int stationid;
	/**·��id*/
	public int roadid;
	/**��һվ��id*/
	public int nextStationid;
	/**γ��*/
	public double lat;
	/**����*/
	public double lon;
	/**��������*/
	public String name;
	/**��ά����ַ���*/
	public String twoDimensionalCode;
	
	@Override
	public String toString() {
		return "Station [lat=" + lat + ", lon=" + lon + ", name=" + name
				+ ", nextStationid=" + nextStationid + ", roadid=" + roadid
				+ ", stationid=" + stationid + ", twoDimensionalCode="
				+ twoDimensionalCode + "]";
	}
}
