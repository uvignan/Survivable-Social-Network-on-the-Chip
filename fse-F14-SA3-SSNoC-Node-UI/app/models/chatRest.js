var bcrypt = require('bcrypt-nodejs');
var request = require('request');
//var target="sivatest";
var rest_api = require('../../config/rest_api')

function chatMessages(){
    this.local={
        name : user_name,
    target : target,
    content: content
    }
  
}
    chatMessages.sendChatMessage = function(source,target,content,callback){
        request.post({url:rest_api.message + source +'/'+target, body: {content:content}, json:true},function(err,res) {
            if (err)
               {
                callback(err.statusCode);
                return;
               }
            if(res.statusCode !== 200 &&  res.statusCode !== 201){
                callback(res.statusCode);
                return;
            }
            callback(res.statusCode);
            return;
        });
    };

    chatMessages.getChatMessages =function(source,target,callback){
        request(rest_api.messages+'/'+source+'/'+target,{json:true}, function(err,res,body){
            if (err){
                callback(err,res);
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

module.exports = chatMessages;