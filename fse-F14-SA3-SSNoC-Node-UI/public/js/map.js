/**
 * Created by Vignan on 10/24/2014.
 */

function init(){
	var serverBaseUrl = document.domain;
	var socket = io.connect(serverBaseUrl);

	var CurUsernameRaw = $("p.navbar-brand").find("span").html();
	var CurNames = CurUsernameRaw.split(" ");
	var CurUserName = CurNames[1];
	var name;
	var curLat;
	var curLong;
	var map;
	var currentMode = "User";
	var markers = Array();

	initMap();

// init map
	function initMap() {

		if (navigator.geolocation) {
			navigator.geolocation.getCurrentPosition(setPosition);
		} else {
			x.innerHTML = "Geolocation is not supported by this browser.";
		}
	}

	function setPosition(position) {

		curLat = position.coords.latitude;
		curLong = position.coords.longitude;
		createMap();
	}

	function createMap(){

		map = L.map('map').setView([curLat, curLong], 17);
		L.tileLayer('../img/MapQuest/{z}/{x}/{y}.jpg', {
			attribution: 'Map data &copy; ',
			maxZoom: 18
		}).addTo(map);

	}


// send request to the server to ask each other to report their location
	function loadUser(){
		cleanMarkers();
		markers = Array();
		// var marker = L.marker([curLat, curLong]).addTo(map);
		// marker.bindPopup(CurUserName);
		//  		markers.push(marker);
		socket.emit("getUserLocations");
	}

// handle the back request from each users
	socket.on('userLoc', function (data) {
		console.log(data.username);
		console.log(data.latitude);
		var marker = L.marker([data.latitude, data.longitude]).addTo(map);
		marker.bindPopup(data.username);
		markers.push(marker);
	});

// send request to the server to fetch data from the database

	function loadMessage(data){
		cleanMarkers();
		markers = Array();
		for(var i = 0; i < data.length; i++){
			console.log(data[i].author);
			console.log(data[i].content);
			console.log(data[i].latitude);
			console.log(data[i].longitude);
			var imgPath=data[i].imgPath;
			var marker = L.marker([data[i].latitude, data[i].longitude]).addTo(map);
			marker.bindPopup(data[i].content + '<img src='+imgPath+' alt="Smiley face" height="42" width="42">');
			console.log(imgPath);
			markers.push(marker);
		}
	}

// it is to clean the markers because everytime when we want to switch the mode, the old markers should be deleted
	function cleanMarkers(){
		for(var i = 0; i < markers.length; i++){
			console.log(markers[i]);
			map.removeLayer(markers[i]);
		}
	}

	function userLocation(){
		//user location gathering
		var userPosLat;
		var userPosLong;
		getLocation();
		function getLocation() {
			if (navigator.geolocation) {
				navigator.geolocation.getCurrentPosition(showPosition);
			} else {
				x.innerHTML = "Geolocation is not supported by this browser.";
				console.log("MapLocation not found");
			}
		}
		function showPosition(position) {
			userPosLat = position.coords.latitude;
			userPosLong = position.coords.longitude;

			socket.emit('emitUserLoc', {name: name, userPosLat:userPosLat, userPosLong:userPosLong});
		}
	}

// button to switch the mode
	$("#changeMode").click(function()
	{
		if(currentMode == "User"){
			$(this).html("Message");
			currentMode = "Message";
			socket.emit("refreshWall",{user_name: name});

		}
		else{
			console.log("adasds" + markers.length);
			$(this).html("User");
			currentMode = "User";
			loadUser();

//			loadMessage();
		}
	});

	socket.on('publicWallMessages',function(data){
		loadMessage(data.messages);
	});

	socket.on("newChatMsgAlert",function(data){
		if(name=data.target){
			alert(data.source+' has sent you a private message \n' +
			'Message: '+data.content);
		}
	});

	socket.on('getLocation',function(){
		userLocation();
	});

	socket.on('connect', function () {
		sessionId = socket.socket.sessionid;
		$.ajax({
			url:  '/user',
			type: 'GET',
			dataType: 'json'
		}).done(function(data) {
			name = data.name;
			var status = data.status;
			var statusDate = data.statusDate;
			socket.emit('newUser', {id: sessionId, name: name, status:status, statusDate:statusDate});
			userLocation();
		});
	});
}
$(document).on('ready',init);

