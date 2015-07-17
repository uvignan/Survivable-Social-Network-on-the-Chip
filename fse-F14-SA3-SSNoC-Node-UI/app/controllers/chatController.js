var chatRest= require('../models/chatRest');

module.exports = function(_,io,participants,passport){
    return{


       getChatMessagePage : function(req,res){
        var target_name=req.body.target;
        var source_name = req.body.source;
        res.render('privateChat', {sourceName: source_name, targetName : target_name} );        
        },

       sendChatMessage : function(req,res){
            var source = req.body.source;
            var target = req.body.target;
            var content = req.body.content;
            chatRest.sendChatMessage(source,target,content,function(body){
                if(body!=201){
                    console.log("error returned for sendChatMessage:"+res.statusCode);
                    res.render('privateChat');
                }else{
                    console.log("send private chat message successful");
                    res.json('privateChat');
                }
            });
        },

        getChatMessages : function(req,res) {
            var sourceUser = req.body.source;
            var targetUser = req.body.target;
            chatRest.getChatMessages(sourceUser, targetUser, function (err, results) {
                if (err) {
                    console.log("Error getting Wall Messages: " + err);
                } else {
                    res.json(200,results);
                }
            })

        }

    };
};