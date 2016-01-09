package com.company;

import java.util.*;
import java.io.*;

public class TicketManager {

    //Added static class field to hold resolved tickets
    static LinkedList<Ticket> resolvedTickets = new LinkedList<Ticket>();

    public static void main(String[] args) {
        // this holds current open tickets
        LinkedList<Ticket> ticketQueue = new LinkedList<Ticket>();

        try
        {
            File saveOpen = new File("open_tickets.txt"); // it makes file object named open_ticket.txt

            Scanner sc = new Scanner(saveOpen);
        // creating variables to hold the datas stored in each line of the text file
            String description;
            int priority;
            String reporter;
            Date date;
            int ticketID;

        // continue to end the file
            while(sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] array = new String[5];
                array = line.split(",");// Comma splits the array into 5

                description = array[0];
                priority = Integer.parseInt(array[1]);
                reporter = array[2];
                date = new Date(array[3]);
                ticketID = Integer.parseInt(array[4]);

                Ticket old = new Ticket(description, priority, reporter, date, ticketID);
                ticketQueue.add(old);
            }
            sc.close(); // closed the open file
            // last ticket from the Queue
            Ticket last = ticketQueue.getLast();
            // it sets the Ticket ID counter to one more  than the last current ticket or open ticket
            Ticket.setStaticTicketIDCounter(last.getTicketID() + 1);

        }

        catch(Exception e)
        {

        }

        Scanner scan = new Scanner(System.in);

        while(true){

            System.out.println("1. Enter Ticket\n2. Delete By ID\n3. Delete By Issue\n4. Search By Name\n5. Display All Tickets\n6. Quit");
            int task = Integer.parseInt(scan.nextLine());

            if (task == 1) {
                //Call addTickets, which will let us enter any number of new tickets
                addTickets(ticketQueue);

            } else if (task == 2) {
                //delete a ticket
                deleteTicket(ticketQueue);

            } else if (task == 3) {
                //delete a ticket
                deleteByIsuue(ticketQueue);

            } else if (task == 4) {
                //delete a ticket
                searchAllTickets(ticketQueue);

            } else if (task == 5) {
                //delete a ticket
                printAllTickets(ticketQueue);

            } else if ( task == 6 ) {
                //Quit. Future prototype may want to save all tickets to a file
                System.out.println("Quitting program");
                break;
            }
            else {
                //this will happen for 3 or any other selection that is a valid int
                //TODO Program crashes if you enter anything else - please fix
                //Default will be print all tickets
                printAllTickets(ticketQueue);
            }
        }

