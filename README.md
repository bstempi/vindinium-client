# vindinium-cleint

## Overview

This is a simpel Java client for the [Vindinium](http://vindinium.org) game.  Vindinium is a simple game played by an AI via HTTP that I found via Reddit.

The project is created using the Google HTTP client and GSON.  I decided to write a client because other clients seem to have some strange/verbose ways of parsing server communication and of presenting the game.  This client is focused on staying out of the way and letting people develop AIs to play.  It is also meant to be very easy to extend.

### License
This software is public domain.

### Author
Hi, I'm Brian!  You can find me at [my personal website](http://brianstempin.com) or at [GitHub](http://github.com/bstempi).

### Setup

This is a simple Maven project, so it can be set up directly from the CLI via Maven or by any IDE that has decent Maven support.  The project was developed with IntelliJ, so its known to work.

#### Maven

    mvn compile
    
At this time, there is no packaging, so it can be run from the command line directly, via Maven, or via an IDE.  The easiest way is probably this:

    mvn exec:java -Dexec.mainClass='com.brianstempin.vindiniumclient.Main' -Dexec.args='YOURKEY TRAINING com.brianstempin.vindiniumclient.bot.RandomBot'

#### CLI Usage

The CLI takes 3 arguments:

* Your key
* The game URL
* The fully qualified class name of the bot that will play

The arguments are space-delimited.

##### Key
The key is specified by the Vindinium website when a user name is registered.

##### Game Url
Instead of specifying a game URL, a user can say `TRAINING` or `COMPETITION`, which will connect to Vindinium's training and competition arena, respectively.  The only time a user will not use one of these two arguments is when they are connecting to a different server, such as a local development server.

##### Fully Qualified Bot Class Name
The Main class will reflectively instantiate a `Bot` to play the game.  In order to locate the bot, the fully quallifed name is needed, in the general form of `com.packagename.packagenamingpackage.SomeBot`.

### Extending
This client is broken into a few pieces:  Bots, DTO, and Main.

#### Bot
com.brainstempin.vindiniumclient.bot

There is a `Bot` interface that serves as the parent of all bots.  The gits is that a `GameState` is passed into the bot, it makes its decision, and then returns a `BotMove`.  The `Main` class will take that move, attach an API key to it, and send it off to the server.  Easy peasy.  The only thing to watch out for is that there must be a publically available default constructor.  This is needed to reflectively instantiate a bot during the game.

For an example of a bot, take a look at `RandomBot`.

#### DTO
com.brianstempin.vindiniumclient.dto

The DTO, or Data Transfer Objects, are just a Java modeling of the JSON responses from the server and the URL encoded messages to the server.  They have annotations to allow them to be automatically marshalled and unmarshalled by GSON and the Google HTTP client.  They're just glorified POJOs.

Unless Vindinium changes their communication structure, this can probably be left alone.  If there's a desire to presnt the game as a different class hierarchy, it should go into a separate package.

#### Main
com.brianstempin.vindiniumclient

Everything starts at the `Main` class.  Its responsible for parsing the arguments, running the game loop, etc.  Short and simple.

### Road Map
There are a few things that I'd like to do in order to consider this client "complete."  These are in no particular order.

#### Logging and Profiling
Since the bot only has a second to respond, response times are important.  It'd be nice to be able to see how long a bots responses are taking and where the hot-spots are.

It would also be nice to have a feature to dump game outcomes and stas to files.

#### Multiple Game Play
If someone wants their bot to go out and destroy everything, then they need something to run the bot in a loop.  It'd be nice if `Main` could accept a param to tell it how many games to play.

If someone *really* wants to give their bot some play time, then it might be nice to be able to run concurrent games.

#### Bot Setup
Because the bot is started via reflection, there must be a default constructor.  In order to allow people to do some initial set-up before the game, the `Bot` class should have a `setup()` method that gets called before the first server call.

#### Alternate Game State Representation
The game state representation used by the game isn't very pretty and makes certain tasks, such as indexing the location of mines on the map, a bit of a pain.  It would be nice to provide an alternate game state that included the immutable parts separate from the mutable parts and that made use of `Map`s and `List`s to make traveral easier.

#### Programatic Running of Games
`Main` should expose some methods so that games can be started without a CLI.