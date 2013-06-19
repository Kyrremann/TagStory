![alt text][logo]
Tag Story - A INF5162 project
-----------------------------
Tag Story is a project focusing on getting people in all ages out into the woods. This is done by making narrative stories that the user can help to develop by visiting different tags outside.

Better description is pending...

--
24 May 2013
Tag Story is up and running and is going to be used in a project this summer. In that case we will build a new web page and reorganise the Github project. We will also create wiki pages about the project, how to make stories, and a list of stories to try.
--

TODO
----
### Back-end
* Need a back-end to support hosting and sharing stories
* A place to store profiles

### Online
* Create a web site
 * Inform about the Tag Story project/framework
* Later
 * Let the user make a profile
 * Let the user create stories

### Wiki
* A wiki-page explaining the project in more depth (at least compared to the web-page)
* A page with all the current stories
* How-to create a story
* How-to use the app
* Create a list of what is implemented

### App
* ~~Get compass to work properly~~
 * To unstable to use, scapping it
* User profile and functionality in the application
 * Connect with Google, Facebook, Twitter and other popular services
* Quiz and story scoring
* Better UI for the quizmode. Green when correct, red when you failed
 * Have a small image next to the question, question mark for the current question, 'check' sign for correct, and an 'X' for wrong
* ~~Let the user click and zoom images~~
 * Implemented
* Travel - Let the user move X meters and then "automagically" scan the tag.
* Statistic about where you are on the route, also see where other are
* ~~Scan tags with QR-code or GPS~~
 * Implemented
* Google Map: Get location from where you are from you where you should go
* Refactoring of the NFC and QR scanning, GPS is working best now
* Better history of where you are in the story (in case of app failure)
* Send "help" or messages to people on a Story. Similar to Hunger Games
* Inform of broken or tag not working/existing
* A fallback mode if the app suddenly crash or get closed by outside mode
 * Something in the line of "save and continue" or "save and quit"
* Dialog for asking users to turn on certain features
 * Such as NFC or GPS
* Change text on buttons
* Tutorials on how to interact with the app
* An own activity showing the answer for a question
 * Should probably difference between asnwer and hint
* XML styling of elements and views used in the app[1][http://developer.android.com/guide/topics/ui/themes.html]
* Create abstract class which takes care of loading in the story parts for all the different classes, just the basic

### Other
* Tags can not be hanged on metal
* Check out what TripAdvisor have done on suggesting routes, maybe we can do something similar
* Move all features request/TODOs to the "Issues" section
* Make a clean version of the framework and fork the others, or branches

[logo]: http://www.uio.no/studier/emner/matnat/ifi/INF5261/h12/project-groups/tag-story--out/feature-graphic-tag-story.png "Tag Story banner"
