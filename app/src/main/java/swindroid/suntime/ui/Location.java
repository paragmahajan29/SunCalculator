package swindroid.suntime.ui;

import java.io.Serializable;

public class Location implements  Serializable{
	
	String name;
	double latitude;
	double longitude;
	String location;
	
	public Location(String name, double latitude, double longitude,
			String location) {
		super();
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.location = location;
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	@Override
	public String toString() {
		return "Location [name=" + name + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", location=" + location + "]";
	}

}
