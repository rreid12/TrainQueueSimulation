package edu.wit.comp2000.group4.application3;

/*
 * Course: Data Structures
 * Assignment: #3 - Train Simulation
 * Group: #4
 * Team Members: Taylor, Ryan Reid, Schyler
 */

/**
 * A Passenger is the smallest component of a big network of Stations, Trains,
 * Platforms, etc. Will keep track of distance traveled, destination, and
 * origin, but won't exactly do anything but exist for the Simulation.
 * 
 * @author reidr
 * Other contributors: Schyler
 */

public class Passenger {
	private Station origin; // the station where the passenger waits to get on a
							// train
	private Station destination; // where the passenger wants to go
	private static int passengerCount = 0;
	private final int passengerID; // individual ID for passenger (could change this to array
					// index in Station/Train implementation
	private TrainRoute trainRoute;
	private boolean isInbound;

	/**
	 * Constructor
	 * @param orig
	 * @param dest
	 * @param trnRoute
	 */
	public Passenger(Station orig, Station dest, TrainRoute trnRoute) {
		passengerCount++;
		this.passengerID = passengerCount;
		this.trainRoute = trnRoute;
		this.origin = orig;
		this.destination = dest;
		addPassengerToStationPlatform();
		this.origin.enqueuePassenger(this);
	}

	/**
	 * Increments stationsPassed as the train carrying the passenger passes
	 * other stations
	 */
	private void addPassengerToStationPlatform() {
		isInbound = false;
		
		if((destination.getPosition() - origin.getPosition() + trainRoute.getRouteLength()) 
			% trainRoute.getRouteLength() < trainRoute.getRouteLength()/2) {
			isInbound = false;
		} else {
			isInbound = true;
		}
	}
	
	/****************************
	 *	Getters and Setters 	*
	 ****************************/
	
	/**
	 * 
	 * @return isInbound
	 */
	public boolean isInbound() {
		return isInbound;
	}
	
	/**
	 * 
	 * @return passengerID
	 */
	public int getID() {
		return passengerID;
	}
	
	/**
	 * 
	 * @return origin
	 */
	public Station getOrigin() {
		return origin;
	}
	
	/**
	 * 
	 * @return destination
	 */
	public Station getDestination() {
		return destination;
	}
	
	/**
	 * Overrides Object's toString() method to print passenger and their origin/destination
	 */
	@Override
	public String toString() {
		return "Passenger " + this.passengerID + " [" + this.origin + "->" + this.destination + "]";
	}
	
	/********************
	 * 	Testing methods	*
	 ********************/
	
	public static void main(String[]args){
		// Testing constructor & toString()
		Passenger p = new Passenger(new Station(3), new Station(8), new TrainRoute(10));
		System.out.println(p);
		
		// Testing addPassengerToStationPlatform() and isInbound()
		p.addPassengerToStationPlatform();
		System.out.println(p.isInbound());
		
		// Testing getID()
		System.out.println(p.getID());
		
		// Testing getOrigin()
		System.out.println(p.getOrigin());
		
		// Testing getDestination()
		System.out.println(p.getDestination());
	}
}
