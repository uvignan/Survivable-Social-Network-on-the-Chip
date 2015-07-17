var uploadRest= require('../models/uploadRest');
var messageWallR = require('../models/MessageWallRest');

var gm = require('gm');
var imageMagick = gm.subClass({ imageMagick: true });

var fs = require('fs');

module.exports = function(_,io,participants,passport){
    return{

        uploadImage : function(req, res) {
            console.log(req.body);
            console.log(req.files);
          var randomNumber =   Math.random();
            // get the temporary location of the file
            var tmp_path = req.files.thumbnail.path;
            // set where the file should actually exists - in this case it is in the "images" directory
            var target_path = './public/img/' + req.files.thumbnail.name;
            // move the file from the temporary location to the intended location
            fs.rename(tmp_path, target_path, function (err) {
                if (err) {
                    console.log(err);
                    throw err
                }
                ;
                // delete the temporary file, so that the explicitly set temporary upload dir does not get filled with unwanted files
                fs.unlink(tmp_path, function () {
                    if (err) throw err;
                    res.send('File uploaded to: ' + target_path + ' - ' + req.files.thumbnail.size + ' bytes' + req.body.imagemessage);
                });


            imageMagick('./public/img/' + req.files.thumbnail.name).resize(250, 250).write('./public/img/resized' +randomNumber+ req.files.thumbnail.name, function (err) {
                if (!err) console.log('crazy has arrived');
                else console.log('2' + err);
//readread
//                fs.readFile('./public/images/new' + req.files.thumbnail.name, function(err, original_data){
//                    console.log("ssssssssssssssssssss");
//                    if(err){
//                        console.log('3'+err);
//                    }
 //           });
                    messageWallR.sendWallMessage(req.session.passport.user.user_name,req.body.imagemessage,'img/resized' +randomNumber+ req.files.thumbnail.name,req.body.latitude,req.body.longitude);

//                    fs.writeFile('./public/images/newnew' + req.files.thumbnail.name, original_data, function(err) {
//                        console.log("aaaaaaaaaaaaaaaaaaaaa");
//                        if (err){
//                            console.log("babababab\n"+err);
//                        }
//                    });
//                    var base64Image = new Buffer(original_data, 'binary').toString('base64');
//                    fs.writeFile('./public/images/final' + req.files.thumbnail.name + '.txt', base64Image, function(err) {});


            });

            });
            res.redirect('/messageWall');
            var role;
            for(var i = 0; i < participants.all.length; i++) {
//                console.log("gooood " + participants.all[i].userName);
                if(participants.all[i].userName==req.session.passport.user.user_name){
                    role = participants.all[i].privilegeLevel;
                }
            }
            if (role == "Coordinator"){
                res.render('messageWallCoordinator',{title:"Hello "+req.session.passport.user.user_name+" !!"});
            }
            else if (role == "Monitor"){
                res.render('messageWallMonitor',{title:"Hello "+req.session.passport.user.user_name+" !!"});
            }
            else if (role == "Administrator"){
                res.render('messageWallAdministrator',{title:"Hello "+req.session.passport.user.user_name+" !!"});
            }
            else{
                res.render('messageWall',{title:"Hello "+req.session.passport.user.user_name+" !!"});
            }

            //
          //  uploadRest.sendImage();
            //

        }

//       getChatMessagePage : function(req,res){
//        var target_name=req.body.target;
//        var source_name = req.body.source;
//        res.render('privateChat', {sourceName: source_name, targetName : target_name} );
//        },
//
//       sendChatMessage : function(req,res){
//            var source = req.body.source;
//            var target = req.body.target;
//            var content = req.body.content;
//            chatRest.sendChatMessage(source,target,content,function(body){
//                if(body!=201){
//                    console.log("error returned for sendChatMessage:"+res.statusCode);
//                    res.render('privateChat');
//                }else{
//                    console.log("send private chat message successful");
//                    res.json('privateChat');
//                }
//            });
//        },
//
//        getChatMessages : function(req,res) {
//            var sourceUser = req.body.source;
//            var targetUser = req.body.target;
//            chatRest.getChatMessages(sourceUser, targetUser, function (err, results) {
//                if (err) {
//                    console.log("Error getting Wall Messages: " + err);
//                } else {
//                    res.json(200,results);
//                }
//            })
//
//        }

    };
};