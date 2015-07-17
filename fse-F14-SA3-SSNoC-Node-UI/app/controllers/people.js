module.exports = function(_, io, participants, passport) {
  return {
    getNormalPeople: function(req,res) {
        res.
            render("people", {userId: req.session.userId, title: ("People"), user_name: req.session.passport.user.user_name});
        console.log(req.body);
    },



    getPeople: function(req, res) {
        //get participants
        for(var i = 0; i < participants.all.length; i++) {
            console.log("gooood   " + participants.all[i].userName);
            var role;
            if(participants.all[i].userName==req.session.passport.user.user_name){
                 role = participants.all[i].privilegeLevel;
            }
        }
        switch (role){

            case  "Citizen": {

            res.
                render("people", {userId: req.session.userId, title: ("People"), user_name: req.session.passport.user.user_name});
                console.log(req.body);
                break;
            }

            case  "undefined": {

                res.
                    render("people", {userId: req.session.userId, title: ("People"), user_name: req.session.passport.user.user_name});
                console.log(req.body);
                break;
            }

            case  undefined: {

                res.
                    render("people", {userId: req.session.userId, title: ("People"), user_name: req.session.passport.user.user_name});
                console.log(req.body);
                break;
            }

            case  "Coordinator": {

                res.
                    render("peopleCoor", {userId: req.session.userId, title: ("Coordinator_view"), user_name: req.session.passport.user.user_name});
                console.log(req.body);
                break;
            }

            case  "Monitor": {

                res.
                    render("peopleMo", {userId: req.session.userId, title: ("Monitor_view"), user_name: req.session.passport.user.user_name});
                console.log(req.body);
                break;
            }
            case  "Administrator":
            {
                res.render("peopleAdmin", {userId: req.session.userId, title: ("Admin_View "), user_name: req.session.passport.user.user_name});

                console.log(req.body);
                break;

            }

    }

    }
  };
};
