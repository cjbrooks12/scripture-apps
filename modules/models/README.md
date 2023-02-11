# Models

This module contains the model definitions for the types used throughout the app and shared among multiple modules. In
effect, these models define the interface between the layers/modules of the app. Individual models used for 
serialization, defining DB schemas, or anything like that should be defined at the point they're used, then mapped to 
these common models as they're passed throughout the app.
