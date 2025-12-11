when we are doing a controller you can use the annotaiotn @controller
and then when you are sending away a response then you have to 
use response body tag @responsebody in every function beacuse you have to tell the spring that this fuction returns a response which is a json format.
this is ass

so we have restController tag which combines them both.
requestmapping tag is defining the base url path for all methods in that controller

so all the /api/users ones should go to the users contrler.
and frm there we can define the get post put deletes 
