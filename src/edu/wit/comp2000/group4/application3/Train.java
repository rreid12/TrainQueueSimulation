package edu.wit.comp2000.group4.application3;

import java.util.ArrayList;
import java.util.Iterator;

/*
 * Course: Data Structures
 * Assignment: #3 - Train Simulation
 * Group: #4
 * Team Members: Taylor, Ryan Reid, Schyler
 */

/**
 * 
 * @author reidr on 6/27/2017
 * Other contributors: Taylor
 *
 */
public final class Train {

	private int trainCapacity;
	private static int trainCount = 0;
	private int TrainID = 0;
	private boolean isInbound;
	private int position;
	private TrainRoute trainRoute;
	private Station nextStation;

	private ArrayList<Passenger> passengers = new ArrayList<Passenger>();

	/**
	 * Constructor
	 * @param startStat
	 * @param inOrOut
	 * @param trnRoute
	 * @param trainCapacity
	 */
	public Train(Station startStat, String inOrOut, TrainRoute trnRoute, int trainCapacity) {
		passengers = new ArrayList<>();
		trainCount++;
		this.TrainID = trainCount;
		this.trainCapacity = trainCapacity;
		this.trainRoute = trnRoute;
		this.nextStation = startStat;
		setIsInbound(inOrOut);
		this.position = nextStation.getPosition();
		findNextStation();
	}

	/**
	 * Updates the status of the train
	 * 1 - Checks to see if the train is at a station, if it is disembark all passengers who have arrived at their destination
	 * 2 - Picks up passengers from the platform of passengers heading in the same direction
	 * 3 - Determines which station it is heading to next
	 * 4 - Moves the train in the correct direction (iterates position by 1)
	 */
	public void update() {
		//announce train position
		Driver.writeToLogFile("Train " + this.getTrainID() + " is at position " + position);
		if(this.position == nextStation.getPosition()) {
			Driver.writeToLogFile("Train " + this.getTrainID() + " arriving at Station " + nextStation.getStationID());
			
			//unload passengers
			if(!passengers.isEmpty()) {
				for(Iterator<Passenger> it = passengers.iterator(); it.hasNext();) {
					Passenger pass = it.next();
					if(exitTrain(pass)) {
						it.remove();
					}
				}
			}
			
			nextStation.clearExitPlatform();
			//pick up passengers
			if(isInbound) {
				while(!nextStation.isInboundPlatformEmpty() && passengers.size() <= this.trainCapacity ) {
					boardTrain();
				}
					
			}
			else if(!isInbound) {
				while(!nextStation.isOutboundPlatformEmpty() && passengers.size() <= this.trainCapacity) {
					boardTrain();
				}
			}
			
			//head to nextStation
			findNextStation();
			
		}
		moveTrain();
	}
	
	/**
	 * Used to find the next station on the route depending on the direction of the train
	 * @return next station in the route 
	 */
	private Station findNextStation() {
		if(isInbound()) {
			return nextStation = trainRoute.getNextInboundStation(nextStation);
		}
		else {
			return nextStation = trainRoute.getNextOutboundStation(nextStation);
		}
	}
	/**
	 * Changes the position of the train depending on which direction it is heading
	 * Important to note that routes are circular so the trains DO NOT turn around
	 */
	private void moveTrain() {
		if(isInbound()) {
			if(position == 1)
				this.position = trainRoute.getRouteLength();
			else
				position--;
		}
		else {
			if(position == trainRoute.getRouteLength()) 
				this.position = 1;
			else
				position++;
		}
	}
	
	/**
	 * Sets the direction of the train depending on the input from the config file
	 * @param inOrOut 
	 */
	private void setIsInbound(String inOrOut) {
		if(inOrOut.equals("in")) {
			this.isInbound = true;
			Driver.writeToLogFile(this.toString() + " has started its inbound route from " + nextStation.toString());
		}
		else {
			this.isInbound = false;
			Driver.writeToLogFile(this.toString() + " has started its outbound route from " + nextStation.toString());
		
		}
	}

	/**
	 * Boards the passengers who are headed in the same direction of the train
	 * Also logs any passengers who board
	 */
	private void boardTrain() {
		if (isInbound) {
			Passenger p = nextStation.dequeueInboundPassenger();
			passengers.add(p);
			Driver.writeToLogFile(p.toString() + " boarding " + this.toString() + " heading inbound");
		} else if (!isInbound) {
			Passenger p = nextStation.dequeueOutboundPassenger();
			passengers.add(p);
			Driver.writeToLogFile(p.toString() + " boarding " + this.toString() + " heading outbound");
		}
	}

	/**
	 * Lets passengers off who have reached their destination, also logs it
	 * @param pass
	 * @return
	 */
	private boolean exitTrain(Passenger pass) {

		boolean passRemoved = false; 
		
		if (pass.getDestination() == nextStation) {
			nextStation.disembarkPassenger(pass);
			Driver.writeToLogFile(pass.toString() + " has arrived at their destination");
			passRemoved = true;
		} 
		
		return passRemoved;
	}
	
	public boolean isInbound() {
		return isInbound;
	}

	/**
	 * Overrides Object's toString() method to print the Train's ID
	 */
	@Override
	public String toString() {
		return "Train " + this.TrainID;
	}
	
	public int getTrainID() {
		return this.TrainID;
	}
	
	public int getPosition() {
		return this.position;
	}
	
	/**
	 * Provides a status update of the train
	 * Where it is
	 * Direction
	 * Passengers on board
	 */
	public void printStatusUpdate() {
		if(isInbound) {
			System.out.println(this.toString() + " is at position " + this.getPosition() + " heading inbound with " + this.passengers.size() + " passengers onboard.");
		} else {
			System.out.println(this.toString() + " is at position " + this.getPosition() + " heading outbound with " + this.passengers.size() + " passengers onboard.");

		}
	}
	
	/**
	 * Tests the train class. *Unit tests do not work because of dependence on other classes*
	 * @param args
	 */
	public static void main(String[] args) {
		
		//Creates new station objects
		Station station1 = new Station(1);
		Station station3 = new Station(3);
		Station station10 = new Station(10);
		
		//Creates a train route
		TrainRoute route1 = new TrainRoute(30);
		
		//Create new trains of passengers
		Train t1 = new Train(station1, "in", route1, 15);
		Train t2 = new Train(station10, "out", route1, 15);
		
		//Create a new passenger
		Passenger p1 = new Passenger(station1, station3, route1);
		
		t1.setIsInbound("in");
		t2.setIsInbound("out");
		System.out.println(t1);
		System.out.println(t2);
		
		t1.exitTrain(p1);
		
		t1.printStatusUpdate();
		t2.printStatusUpdate();
		
	}
}
