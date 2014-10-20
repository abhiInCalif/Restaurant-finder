package com.zipidat.restaurant.service;

import org.hibernate.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import com.zipidat.restaurant.RestaurantVector;
import com.zipidat.restaurant.YelpScraper;
import com.zipidat.restaurant.manager.Restaurant;
import com.zipidat.yelp.Business;
import com.zipidat.yelp.YelpSearchResult;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Controller
@RequestMapping("/main")
public class Main 
{
	private static Restaurant manager = new Restaurant();
	
	@RequestMapping(value = "/recommend", method = RequestMethod.GET)
	public String generateRecommendation(@RequestParam("am") double american, @RequestParam("in") double indian, @RequestParam("ch") double chinese,
			@RequestParam("mex") double mexican, @RequestParam("ita") double italian, @RequestParam("lat") double lat,
			@RequestParam("lon") double lon, @RequestParam("jap") double japanese, @RequestParam("c") String callback, ModelMap model) throws IOException
	{
		manager.initializeVectorArray();
		// create the Restaurant Manager object
		double total = american + mexican + italian + indian + chinese + japanese;
		american = american / total;
		chinese = chinese / total;
		indian = indian / total;
		mexican = mexican / total;
		italian = italian / total;
		japanese = japanese / total;
		
		RestaurantVector rest = new RestaurantVector();
		rest.setVector(american, chinese, 0, indian, italian, mexican, japanese);
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
				 manager.addVectoToArray(rv);
			 }
		 }
		 
		 // calculate the closest vector
		ArrayList<RestaurantVector> ordered = manager.classify(rest);
		
		//GSOn the response and pass it back to the front end
		Gson gson = new Gson();
		Type listOfTestObject = new TypeToken<ArrayList<RestaurantVector>>(){}.getType();
		String json = gson.toJson(ordered, listOfTestObject);
		model.addAttribute("json", json);
		model.addAttribute("callback", callback);
		
		return "JSONView";
	}
}
