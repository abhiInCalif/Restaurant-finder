package com.zipidat.restaurant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.Gson;
import com.zipidat.yelp.Yelp;
import com.zipidat.yelp.YelpSearchResult;

/**
 * The purpose of this class is to scrape yelp!
 * It will pull information about restaurants in your nearby area.
 * @author abhinavkhanna
 *
 */
public class YelpScraper
{
	private double lat;
	private double lon;
	private String consumerKey;
	private String consumerSecret;
	private String token;
	private String tokenSecret;
	
	
	public YelpScraper(double lat, double lon)
	{
		this.lat = lat;
		this.lon = lon;
	}
	
	public YelpScraper(String consumerKey, String consumerSecret, String token, String tokenSecret, double lat, double lon)
	{
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
		this.token = token;
		this.tokenSecret = tokenSecret;
		this.lat = lat;
		this.lon = lon;
	}
	
	public String scrapePage(String name, String location) throws IOException
	{
		String description = null;
		HtmlPage htmlPage;
		name = name.replace(' ', '-');
		location = location.replace(' ', '-');
		WebClient web = new WebClient(BrowserVersion.FIREFOX_10);
		String url = "http://yelp.com/biz/" + name + "-" + location;
		url = url.toLowerCase();
		System.out.println(url);

		try {
			htmlPage = web.getPage(url);
			description = htmlPage.asText();
		}
		catch(Exception e)
		{
			// do nothing
			System.out.println("ERROR");
		}
		//HtmlPage htmlPage = web.getPage("http://yelp.com/biz/starbucks-los-altos");
		System.out.println(description);
		
		
		return description;
	}
		
	public YelpSearchResult callYelp()
	{
		Yelp yelp = new Yelp(consumerKey, consumerSecret, token, tokenSecret);
		String response = yelp.search("restaurants", this.lat, this.lon);
		System.out.println(response);
		YelpSearchResult yr = new Gson().fromJson(response, YelpSearchResult.class);
		
		return yr;
	}
	
	public String getReviews(String name, String location)
	{
		String json = null;
		if(this.consumerKey != null)
		{
			json = new Yelp(this.consumerKey, this.consumerSecret, this.token, this.tokenSecret).getReview(name, location).toLowerCase();
		}
		
		return json;
	}
	
}
