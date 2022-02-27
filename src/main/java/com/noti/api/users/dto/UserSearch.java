package com.noti.api.users.dto;

import java.util.Arrays;

import lombok.Getter;
import lombok.Setter;

/** 
 * 사용자 검색 조건을 정의 합니다.
 */
@Getter
@Setter
public class UserSearch {
	
	 private int page = 0;
	    private int size = 0;
	    private String orderOptionName;
	    private String orderOptionValue;
	    private String searchOptionName;
	    private String searchOptionValue;
	    
	    public boolean isSearch() {

	    	String [] optionNameList = {"userEmail", "userName", "userAdminLevel"};
	    	
	    	if( this.searchOptionName == null || this.searchOptionValue == null || this.searchOptionName.equals("") || this.searchOptionValue.equals("") ) {
	    		return false;
	    	}
	    	if( !Arrays.asList(optionNameList).contains(this.searchOptionName) ) {
	    		return false;
	    	}
	    	
	    	return true;
		}
	    
	    public boolean isOrder() {
	    	
	    	String [] optionNameList = { "userId", "userEmail", "userName", "createDate", "updateDate" };
	    	String [] optionValueList = { "desc", "asc" };
	    	
	    	if( this.orderOptionName == null || this.orderOptionValue == null || this.orderOptionName.equals("") || this.orderOptionValue.equals("") ) {
	    		return false;
	    	}
	    	
	    	if( !Arrays.asList(optionNameList).contains(this.orderOptionName) ) {
	    		return false;
	    	}

	    	if( !Arrays.asList(optionValueList).contains(this.orderOptionValue) ) {
	    		return false;
	    	}

	    	this.orderOptionName = this.orderOptionName.equals("userId") ? "_id" : this.orderOptionName; 

	    	return true;
		}
}
