package com.zipidat.restaurant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import com.zipidat.yelp.Business;
import com.zipidat.yelp.YelpSearchResult;

public class Main 
{
	private static final int KTHRESSHOLD = 1;
	private static ArrayList<RestaurantVector> restVectorArray = new ArrayList<RestaurantVector>();
	/**
	 * Processing steps:
	 * 1) Take in the arguments passed
	 * 		Long, lat, American, italian, indian, greek, mexican, japanese, chinese
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException
	{		
		if(args.length != 9) System.out.println("You need to enter in 9 arguments");
		else
		{
			double total = 0;
			for(int i = 2; i < args.length; i++)
			{
				total += Double.parseDouble(args[i]);
			}

			// create the vector
			double lat = Double.parseDouble(args[0]);
			double lon = Double.parseDouble(args[1]);
			double american = Double.parseDouble(args[2]) / total;
			double italian = Double.parseDouble(args[3]) / total;
			double indian = Double.parseDouble(args[4]) / total;
			double greek = Double.parseDouble(args[5]) / total;
			double mexican = Double.parseDouble(args[6]) / total;
			double japanese = Double.parseDouble(args[7]) / total;
			double chinese = Double.parseDouble(args[8]) / total;
			
			RestaurantVector rest = new RestaurantVector();
			rest.setVector(american, chinese, greek, indian, italian, mexican, japanese);
			System.out.println(rest);
			
			// find all the businesses nearby, and then calculate a distance from them;
			YelpScraper ys = new YelpScraper("zTQq-YyO0vENSmb5wHXktw", "Dg34vA3pq-REgWt-tAoQaRskWoo", "PpP2di1RBgHcoJi-uyhalnSsViFNFTfq", "Gr4s2YO4jcJA26FkBGKGl0LZixE", lat, lon);
			 YelpSearchResult ysr = ys.callYelp();
			 
			 for(Business business : ysr.getBusinesses())
			 {
				 String desc = ys.getReviews(business.getName(), business.getLocation().getCity()); 
				 RestaurantVector rv = new RestaurantVector();
				 //System.out.println("Reached Here! Bob");
				 if(desc != null)
				 {
					 //System.out.println("Reached Here! Abhi");
					 rv.createVector(desc);
					 rv.setName(business.getUrl());
					 restVectorArray.add(rv);
				 }
			 }
			 
			 // calculate the closest vector
			 classify(rest);
		}
	}
	
	public static void classify(RestaurantVector v1)
	{
		double[] distances = new double[restVectorArray.size()];
		for(int i = 0; i < distances.length; i++)
		{
			distances[i] = v1.computeDistance(restVectorArray.get(i));
		}

		int index1 = getMinValue(distances);
		
		System.out.println(distances[index1]);
		System.out.println(restVectorArray.get(index1));
		System.out.println(restVectorArray);

		
		
		// sort the array by the comparator
		Collections.sort(restVectorArray, v1.DISTANCE_ORDER);
		
		System.out.println(restVectorArray);
		
	}
	
	public static int getMinValue(double[] numbers){  
		double minValue = numbers[0];
		int returnValue = 0;
		
		for(int i=1;i < numbers.length;i++){  
			if(numbers[i] < minValue && numbers[i] > 0){  
				minValue = numbers[i];
				returnValue = i;
			}  
		}
		System.out.println("minValue: " + minValue);
		
		return returnValue;  
	}
}
