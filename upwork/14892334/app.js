/**
 * Load up the project dependencies
 */
var express = require('express')
var colors = require('colors')
var url = require('url')
var jwt = require('jwt-simple');

/**
 * Create the express app
 * NOTE: purposely not using var so that app is accesible in modules.
 */
app = express()

/**
 * Set the secret for encoding/decoding JWT tokens
 */
app.set('jwtTokenSecret', 'secret-value')

/**
 * An example protected route.
 */
app.get('/secret', function(req, res){    
        var parsed_url = url.parse(req.url, true);
        var token = parsed_url.query.access_token;
        if (token)
        {
            try{
                var decoded = jwt.decode(token, app.get('jwtTokenSecret'))
                res.json(decoded)
            }catch(e)
            {
                res.send('Invalid token');
            }
        }
        else
        {
            res.send('No token for authentication');
        }
})

app.get('/token', function(req, res){

    var token = jwt.encode({
            username: 'Chandra',
            password: '12345'
        }, app.get('jwtTokenSecret'));

    res.json({
        token : token
    })
})


/**
 * Start listening
 */
var server = app.listen(3000, function() {
    console.log('Listening on port %d'.green, server.address().port)
});


