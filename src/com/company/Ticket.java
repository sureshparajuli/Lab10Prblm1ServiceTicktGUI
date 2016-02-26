package com.company;

import java.util.Date;
import java.util.Scanner;
import java.util.LinkedList;

public class Ticket {//this is ticket class all the methods are removed from here and moved to ticket manager

    private int priority;
    private String reporter; //Stores person or department who reported issue
    private String description;
    private Date dateReported;
    // adding variables for Problems 5.
    private Date dateResolved;
    private String resolution;

    //STATIC Counter - accessible to all Ticket objects.
    //If any Ticket object modifies this counter, all Ticket objects will have the modified value
    //Make it private - only Ticket objects should have access
    private static int staticTicketIDCounter = 1;
    //The ID for each ticket - instance variable. Each Ticket will have it's own ticketID variable
    protected int ticketID;

    public Ticket(String desc, int p, String rep, Date date) {
        this.description = desc;
        this.priority = p;
        this.reporter = rep;
        this.dateReported = date;
        this.ticketID = staticTicketIDCounter;
        staticTicketIDCounter++;
    }

    public Ticket(String desc, int p, String rep, Date date, int id) {
        this.description = desc;
        this.priority = p;
        this.reporter = rep;
        this.dateReported = date;
        this.ticketID = id;
    }


    protected int getPriority() {
        return priority;
    }

    //Added method to retrieve ticketID
    protected int getTicketID() {
        return ticketID;
    }

    //Added method to retrieve ticket description
    protected String getDescription() {
        return description;
    }

    protected String getReporter() { return reporter; }

    protected Date getDateReported() { return dateReported; }

    protected String getResolution()
    {
        return resolution;
    }

    protected Date getDateResolved() {
        return dateResolved;
    }

    protected void setResolution(String resolution)
    {
        Date dateReported = new Date();
        this.resolution = resolution;
        this.dateResolved = dateReported;
    }

    protected static void setStaticTicketIDCounter(int num)
    {
        staticTicketIDCounter = num;
    }

    public String toString(){
        return("ID= " + this.ticketID + " Issued: " + this.description + " Priority: " + this.priority + " Reported by: "
                + this.reporter + " Reported on: " + this.dateReported);
    }
}

