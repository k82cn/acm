##Pre-requisites

Obviously you'll need Node and npm

##Installing and Setting Up

Install the project dependencies:

    npm install

##Using It

Run the application:

	node app.js

To get a token, make a `GET` request to:

    http://localhost:3000/token

Make a note of the access token in the returned JSON.

Now try making a `GET` request to the following URL:

	http://localhost:3000/secret?access_token="the access token got from previous url (/token)"

The output is username and password.

