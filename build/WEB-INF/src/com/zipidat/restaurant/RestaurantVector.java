package com.zipidat.restaurant;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;

import com.zipidat.restaurant.manager.In;
import com.zipidat.restaurant.manager.Restaurant;


public class RestaurantVector
{
	public Comparator<RestaurantVector> DISTANCE_ORDER = new DistanceOrder();
	private double chinese;
	private double italian;
	private double american;
	private double mexican;
	private double indian;
	private double japanese;
	private double greek;
	private boolean formal;
	private String name;
	
	private static final int AMERICAN = 0;
	private static final int CHINESE = 1;
	private static final int INDIAN = 3;
	private static final int MEXICAN = 5;
	private static final int ITALIAN = 4;
	private static final int GREEK = 2;
	private static final int JAPANESE = 6;
	
	
	/**
	 * @return the chinese
	 */
	public double getChinese() {
		return chinese;
	}
	/**
	 * @param chinese the chinese to set
	 */
	public void setChinese(double chinese) {
		this.chinese = chinese;
	}
	/**
	 * @return the italian
	 */
	public double getItalian() {
		return italian;
	}
	/**
	 * @param italian the italian to set
	 */
	public void setItalian(double italian) {
		this.italian = italian;
	}
	/**
	 * @return the american
	 */
	public double getAmerican() {
		return american;
	}
	/**
	 * @param american the american to set
	 */
	public void setAmerican(double american) {
		this.american = american;
	}
	/**
	 * @return the mexican
	 */
	public double getMexican() {
		return mexican;
	}
	/**
	 * @param mexican the mexican to set
	 */
	public void setMexican(double mexican) {
		this.mexican = mexican;
	}
	/**
	 * @return the indian
	 */
	public double getIndian() {
		return indian;
	}
	/**
	 * @param indian the indian to set
	 */
	public void setIndian(double indian) {
		this.indian = indian;
	}
	/**
	 * @return the japanese
	 */
	public double getJapanese() {
		return japanese;
	}
	/**
	 * @param japanese the japanese to set
	 */
	public void setJapanese(double japanese) {
		this.japanese = japanese;
	}
	/**
	 * @return the formal
	 */
	public boolean isFormal() {
		return formal;
	}
	/**
	 * @param formal the formal to set
	 */
	public void setFormal(boolean formal) {
		this.formal = formal;
	}
	/**
	 * @return the greek
	 */
	public double getGreek() {
		return greek;
	}
	/**
	 * @param greek the greek to set
	 */
	public void setGreek(double greek) {
		this.greek = greek;
	}
	
	private double calculateRating(String description, int i)
	{
		double count = 0;
		
		for(int j = 0; j < Restaurant.codeWords.get(i).length; j++)
			count += description.split(Restaurant.codeWords.get(i)[j]).length - 1;
		
		return count;
	}
	
	public void createVector(String description) throws IOException
	{
		double total = 0;
		
		double american = this.calculateRating(description, AMERICAN);
		double chinese = this.calculateRating(description, CHINESE);
		double greek = this.calculateRating(description, GREEK);
		double indian = this.calculateRating(description, INDIAN);
		double italian = this.calculateRating(description, ITALIAN);
		double mexican = this.calculateRating(description, MEXICAN);
		double japanese = this.calculateRating(description, JAPANESE);
		
		total = american + chinese + greek + italian + indian + mexican + japanese;
		
		this.setAmerican(american / total);
		this.setChinese(chinese / total);
		this.setGreek(greek / total);
		this.setIndian(indian / total);
		this.setItalian(italian / total);
		this.setJapanese(japanese / total);
		this.setMexican(mexican / total);
		
	}
	
	public void setVector(double american, double chinese, double greek, double indian, double italian, double mexican, double japanese)
	{
		this.setAmerican(american);
		this.setChinese(chinese);
		this.setGreek(greek);
		this.setIndian(indian);
		this.setItalian(italian);
		this.setJapanese(japanese);
		this.setMexican(mexican);
	}
	
	public double computeDistance(RestaurantVector v2)
	{
		double squaredMagnitude = 0;
		
		squaredMagnitude += (this.getAmerican() - v2.getAmerican()) * (this.getAmerican() - v2.getAmerican());
		squaredMagnitude += (this.getChinese() - v2.getChinese()) * (this.getChinese() - v2.getChinese());
		squaredMagnitude += (this.getMexican() - v2.getMexican()) * (this.getMexican() - v2.getMexican());
		squaredMagnitude += (this.getGreek() - v2.getGreek()) * (this.getGreek() - v2.getGreek());
		squaredMagnitude += (this.getIndian() - v2.getIndian()) * (this.getIndian() - v2.getIndian());
		squaredMagnitude += (this.getJapanese() - v2.getJapanese()) * (this.getJapanese() - v2.getJapanese());
		squaredMagnitude += (this.getItalian() - v2.getItalian()) * (this.getItalian() - v2.getItalian());
		
		double root = Math.sqrt(squaredMagnitude);
		
		return root;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	public String toString()
	{
		String str = this.name + ": " + "American: " + this.american;
		str += " Chinese: " + this.chinese + " Italian: " + this.italian;
		str += " Mexican: " + this.mexican + " Japanese: " + this.japanese;
		str += " Indian: " + this.indian;
		
		return str;
	}
	
	private class DistanceOrder implements Comparator<RestaurantVector>
	{

		public int compare(RestaurantVector o1, RestaurantVector o2) 
		{
			if(computeDistance(o1) < computeDistance(o2))
				return -1;
			else if(computeDistance(o1) > computeDistance(o2))
				return 1;
			else
				return 0;
		}
		
	}
	

}
