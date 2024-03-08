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
-main: contains the solution for the code challenge using  mvvm and with MutableState variables

-MutableFlow