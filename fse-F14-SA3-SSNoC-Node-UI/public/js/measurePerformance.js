/**
 * Created by Admin on 2014/10/14.
 */
function init() {
    var serverBaseUrl = document.domain;
    var name;
    var socket = io.connect(serverBaseUrl);

    var test;
    //Time Variables
    var sendTime;
    var endTime;
    var myInterval;
    var duration;
    // Posts & Gets Variables
    var postsCount;
    var postsPerSec =0;
    var getsCount
    var getsPerSec=0;
    //Out of Memory
    var outOfMemoryTrue=0;

    $('#setupButton').click(function () {
        if (test == 0) {
            setupMeasurePerformance();
        }
        else {
            alert("A measurement is still in progress!!!!!");
        }
    });

    function setupMeasurePerformance(duration,testMessage) {
        $.ajax({
            url: '/setup',
            type: 'POST',
            dataType: 'json',
            data:{duration:duration,testMessage:testMessage}
        }).done(function (data) {
            if(data){
                postsCount=0;
                getsCount=0;
                SendingRequests();
                var duration = $("#duration").val();
                $('#alert').text("Performance Monitor Started....");
                $('#alert').show();
                test = 1;
                socket.emit('updateTestStatus', {test: test});
            }
        });
    }

    $('#teardownButton').click(function () {
        if (test == 1) {
            endTime = new Date().getTime();
            duration = (endTime - startTime)/1000;
            console.info(duration);
            clearInterval(myInterval);
            setTimeout(function(){
                test = 0;
                socket.emit('updateTestStatus', {test: test});
                teardownMeasurePerformance();
            },1000);
        }
        else {
            alert("No measurement is in progress!!!!!");
        }
    });

    function teardownMeasurePerformance() {
        console.log("tearing");
        $.ajax({
            url: '/teardown',
            type: 'POST',
            dataType: 'json'
        }).done(function (data) {
            console.info("Stop Performance Monitor request Sent");
            postsPerSec=(postsCount/duration).toFixed();
            getsPerSec=(getsCount/duration).toFixed();
        });
    }

    function SendingRequests() {
        duration = $("#duration").val();
        startTime = new Date().getTime();
        endTime = startTime + duration*1000;
        myInterval = setInterval(function(){
            var testMessage = "Today is a day, I have to confess that. What else to say";
                $.ajax({
                    url: '/sendTestWallMessage',
                    type: 'POST',
                    dataType: 'json',
                    async:false,
                    data: {message: testMessage}
                }).done(function (data) {
                    if (data == "wall message saved") {
                        postsCount += 1;
                    }
                    if (data == "Free Memory<2MB") {
                        endTime = new Date().getTime();
                        duration = (endTime - startTime) / 1000;
                        console.info(duration);
                        outOfMemoryTrue=1;
                        clearInterval(myInterval);
                        test = 0;
                        socket.emit('updateTestStatus', {test: test});
                        teardownMeasurePerformance();
                        $("#alert").html("System out of memory");
                    }
                });
            if(outOfMemoryTrue==0){
                $.ajax({
                    url:'/getMessageWall',
                    type: 'GET',
                    dataType: 'json'
                }).done(function (data) {
                    getsCount+=1;
                });
            }
            if(outOfMemoryTrue==0) {
                var currentTime = new Date().getTime();
                console.log("currentTime:" + currentTime);
                if (currentTime >= endTime) {
                    clearInterval(myInterval);
                    setTimeout(function () {
                            test = 0;
                            socket.emit('updateTestStatus', {test: test});
                            teardownMeasurePerformance();
                    }, 1000);
                }
            }
        }, 1);
    }

    $('#viewButton').click(function () {
        if(test == 0){
            $("#alert").html('');
            $("#alert").html("<table><tr><th>Post Per Second</th><th>Get Per Second</th></tr><tr>"
            + "<td>" + postsPerSec + "</td>"
            + "<td>" + getsPerSec + "</td></tr></table>");
        }
        else{
            alert("A measurement is still in progress!!!!!");
        }
    })

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
        socket.emit('getTestStatus');
    });

    socket.on('testStatus', function (data) {
        test = data.test;
        if (test == 0) {
            $("#alert").html("No Measure Performance Running!!!!!!");
        }
        else {
            $("#alert").html("Measure Performance Running........");
        }
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