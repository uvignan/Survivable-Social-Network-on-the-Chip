/**
 * Created by Vignan on 10/24/2014.
 */
var bcrypt = require('bcrypt-nodejs');
var request = require('request');
var rest_api = require('../../config/rest_api')

function announcements(){}

    announcements.sendAnnouncement = function(user_name,content,callback){
      var options={
          url : rest_api.message +'announcement/'+ user_name,
          body : {content:content},
          json : true
      };
        request.post(options, function (err, res) {
            if (err){
                console.log(err);
                callback(err);
                return;
            }
            if (res.body) {
                console.log(res.body);
                callback(res.body);
                return;
            }
            else {
                console.log(res.body);
                callback(res.body);
                return;
            }
        });
    };

    announcements.getAnnouncements = function(role,callback){
        var role1 = role;
        console.log(role1);
        if (role1 == "Administrator") {
            console.log(role);
            request(rest_api.messages + '/announcement', {json: true}, function (err, res, body) {
                if (err) {
                    callback(err, res);
                    return;
                }
                if (res.statusCode === 200) {
                    callback(null, body);
                    return;
                }
                if (res.statusCode !== 200) {
                    callback(null, null);
                    return;
                }
            });
        }
        else {
            request(rest_api.messages + '/announcement/visible', {json: true}, function (err, res, body) {
                if (err) {
                    callback(err, res);
                    return;
                }
                if (res.statusCode === 200) {
                    callback(null, body);
                    return;
                }
                if (res.statusCode !== 200) {
                    callback(null, null);
                    return;
                }
            });
        }
    };
module.exports = announcements;
