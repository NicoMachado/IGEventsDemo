TicketMaster - Code Challenge

Author: Nicolas Machado

For this solution, I utilized the base Android template to accelerate the development process. It comes with preloaded packages including:

- Dagger2/Hilt for Dependency Injection
- Compose Libraries
- Retrofit

I also incorporated the following libraries:
- Coil for image loading
- SWIPE effect library from [GitHub](https://github.com/saket/swipe). This library proved to be very helpful in implementing the swipe-to-left or swipe-to-right effect on the cards.

The project is structured into three main packages:
1. data: Contains data sources and the repository.
2. domain: Houses the use cases.
3. ui: Comprises UI components.

The project adheres to the MVVM architecture and leverages the Jetpack Compose library for the UI.

The GitHub repository consists of two branches:

1. main: Contains the solution for the code challenge using MVVM and MutableStateFlow for a UIState holder variable. It employs sealed interfaces to represent different states of the UI and facilitates the transmission of data from the UI to the ViewModel and vice versa.

2. MutableFlow-version: In this version, I utilize savedStateHandle to observe user input in the search field and employ a MutableStateFlow to transmit data from the ViewModel to the UI. The search query reacts to user input in real-time without the need to press enter on the keyboard. Additionally, the entry is debounced for 1500 milliseconds to prevent excessive requests to the API.