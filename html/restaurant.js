var restaurant = {};
var list;
var index = 0;
restaurant.service = {};
restaurant.controller = {};
restaurant.baseUrl = "zipidat.com:8080";
//restaurant.baseUrl = "localhost:8080";

/* This portion is dedicated to calling the server with the data from the inputs */
restaurant.controller = {
	
	chinese: 0,
	american: 0,
	japanese: 0,
	indian: 0,
	mexican: 0,
	italian: 0,
	lat: 37.775,
	lon: -122.4183,
	
	getRestaurant: function()
	{
		// call the service call that gets the items from the backend
		
		// if all the variables are set in some shape or form;
		// call the service function that calls database
		restaurant.service.getRestaurant(this.lat, this.lon, this.chinese, this.indian, this.american, this.mexican, this.italian, this.japanese, function(data)
		{
			// save the data into a list object stored locally;
			list = data;
			index = 0;
			// set the title text to be extracted elements of the name
			var strArray = data[0].name.split("/");
			var id = strArray[strArray.length - 1];
			name = id.replace("-", " ");
			$("#details_text_name").text(name);
			
			// call yelp with another api key, and draw additional business data
			restaurant.service.getBusinessInfo(id, function(data)
			{
				$("#details_yelpImage").attr("src", data.image_url);
				$("#details_text_name").text(data.name);
				$("#details_address").text(data.location.display_address[0] + " " + data.location.display_address[2] + " " + data.location.display_address[3]);
				$("#details_snippetText").text(data.snippet_text);
				
				// handle the reviews;
				var html = "";
				if(data.reviews.length >= 2)
					html += "<p>" + data.reviews[1].excerpt + "..." + "</p>";
				
				
				$("#details_text_reviews").html(html);
				
				//display the fucking thing
				//hide the loading screen first
				$("#LoadingScreen").fadeOut(function()
				{
					$("#DetailsScreen").show();
				});
			});	

		});
	},
	
	tryAgain: function()
	{
		// get the next item in the list and redo above operations
		// set the title text to be extracted elements of the name
		if(index >= list.length) return;
		var strArray = list[++index].name.split("/");
		var id = strArray[strArray.length - 1];
		name = id.replace("-", " ");
		$("#details_text_name").text(name);
		
		// call yelp with another api key, and draw additional business data
		restaurant.service.getBusinessInfo(id, function(data)
		{
			$("#details_yelpImage").attr("src", data.image_url);
			$("#details_text_name").text(data.name);
			$("#details_address").text(data.location.display_address[0] + " " + data.location.display_address[2] + " " + data.location.display_address[3]);
			$("#details_snippetText").text(data.snippet_text);
			
			// handle the reviews;
			var html = "";
			if(data.reviews.length >= 2)
				html += "<p>" + data.reviews[1].excerpt + "..." + "</p>";
			
			
			$("#details_text_reviews").html(html);
			
			//display the fucking thing
			//hide the loading screen first
			$("#LoadingScreen").fadeOut(function()
			{
				$("#DetailsScreen").show();
			});
		});
	}
};

restaurant.service = {
	
	getRestaurant: function(lat, lon, chinese, indian, american, mexican, italian, japanese, callback)
	{
		$.getJSON("http://"+ restaurant.baseUrl +"/RestaurantFinder/main/recommend?lat="+ lat +"&lon="+ lon + "&ch="+ chinese + "&in="+ indian + "&am="+ american + "&mex="+ mexican + "&ita="+ italian + "&jap="+ japanese + "&c=?", callback);
	},
	
	getBusinessInfo: function(id, callback)
	{
		var auth = { 
		  //
		  // Update with your auth tokens
		  //
		  consumerKey: "SJFIOfRJWYmvmZnsiAMNsA",
		  consumerSecret: "3gLEFZwVbMS0F-U0nrZEbECvxqY",
		  accessToken: "x4947ccd6jhL6wDDgWDu0fN5amMfNquH",
		  // This example is a proof of concept, for how to use the Yelp v2 API with javascript.
		  // You wouldn't actually want to expose your access token secret like this in a real application.
		  accessTokenSecret: "fHThrTrsqB2W5zZfcs0SHQf5PHs",
		  serviceProvider: { 
		    signatureMethod: "HMAC-SHA1"
		  }
		};

		var accessor = {
		  consumerSecret: auth.consumerSecret,
		  tokenSecret: auth.accessTokenSecret
		};

		parameters = [];
		parameters.push(['callback', 'cb']);
		parameters.push(['oauth_consumer_key', auth.consumerKey]);
		parameters.push(['oauth_consumer_secret', auth.consumerSecret]);
		parameters.push(['oauth_token', auth.accessToken]);
		parameters.push(['oauth_signature_method', 'HMAC-SHA1']);

		var message = { 
		  'action': 'http://api.yelp.com/v2/business/' + id,
		  'method': 'GET',
		  'parameters': parameters 
		};

		OAuth.setTimestampAndNonce(message);
		OAuth.SignatureMethod.sign(message, accessor);

		var parameterMap = OAuth.getParameterMap(message.parameters);
		parameterMap.oauth_signature = OAuth.percentEncode(parameterMap.oauth_signature)
		console.log(parameterMap);

		$.ajax({
		  'url': message.action,
		  'data': parameterMap,
		  'cache': true,
		  'dataType': 'jsonp',
		  'jsonpCallback': 'cb',
		  'success': function(data, textStats, XMLHttpRequest) {
		    console.log(data);
				callback(data);
		  }
		});
		
	}
	
};