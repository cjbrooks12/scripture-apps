# API

This module handles all API requests for the app. It is not concerned with anything other than executing the raw 
request. All globally-held data should be passed into these APIs, not stored anywhere here. No caching should be done at
this layer. Both of those are concerns of the Repository module, not the API module.
