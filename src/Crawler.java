import java.io.*;
import java.nio.file.*;
import java.util.Scanner;
import java.util.concurrent.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.MalformedURLException;
import java.net.URL;

//we need jsoup library for html parsing
//Java.net.URL also useful

public class Crawler {
    
    //argument variables
    private static Path seedPath;
    private static int numPagesToCrawl;
    private static int numLevels;
    private static Path storagePath;
    
    //URL queue. Many threads will access it.
    private static ConcurrentLinkedQueue<String> frontier = new ConcurrentLinkedQueue<String>();
    
	public static void main(String[] args){
	    
	    //initializing the variables
	    seedPath = null;
	    numPagesToCrawl = 0;
	    numLevels = 0;
	    storagePath = null;
	    
	    //prints error message if arguments are wrong then exits
	    if(args.length < 3 || args.length > 4){
	        System.out.println("Incorrect arguments passed. Arguments are of the form: \n"
                    + "[Seed file path] [# Pages to Crawl] [# of Levels][optional: page storage path]");
            return;
	    }
	    
	    //sets the variables to the arguments
        try {
            seedPath = Paths.get(args[0]);
	        numPagesToCrawl = Integer.parseInt(args[1]);
	        numLevels = Integer.parseInt(args[2]);
	        
	        if(args.length ==  4) storagePath = Paths.get(args[3]);
            else storagePath = Paths.get("./crawledPages");
	        
	        File dir = storagePath.toFile();
            if(dir.mkdirs()) System.out.println("Storage folder successfully created");
            else if(!dir.exists()){
                System.out.println("Storage folder creation failed. Exiting...");
                return;
            }
        }catch(NumberFormatException e){
	        System.out.println("Are arg[1] and arg[2] numbers?");
            e.printStackTrace();
	    }catch(InvalidPathException e){
	        System.out.println("Invalid path");
	        e.printStackTrace();
	    }
        
	    ////prints out the variables
	    //System.out.println("Seed File: " + seedPath);
	    //System.out.println("Number of Pages to Crawl: " + numPagesToCrawl);
	    //System.out.println("Number of levels: " + numLevels);
	    //System.out.println("Storage Path: " + storagePath);
	    
	    //initialize the frontier
        Scanner seedScanner = null;
        try {
            seedScanner = new Scanner(seedPath);
            while(seedScanner.hasNext()){
                String URL = seedScanner.next();
                frontier.add(URL);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            seedScanner.close();
        }
        
        //prints the contents of frontier
        System.out.println("Seeds:");
	    for(String i : frontier){
	        //checks if the URL string begins with a valid protocol. Adds one if it doesn't have one
	        String link = i;
	        if(!link.startsWith("http://") && !link.startsWith("https://")){
    	        link  = "http://" + link;
	        }
	        
	        //URL u = null;
            //try {
            //    u = new URL(link);
            //    System.out.println(u);
            //} catch (MalformedURLException e) {
            //    e.printStackTrace();
            //}
	        String doc = null;
	        try {
	            System.out.println(link + "\n");
	            doc = Jsoup.connect(link).get().html();
            } catch (IOException e) {
                e.printStackTrace();
            }
	        
	        System.out.println(doc);
	    }
	}
}