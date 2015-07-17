var bcrypt = require('bcrypt-nodejs');
var request = require('request');
var rest_api = require('../../config/rest_api');


function User(user_name, password, status, statusDate,accountstatus,rolelevel){
  this.local = {
    name : user_name,
    password : password,
    status : status,
    statusDate: statusDate,
    privilegeLevel : rolelevel,
    accountStatus: accountstatus

  };
}

User.updateAll = function( userNameObj, user_name, password, rolelevel,accountstatus,callback){
    request.put({url:rest_api.Update_a_user_record + userNameObj,
        body: {userName:user_name, password:password, privilegeLevel:rolelevel, accountStatus:accountstatus}, json:true },
        function(err, res, body) {
        if (err || res.statusCode !== 200){
            console.log("return unsuccessfully!");
            console.log(res.statusCode);
        }

        console.info("comes from userRest"+body);
    console.log("return successfully");
    return;
});
};



User.generateHash = function(password) {
  return bcrypt.hashSync(password, bcrypt.genSaltSync(8), null);
};

User.prototype.isValidPassword = function(password, callback) {
  request.post(rest_api.is_password_valid + this.local.name + '/authenticate', {json:true, body:{password:password}}, function(err, res, body) {
    if (err || res.statusCode !== 200){
      callback(false);
      return;
    }

    callback(true);
  });
};

User.updateStatus = function(user_name, user_status, createdDate) {
  request.post({url:rest_api.update_user_status + user_name, body: {statusCode:user_status, createdDate:createdDate}, json:true }, function(err, res, body) {
      if (err || res.statusCode !== 200){
        console.log("return unsuccessfully!");
        console.log(res.statusCode);
      }
        console.log("return successfully");
        return;
  });
};

User.getUser = function(user_name, callback) {
  request(rest_api.get_user + user_name, {json:true}, function(err, res, body) {
    if (err){
      callback(err,null);
      return;
    }
    if (res.statusCode === 200) {

      var user = new User(body.userName, body.password, body.statusCode, body.statusDate,body.accountStatus, body.privilegeLevel);

      callback(null, user);
      return;
    }
    if (res.statusCode !== 200) {
      callback(null, null);
      return;
    }
  });
};

User.getAllUsers = function(callback) {
  request(rest_api.get_all_users, {json:true}, function(err, res, body) {
    if (err){
      callback(err,null);
      return;
    }
    if (res.statusCode === 200) {
      var users = body.map(function(item, idx, arr){
        return new User(item.userName, item.password, item.statusCode, item.statusDate, item.accountStatus, item.privilegeLevel);

      });

      users.sort(function(a,b) {
        return a.userName > b.userName;
      });

      console.log("@@@@@ in User.getAllUser succeed users :" + JSON.stringify(users));
      callback(null, users);
      return;
    }
    if (res.statusCode !== 200) {
      callback(null, null);
      return;
    }
  });
};

User.saveNewUser = function(user_name, password, callback) {
  var options = {
    url : rest_api.post_new_user,
    body : {userName: user_name, password: password},
    json: true
  };

    console.log("Adding new user to DB");
  request.post(options, function(err, res, body) {
    if (err){
      callback(err,null);
      return;
    }
    if (res.statusCode !== 200 && res.statusCode !== 201) {
      callback(res.body, null);
      return;
    }

    var new_user = new User(body.userName, password, 1, "Citizen");

    callback(null, new_user);
    return;
  });
};

module.exports = User;
