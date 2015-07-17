
var publicWall=require('./models/MessageWallRest');

var announcements=require('./models/announcementsRest');


module.exports = function(_, io, participants, test, passport) {
  io.on("connection", function(socket){
    socket.on("newUser", function(data) {

      participants.online[data.id] = {'userName' : data.name, 'status': data.status, 'statusDate' : data.statusDate};
      io.sockets.emit("newConnection", {participants: participants});
    });



    socket.on("requireParticipant", function() {
      io.sockets.emit("getParticipant", {participants: participants});
    });


      socket.on("upUser", function(data) {

          for(var i = 0; i < participants.all.length; i++){
              console.log("gooood" + participants.all[i]);
              if(participants.all[i].userName==data.oldname){
                  if(data.name!= ""){
                      participants.all[i].userName=data.name;
                  }else{
                      participants.all[i].userName;
                  }
                  if(data.role!=""){
                      participants.all[i].privilegeLevel=data.role;
                  }else{participants.all[i].privilegeLevel;}
                  if(data.accountStatus!=""){
                      participants.all[i].accountStatus=data.accountStatus;
                  }else{
                      participants.all[i].accountStatus;
                  }
              }
          }
          io.sockets.emit("Update");

      });

    socket.on("statusUpdate", function(data) {
      participants.online[data.id] = {'userName' : data.name, 'status': data.status, 'statusDate' : data.statusDate};
       for(var i = 0; i < participants.all.length; i++){
           console.log("gooood" + participants.all[i]);
           if(participants.all[i].userName==data.name){
               participants.all[i].userStatus=data.status;
               participants.all[i].statusDate=data.statusDate;
           }
       }
      io.sockets.emit("newConnection", {participants: participants});
        publicWall.getWallMessages("Administrator",function(err,results){
            if(err){
                console.log("Error getting Wall Messages: "+err)
            }
            io.sockets.emit("publicWallMessages",{messages:results});
        });
    });

    var role;
      socket.on("refreshWall", function(data) {

          for(var i = 0; i < participants.all.length; i++){
//              if(participants.all[i].userName=="Administrator"){
              if(participants.all[i].userName==data.user_name){
                  role = participants.all[i].privilegeLevel;

              }

          }
          publicWall.getWallMessages(role, function (err, results) {
              if (err) {
                  console.log("Error getting Wall Messages: " + err);
              }
              io.sockets.emit("publicWallMessages", {messages: results});
          });


          announcements.getAnnouncements(role,function (err, results) {
              if (err) {
                  console.log("Error getting Wall Messages: " + err);
              }
              io.sockets.emit("announcements", {messages: results});
          });
      });
        socket.on("refreshAnnouncement",function(data){
            for(var i = 0; i < participants.all.length; i++) {
//              if(participants.all[i].userName=="Administrator"){
                if (participants.all[i].userName == data.user_name) {
                    role = participants.all[i].privilegeLevel;

                }
            }
        announcements.getAnnouncements(role,function (err, results) {

              if (err) {
                  console.log("Error getting Wall Messages: " + err);
              }
              io.sockets.emit("announcements", {messages: results});
        });}
      )
      socket.on("newChatMessage",function(data){
          io.sockets.emit(data.source,data);
          io.sockets.emit(data.target,data);
          io.sockets.emit("newChatMsgAlert",data);
      });

    socket.on("disconnect", function() {
      delete participants.online[socket.id];
      io.sockets.emit("userDisconnected", {id: socket.id, sender:"system", participants:participants});
    });

      socket.on("getTestStatus", function() {
          io.sockets.emit("testStatus", {test: test});
      });

      socket.on("updateTestStatus", function(data) {
          test = data.test;
          io.sockets.emit("testStatus", {test: test});
      });

      socket.on("getUserLocations",function(){
          io.sockets.emit("getLocation");
      });

      socket.on("emitUserLoc",function(data){
          io.sockets.emit("userLoc",{username:data.name,latitude:data.userPosLat,longitude:data.userPosLong});
      });

  });
};
