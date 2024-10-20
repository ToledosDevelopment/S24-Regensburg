package se24.uebung200;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        //implement the processing of an input file  
        // String fileName = System.getProperty("user.dir") + File.separator + "resources/auftraege.csv";

        // String fileName = args[0];
        // String filePath = System.getProperty("user.dir") + File.separator + ".." + File.separator + "resources" + File.separator + fileName;

        String filePath = "C:/Users/ObedT/Desktop/Universidad/Regensburg/Software Engineering/se24-uebung00-angabe/se24-uebung00/app/src/main/resources/auftraege.csv";

        ArrayList<Delivery> deliveries = Delivery.DeliveryHelper.ConvertFileToDeliveryList(filePath);

        for (Delivery delivery : deliveries) {
            System.err.println(delivery.humanFormatDelivery);
        }
    }
}

/*
    2022-04-24T15:59:51;Z;Huber,Hans;3xStaubsauger,5xRegenschirm;273308
*/
// date of the last change 
// This is followed by a single character that indicates the new status of a transport order:
// 'E' for entered, 'A' for start of delivery, 'V' for postponed, 'Z' for delivered
// the name of the driver in the format <last name>,<first name> 
// a comma-separated list of goods in the format goods in the format <number>x<description>. 
// The customer number of the addressee concludes the entry.
