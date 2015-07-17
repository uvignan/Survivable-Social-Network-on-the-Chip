/**
 * Created by Admin on 2014/10/14.
 */
function init() {
    var serverBaseUrl = document.domain;

    var socket = io.connect(serverBaseUrl);

    $("#moreOption").hide();

    var stopwords = "a,able,about,across,after,all,almost,also,am,among,an,and,any,are,as,at,be,because,been,but,by,can,cannot,could,dear,did,do,does,either,else,ever,every,for,from,get,got,had,has,have,he,her,hers,him,his,how,however,i,if,in,into,is,it,its,just,least,let,like,likely,may,me,might,most,must,my,neither,no,nor,not,of,off,often,on,only,or,other,our,own,rather,said,say,says,she,should,since,so,some,than,that,the,their,them,then,there,these,they,this,tis,to,too,twas,us,wants,was,we,were,what,when,where,which,while,who,whom,why,will,with,would,yet,you,your";
    var stopwordsArray = stopwords.split(",");

    var results;
    var curPos;
    var online_users;
	var name;
    function renderTen(){
    	var newLines;
    	var i = curPos+1;
    			// console.log("results" + results.length);

    	while( i < results.length && i - curPos <= 10){
    		newLines = newLines + "<tr>";
    		    			// console.log("i" + i);

    		$.each(results[i], function(key, value) { 
			  newLines = newLines + "<td>" + value + "</td>"
			});
    		newLines = newLines + "</tr>";
    		i++;
    	}
    	curPos = i-1;
    	$("tbody").append(newLines);
    	if(results.length-1 > curPos){
    		$("#moreOption").show();
    	}
    	else{
    		$("#moreOption").hide();
    	}
    }

	function renderResults(contentArray,type){
                        console.log("start render");

		var creatTable = "<table class='table'><thead><tr>";
		results = contentArray;

		curPos = -1;
                                console.log("contentArray:" + contentArray[0]);

		$.each(contentArray[0], function(key, value) { 
			creatTable = creatTable + "<th>" + key + "</th>";
		});

		creatTable = creatTable + "</tr></thead><tbody></tbody><table>";

       	$("#alert").html(creatTable);
                console.log("new table created");

        renderTen();
    }



	function sendOutQuery(content,type){
        	console.log("content:" + content);
        	console.log("type:" + type);

        $.ajax({
            url: '/search',
            type: 'POST',
            dataType: 'json',
            data:{content:content,type:type}
        }).done(function (data) {
        	console.log("sent successfully!");
			console.log(data.queryResult);


        	if(data.hasOwnProperty('queryResult') && data.queryResult.length >= 1 ){
        		var toDisplay = data.queryResult;
	        	if(data.queryType == "User Search By Name" || data.queryType == "User Search By Name"){
	        		toDisplay = reorder(data.queryResult);
	        	}
            console.log(toDisplay);

	        	renderResults(toDisplay,data.queryType);
        	}
			else{
				$("#alert").html("No Results!");
			}

        });
	}

	function reorder(queryResult){

                socket.emit('requireParticipant');

		                      // participants.online[data.id] = {'userName' : data.name, 'status': data.status, 'statusDate' : data.statusDate};
                var onlineArray = [];
                var offlineArray = [];

                for(i = 0; i < queryResult.length;i++){
                	var curName = queryResult[i].USER_NAME;
                	var flag = 0;
                	for (var k in online_users){
	                    // if (typeof onlineParticipant[k] !== 'function') {
	                         if(k == curName){
	                         	onlineArray.push(queryResult[i]);
	                         	flag = 1;
	                         	break;
	                         }
	                    // }
                	}
                	if(flag == 0){
                		offlineArray.push(queryResult[i]);
                	}
                }
                console.log(offlineArray);
				var res = onlineArray.concat(offlineArray);
				return res;
	}

  
	function contentFilter(content){

		var contentArray = content.split(" ");
		var newContentArray = [];

		for (i = 0; i < contentArray.length; ++i) {
			var flag = 0;
			for (j = 0; j < stopwordsArray.length; ++j) {
			    if(contentArray[i] == stopwordsArray[j]){
		    		flag = 1;
		    		break;
		    	}
			}
			if(flag == 0){
		    	newContentArray[newContentArray.length] = contentArray[i];
		    }
		}
		return newContentArray;
	}


    $('a.searchType').click(function () {
    	$this = $(this);
    	$("#currentSearchType").html($this.html());
    });

    $('#searchButton').click(function () {
    	var currentSearchType = $("#currentSearchType").html();
    		    	console.log("!!!:" + currentSearchType);

    	if(currentSearchType == "Choose Search Type"){
    		$("#alert").html("Please choose search type!");
    	}
    	else{

	    	var content = $("#content").val();

	    	console.log("1content:" + content);

			var filteredContent;

	    	if(currentSearchType == "User Search By Name" || currentSearchType == "User Search By Status"){
	    		filteredContent = content;
	    	}
	    	else{
	    		filteredContent = contentFilter(content);
	    	}

	    	if(filteredContent.length == 0){
	    		$("#alert").html("No Results!");
	    	}
	    	else{
	    		sendOutQuery(filteredContent,currentSearchType);
	    	}
    	}
	});


	$("#moreOption").click(function(){
		renderTen();
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


	socket.on('connect', function () {
    });


    socket.on('getParticipant', function (data) {

        online_users = [];


        for (var sId in data.participants.online){
            userName = data.participants.online[sId].userName;
                                    console.log("participants:" + data.participants.online[sId].userName);

            online_users.push(userName);
        }

                console.log("online_users:" + online_users);

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
$(document).on('ready', init);
