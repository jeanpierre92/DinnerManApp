README -- FatSecretAPI
----------------------

This package is for the FatSecret PlatForm to achieve deep integration with the
FatSecret JavaScript API. The FatSecret REST API calls covered in this library are
profile.create, profile.get_auth and profile.request_script_session_key.


Library Function Overview
-------------------------

/* Create a new profile
* @param userID {String} Your ID for the newly created profile (set to null if you are not using your own IDs)
* Returns FatSecretAuth with the token and secret of the newly create profile
*/
public FatSecretAuth ProfileCreate(String userID) throws FatSecretException {

/* Get the auth details of a profile
* @param userID {String} Your ID for the profile
* Returns FatSecretAuth with the token and secret of the newly create profile
*/
public FatSecretAuth ProfileGetAuth(String userID) throws FatSecretException {

/* Create a new session for JavaScript API users
* @param token {String} The token of the profile
* @param secret {String} The secret of the profile
* @param expires {int} The number of minutes before a session is expired after it is first started. Set this to 0 to never expire the session. (Set to any value less than 0 for default)
* @param consumeWithin {int} The number of minutes to start using a session after it is first issued. (Set to any value less than 0 for default)
* @param permittedReferrerRegex {string} A domain restriction for the session. (Set to null if you do not need this)
* @param cookie {bool} The desired session_key format
* Returns the session key for the newly created session
*/
public String ProfileRequestScriptSessionKey(String token, String secret, int expires, int consumeWithin, String permittedReferrerRegex, boolean cookie) throws FatSecretException {

/* Create a new session for JavaScript API users
* @param userID {String} Your ID for the profile
* @param expires {int} The number of minutes before a session is expired after it is first started. Set this to 0 to never expire the session. (Set to any value less than 0 for default)
* @param consumeWithin {int} The number of minutes to start using a session after it is first issued. (Set to any value less than 0 for default)
* @param permittedReferrerRegex {string} A domain restriction for the session. (Set to null if you do not need this)
* @param cookie {bool} The desired session_key format
* Returns the session key for the newly created session
*/
public String ProfileRequestScriptSessionKey(String userID, int expires, int consumeWithin, String permittedReferrerRegex, boolean cookie) throws FatSecretException {


How to Run the Examples
----------------------

Simply extract the files to a web server able to process ASPX pages (E.G.: a TomCat
web server). Point your browser to the index.html file in the root directory.

The samples have been written to run out of the box, but you must first set up the
API_KEY and API_SECRET in the web.xml file in the WebContent/WEB-INF directory. You can
register for an API KEY at: http://platform.fatsecret.com/api/Default.aspx?screen=r


Examples Overview
-----------------

Create a new profile
--------------------
This example demonstrates how to create a new profile by signing the request as per the
OAuth specifications and calling the FatSecret REST API profile.create method call. The
auth_token and auth_secret is returned to you for storing.

Create a new profile with a user_id
-----------------------------------
This example demonstrates how to create a new profile with your own user_id by signing the
request as per the OAuth specifications and calling the FatSecret REST API profile.create
method call. The auth_token and auth_secret is returned using the FatSecret REST API
profile.get_auth method call.

Setting up a session cookie
---------------------------
This example demonstrates how to create a new session by signing the request as per the
OAuth specifications and calling the FatSecret REST API profile.request_script_session_key
method call. The session_key is returned to you and written as a cookie to begin a new
session. This is the recommended way to begin a new sesssion.

Setting up a session and appending to URL
-----------------------------------------
This example demonstrates how to create a new session by signing the request as per the
OAuth specifications and calling the FatSecret REST API profile.request_script_session_key
method call. The session_key is returned to you and appended to the URL to begin a new
session.


Documentation
-------------

If you need additional documentation, please refer to:

* FatSecret REST API Documentation - http://platform.fatsecret.com/api/Default.aspx?screen=rapih
* FatSecret JavaScript API Documentation - http://platform.fatsecret.com/api/Default.aspx?screen=jsapih
* FatSecret Forum - http://groups.google.com/group/fatsecret-platform-api

