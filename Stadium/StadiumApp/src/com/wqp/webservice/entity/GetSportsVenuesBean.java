package com.wqp.webservice.entity;

import java.io.Serializable;

public class GetSportsVenuesBean implements Serializable { 
	private static final long serialVersionUID = 1L;
	
	private int VenuesID;
	private String VenuesImager;
	private String VenuesName;
	private String Address;
	private String ReservePhone;
	private String RideRoute;
	private String VenuesEnvironment;
	
	public int getVenuesID() {
		return VenuesID;
	}
	public void setVenuesID(int venuesID) {
		VenuesID = venuesID;
	}
	public String getVenuesImager() {
		return VenuesImager;
	}
	public void setVenuesImager(String venuesImager) {
		VenuesImager = venuesImager;
	}
	public String getVenuesName() {
		return VenuesName;
	}
	public void setVenuesName(String venuesName) {
		VenuesName = venuesName;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public String getReservePhone() {
		return ReservePhone;
	}
	public void setReservePhone(String reservePhone) {
		ReservePhone = reservePhone;
	}
	public String getRideRoute() {
		return RideRoute;
	}
	public void setRideRoute(String rideRoute) {
		RideRoute = rideRoute;
	}
	public String getVenuesEnvironment() {
		return VenuesEnvironment;
	}
	public void setVenuesEnvironment(String venuesEnvironment) {
		VenuesEnvironment = venuesEnvironment;
	}
	

}
