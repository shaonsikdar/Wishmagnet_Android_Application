package com.audacity.wishmagnet.model;

public class MembersModel {

	public String memberName = new String();
	public String lastActiveTime = new String();
	
	@Override
	public String toString() {
		
		return memberName + " " + lastActiveTime;
	}
}//end of MembersModel