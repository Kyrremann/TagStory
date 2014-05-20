![alt text][logo]
Tag Story - A INF5162 project
-----------------------------
Tag Story is a project focusing on getting people in all ages out into the woods. This is done by making narrative stories that the user can help to develop by visiting different tags outside.

Better description is pending...

--

We have started working on a side project for writing stories for TagStory, called TagStory - Story Creator.
See [github repo](https://github.com/Kyrremann/TagStory-StoryCreator)

--

TODO
----
~~Line~~ means that it is implemented
### Back-end
* Need a back-end to support hosting and sharing stories
 * Heroku
* A place to store profiles
 * CloudAnt

### Online
* ~~Create a web site~~
 * Inform about the Tag Story project/framework
* Later
 * Let the user make a profile
 * ~~Let the user create stories~~
  * Working on a site for creating stories in the side project TagStory - Story Creator, see [github repo](https://github.com/Kyrremann/TagStory-StoryCreator)

### Wiki
* A wiki-page explaining the project in more depth (at least compared to the web-page)
* A page with all the current stories
* How-to create a story
* How-to use the app
* Create a list of what is implemented/changelog

### App
* ~~Get compass to work properly~~
 * To unstable to use, scrapping it, need to be deleted from the codebase
* User profile and functionality in the application
 * Connect with Google, Facebook, Twitter and other popular services
* Quiz and story scoring
* Better UI for the quizmode. Green when correct, red when you failed
 * Have a small image next to the question, question mark for the current question, 'check' sign for correct, and an 'X' for wrong
* ~~Let the user click and zoom images~~
* Travel - Let the user move X meters and then "automagically" scan the tag.
* Statistic about where you are on the route, also see where other are
 * Simple statistic on how long you used, and how far you walked
  * Send statistics anonymous to server
* ~~Scan tags with QR-code or GPS~~
* Google Map: Get direction from where you are from you where you should go
 * Maybe switch to OpenStreetMap
* Refactoring of the NFC and QR scanning, GPS is working best now
* Better history of where you are in the story (in case of app failure)
* Send "help" or messages to people on a Story. Similar to Hunger Games
* Inform of broken or tag not working/existing
* A fallback mode if the app suddenly crash or get closed by outside mode
 * Something in the line of "save and continue" or "save and quit"
* Dialog for asking users to turn on certain features
 * ~~GPS implemented~~
 * NFC left
* ~~Change text on buttons~~
* Tutorials on how to interact with the app
* ~~An own activity showing the answer for a question~~
 * ~~Should probably difference between asnwer and hint~~
* ~~XML styling of elements and views used in the app[[1]][xml-style]~~
* Create abstract class which takes care of loading in the story parts for all the different classes, just the basic
* Add code to allow custom fonts
 * Different way to be solved, but either with a reflection class and a application class that checks to see if there are textviews that don't have their typeface set
 * Use only Google Fonts
* Ensure black text on older phones
* Rewrite the JSON Story hierarchy
* Contact us dialog
 * help@tagstory.no

### Other
* Tags can not be hanged on metal
* Check out what TripAdvisor have done on suggesting routes, maybe we can do something similar
* Move all features request/TODOs to the "Issues" section
* Make a clean version of the framework and forks for the others/spin-offs

[logo]: http://www.uio.no/studier/emner/matnat/ifi/INF5261/h12/project-groups/tag-story--out/feature-graphic-tag-story.png "Tag Story banner"
[xml-style]: http://developer.android.com/guide/topics/ui/themes.html
