package com.demo.smarthome.device;

import java.io.Serializable;

/**
 * 设备类
 *
 * @author Administrator
 *
 */
public class Dev implements Serializable {
	private static final long serialVersionUID = -2843552716334804888L;
	private String idHex;
	private String devType;
	private String devTypeName;
	private String nickName = "";
	private String lastUpdate = "";
	private String torken = "";
	private String ipPort = "";
	private String pass = "";
	private int errorCount =0;
	private final int ERROR_COUNT_MAX=180;
	private boolean dataChange;
	private boolean check;
	private String id;//设备编码
	private String recordId;	//
	private String addtime;
	private int master;
	private String devPic;
	private String devVersion;



	public void runStep(){
		if(errorCount>=ERROR_COUNT_MAX){
			this.onLine=false;
		}else{
			errorCount++;
		}
	}
	public boolean isDataChange() {
		return this.dataChange;
	}

	public void isDataChange(boolean ok) {
		this.dataChange = ok;
		if(ok){
			onLine=ok;
			errorCount=0;
		}
	}

	private int lampRVal;

	public int getLampRVal() {
		return lampRVal;
	}

	public void setLampRVal(int lampRVal) {
		if (lampRVal < 0) {
			lampRVal = 0;
		}
		if (lampRVal > 99) {
			lampRVal = 99;
		}
		this.lampRVal = lampRVal;
		isDataChange(true);
	}

	public int getLampGVal() {
		return lampGVal;
	}

	public void setLampGVal(int lampGVal) {
		if (lampGVal < 0) {
			lampGVal = 0;
		}
		if (lampGVal > 99) {
			lampGVal = 99;
		}
		this.lampGVal = lampGVal;
		isDataChange(true);
	}

	public int getLampBVal() {
		return lampBVal;
	}

	public void setLampBVal(int lampBVal) {
		if (lampBVal < 0) {
			lampBVal = 0;
		}
		if (lampBVal > 99) {
			lampBVal = 99;
		}
		this.lampBVal = lampBVal;
		isDataChange(true);
	}

	private int lampGVal;
	private int lampBVal;

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	private double temp = 0;
	private boolean lightState = false;

	public double getTemp() {
		return temp;
	}

	public void setTemp(double temp) {
		this.temp = temp;
		isDataChange(true);
	}

	public boolean isLightState() {
		return lightState;
	}

	public void setLightState(boolean lightState) {
		this.lightState = lightState;
		isDataChange(true);
	}

	public String getTorken() {
		return torken;
	}

	public void setTorken(String torken) {
		this.torken = torken;
	}

	public String getIpPort() {
		return ipPort;
	}

	public void setIpPort(String ipPort) {
		this.ipPort = ipPort;
	}

	public String getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	private boolean onLine = false;

	public String getIdHex() {
		return idHex;
	}

	public void setIdHex(String idHex) {
		this.idHex = idHex;
	}

	public String getId() {
		return id;
	}

	public void setId(String deviceId) {
		this.id = deviceId;
	}

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getDevType() {
		return devType;
	}

	public void setDevType(String devType) {
		this.devType = devType;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getDevTypeName() {
		return devTypeName;
	}

	public void setDevTypeName(String devTypeName) {
		this.devTypeName = devTypeName;
	}

	public int getMaster() {
		return master;
	}

	public void setMaster(int master) {
		this.master = master;
	}

	public String getDevPic() {
		return devPic;
	}

	public void setDevPic(String devPic) {
		this.devPic = devPic;
	}

	public String getDevVersion() {
		return devVersion;
	}

	public void setDevVersion(String devVersion) {
		this.devVersion = devVersion;
	}

	@Override
	public String toString() {
		return "id:" + id + " " + (onLine ? "在线" : "不在线") + "  nickName:"
				+ nickName + "  lastUpdate:" + lastUpdate + "  torken:"
				+ torken + "  ipPort:" + ipPort;
	}

	public boolean isOnLine() {
		return onLine;
	}

	public void setOnLine(boolean onLine) {
		this.onLine = onLine;
	}

}
