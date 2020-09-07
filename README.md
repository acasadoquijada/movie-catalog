# Movie Catalog

## Setup instructions

In order to compile and install the application in a emulated or physical you need to do as follows:

1 - Clone the this repository

```
git clone https://acasadoquijada@bitbucket.org/acasadoquijada/movie-catalog.git
```

2 - Open the project using Android Studio

```
File > New > Import Project 
```

3 - Obtain an API key from themoviedb.org. Please see this [link](https://www.themoviedb.org/documentation/api)

```
Check for the API Key (v3 auth)
```

4 - Once we have an API Key, set it in [retrofit.MainController line 13]()
. During the development of the app an API key as uploaded by mistake. This key has been revoked before making the repository public.

```
private val apiKey = "your api key"
```

5 - Select your device (virtual or pyhsical)

6 - Click on ***Run 'app'***. 

7 - Now you should be able to use the application


## Style checking

To check the code style the following tools have been used:

* [ktlin](https://ktlint.github.io/) by using [Ktlint Gradle](https://github.com/jlleitschuh/ktlint-gradle#main-tasks)
  * ktlintCheck and ktlintFormat taks have been runned
* [Lint Android Studio](https://developer.android.com/studio/write/lint)
  * All the relevant warning have been avoided with this tool 

## Libraries used

* [Retrofit](https://square.github.io/retrofit/) needed to perform the API calls to themoviedb.org in order to get the Movie/TV Show information
* [Navigation Component](https://developer.android.com/guide/navigation/navigation-getting-started) to perform the navigation between the fragments
* [Picasso](https://square.github.io/picasso/) to set the poster of the Movies/TV Shows into ImageViews
* [Room](https://developer.android.com/topic/libraries/architecture/room) to store the watchlist information in the device


## Problems found during development and future work

The greatest problem I found was while integrating Room into the picture. I had almost everything working, but due to a "wrong" design decisision the toggle button of the elements in the watchList wasn't updated correctly. Because of that, I had to performed a different approach.

With more time, I would like to:
* Improve the UI (how the tv/cinema logo is shown), add support for large devices (tablets)
* Improve user experience (not possible to rotate phone in the middle of a search)
* Increase the code coverage
* Improve the UI package class structure (a superclass could be defined with common methods)
* Use styles for common elements in the layouts,

## External resources

* **ic_heart** made by <a href="https://www.flaticon.com/authors/kiranshastry" title="Kiranshastry">Kiranshastry</a> from <a href="https://www.flaticon.com/" title="Flaticon"> www.flaticon.com</a>

* **ic_television**  made by <a href="https://www.flaticon.com/authors/freepik" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a></div>
* **ic_cinema** made by <a href="https://www.flaticon.com/authors/monkik" title="monkik">monkik</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a></div>