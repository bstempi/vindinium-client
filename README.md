# vindinium-client

![Build status](https://travis-ci.org/bstempi/vindinium-client.svg)

## Overview

This is a simple Java client for the [Vindinium](http://vindinium.org) game.  Vindinium is a simple game played by an AI via HTTP that I found via Reddit.

The project is created using the Google HTTP client and GSON.  I decided to write a client because other clients seem to have some strange/verbose ways of parsing server communication and of presenting the game.  This client is focused on staying out of the way and letting people develop AIs to play.  It is also meant to be very easy to extend.

There are two flavors of the client.  The simple one gives a very bare-bones game state and forces the bot to do a lot of the work parsing the map.  The advanced one gives a much nicer game state that includes hash maps containing locations of objects on the map.  This convenience comes at the expense of time.

### License
This software is public domain.

### Author
Hi, I'm Brian!  You can find me at [my personal website](http://brianstempin.com) or at [GitHub](http://github.com/bstempi).

### Setup

This is a simple Maven project, so it can be set up directly from the CLI via Maven or by any IDE that has decent Maven support.  The project was developed with IntelliJ, so its known to work.

#### Maven

To build:

    mvn compile

To create an uber JAR:

    mvn package
    
Command for running from Maven:

    mvn exec:java -Dexec.mainClass='com.brianstempin.vindiniumclient.Main' -Dexec.args='YOURKEY TRAINING simple com.brianstempin.vindiniumclient.bot.RandomBot'

#### CLI Usage

The CLI takes 4 arguments:

* Your key
* The game URL
* The bot type
* The fully qualified class name of the bot that will play

The arguments are space-delimited.

##### Key
The key is specified by the Vindinium website when a user name is registered.

##### Game Url
Instead of specifying a game URL, a user can say `TRAINING` or `COMPETITION`, which will connect to Vindinium's training and competition arena, respectively.  The only time a user will not use one of these two arguments is when they are connecting to a different server, such as a local development server.

#### Bot Type
This parameter tells us which type of bot is being used so that the appropriate bot runner can be invoked.  Pass in `simple` for `SimpleBot` or `advanced` for `AdvancedBot`.

##### Fully Qualified Bot Class Name
The Main class will reflectively instantiate a `Bot` to play the game.  In order to locate the bot, the fully qualified name is needed, in the general form of `com.packagename.packagenamingpackage.SomeBot`.

### Extending
This client is broken into a few pieces:  Bots, DTO, and Main.

#### SimpleBot
com.brainstempin.vindiniumclient.bot.simple.SimpleBot

The gist is that a `GameState` is passed into the bot, it makes its decision, and then returns a `BotMove`.  The `SimpleBotCallable` class will take that move, attach an API key to it, and send it off to the server.  Easy peasy.  The only thing to watch out for is that there must be a publically available default constructor.  This is needed to reflectively instantiate a bot during the game.  If you wish to run a bot that has constructor parameters, then you must modify `Main.java`.

For an example of a bot, take a look at `RandomBot` or `MurderBot`.

#### DTO
com.brianstempin.vindiniumclient.dto

The DTO, or Data Transfer Objects, are just a Java modeling of the JSON responses from the server and the URL encoded messages to the server.  They have annotations to allow them to be automatically marshalled and unmarshalled by GSON and the Google HTTP client.  They're just glorified POJOs.

Unless Vindinium changes their communication structure, this can probably be left alone.  If there's a desire to present the game as a different class hierarchy, it should go into a separate package.

#### Main
com.brianstempin.vindiniumclient

Everything starts at the `Main` class.  Its responsible for instantiating the bot, a bot runner, and then running the bot.  Easy peasy.

#### AdvancedBot
com.brianstempin.vindiniumclient.bot.advanced.AdvancedBot

This interface is an improvement on `SimpleBot`.  Instad of taking a `GameState`, it takes an `AdvancedGameState`.  This enhanced game state delivers the game state in a format that is easier for the developer to deal with.

For an example, take a look at `AdvancedMurderBot`.

#### AdvancedGameState
com.brianstempin.vindiniumclient.bot.advanced.AdvancedGameState

This class extends `GameState`.  In addition to the elements in `GameState`, this new type offers a graph of the board and hash maps for each of the elements on the board to make searching easy.

### Road Map
There are a few things that I'd like to do in order to consider this client "complete."  These are in no particular order.

#### Logging and Profiling
Since the bot only has a second to respond, response times are important.  Log4j is used for logging.  Users are free to add code to dump the game state on each turn.  It might be nice to add more metrics to the code.

#### Multiple Game Play
If someone wants their bot to go out and destroy everything, then they need something to run the bot in a loop.  It'd be nice if `Main` could accept a param to tell it how many games to play.

If someone *really* wants to give their bot some play time, then it might be nice to be able to run concurrent games.

#### Improving the AdvancedGameState
While it is absolutely an improvement over the `GameState`, it still leaves something to be desired.  For now, the `AdvancedGameState` is to be considered unstable since it will be undergoing some improvements.

#### Testing
Testing the decisioners is hard.  Its tedious to go through and set up game scenarios to test each possible outcome of each decisioner.  Right now, the testing is not a good example of how a project should be tested.