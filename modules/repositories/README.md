# Repository

This module is essentially the control center for the entire application. This is considered the "source of truth" for
the whole app, managing the global state and coordinating updates with the other modules. Ideally, most of these 
controls are entirely passive, being attached as Interceptors to the Repository ViewModels so that they react to state 
changes, rather than being directly notified of a change. 

The entire UI is also driven by state changes within the Repository layer.

It's important to know that the API/DB modules are not considered "layers", they are simply "data sources" that the 
Repository Layer is communicating with. The Repository Layer handles all caching logic (typically in-memory and stored 
within a Ballast ViewModel) and all business logic, while the API and DB modules are "dumb". They do not store any info
in themselves, they simply execute API calls/DB queries with the data given to them by the Repository.
