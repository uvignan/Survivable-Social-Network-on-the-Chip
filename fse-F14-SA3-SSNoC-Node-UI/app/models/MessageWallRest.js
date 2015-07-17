/**
 * Created by Vignan on 10/16/2014.
 */
var bcrypt = require('bcrypt-nodejs');
var request = require('request');
var rest_api = require('../../config/rest_api')

function wallMessages(){}

        wallMessages.sendWallMessage = function(user_name,content,imagePath,latitude,longitude,callback){
        var options = {
            url : rest_api.message + user_name,
            body : {author:user_name,
                    content:content,
                    imgPath:imagePath,
                latitude:latitude,
                longitude:longitude},
            json: true
        };
        request.post(options,function(err,res) {
            if (err){
                console.log(err);
                callback(err);
                return;
            }
            if (res.body) {
               // callback(res.body);
                return;
            }
            else {
                callback(res.body);
                return;
            }
//            if (res.statusCode === 200) {
//                console.log(res.body);
//                callback(null, res.body);
//                return;
//            }
//            if (res.statusCode !== 200) {
//                callback(null, null);
//                return;
//            }

        });

    };

    wallMessages.getWallMessages = function(role,callback){
        var role1 = role;
        if (role1 == "Administrator" ) {
            console.info("*************************"+role1);
            request(rest_api.messages + '/wall', {json: true}, function (err, res, body) {
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
        else{
            console.info(role);
            console.info("**-*/-"+role1);
            request(rest_api.messages + '/wall/visible', {json: true}, function (err, res, body) {
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

module.exports = wallMessages;