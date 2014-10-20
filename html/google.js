var mapfunction = {};
var infoWindow;
var image = new google.maps.MarkerImage('http://i.imgur.com/idYqEDH.png',
new google.maps.Size(46, 46),
// origin
new google.maps.Point(0 , 0),
// anchor
new google.maps.Point(23, 23));

var geocoder = new google.maps.Geocoder();
var mc;
var markernumber;
var clusterStyles = [
  {
	textSize: 20,
    textColor: 'white',
    url: 'http://i.imgur.com/idYqEDH.png',
    height: 46,
    width: 46
  }
 ];
mapfunction = {
	initialize: function() {
		markernumber = 0;
        var mapDiv = document.getElementById('map-canvas');
        var latLng = new google.maps.LatLng(40.348523,-74.658751);
        map = new google.maps.Map(mapDiv, {
          center: latLng,
          zoom: 17,
          mapTypeId: google.maps.MapTypeId.ROADMAP
        });
        var image = 'http://i.imgur.com/QlTI3be.png';
        var image2 = 'http://i.imgur.com/STMwfco.png';
        var myLatLng = new google.maps.LatLng(40.348523,-74.658751);

        infoWindow = new google.maps.InfoWindow();
        google.maps.event.addListenerOnce(map, 'tilesloaded', function() {
        controller.drawAll();
        });

        // sets code for #tracking, i.e. map updates every time its moved
        google.maps.event.addListener(map, 'dragend', function() {
        controller.drawVisible();
        });
        google.maps.event.addListener(map, 'zoom_changed', function() {
        controller.drawVisible();
        });

        mc = new MarkerClusterer(map);
        service.getLocation(-500, -500, 500, 500, function(data)
		{
			// loads into mc all of the events only once though
			for(var i = 0; i < data.length; i++)
			{
				markernumber++;
				mapfunction.displayEvents(data[i].host, data[i].description, data[i].id, data[i].lat, data[i].lon);
			}
			if (data.length != 0)
			{
				markernumber = data[data.length - 1].id + 1;
			}
			mc.setStyles(clusterStyles);
		});
        mc.setZoomOnClick(false);

        // every time cluster is clicked, mouseover, etc
		google.maps.event.addListener(mc, 'click', function(cluster) {
			alert(cluster.getSize() + " events are currently located here");
			var markerarray = cluster.getMarkers();
			var list2 = [];
			var counter = 0;
			for (var i = 0; i < markerarray.length; i++)
			{
				list2[counter] = list[markerarray[counter].myId - 1];

			}
			controller.generateHTML(idarray);
			alert(cluster.getSize() + " done");

		});
		google.maps.event.addListener(mc, 'mouseover', function(cluster) {
			var markerarray = cluster.getMarkers();
			for (var i = 0; i < markerarray.length; i++)
			{
				$("#" + (markerarray[i].myId - 1)).addClass("map_iconHover");
			}
		});
		google.maps.event.addListener(mc, 'mouseout', function(cluster) {
			var markerarray = cluster.getMarkers();
			for (var i = 0; i < markerarray.length; i++)
			{
				$("#" + (markerarray[i].myId - 1)).removeClass("map_iconHover");
			}
		});
	},

	// Creates a google LatLng object of the location given an address
	// Uses the function input to do something to this LatLng object 
	findGeocode: function(address, myfunction) {
		geocoder.geocode( { 'address': address}, function(results, status) {
			if (status == google.maps.GeocoderStatus.OK)
			{
				myfunction(results[0]);
			}
		});
	},

	// display ONE event
	displayEvents: function(host, desc, id, lat, lng)
	{
		var latlng = new google.maps.LatLng(lat, lng, true);
		var marker = new google.maps.Marker({
			map: map,
			position: latlng,
			icon: image
		});

		marker.setValues({
			myId: id
		});

		// click listener
		google.maps.event.addListener(marker, 'click', function() {
			var myHtml = '<b>Hosted by '+ host +'</b><br />' + desc + '<br/>';
			infoWindow.setContent(myHtml);
			infoWindow.open(map, marker);
			// sets the other picture
			var image2 = new google.maps.MarkerImage('http://i.imgur.com/PNb5uv6.png',
			new google.maps.Size(46, 46),
			// origin
			new google.maps.Point(0 , 0),
			// anchor
			new google.maps.Point(23, 23));
			marker.setIcon(image2);


			// sets the gold glow
			var image3 = new google.maps.MarkerImage('http://i.imgur.com/x5aTeU5.png',
			new google.maps.Size(70, 70),
			// origin
			new google.maps.Point(0 , 0),
			// anchor
			new google.maps.Point(38, 35));
			marker.setShadow(image3);
		});

		// mouseover listener
		google.maps.event.addListener(marker, 'mouseover', function() {
			// sets the other picture
			var image2 = new google.maps.MarkerImage('http://i.imgur.com/PNb5uv6.png',
			new google.maps.Size(46, 46),
			// origin
			new google.maps.Point(0 , 0),
			// anchor
			new google.maps.Point(23, 23));
			marker.setIcon(image2);

			// sets the gold glow
			var image3 = new google.maps.MarkerImage('http://i.imgur.com/x5aTeU5.png',
			new google.maps.Size(70, 70),
			// origin
			new google.maps.Point(0 , 0),
			// anchor
			new google.maps.Point(38, 35));
			marker.setShadow(image3);

			$("#" + id).addClass("map_iconHover");
		});

		// sets to default image if mouse moves away
		google.maps.event.addListener(marker, 'mouseout', function() {
			marker.setIcon(image);
			marker.setShadow(null);
			$("#" + id).removeClass("map_iconHover");
		});

		mc.addMarker(marker, true);
	}
};
google.maps.event.addDomListener(window, 'load', mapfunction.initialize);
