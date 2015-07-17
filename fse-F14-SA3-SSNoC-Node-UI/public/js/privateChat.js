function init(){
	var serverBaseUrl = document.domain;
    var socket = io.connect(serverBaseUrl);

	//source and target user details
	var source = $('#sourceUser').val();
	var target = $('#targetUser').val();

	var name;

    $('#sendChatMessageButton').click(function(){
		var content=$('#chatMessage').val();
		var d = new Date();
		var minute = d.getMinutes();
		var hour = d.getHours();
		var date = d.getDate();
		var month = d.getMonth()+1;
		var year = d.getFullYear();
		var postedAt = year + '-' + (month < 10 ? '0' + month : month) + '-' + (date < 10 ? '0' + date : date) + ' ' + (hour < 10 ? '0' + hour : hour) + ':' + (minute < 10 ? '0' + minute : minute);
		socket.emit('newChatMessage',{source:source,target:target,content:content,postedAt:postedAt});
    	$.ajax({	
            url: '/sendChatMessage',
            type: 'POST',
            dataType: 'json',
            data : { source:source,target:target,content:content}
    	}).done(function(data){
			$('#chatMessage').val('');
    		console.log("private message saved");
    	}).fail(function(){
    		console.log("private message save failed");
    	});
	});

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

	socket.on(source,function(data){
		var name = data.source;
		var content = data.content;
		var postedAt = data.postedAt;
		var message = '<div class="panel panel-info">' +
			'<div class="panel-heading"><strong>' + name + '</strong></div>' +
			'<table class="table" style="table-layout: fixed" width="100%">' +
			'<tr><td width="57%" style="word-wrap: break-word ">' + content + '</td>' +
			'<td width="43%">' + postedAt + '</td></tr>' +
			'</table>' +
			'</div>';
		$('#privateChat').append(message);
	});

	socket.on(target,function(data){
		if(source==target){
			var name = data.source;
			var content = data.content;
			var postedAt = data.postedAt;
			var message = '<div class="panel panel-info">' +
				'<div class="panel-heading"><strong>' + name + '</strong></div>' +
				'<table class="table" style="table-layout: fixed" width="100%">' +
				'<tr><td width="57%" style="word-wrap: break-word ">' + content + '</td>' +
				'<td width="43%">' + postedAt + '</td></tr>' +
				'</table>' +
				'</div>';
			$('#privateChat').append(message);
		}
	});

	socket.on('getLocation',function(){
		userLocation();
	});

	socket.on('connect', function () {
			sessionId = socket.socket.sessionid;
			//getting the user details for the session to keep him online if he is in this page
			$.ajax({
				url:  '/user',
				type: 'GET',
				dataType: 'json'
			}).done(function(data) {
				name = data.name;
				var status = data.status;
				var statusDate = data.statusDate;
				socket.emit('newUser', {id: sessionId, name: name,status:status, statusDate:statusDate});
				userLocation();
			});
			//getting the conversation history between the source and target
			$.ajax({
				url:'/getChatHistory',
				type:'POST',
				dataType:'json',
				data : { source:source,target:target }
			}).done(function(data){
			for(var i=0; i<data.length; i++) {
				var name = data[i].author;
				var content = data[i].content;
				var postedAt = data[i].postedAt;
				var message = '<div class="panel panel-info">' +
					'<div class="panel-heading"><strong>' + name + '</strong></div>' +
					'<table class="table" style="table-layout: fixed" width="100%">' +
					'<tr><td width="57%" style="word-wrap: break-word ">' + content + '</td>' +
					'<td width="43%">' + postedAt + '</td></tr>' +
					'</table>' +
					'</div>';
				$('#privateChat').append(message);
			}
		});
	});
}
$(document).on('ready',init);
