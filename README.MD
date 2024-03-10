TicketMaster  - Code Challenge

Author : Nicolas Machado

I use for this solution the base android template, that allows me to speed up the development process.

It contains the following packages preloaded:

Dagger2/Hilt for DI
Compose Libraries
Retrofit

I added the following libraries:
Coil for image loading


The project is divided in 3 main packages:
- data: contains the data sources and the repository
- domain: contains the use cases
- ui: contains the UI components

The project is using the MVVM architecture, and the use of the Jetpack Compose library for the UI.

The repository in GitHub contains two branches:
-main: contains the solution for the code challenge using  mvvm and with MutableStateFlow of a UIState holder variable.
Using sealed interfaces to represent the different states of the UI.
And to pass down data to the UI, and pass up events from the UI to the ViewModel.

-MutableFlow-version.
In this version I use savedStateHandle to observe the input of the user in the search field, and 
use a MutableStateFlow to pass the data from the ViewModel to the UI.
The search query is reactive to user input and you don't need to press enter on the keyboard.
The entry is debounced 1500 ms to avoid making too many requests to the API.