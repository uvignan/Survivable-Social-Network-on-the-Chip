var bcrypt = require('bcrypt-nodejs');
var request = require('request');
var rest_api = require('../../config/rest_api')

function searchRest(){}

        searchRest.sendQuery = function(content,type,callback){
            var options = {
                url : rest_api.search,
                body : {content:content,type:type},
                json: true
            };
            request.post(options,function(err, res, body) {
                if (err) {
                    console.log(body);
                    return;
                }

                console.log(body);
                callback(null,body);
                return;
                
            });

    };


module.exports = searchRest;