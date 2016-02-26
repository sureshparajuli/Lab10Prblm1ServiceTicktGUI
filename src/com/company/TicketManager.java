package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.io.*;
import java.time.LocalDateTime;//use local date time easy to generate resolution file

public class TicketManager extends JFrame{
    //auto generated code define all the GUI components used in the application
    private JPanel rootPanel;
    private JTextField textDescription;
    private JTextField textReporter;
    private JButton enterTicketButton;
    private JTextField textTicketId;
    private JButton deleteByIdButton;
    private JTextField textIssue;
    private JButton searchByNameButton;
    private JButton displayAllTicketsButton;
    private JButton quitButton;
    private JButton deleteByIssueButton;
    private JComboBox comboBoxPriority;
    private JTextField textDescriptionToSearch;
    private JLabel showMessageLabel;
    private JList list1;
    private JTextField textResolutionDescription;
    //Added static class field to hold resolved tickets
    static LinkedList<Ticket> resolvedTickets = new LinkedList<Ticket>();

    public TicketManager()
    {
        super("Ticket Manager");//calls super class on JFrame with window name
        setContentPane(rootPanel);//set the rootPanel
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//exists application on window close
        setVisible(true);//display the window

        //run the ticket manager app
        LinkedList<Ticket> ticketQueue = runTicketManager();//call run ticket manager to read open tickets from file and put it in list while program is running

        enterTicketButton.addActionListener(new ActionListener() {//action listener to capture enter ticket button
            @Override
            public void actionPerformed(ActionEvent e) {
                addTickets(ticketQueue);//call add ticket event
                pack();
            }
        });

        deleteByIdButton.addActionListener(new ActionListener() {//action listener to capture delete by id ticket button
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteTicket(ticketQueue);//call delete ticket method
                pack();
            }
        });
        deleteByIssueButton.addActionListener(new ActionListener() {//action listener to capture delete by issue button
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteByIsuue(ticketQueue);//call delete ticket method
                pack();
            }
        });
        searchByNameButton.addActionListener(new ActionListener() {//action listener to capture search by name button
            @Override
            public void actionPerformed(ActionEvent e) {
                LinkedList<Ticket> searchedTicket = searchAllTickets(ticketQueue);//call search all tickets event
            }
        });
        displayAllTicketsButton.addActionListener(new ActionListener() {//action listener to capture display all ticket button
            @Override
            public void actionPerformed(ActionEvent e) {
                printAllTickets(ticketQueue);//call print/display all ticket
                textDescriptionToSearch.setText("");//clear search by description if any
                pack();
            }
        });
        quitButton.addActionListener(new ActionListener() {//action listener to capture quit button
            @Override
            public void actionPerformed(ActionEvent e) {
                quitApplication(ticketQueue);//call quit application
                System.exit(0);
            }
        });
        pack();//fixes the size of the window
    }

    private LinkedList<Ticket> runTicketManager()//this method read all opened tickets from file and return the ticket queue list
    {                                            //when the program starts at the beginning also capture the ticket id

        // this holds current open tickets
        LinkedList<Ticket> ticketQueue = new LinkedList<Ticket>();//ticket queue

        try
        {
            File saveOpen = new File("open_tickets.txt"); // it makes file object named open_ticket.txt and read all the content later

            Scanner sc = new Scanner(saveOpen);
            // creating variables to hold the datas stored in each field of the text file
            String description;
            int priority;
            String reporter;
            Date date;
            int ticketID;

            // continue to end the file
            //and create Ticket object and add to the list while program is running in memory
            while(sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] array = new String[5];
                array = line.split(",");// Comma splits the array into 5

                //read each field from each ticket line
                description = array[0];
                priority = Integer.parseInt(array[1]);
                reporter = array[2];
                date = new Date(array[3]);
                ticketID = Integer.parseInt(array[4]);

                Ticket old = new Ticket(description, priority, reporter, date, ticketID);//create ticket object
                ticketQueue.add(old);//add ticket to the queue
            }
            sc.close(); // closed the open file
            // last ticket from the Queue for ticket ID
            Ticket last = ticketQueue.getLast();
            // it sets the Ticket ID counter to one more  than the last current ticket or open ticket
            //set the ticket ID
            Ticket.setStaticTicketIDCounter(last.getTicketID() + 1);
        }

        catch(Exception e)
        {

        }
        printAllTickets(ticketQueue);//now call print all ticket to display opened tickets read from the file
        return ticketQueue;
    }

    //Move the adding ticket code to a method
    //method to add new ticket to the queue
    protected void addTickets(LinkedList<Ticket> ticketQueue) {
        boolean moreProblems = true;
        String description;
        String reporter;
        //let's assume all tickets are created today, for testing. We can change this later if needed
        Date dateReported = new Date(); //Default constructor creates date with current date/time
        int priority;

        description = textDescription.getText();//get from the form: description
        reporter = textReporter.getText();//get from the form: reporter
        priority = Integer.parseInt(comboBoxPriority.getSelectedItem().toString());//get from the form: priority

        Ticket t = new Ticket(description, priority, reporter, dateReported);//create new ticket
        ticketQueue.add(t);//add the new ticket to the queue

        //To test, let's print out all of the currently stored tickets
        printAllTickets(ticketQueue);

        clearForm();//call clear form to clear newly entered ticket info
    }

    protected void deleteTicket(LinkedList<Ticket> ticketQueue) {
        //What to do here? Need to delete ticket, but how do we identify the ticket to delete?
        //TODO implement this method
        //int variable to hold ticket ID to be deleted
        int deleteID = 0;

        if (ticketQueue.size() == 0) {    //no tickets! since ticket queue is empty
            showMessageLabel.setText("No tickets to delete!");//print it to the message
            return;//stop here
        }
        //Try block to validate user input
        try
        {
            String strTicketID = textTicketId.getText();//get from the form: ticket id
            deleteID = Integer.parseInt(strTicketID);//try parsing it to int
        }

        //Catch bad data and prompt the user to try again
        catch(Exception e)
        {
            showMessageLabel.setText("ERROR: Invalid input, enter an integer number!");
            return;
        }

        //Loop over all tickets. Delete the one with this ticket ID
        boolean found = false;
        for (Ticket ticket : ticketQueue) {
            if (ticket.getTicketID() == deleteID) {
                found = true;//change found to true
                String resolve = textResolutionDescription.getText();
                ticket.setResolution(resolve);
                resolvedTickets.add(ticket);
                ticketQueue.remove(ticket);
                showMessageLabel.setText(String.format("Ticket %d deleted", deleteID));//display to message label which ticket deleted
                break; //don't need loop any more.
            }
        }
        if (found == false) {
            showMessageLabel.setText("Ticket ID not found, no ticket deleted");//no ticket found with
            //TODO – re-write this method to ask for ID again if not found
        }

        //Added TICKETMANAGER class call to make static method work
        printAllTickets(ticketQueue);  //print updated list
        clearForm();
    }

    //Added method to delete by issue (assuming this means by description)
    protected void deleteByIsuue(LinkedList<Ticket> tickets) {
        String deleteDescription = "";
        //Variable to control loop
        boolean done = false;
        if (tickets.size() == 0) {    //no tickets! ticket queue doesn't contain any tickets
            showMessageLabel.setText("No tickets to delete!\n");//show message
            return;//stop here
        }

        deleteDescription = textIssue.getText();//get the issue, and delete the ticket with the issue
        //Loop over all tickets. Delete the one with this ticket ID
        boolean found = false;
        for (Ticket ticket : tickets) {
            if (ticket.getDescription().equals(deleteDescription)) {//check if each ticket from queue matches the issue description
                found = true;//set found flag to true
                String resolved = textResolutionDescription.getText();//get the issue to be resolved
                ticket.setResolution(resolved);//set the ticket resolution
                resolvedTickets.add(ticket);//ticket to be deleted to resolved list which would be saved to resolved file
                tickets.remove(ticket);//delete ticket from the ticket queue
                showMessageLabel.setText(String.format("Ticket with description %s deleted", deleteDescription));//print message which ticket deleted
                break; //don't need loop any more.
            }
        }
        if (found == false) {//if no ticket matching description found
            showMessageLabel.setText("Ticket ID not found, no ticket deleted");//print message saying no ticket deleted
            //TODO – re-write this method to ask for ID again if not found
            done = false;
        }

        //Added TICKETMANAGER class call to make static method work
        printAllTickets(tickets);  //print updated list
        clearForm();
    }

    //Added method to search all tickets for tickets containing search phrase
    protected LinkedList<Ticket> searchAllTickets(LinkedList<Ticket> tickets) {
        LinkedList<Ticket> searchedTickets = new LinkedList<Ticket>();
        String searchWord = textDescriptionToSearch.getText();
        String current;
        //loop through each ticket in the queue and look for ticket with matching search phrase
        for (Ticket t : tickets ) {
            current = t.getDescription();

            if(current.contains(searchWord))
            {
                searchedTickets.add(t);
            }
        }

        printAllTickets(searchedTickets);
        //textDescriptionToSearch.setText("");
        clearForm();
        return searchedTickets;//return the list of ticket matching the search phrase
    }

    //print all the ticket from the ticket queue to JList
    protected void printAllTickets(LinkedList<Ticket> tickets) {
        DefaultListModel<String> model = new DefaultListModel<>();//create model to be set to Jlist
        model.addElement(" ------- All open tickets ----------");
        for (Ticket t : tickets ) {
                model.addElement( t.toString() );//populate model with each ticket string
        }
        model.addElement(" ------- End of ticket list ----------");
        list1.setModel(model);//set JList model
    }

    //when quit button is clicked this method runs and save opened tickets to opn_ticket.txt file
    //and resolved tickets from the resolved list(deleted ticket) to resolved file
    protected void quitApplication(LinkedList<Ticket> ticketQueue)
    {
        LocalDateTime date = LocalDateTime.now();
        File saveOpen = new File("open_tickets.txt");
        //create a file with current date time so that one resolved file will not overwrite or
        //replace other resolved file
        File saveResolved = new File("resolved_as_of_" + date.getYear() + date.getMonthValue()
                            + date.getDayOfMonth() + date.toLocalTime().getHour()
                + date.getDayOfMonth() + date.toLocalTime().getMinute()
                + date.getDayOfMonth() + date.toLocalTime().getSecond()+ ".txt");
        // File is keeping track of open tickets
        try
        {
            //statements/code block to write (save) to opened ticket file
            BufferedWriter openWriter = new BufferedWriter(new FileWriter(saveOpen));
            for (Ticket t : ticketQueue ) {
                openWriter.write(t.getDescription().replace(',', ' ') + "," +
                        t.getPriority() + "," +
                        t.getReporter().replace(',', ' ') + "," +
                        t.getDateReported() + "," +
                        t.getTicketID() + "\n"); //Write a toString method in Ticket class
                //println will try to call toString on its argument
            }

            //statements/code block to write (save) to resolved ticket file
            openWriter.close();// closing the open file
            // Opening new file to keep track of result file tickets
            BufferedWriter resolvedWriter = new BufferedWriter(new FileWriter(saveResolved));
            for (Ticket t : resolvedTickets ) {
                resolvedWriter.write(t.getDescription().replace(',', ' ') + "," +
                        t.getPriority() + "," +
                        t.getReporter().replace(',', ' ') + "," +
                        t.getDateReported() + "," +
                        t.getTicketID() + "," +
                        t.getResolution().replace(',', ' ') + "," +
                        t.getDateResolved() + "\n"); //Write a toString method in Ticket class
                //println will try to call toString on its argument
            }
            resolvedWriter.close(); // closing the file

        }
        // Handiling the erro message and throwing Error
        catch(Exception e)
        {
            e.printStackTrace();

            System.out.println("File ERROR");
        }
    }

    //methods to clear form text fileds and message block
    private void clearForm()
    {
        textDescription.setText("");
        textTicketId.setText("");
        textReporter.setText("");
        textIssue.setText("");
        textResolutionDescription.setText("");
        comboBoxPriority.setSelectedIndex(0);
    }
}
