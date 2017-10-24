package edu.wit.comp2000.group4.application3;

/*
 * Course: Data Structures
 * Assignment: #3 - Train Simulation
 * Group: #4
 * Team Members: Taylor, Ryan Reid, Schyler
 */

/**
 * @author reidr (Ryan Reid)
 */

import java.util.ArrayList;
import com.pearson.carrano.*;

public class Station {
	
	private static int station = 0;
	private int stationID;
	private ArrayQueue<Passenger> outboundPlatform = new ArrayQueue<Passenger>();
	private ArrayQueue<Passenger> inboundPlatform = new ArrayQueue<Passenger>();
	private ArrayList<Passenger> exitPlatform = new ArrayList<Passenger>();
	private int position;
	
	/**
	 * Constructor sets the position of the station
	 * @param pos - position of the station along the route
	 */
	public Station(int pos) {
		station++; //iterate static station variable
		setStationID(station);
		setPosition(pos);
	}
	
	/**
	 * Sets the ID of the station
	 * @param id
	 */
	private void setStationID(int id) {
		this.stationID = id;
	}
	
	/**
	 * @return the ID of the station
	 */
	public int getStationID() {
		return stationID;
	}
	
	/**
	 * Direct passenger to the correct queue (inbound or outbound)
	 * @param p passenger that is entering the station
	 */
	public void enqueuePassenger(Passenger p) {
		if(p.isInbound()) { //need condition to determine direction of passenger {
			inboundPlatform.enqueue(p);
			Driver.writeToLogFile(p.toString() + " queued up at " + this.toString() +  " heading inbound");
			
		} else {
			outboundPlatform.enqueue(p);
			Driver.writeToLogFile(p.toString() + " queued up at " + this.toString() +  " heading outbound");

		}
	}
	
	/**
	 * Remove a passenger from the outbound queue (board a train)
	 * @return
	 */
	public Passenger dequeueOutboundPassenger() {
		if(!outboundPlatform.isEmpty()) {
			return outboundPlatform.dequeue();
		}
		else {
			throw new EmptyQueueException();
		}
	}
	
	/**
	 * Remove a passenger from the inbound queue (board a train)
	 * @return
	 */
	public Passenger dequeueInboundPassenger() {
		if(!inboundPlatform.isEmpty()) {
			return inboundPlatform.dequeue();
		}
		else {
			throw new EmptyQueueException();
		}
	}

	/**
	 * Directs passenger to the exit platform to leave the station
	 * @param p Passenger that is arriving at the station
	 */
	public void disembarkPassenger(Passenger p) {
		exitPlatform.add(p);
	}	
	
	/**
	 * Initially sets the position of the station( when the station is instantiated)
	 * @param pos
	 */
	private void setPosition(int pos) {
		position = pos;
	}
	
	/**
	 * 
	 * @return the position of the station in the route
	 */
	public int getPosition() {
		return position;
	}
	
	/**
	 * @return true if inbound platform is empty
	 */
	public boolean isInboundPlatformEmpty() {
		return inboundPlatform.isEmpty();
	}
	
	/**
	 * 
	 * @return true if outbound platform is empty
	 */
	public boolean isOutboundPlatformEmpty() {
		return outboundPlatform.isEmpty();
	}
	
	/**
	 * Empties the exit platform to avoid chaos at the exits
	 */
	public void clearExitPlatform() {
		exitPlatform.clear();
	}
	
	/**
	 * Overrides Object's toString() method to print Station ID
	 */
	@Override
	public String toString() {
		return "Station " + this.stationID;
	}
	
	/**
	 * Gives a staus update of the station
	 * Are there any passengers waiting?
	 */
	public void printStatusUpdate() {
		if(outboundPlatform.isEmpty() && inboundPlatform.isEmpty())
			System.out.println(this.toString() + " has empty platforms in both directions" );
		else 
			System.out.println(this.toString() + " has some passengers waiting on its platforms");
	}
	
	public static void main(String args[]) {

		//unit testing
		Driver.openLogFile("log.txt");
		Station stat = new Station(4);
		TrainRoute trnRoute = new TrainRoute(10);
		
		System.out.println("Creating a new station at position 4...");
		System.out.println();
		System.out.println("Position of station: " + stat.getPosition());
		System.out.println("ID of station: " + stat);
		System.out.println("Is the outbound platform empty? " + stat.isOutboundPlatformEmpty());
		System.out.println("Is the inbound platform empty? " + stat.isOutboundPlatformEmpty());
		System.out.println();
	

		System.out.println("Receiving a passenger from a train...");
		stat.disembarkPassenger(new Passenger(stat, new Station(5), trnRoute));
		System.out.println("Passengers on the exit platform: " + stat.exitPlatform.size());
		System.out.println("Clearing the exit platform...");
		stat.clearExitPlatform();
		System.out.println("Passengers on the exit platform: " + stat.exitPlatform.size());
		System.out.println();
		
		
		System.out.println("Enqueueing inbound passenger...");
		new Passenger(stat, new Station(2), trnRoute);
		System.out.println("Is the inbound platform empty? " + stat.isInboundPlatformEmpty());
		System.out.println();
		
		
		System.out.println("Giving the final status update of the station...");
		stat.printStatusUpdate();
		
		
		
	}
	
	
	
}
