package com.zipidat.restaurant.manager;

import java.util.ArrayList;
import java.util.Collections;

import com.zipidat.restaurant.RestaurantVector;

public class Restaurant 
{
	private static final int KTHRESSHOLD = 1;
	private ArrayList<RestaurantVector> restVectorArray = new ArrayList<RestaurantVector>();
	public static ArrayList<String[]> codeWords = new ArrayList<String[]>();
	
	public Restaurant()
	{
		codeWords = new ArrayList<String[]>();
		String[] files = {"/american.txt", "/chinese.txt", "/greek.txt", "/indian.txt", 
								"/italian.txt", "/mexican.txt", "/japanese.txt"};
		//String base = "/var/lib/tomcat6/webapps/RestaurantFinder/WEB-INF/TextFiles";
		String base = "/Users/abhinavkhanna/Documents/Zipidat/tomcat/webapps/RestaurantFinder/WEB-INF/TextFiles";
		// create the arrays from the files;
		for (int i = 0; i < files.length; i++)
		{
			In in = new In(base + files[i]);
			String[] s = in.readAll().split(",");
			codeWords.add(s);
		}
	}
	
	public ArrayList<RestaurantVector> classify(RestaurantVector v1)
	{
		double[] distances = new double[restVectorArray.size()];
		for(int i = 0; i < distances.length; i++)
		{
			distances[i] = v1.computeDistance(restVectorArray.get(i));
		}

		int index1 = getMinValue(distances);
		
		//System.out.println(distances[index1]);
		String chosen = restVectorArray.get(index1).getName().toString();


		
		
		// sort the array by the comparator
		Collections.sort(restVectorArray, v1.DISTANCE_ORDER);
		
		//System.out.println(restVectorArray);
		
		return restVectorArray;
	}
	
	public void addVectoToArray(RestaurantVector rv)
	{
		restVectorArray.add(rv);
	}
	
	public static int getMinValue(double[] numbers){  
		double minValue = Double.MAX_VALUE; // arbitrary constant
		int returnValue = 0;
		
		for(int i=0;i < numbers.length;i++){  
			if(numbers[i] < minValue && numbers[i] > 0 && numbers[i] != Double.NaN){  
				minValue = numbers[i];
				returnValue = i;
			}  
		}
		System.out.println("minValue: " + minValue);
		
		return returnValue;  
	}

	public void initializeVectorArray() {
		// TODO Auto-generated method stub
		this.restVectorArray = new ArrayList<RestaurantVector>();
	}
}
