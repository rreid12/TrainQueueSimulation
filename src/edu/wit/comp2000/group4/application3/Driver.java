package edu.wit.comp2000.group4.application3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Formatter;
import java.util.Random;
import java.util.Scanner;

/*
 * Course: Data Structures
 * Assignment: #3 - Train Simulation
 * Group: #4
 * Team Members: Taylor, Ryan Reid, Schyler
 */

/**
 * 
 * @author reidr (Ryan Reid) on 6/27/2017
 *
 */
public class Driver {
	private static PrintWriter logFile;

	/**
	 * Opens the config file to be read in the data to start the simulation
	 * @param fileName
	 * @return
	 */
	public static Scanner openReadFile(String fileName) {
		Scanner inFile = null;
		try {
			inFile = new Scanner(new File(fileName));
			System.out.println("Configuring the route from " + fileName);
			return inFile;
		} catch (FileNotFoundException e) {
			System.out.println("Input file failed to load: " + e.getMessage());
			e.printStackTrace();
		}
		return inFile;
	}
	
	/**
	 * Opens the log file to be written to
	 * @param fileName
	 */
	public static void openLogFile(String fileName) {
		try {
			logFile = new PrintWriter(fileName);
			System.out.println("Setting up logging file "+ fileName);
		} catch (FileNotFoundException e) {
			System.out.println("Input file failed to load: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Allows all classes to write to log file to report different events
	 * @param log
	 */
	public static void writeToLogFile(String log) {
		logFile.println(log);
	}

	/**
	 * This drives the TrainSimulation...input is taken from a configuration
	 * file and each object is instantiated as necessary. An update
	 * method is called from the Train class to update the state of the
	 * simulation after every 'tick'
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String fileName = "config.dat";
		Scanner inFile = openReadFile(fileName);
		openLogFile("log.txt");
		logFile.flush(); //ensure log file is emptied of any previous text stored

		// first line of config
		@SuppressWarnings("unused")
		int numberOfTrainRoutes = inFile.nextInt();
		int routeLength = inFile.nextInt();

		TrainRoute trainRoute = new TrainRoute(routeLength);

		// second line (number of stations and their positions along the route)
		int numberOfStations = inFile.nextInt();
		int[] stationPosition = new int[numberOfStations];

		for (int i = 0; i < numberOfStations; i++) {
			stationPosition[i] = inFile.nextInt();
			// adds all the stations to the route...

			trainRoute.addStation(new Station(stationPosition[i]));
		}

		// third line (number of trains, train capacity, their starting station followed by
		// their direction (out or in))
		// starting station must be a stationID/station number
		int numberOfTrains = inFile.nextInt();
		int trainCapacity = inFile.nextInt();
		int[] trainStartingStation = new int[numberOfTrains];
		String[] inOrOutbound = new String[numberOfTrains];

		for (int i = 0; i < numberOfTrains; i++) {
		
			trainStartingStation[i] = inFile.nextInt();
			inOrOutbound[i] = inFile.next();

			trainRoute.addTrain(new Train(trainRoute.getStation(trainStartingStation[i]), inOrOutbound[i], trainRoute, trainCapacity));
		}

		// 4th line max passengers allowed, followed by each passengers origin
		// then destination in that order
		// Origin and destination correspond to StationID/StationNumber
		// NOTE: passengers must have origin and destination AT STATIONS
		int maxPassengersAllowed = inFile.nextInt();

		// 5th line (number of clock ticks)
		int ticks = inFile.nextInt();
		int passengersCreated = 0;
		
		for (int i = 0; i < ticks; i++) {
			// 6th line each passengers origin
			// then destination in that order
			// Origin and destination correspond to StationID/StationNumber
			// NOTE: passengers must have origin and destination AT STATIONS
			if (i % 3 != 0 && passengersCreated < maxPassengersAllowed) {
				//creates passengers at random times in the simulation
				new Passenger(trainRoute.getStation(inFile.nextInt()), trainRoute.getStation(inFile.nextInt()), trainRoute);
				passengersCreated++;
			}
			trainRoute.moveAlong(); //calls the method which essentially moves the state of the simulation along
		}
		
		trainRoute.provideSummary(); //give ending summary

		//close files
		inFile.close();
		logFile.close();
	}

}
