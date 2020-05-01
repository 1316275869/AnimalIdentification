package com.example.myapplication.androidclient;

import java.io.Serializable;

public class Personal implements Serializable {

	private String p_name;
	private String p_useid ;
	private String p_password ;
	private String p_headphoto;



	public Personal() {

	}

	public Personal(String p_name, String p_useid, String p_password, String p_headphoto) {
		super();
		this.p_name = p_name;
		this.p_useid = p_useid;
		this.p_password = p_password;
		this.p_headphoto = p_headphoto;
	}

	public String getP_name() {
		return p_name;
	}

	public void setP_name(String p_name) {
		this.p_name = p_name;
	}
	public String getP_useid() {
		return p_useid;
	}
	public void setP_useid(String p_useid) {
		this.p_useid = p_useid;
	}
	public String getP_password() {
		return p_password;
	}


	public void setP_password(String p_password) {
		this.p_password = p_password;
	}

	public String getP_headphoto() {
		return p_headphoto;
	}
	public void setP_headphoto(String p_headphoto) {
		this.p_headphoto = p_headphoto;
	}

	public String tString() {
		return  p_name + "&" + p_useid + "&" + p_password + "&"
				+ p_headphoto;
	}
	public  static Personal splitMsg(String msg){
		String[] msgsplit=msg.split("&");
		if (msgsplit[1]==null){
			return null;
		}else {
			Personal personal=new Personal(msgsplit[0],msgsplit[1],msgsplit[2],msgsplit[3]);
			return personal;
		}
	}

}