        scan.close();
       // creating date of the moment
        Date date = new Date();
        File saveOpen = new File("open_tickets.txt");
        File saveResolved = new File("resolved_as_of_" + date);
        // File is keeping track of open tickets
        try
        {
            BufferedWriter openWriter = new BufferedWriter(new FileWriter(saveOpen));
            for (Ticket t : ticketQueue ) {
                openWriter.write(t.getDescription() + "," +
                        t.getPriority() + "," +
                        t.getReporter() + "," +
                        t.getDateReported() + "," +
                        t.getTicketID() + "\n"); //Write a toString method in Ticket class
                //println will try to call toString on its argument
            }
            openWriter.close();// closing the open file
            // Opening new file to keep track of result file tickets
            BufferedWriter resolvedWriter = new BufferedWriter(new FileWriter(saveResolved));
            for (Ticket t : resolvedTickets ) {
                resolvedWriter.write(t.getDescription() + "," +
                        t.getPriority() + "," +
                        t.getReporter() + "," +
                        t.getDateReported() + "," +
                        t.getTicketID() + "," +
                        t.getResolution() + "," +
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

    protected static void deleteTicket(LinkedList<Ticket> ticketQueue) {
        //What to do here? Need to delete ticket, but how do we identify the ticket to delete?
        //TODO implement this method
        //int variable to hold ticket ID to be deleted
        int deleteID = 0;

        TicketManager.printAllTickets(ticketQueue);   //display list for user

        //Variable to control loop
        boolean done = false;
        while(!done)
        {
            if (ticketQueue.size() == 0) {    //no tickets!
                System.out.println("No tickets to delete!\n");
                return;
            }

            Scanner deleteScanner = new Scanner(System.in);
            System.out.println("Enter ID of ticket to delete");
            //Try block to validate user input
            try
            {
                deleteID = deleteScanner.nextInt();
                done = true;
            }
            //Catch bad data and prompt the user to try again
            catch(Exception e)
            {
                System.out.println("ERROR: Invalid input, enter an integer number!");
                done = false;
            }

            //Loop over all tickets. Delete the one with this ticket ID
            boolean found = false;
            for (Ticket ticket : ticketQueue) {
                if (ticket.getTicketID() == deleteID) {
                    found = true;
                    Scanner in = new Scanner(System.in);
                    System.out.println("What is the resolution description?");
                    String resolved = in.nextLine();
                    ticket.setResolution(resolved);
                    resolvedTickets.add(ticket);
                    ticketQueue.remove(ticket);
                    System.out.println(String.format("Ticket %d deleted", deleteID));
                    break; //don't need loop any more.
                }
            }
            if (found == false) {
                System.out.println("Ticket ID not found, no ticket deleted");
                //TODO – re-write this method to ask for ID again if not found
                done = false;
            }

            //Added TICKETMANAGER class call to make static method work
            TicketManager.printAllTickets(ticketQueue);  //print updated list
        }

    }

    //Move the adding ticket code to a method
    protected static void addTickets(LinkedList<Ticket> ticketQueue) {
        Scanner sc = new Scanner(System.in);

        boolean moreProblems = true;
        String description;
        String reporter;
        //let's assume all tickets are created today, for testing. We can change this later if needed
        Date dateReported = new Date(); //Default constructor creates date with current date/time
        int priority;

        while (moreProblems){
            System.out.println("Enter problem");
            description = sc.nextLine();
            System.out.println("Who reported this issue?");
            reporter = sc.nextLine();
            System.out.println("Enter priority of " + description);
            priority = Integer.parseInt(sc.nextLine());

            Ticket t = new Ticket(description, priority, reporter, dateReported);
            ticketQueue.add(t);

            //To test, let's print out all of the currently stored tickets
            printAllTickets(ticketQueue);

            System.out.println("More tickets to add?");
            String more = sc.nextLine();
            if (more.equalsIgnoreCase("N")) {
                moreProblems = false;
            }
        }

    }
    protected static void printAllTickets(LinkedList<Ticket> tickets) {
        System.out.println(" ------- All open tickets ----------");

        for (Ticket t : tickets ) {
            System.out.println(t); //Write a toString method in Ticket class
            //println will try to call toString on its argument
        }
        System.out.println(" ------- End of ticket list ----------");

    }

    //Added method to search all tickets for tickets containing search phrase
    protected static LinkedList<Ticket> searchAllTickets(LinkedList<Ticket> tickets) {
        LinkedList<Ticket> searchedTickets = new LinkedList<Ticket>();
        Scanner in = new Scanner(System.in);
        System.out.println("What phrase would you like to search by?");
        String searchWord = in.nextLine();
        String current;

        for (Ticket t : tickets ) {
            current = t.getDescription();

            if(current.contains(searchWord))
            {
                searchedTickets.add(t);
            }
        }

        TicketManager.printAllTickets(searchedTickets);
        return searchedTickets;
    }
    //Added method to delete by issue (assuming this means by description)
    protected static void deleteByIsuue(LinkedList<Ticket> tickets) {
        LinkedList<Ticket> searched = searchAllTickets(tickets);
        int deleteID = 0;
        //Variable to control loop
        boolean done = false;
        while(!done)
        {
            if (tickets.size() == 0) {    //no tickets!
                System.out.println("No tickets to delete!\n");
                return;
            }

            Scanner deleteScanner = new Scanner(System.in);
            System.out.println("Enter ID of ticket to delete");
            //Try block to validate user input
            try
            {
                deleteID = deleteScanner.nextInt();
                done = true;
            }
            //Catch bad data and prompt the user to try again
            catch(Exception e)
            {
                System.out.println("ERROR: Invalid input, enter an integer number!");
                done = false;
            }

            //Loop over all tickets. Delete the one with this ticket ID
            boolean found = false;
            for (Ticket ticket : tickets) {
                if (ticket.getTicketID() == deleteID) {
                    found = true;
                    Scanner in = new Scanner(System.in);
                    System.out.println("What is the resolution description?");
                    String resolved = in.nextLine();
                    ticket.setResolution(resolved);
                    resolvedTickets.add(ticket);
                    tickets.remove(ticket);
                    System.out.println(String.format("Ticket %d deleted", deleteID));
                    break; //don't need loop any more.
                }
            }
            if (found == false) {
                System.out.println("Ticket ID not found, no ticket deleted");
                //TODO – re-write this method to ask for ID again if not found
                done = false;
            }

            //Added TICKETMANAGER class call to make static method work
            TicketManager.printAllTickets(tickets);  //print updated list
        }
    }

}