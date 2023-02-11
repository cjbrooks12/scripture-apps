# Databse

This module handles all data stored locally within the app, which includes local SQLite databases and SharedPreferences.
It is not concerned with anything other than executing the raw transations/queries. All globally-held data should be 
passed into these DB queries, not stored anywhere here. No caching should be done at this layer. Both of those are 
concerns of the Repository module, not the Database module.
