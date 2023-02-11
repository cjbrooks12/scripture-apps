# UI

This module implements the Compose UI for each screen. Screens should generally be stateless, not creating the 
ViewModels on their own, as the app modules should ultimately be the one to create the ViewModel and manage the state
over time for each UI component/screen.
