package com.evapps.chargepoints.service;

import com.evapps.chargepoints.model.Session;
import com.evapps.chargepoints.repo.SessionRepository;
import com.evapps.chargepoints.utils.TextColours;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class XmlService {

    private static final Logger log = LogManager.getLogger(XmlService.class);
    private ArrayList<Session> locations;

    public XmlService()
    {
        Session poi;
        locations = new ArrayList<>();

        poi = new Session();
        poi.setLoc("Woodquay, Galway City, County Galway");
        poi.setType("Type-2");
        poi.setStatus("None");
        poi.setElapsedMins(0);
        locations.add(poi);

        poi = new Session();
        poi.setLoc("Circle K / Topaz Service Station, Newcastle Road");
        poi.setType("CHAdeMO DC 50kW");
        poi.setStatus("None");
        poi.setElapsedMins(0);
        locations.add(poi);

        poi = new Session();
        poi.setLoc("Glynn's Centra/Circle K");
        poi.setType("CHAdeMO DC 50kW");
        poi.setStatus("None");
        poi.setElapsedMins(0);
        locations.add(poi);

    }

    public String  getAvailability( String description ) {
        String availability;

        log.debug("--> getAvailability()");
        String [] tmp = description.split("<BR>", 100 );
        int firstbracket = tmp[1].indexOf('(');
        int secondbracket = tmp[1].indexOf(')');
        String av = tmp[1].substring(firstbracket+1, secondbracket);
        if( av.contains("Available") )
            av = "Available";
        else if( av.contains("Occupied") )
            av = "Occupied";
        availability = av;

        log.debug("<-- getAvailability() <-- Availability: " + availability);
        return availability;
    }

    private String getChargerType( String description ) {
        String type;

        log.debug("--> getChargerType()");
        int firstBr = description.indexOf("<BR>");
        int aloc = description.indexOf("<a", firstBr+2);

        type = description.substring(firstBr+4, aloc);
        if( type.indexOf("\n") == 0 ) // There's a \n at the start, so remove it
        {
            type = type.substring(1);
        }

        log.debug("<-- getChargerType()] <-- type: " + type);
        return type;
    }

    public void isWatchedLocation(String name, String description, SessionRepository repository) {

        for (int i = 0; i < locations.size(); i++) {
            if( name.contains( locations.get(i).getLoc() ) ) {
                if( getChargerType(description).contains(locations.get(i).getType()) ) {
                    String prev = locations.get(i).getStatus();
                    locations.get(i).setStatus( getAvailability(description) );
                    log.debug("isWatchedLocation: Prev ==> [" + prev + "], Current ==> [" + locations.get(i).getStatus() + "]");

                    if( transitionedToOccupied(prev, locations.get(i).getStatus())) {
                        log.debug("isWatchedLocation: " + locations.get(i).getLoc() + " transitioned to Occupied!");
                        locations.get(i).setStartedAt( LocalDateTime.now() );
                        locations.get(i).setElapsedMins(0);
                    }
                    else if( transitionedToAvailable(prev, locations.get(i).getStatus())) {
                        log.debug("isWatchedLocation: " + locations.get(i).getLoc() + " transitioned to Available!");
                        locations.get(i).setStoppedAt(LocalDateTime.now());
                        log.debug("isWatchedLocation: saving session [" + locations.get(i).getLoc()
                                    + ", " + locations.get(i).getStatus()
                                    + ", " + locations.get(i).getStartedAt()
                                    + ", " + locations.get(i).getStoppedAt()
                                    + ", " + locations.get(i).getElapsedMins());
                        repository.save(locations.get(i));
                        resetStation(i); //locations.get(i).setElapsedMins(0);
                    }
                    else if( locations.get(i).getStatus().contains("Available") )
                        locations.get(i).setElapsedMins(0);
                    else if( locations.get(i).getStatus().contains("Occupied") && (locations.get(i).getStartedAt() == null) ){
                        log.debug("isWatchedLocation: " + locations.get(i).getLoc() + " found to be Occupied, starting to count time...");
                        locations.get(i).setStartedAt( LocalDateTime.now() );
                    }
                    else if( locations.get(i).getStartedAt() != null ) {
                        Duration diff = Duration.between( locations.get(i).getStartedAt(), LocalDateTime.now() );
                        locations.get(i).setElapsedMins( diff.toMinutes() );
                    }
                }
            }
        }
    }

    private boolean transitionedToOccupied( String prevStatus, String currStatus ) {
       boolean transitionedToOccupied = false;

       log.debug("transitionedToOccupied: Previous status: (" + prevStatus + "), Current Status: (" + currStatus + ")" );
       if( prevStatus.equals("Available") && currStatus.equals("Occupied") )
           transitionedToOccupied = true;

       log.debug("transitionedToOccupied: Returning: " + transitionedToOccupied);
       return transitionedToOccupied;
    }

    private boolean transitionedToAvailable( String prevStatus, String currStatus ) {
        boolean transitionedToAvailable = false;

        log.debug("transitionedToAvailable: Previous status: (" + prevStatus + "), Current Status: (" + currStatus + ")" );

        if( prevStatus.equals("Occupied") && currStatus.equals("Available") )
            transitionedToAvailable = true;

        log.debug("transitionedToAvailable: Returning: " + transitionedToAvailable);
        return transitionedToAvailable;
    }

    public void printLocationInfo() {
        String format = "\n%2s%-48.48s%3s%-15.15s%3s%-40.40s%3s%-11.11s%2s";

        clrscr();
        printTime();
        drawLine();
        System.out.format(format,"| ", "LOCATION", " | ", "TYPE", " | ", TextColours.ANSI_WHITE+"STATUS", " | ", "USAGE TIME", " |");
        System.out.println(TextColours.ANSI_RESET);
        drawLine();
        String fg = TextColours.ANSI_RED;

        for (int i = 0; i < locations.size(); i++) {
            if( locations.get(i).getStatus().contains("Available") )
                fg = TextColours.ANSI_GREEN;
            System.out.format(  format, "| ", locations.get(i).getLoc(), TextColours.ANSI_RESET+" | ",
                                locations.get(i).getType(), " | ", fg+locations.get(i).getStatus(),
                                TextColours.ANSI_RESET+" | ", locations.get(i).getElapsedMins()+" MINS",
                                TextColours.ANSI_RESET+" |");
            fg = TextColours.ANSI_RED;
        }
        System.out.println(TextColours.ANSI_RESET);
        drawLine();
        System.out.println();
    }

    private void printTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        System.out.println(formatter.format(date));
    }

    private void drawLine() {
        int lineLgt = 122;

        for (int i = 0; i < lineLgt; i++) {
            System.out.print("-");
        }
    }

    public static void clrscr(){
        //System.out.println("Clearing the screen...");
        for (int y = 0; y < 25; y++)
            System.out.println("\n");
    }

    private void resetStation( int i ) {
        log.debug("Resetting station times for [" + locations.get(i).getLoc() + "]");
        locations.get(i).setStartedAt(null);
        locations.get(i).setStoppedAt(null);
        locations.get(i).setElapsedMins(0);
    }
}
