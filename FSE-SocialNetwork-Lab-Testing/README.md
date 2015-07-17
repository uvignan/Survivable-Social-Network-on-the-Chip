### Unit Testing Lab 

1) Download the repo using the [Download ZIP] link. Don't use the [Clone in Desktop] link. Don't try to push your changes back to this repository.  
2) Import the repo into your Eclipse workspace.   
3) Complete the Unit Testing Lab Tasks… Pair with a classmate or do them solo. It's your choice.  
4) After finishing all Unit Testing Lab Tasks, start the Unit Testing Assignment.  

#### Unit Testing Lab Tasks

_Refactor the test code..._  
__L1. Rename poorly named tests.__  
__L2. Find overprotective tests, remove redundancy.__  
__L3. Remove replicated code in tests.__  
__L4. Split tests that have more than one reason to fail into multiple tests.__  
__L5. Add extra tests to cover any uncovered cases, including boundary cases and sad paths.__  



### Unit Testing Assignment

Perform this assignment on your own. This is an individually graded assignment. 
No collaboration. 

1) Take the top task from the list of tasks.   
2) Implement the task with unit tests.  
3) Repeat for next task.  
4) Submit to WEB-CAT when finished.  

__Tips:__  
  Test-drive using TDD or write unit tests after.  
  Make sure all tests, both the new ones written by you and those provided with the repo, pass.  
  Make sure you have all the necessary tests in place before starting the next task.  
  Run all tests frequently, each time you change production code or finish a new test.  
  Make sure a test doesn’t pass by accident: make it fail by introducing a defect if necessary.  
 

#### Unit Testing Assignment Tasks

Each task is specified by an action and an example of how to carry out the action in the code. 
These variables are used in the examples:
```
sn : SocialNetwork  
me, her : Account  
myUserName : String  
herUserName : String  
```
  
`her` is the originator of a friend request: `her` invokes the `befriend` method of `me` with `her` as a paramater:  
```
her -> befriend(her) -> me
```  

`me` is the target of a friend request: `me` invokes the `accepted` method of `her` with `me` as a parameter to inform `her` that `me` just accepted a friend request from `her`:  
```
me -> accepted(me) -> her
```  

__A1. List all friend requests `me` had sent to others:__  
```
me.whoDidIAskToBefriend() => returns Set<String>             <- implement this method  
```
__A2. Accept all friend requests that are pending a response from `me`:__  
```
sn.acceptAllFriendRequestsTo(me)                             <- implement this method  
``` 
__A3. Reject a friend request. Tell `her` that `me` has just rejected `her` friend request:__  
```
her.rejected(me)                                             <- implement this method  
sn.rejectFriendRequestFrom(herUserName, me)                  <- implement this method  
```
__A4. Reject all friend requests sent to `me`:__  
```
sn.rejectAllFriendRequestsTo(me)                             <- implement this method 
```
__A5. Automatically accept all new friend requests sent to `me`:__  
```
me.autoAccept()                                              <- implement this method 
sn.autoAcceptFriendRequestsTo(me)                            <- implement this method 
```
*From now on, whenever somebody sends me a friend request, my account will automatically accept it.*  

__A6. Unfriend an existing friend. Tell `her` that `me` has just unfriended `her`:__   
```
her.unfriend(me)                                             <- implement this method 
sn.sendUnfriendRequestTo(herUserName, me)                    <- implement this method 
```
__A7. Remove `me` from social network:__  
```
sn.leave(me)                                                 <- implement this method 
```

#### Optional Tasks

No extra points for optional tasks. Complete them in your own local workspace. Don’t submit them to WEB-CAT unless you have all the units tests to accompany them.

__O1. List my (`me`'s) friends’ friends:__  
```
sn.whoAreFriendsOfFriendsOf(me)                              <- implement this method 
```
  *But, I shouldn’t be in the resulting list because I am not a friend of myself.* 
  
__O2. Recommend new friends to `me`:__  
```
sn.recommendFriendsTo(me)                                    <- implement this method 
```
  *If two of friends have a common friend, I could befriend them. So the Social Network should recommend them to me.*  
	
#### How to Submit Your Solution to WEB-CAT

1) Export your code to a JAR file.  
_Select the Java project to export from Package Explorer._  
_Select File->Export._  
_Name your JAR file using this convention: `SN-YourAndrewID.jar.` Hit Next._  
_Check "Create source folder structure" and hit Finish._  
2) Go to: [https://web-cat.cs.vt.edu](https://web-cat.cs.vt.edu).    
3) Select "Carnegie Mellon University Silicon Valley" from pull down menu.  
4) Login to your account.  
5) Check under “Assignments Accepting Submissions in Your Courses”.   
6) Submit your JAR file for: "Social Network: Unit Testing Assignment".     
7) Wait for the results. The system will auto-grade your assignment. 
_Your tests will be run. Every failing test will lower your grade._  
_Your code will be checked against reference correctness tests supplied by the instructor._ 
_Every failing correctness test will lower your grade: you'll be able to see which tests fail._  
_The coverage of your code will be analyzed._  
_Low code coverage will lower your grade: you'll be able to see which parts of the code are not covered._  
8) Inspect your grade and results.  
9) If you're not satisfied by your grade, fix your code, write more unit tests, refactor, and resubmit.  

You are allowed up to N submissions. 

Good luck!





