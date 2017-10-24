package edu.wit.comp2000.group4.application3;

import java.util.ArrayList;
import java.util.Random;

/*
 * Course: Data Structures
 * Assignment: #3 - Train Simulation
 * Group: #4
 * Team Members: Taylor, Ryan Reid, Schyler
 */

/**
 * 
 * @author reidr
 * Other contributors: Schyler, Taylor
 * */

public class TrainRoute {
	private ArrayList<Station> stations = new ArrayList<Station>();
	private ArrayList<Train> inboundTrack = new ArrayList<Train>();
	private ArrayList<Train> outboundTrack = new ArrayList<Train>();
	//private final int DEFAULT_CAPACITY = 5;
	private int routeLength;
	Random rand = new Random();
	
	/**
	 * Constructor
	 * @param length
	 */
	public TrainRoute(int length){
		this.routeLength = length;
	}
	
	/**
	 * Updates the status of the route and the simulation as a whole...moves the trains around the route
	 */
	public void moveAlong(){
		
		if(!inboundTrack.isEmpty()) {
			for(Train tr : inboundTrack) {
				tr.update();
			}
		}
		
		if(!outboundTrack.isEmpty()) {
			for(Train tr : outboundTrack) {
				tr.update();
			}
		}
	}
	
	/**
	 * Used to add a station to the route in the set up of the simulation
	 * @param stat
	 */
	public void addStation(Station stat) {
		stations.add(stat);
	}
	
	/**
	 * Used to add a train to the route in the set up of the simulation
	 * @param tr
	 */
	public void addTrain(Train tr) {
		if(tr.isInbound()) {
			inboundTrack.add(tr);
		}
		else{
			outboundTrack.add(tr);
		}
	}
	
	/**
	 * gets the length of the route
	 * @return
	 */
	public int getRouteLength() {
		return routeLength;
	}
	
	/**
	 * Finds the next station on the route for outbound trains
	 * @param stat - current station
	 * @return - next station train will be heading toward
	 */
	public Station getNextOutboundStation(Station stat) {
		int indexOfNewStation = stations.indexOf(stat) + 1;
		
		if(indexOfNewStation >= stations.size()) {
			return stations.get(0);
		}
		else {
			return stations.get(indexOfNewStation);
		}
	}
	
	/**
	 * Finds the next station on the route for inbound trains
	 * @param stat
	 * @return - next station train will be heading toward
	 */
	public Station getNextInboundStation(Station stat) {
		int indexOfNewStation = stations.indexOf(stat) - 1;
		
		if(indexOfNewStation < 0) {
			return stations.get(stations.size() - 1);
		}
		else {
			return stations.get(indexOfNewStation);
		}
	}
	
	/**
	 * Returns the object station corresponding to its ID
	 * @param id
	 * @return - Station object
	 */
	public Station getStation(int id) {
		for(Station stat : stations) {
			if(stat.getStationID() == id) {
				return stat;
			}
		}
		
		return null;
	}
	
	/**
	 * Provides a summary of what the ending state of the simulation is
	 * (For all trains and stations)
	 */
	public void provideSummary() {
		System.out.println("The simulation has ended. Here is the terminating status update: ");
		System.out.println();
		for(Train trn : inboundTrack) {
			trn.printStatusUpdate();
		}
		
		for(Train trn : outboundTrack) {
			trn.printStatusUpdate();
		}
		
		System.out.println();
		for(Station stat : stations) {
			stat.printStatusUpdate();
		}
	}
	
	/********************
	 * 	Testing methods	*
	 ********************/
	
	public static void main(String[]args){
		// Testing constructor & toString()
		TrainRoute tr = new TrainRoute(10);
		System.out.println(tr);
		
		// Testing addStation() and getStation()
		tr.addStation(new Station(7));
		System.out.println(tr.getStation(7));
		
		// Testing addTrain()
		System.out.print("Adding Train...");
		tr.addTrain(new Train(null, null, tr, tr.routeLength));
		System.out.println("Done");
		
		// Testing getrouteLength()
		System.out.println(tr.getRouteLength());
		
	}
}