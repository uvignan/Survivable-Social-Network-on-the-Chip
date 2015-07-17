function init() {
  var serverBaseUrl = document.domain;
  var socket = io.connect(serverBaseUrl);

  var sessionId = '';

  window.my_name = '';


  var my_status = '';

  var my_statusDate = '';

  var map_status_color = {"OK":"btn-success","HELP":"btn-warning","EMERGENCY":"btn-danger","UNDEFINED":""};


  function updateParticipants(participants) {
    $('#participants_online').html('');
    $('#participants_offline').html('');
    var map = {};
    var userName = '';
    var userEle = '';
    for (var sId in participants.online){
      userName = participants.online[sId].userName;

      var status = participants.online[sId].status;
      var statusDate = participants.online[sId].statusDate;
        var accountStatus = participants.online[sId].accountStatus;
      if (map[userName] == undefined || map[userName] !== sessionId){
        map[userName] = {sId:sId,status:status,statusDate:statusDate};

      }
    }
    keys = Object.keys(map);
    keys.sort();

    for (var i = 0; i < keys.length; i++) {
      var name = keys[i];

      var img_ele = '<img src="/img/online_user.png" height=50/>';
      var name_ele = '<font color="#336699"><strong>' + name + '</strong></font>';
      var user_ele = '<div class="col-xs-5 col-sm-5 col-md-4 col-lg-4">'+img_ele+'<br>'+name_ele+'</div>';
      var dropdown_symbol = map[name].sId === sessionId ? '':'<i class="glyphicon glyphicon-chevron-down text-muted"></i>';
      var dropdown_ele = '<div class="col-xs-1 col-sm-1 col-md-1 col-lg-1 dropdown-user" data-for=".' + name + '">' + dropdown_symbol + '</div>';
      var status_ele = '<button class="btn ' + map_status_color[map[name].status] + '">'+'<strong>' +map[name].status+'</strong>' +'</button>';
        var statusDate_ele1 = map[name].statusDate ;
      var statusDate_ele = map[name].statusDate ;
      var statusDisp_ele = '<div class="col-xs-6 col-sm-6 col-md-7 col-lg-7 ">'+status_ele+'</strong>'+'<br>'+'<font color="#909090" size="2"><strong>'+statusDate_ele+'</font></strong></div>';


      var info_ele = '<div class="row user-row search_item">' + user_ele + statusDisp_ele + dropdown_ele +'</div>';

      var detail_ele = '<div class="row user-info ' + name + '"><a class="btn btn-info col-xs-6 col-sm-6 col-md-6 col-lg-6 col-xs-offset-3 col-sm-offset-3 col-md-offset-3 col-lg-offset-3">Wanna do something?</a><hr/></div></div>';
      if (map[name].sId === sessionId || name === my_name) {
      } else {
        $('#participants_online').append(info_ele);
        $('#participants_online').append(detail_ele);
      }
    }

    participants.all.forEach(function(userObj) {
      if (map[userObj.userName] == undefined) {

        var img_ele = '<img src="/img/offline_user.png" height=50/>';
        var name_ele = '<font color="#888888"><strong>' + userObj.userName + '</strong></font><br/>';
        var user_ele = '<div class="col-xs-5 col-sm-5 col-md-4 col-lg-4">'+img_ele+'<br>'+name_ele+'</div>';
        var dropdown_ele = '<div class="col-xs-1 col-sm-1 col-md-1 col-lg-1 dropdown-user" data-for=".' + userObj.userName + '"><i class="glyphicon glyphicon-chevron-down text-muted"></i></div>';
        var status_ele ='<button class="btn ' +map_status_color[userObj.userStatus] + '">' +'<strong>' + userObj.userStatus + '</strong></button>';
        var statusDate_ele = userObj.statusDate;
        var statusDisp_ele = '<div class="col-xs-6 col-sm-6 col-md-7 col-lg-7 ">'+status_ele +'<br>'+'<font color="#909090" size="2"><strong>'+statusDate_ele+'</font></strong></div>';

        var changeButton = '<button class="btn btn-danger col-sm-2 col-sm-offset-5" id="UpdateAll'+userObj.userName+'" name="'+userObj.userName+'">Update</button><br>';

        var info_ele = '<div class="row user-row search_item">' + user_ele + statusDisp_ele + dropdown_ele +'</div>';
        var alert = '<br><div id="alert'+userObj.userName+'" class="alert alert-danger col-sm-4 col-sm-offset-4"></div>'

        var detail_ele = '<div class="row user-info ' + userObj.userName + '"><a class="btn btn-warning col-sm-4 col-sm-offset-4">Currently UserName: '+userObj.userName+'</a><br><br>' +
            '<div class="input-group col-sm-4 col-sm-offset-4"><span class="input-group-addon"><span class="glyphicon glyphicon-user"></span></span><input id="userName'+userObj.userName+'" type="text" name="name" placeholder="Please enter an user name" class="name form-control"/></div><br>' +

            '<a class="btn btn-success col-sm-4 col-sm-offset-4">Currently password: ENCRYPTED</a><br><br>' +
            '<div class="input-group col-sm-4 col-sm-offset-4"><span class="input-group-addon"><span class="glyphicon glyphicon-time"></span></span><input id="password'+userObj.userName+'" type="text" name="passwd" placeholder="Please enter an user password" class="name form-control"/></div><br>' +
            '<a class="btn btn-info col-sm-4 col-sm-offset-4">Currently Account Status: '+userObj.accountStatus+'</a><br><br>' +
            '<div class="input-group col-sm-4 col-sm-offset-4"><span class="input-group-addon"><span class="glyphicon glyphicon-play"></span></span><input id="accountStatus'+userObj.userName+'" type="text" name="aStatus" placeholder="Please enter an user Account Status" class="name form-control"/></div><br>' +
            '<a class="btn btn-primary col-sm-4 col-sm-offset-4">Currently Role: '+userObj.privilegeLevel+'</a><br><br>' +
            '<div class="input-group col-sm-4 col-sm-offset-4"><span class="input-group-addon"><span class="glyphicon glyphicon-eye-open"></span></span><input id="role'+userObj.userName+'" type="text" name="role" placeholder="Please enter an user Role" class="name form-control"/></div><br>' +
            changeButton + alert +
            '</div><div><hr></div>';
        $('#participants_offline').append(info_ele);
        $('#participants_offline').append(detail_ele);
        $('#alert'+userObj.userName).hide();




          $('#UpdateAll'+userObj.userName).click(function (){
              var sourceUser = $(this).attr('name');

            //  $('#alert'+sourceUser).text(sourceUser);
            //  $('#alert'+sourceUser).show();

              console.log("yeahyeah");
              //$('#alert'+sourceUser).show();

               if( !validateUserNameAndPassword(sourceUser) ) {
               return false;
               }

              console.info("varification passed");

              $.ajax({
                  url: '/UpdateAll/',
                  type: 'PUT',
                  dataType: 'json',
                  data : {
                      userNameObj : sourceUser,
                      userName: ($('#userName'+sourceUser).val()),
                      password: ($('#password'+sourceUser).val()),
                      accountStatus: ($('#accountStatus'+sourceUser).val()),
                      role: ($('#role'+sourceUser).val())
                  }
              }).done(function (data) {
                 location.reload();
                  socket.emit('upUser', {oldname : sourceUser, name: $('#userName'+sourceUser).val(), password:$('#password'+sourceUser).val(), accountStatus: $('#accountStatus'+sourceUser).val(), role: $('#role'+sourceUser).val()});

                  console.log("Donedonedoen");
              }).error(function (err) {
                  console.log("Here is an err");
                  console.log(err);
              });


          });
      }
    });


    $('.user-info').hide();


    $('.dropdown-user').click(function() {
       // $('#alert').hide();
      var dataFor = $(this).attr('data-for');
      var idFor = $(dataFor);
      var currentButton = $(this);
      idFor.slideToggle(400, function() {
        if(idFor.is(':visible'))
          {
            currentButton.html('<i class="glyphicon glyphicon-chevron-up text-muted"></i>');
          }
          else
            {
              currentButton.html('<i class="glyphicon glyphicon-chevron-down"></i>');
            }
      })
    });

  }


function updateStatus(status, name, statusDate){
    $.ajax({
      url:  '/status/' + name,
      type: 'POST',
      dataType: 'json',
      data: {status: status, statusDate:statusDate}
    }).done(function(data){
        console.log("The user's status has been updated successfully!");

        socket.emit('statusUpdate', {id: sessionId, name: name, status:status, statusDate:statusDate});
    });
}

/*render user's status*/
function renderStatus(){
  var status_ele ='<div class="dropdown">'
                  +'<button class="btn btn-mini dropdown-toggle " type="button" id="my_status" data-toggle="dropdown">'
                     +my_status
                     +' | <span class="glyphicon glyphicon-th-list"></span>'
                  +'</button>'
                   +'<ul class="dropdown-menu" role="menu list-inline" aria-labelledby="status">'
                     +'<li role="presentation"><a class="statusList" role="menuitem" tabindex="-1" href="#">OK</a></li>'
                     +'<li role="presentation"><a class="statusList" role="menuitem" tabindex="-1" href="#">HELP</a></li>'
                     +'<li role="presentation"><a class="statusList" role="menuitem" tabindex="-1" href="#">EMERGENCY</a></li>'
                     +'<li role="presentation" class="divider"></li>'
                     +'<li role="presentation"><a class="statusList" role="menuitem" tabindex="-1" href="#">UNDEFINED</a></li>'
                   +'</ul>'
                 +'</div>';

  var statusDate_ele ='<div id="my_statusDate">' +'<strong>'+'<font color="#909090" size="2">'+my_statusDate+'</font>'+'</strong>'+'</div>';


  var statusDisp_ele = '<div class="col-xs-6 col-sm-6 col-md-7 col-lg-7 ">'+'<strong>'+status_ele+'</strong>'+'<br>'+statusDate_ele+ '</div>';


  $("#myself").find(".row").append(statusDisp_ele);

  $("#my_status").addClass(map_status_color[my_status]);


/*change status*/
  $("a.statusList").click(function() {
    var chosen_status = $(this).html();
    $("#my_status").removeClass(map_status_color[my_status]);
    $("#my_status").addClass(map_status_color[chosen_status]);
    $("#my_status").html(chosen_status +' | <span class="glyphicon glyphicon-th-list"></span>');
    my_status = chosen_status;
    my_statusDate = generateDate();
    $("#my_statusDate").html('<strong>'+'<font color="#909090" size="2">'+my_statusDate+'</font>'+'</strong>');
    updateStatus(my_status,my_name,my_statusDate);
  });
}




function generateDate(){
    var d = new Date();
    var minute = d.getMinutes();
    var hour = d.getHours();
    var date = d.getDate();
    var month = d.getMonth()+1;
    var year = d.getFullYear();
    return year + '-' + (month < 10 ? '0' + month : month) + '-' + (date < 10 ? '0' + date : date) + ' ' + (hour < 10 ? '0' + hour : hour) + ':' + (minute < 10 ? '0' + minute : minute);
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

            socket.emit('emitUserLoc', {name: my_name, userPosLat:userPosLat, userPosLong:userPosLong});
        }
    }

  socket.on('connect', function () {
    sessionId = socket.socket.sessionid;
    $.ajax({
      url:  '/user',
      type: 'GET',
      dataType: 'json'
    }).done(function(data) {
      var name = data.name;

      var status = data.status;
      var statusDate = data.statusDate;
      my_name = data.name;
      my_status = data.status;
      my_statusDate = data.statusDate;
      renderStatus();

      socket.emit('newUser', {id: sessionId, name: name, status:status, statusDate:statusDate});
        userLocation();
    });
  });

  socket.on('newConnection', function (data) {
    updateParticipants(data.participants);
  });


  socket.on('statusUpdate', function (data) {
    updateParticipants(data.participants);
  });

    socket.on('Update', function (data) {
        updateParticipants(data.participants);
    });

    socket.on('getLocation',function(){
        userLocation();
    });

  socket.on('userDisconnected', function(data) {
    updateParticipants(data.participants);
  });

  socket.on('error', function (reason) {
    console.log('Unable to connect to server', reason);
  });

  var panels = $('.user-info');
  panels.hide();
  $('.dropdown-user').click(function() {
    var dataFor = $(this).attr('data-for');
    var idFor = $(dataFor);
    var currentButton = $(this);
    idFor.slideToggle(400, function() {
      if(idFor.is(':visible'))
        {
          currentButton.html('<i class="glyphicon glyphicon-chevron-up text-muted"></i>');
        }
        else
          {
            currentButton.html('<i class="glyphicon glyphicon-chevron-down text-muted"></i>');
          }
    })
  });
}

$(document).on('ready', init);
