var bcrypt = require('bcrypt-nodejs');
var request = require('request');

var rest_api = require('../../config/rest_api')

function uploadRest(){
    this.local={
    name : user_name,
    target : target,
    content: content
    }
  
}

uploadRest.sendImage = function(Imagebuffer,callback){
    var options = {
        url : rest_api.upload + user_name,
        body : {Image : Imagebuffer,
                Message : Message},
        json: true
    };

    request.post(options,function(err,res) {
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



module.exports = uploadRest;