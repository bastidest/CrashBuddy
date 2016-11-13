# This was a project for the HackaTUM 2016


## Inspiration
We imagined if we were in an accident, we would be shocked and we would not be able to provide the best help possible.

## What it does
* Guides you trough the necessary steps after an accident
* Call the police at any time from the status bar
* Reminds you where your personal items are (first aid kit, warning triangle…)
* Tells you what to do if you are to shocked to read
* Provides an overlay with your position/current address when you call the police

## How I built it
CrashBuddy is built in Android using a strongly Fragment based layout. The questions are dynamically loaded from a JSON file and are extendable by simple HTML content. We used AndroidStudio, Java and nearly no third party libraries.

## Challenges I ran into
* Building the decision tree for every situation
* Creating the Breadcrumb View 

## Accomplishments that I'm proud of
* The custom Breadcrub View that lets you navigate through the questions that you have been asked
* The visual design in general

## What I learned
* A lot about custom views and canvas drawing
* How to create widgets and overlays in Android

## What's next for CrashBuddy
* Minor design tweaks
* Improving the decision tree / new decision trees for other countries
* Lots of new help instructions