#!/usr/bin/env node
var firebase = require('firebase-admin');
var request = require('request');
var API_KEY = "AAAAqv9mUR0:APA91bHFou4Y4DAcx6n9TvDPtT7CtsjYkYlmPjVfmReP1G71mN9Vl5o6OrRx7i2q7VBZE0bv5swSA4_822aCysp2rtjo4C2VMlJ0McVsm8UmeAtBZEBWGDsU5IMMdWxKnMH3HNJQPmAM"; // Your Firebase Cloud Messaging Server API key
// Fetch the service account key JSON file contents
var serviceAccount = require("/Users/visito.json");
// Initialize the app with a service account, granting admin privileges
firebase.initializeApp(
{
 credential: firebase.credential.cert(serviceAccount),
 databaseURL: "https://visito-2b3c7.firebaseio.com/"
});
ref = firebase.database().ref();
function listenForNotificationRequests()
{
 console.log("listening for notifications");
 var requests = ref.child("notificationsRequest");
 requests.on('child_added', function(requestSnapshot)
 {
   var request = requestSnapshot.val();
   sendNotificationToUser(
   request.username,
   request.message,
   function()
   {
    requestSnapshot.ref.remove();
   });
 }, function(error)
  {
   console.error(error);
  });
};
function sendNotificationToUser(username, message, onSuccess) {
  request({
   url: 'https://fcm.googleapis.com/fcm/send',
   method: 'POST',
   headers: {
   'Content-Type' :' application/json',
   'Authorization': 'key='+API_KEY
   },
  body: JSON.stringify({
   notification: {
    title: message
   },
   to : '/topics/updates'
  })
  }, function(error, response, body) {
   if (error) { console.error(error); }
   else if (response.statusCode >= 400) {
   console.error('HTTP Error: '+response.statusCode+' — '+response.statusMessage);
  }
 else {
  onSuccess();
  console.log("notification sent");
 }
 });
}
// start listening
listenForNotificationRequests();
