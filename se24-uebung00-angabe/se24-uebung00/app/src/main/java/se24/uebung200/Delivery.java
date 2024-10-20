package se24.uebung200;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.lang.reflect.Array;
import java.util.Scanner;

public class Delivery {

    public LocalDateTime lastChange;
    public String status;
    public String firstName;
    public String lastName;
    public String rawListGoods;
    public int customerAddress;
    public String humanFormatDelivery;

    public Delivery(String rawDeliveryInformation) {
        if (rawDeliveryInformation == null || rawDeliveryInformation.isEmpty()) {
            throw new IllegalArgumentException("Input cannot be null or empty");
        }

        String[] deliveryElements = rawDeliveryInformation.split(";");

        if (deliveryElements.length != 5) {
            throw new IllegalArgumentException("Invalid delivery information format. Expected 5 elements.");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        this.lastChange = LocalDateTime.parse(deliveryElements[0], formatter);

        this.status = deliveryElements[1].trim();

        String[] nameParts = deliveryElements[2].split(",");
        if (nameParts.length != 2) {
            throw new IllegalArgumentException("Invalid name format. Expected '<last name>,<first name>'.");
        }
        this.firstName = nameParts[1].trim();
        this.lastName = nameParts[0].trim();

        this.rawListGoods = deliveryElements[3].trim();

        this.customerAddress = Integer.parseInt(deliveryElements[4].trim());

        this.RawDeliveryToHumanString();
    }

    public void RawDeliveryToHumanString(){
        this.humanFormatDelivery = firstName + " " + lastName + " changed the status of the delivery on " + DeliveryHelper.GetLastChange(lastChange) 
        + " to " + DeliveryHelper.GetStatusName(status) + ".\n";
        this.humanFormatDelivery += DeliveryHelper.GetListOfItems(rawListGoods) + ".\n";
        this.humanFormatDelivery += "The customer address number " + Integer.toString(customerAddress) + ".";
    }

    public static class DeliveryHelper {
        public static ArrayList<Delivery> ConvertFileToDeliveryList(String fileName) {
            ArrayList<Delivery> deliveries = new ArrayList<Delivery>();
            String line = "";
            try {
                BufferedReader br = new BufferedReader(new FileReader(fileName));
                while ((line = br.readLine()) != null) 
                {
                    deliveries.add(new Delivery(line));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return deliveries;
        }

        public static String GetStatusName(String statusLetter){
            String statusName = "";
            switch (statusLetter) {
                case "E":
                    statusName = "'entered'";
                    break;
                case "A":
                    statusName = "'start of delivery'";
                    break;
                case "V":
                    statusName = "'postponed'";
                    break;
                case "Z":
                    statusName = "'delivered'";
                    break;
                default:
                    throw new AssertionError("There is no status name for the letter '" + statusLetter + "'");
            }
            return statusName;
        }

        public static String GetLastChange(LocalDateTime lastChange){
            return lastChange.getMonth() + " " + lastChange.getDayOfMonth() + " at " + lastChange.getHour() + ":" + lastChange.getMinute();
        }

        public static String GetListOfItems(String rawListGoods){
            String listOfItems = "";
            String[] rawItems = rawListGoods.split(",");
            int index = 0;

            for(String rawItem : rawItems){
                String item;
                String[] elements = new String[2];
                if (index == 0) {
                    item = "";
                } else {
                    item = ", ";
                }
                
                int indexOfX = rawItem.indexOf("x");
                if (indexOfX > 0) {
                    elements[0] = rawItem.substring(0, indexOfX).trim(); // quantity
                    elements[1] = rawItem.substring(indexOfX + 1).trim(); // article name
                    
                    String quantityText = elements[0].equals("1") ? "time" : "times";
                    
                    item += elements[0] + " " + quantityText + " the article '" + elements[1] + "'";
                } else {
                    item += rawItem.trim();
                }
            
                listOfItems += item;
                index++;
            }

            return listOfItems;
        }
    }
}

/*
    2022-04-24T15:59:51;Z;Huber,Hans;3xStaubsauger,5xRegenschirm;273308
 */
// date of the last change 
// This is followed by a single character that indicates the new status of a transport order:
//'E' for entered, 'A' for start of delivery, 'V' for postponed, 'Z' for delivered
// the name of the driver in the format <last name>,<first name> 
// a comma-separated list of goods in the format goods in the format <number>x<description>. 

// The customer number of the addressee concludes the entry.

/*Hans Huber changed the status of a delivery on April 25 at 15:59 to 'delivered'. 
    The delivery contains 3 times the article 'vacuum cleaner', 5 times the article 'umbrella'.
    The addressee has the customer number 273308. */