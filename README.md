# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

## Phase 2 URL
https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAHZM9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYHSQ4AAaxgotGMEwjOJLJOoNxKlK-KgjJx1EoAB5crkwZqwKlvMlWZRvFBMNlzEaTcVLgAWJwAZhg3R6U1G6mA5PGUwAolBbeVgAgpRwwvJpehJgd0Bw9VQ8adlczSSApZltbSOoyGdoVeo2cZSgoOBw+UXtGmMwTS9nVKVcyg5CgFD5zbDgL3UsWlS2SazTpXq7We+bdabDMbCoCLmUIlCkVBIqofAgsMvgWrCkdLjBrh07pNyqsngPzfUIAm0Jepvs05RD-BkOZygAmJxOH0BnPMUxhgK9HimW9UnvR9n1WZNOFMLxfH8AJoHYckYAAGQgaIkgCNIMiyL88iPF1V2qOomlaAx1ASNAfXlFBZheN4OAOY8oAKfdLl9IChhAlYvlYpYhJgAFzmBAp51KBBcJ5WEcLw1F0VibF5wKLMx1KckqRpJjMW6EciVbApKwAMx5WsbkFYU5WA8V5ydLSWQlKVZSYmA0AgZhIURGEUFTJzjRc0k0B3BA3ygJ0ZPNS0YBtaB7RIsAYvI0p3QARm9Xo-RZQN7lDcNoHKHxgkHaAkAAL0CpMzEQ0LVDMkxGxUZzRxZLV63kRVgpkkBUhQEBpSqfRXiWWERPeTE2QGobZSQcyYCm2tzN8XlNjAObWWwbx0w4QLlrGtjWpQTSOpzPNu0HftB2HRrmqrGsYBnIcGx46LjRkpSeS3HcsE4p0PtKM8BNGISb0HGDEwmPoDgdDA0v1Mo-ycHK+KYiHIKhh8Yb6OqUyQ7w-ECLwUHQbDcN8ZgCPSTJMG8zIYG4czmAgJaPuSnJmHVZHygqaQQywkN6hDZoWlo1R6O6KDobQKLuMky4oMqqr4kScoDVl3G0HyTAPukjUUFkqne0U02wBUjF1KN86TO03TXrl267x1kt7ZZR7LN5V7bLCbXHzczsPIcwxGZgNayqCo32o9sKIqMh78mMKKkfTM0LQgZIEuKu0EdSr70qy9G8oDIMrzDCM5XK81Vdq8T6ujkQ2pCi6NGC3JGqrO6Gw0-Iu4O7hMlel23vkd2mTHR7ImGCAaBenv5H1pXPu+i2-t3IzAeNYGucdQu+dR9GENTTxidQyFayw6EYAAcRA1laaIvfSKKPnKlvkXxfsECZZxwPOKKyBMrCqUBqrqzQJrAO6A9YG3yDJZAsR77+kUtCZBagrZqVOnbSenVHY3WgWgCeZYmrJw5N7WsUFBQ8hgPIHQ3AhT+3-ugIOMp7JgzDj5CO60m7pxQLHXB8cECRW3s6Zuxs4pZxzraF+BcxElAyk4bKPpS6qAKsGPolcSrVxVmAmqywG6Ew7l3Khvdbb9zbrJNBD9YTENMmQ0okQLCoBoKyKMCAYA-39KdARJCqzCLviBXUH0jTfWsf6DeANyJAxXiDPoXi1DBgqP0BJABJaQwZMo-g9K6J4hF8yhyxn0ehoBpTagvLDJ4CSAByIFYZ7EaPDFKadLhHxUfEh+SSUkgXSZk7JuSpj5JQOUwSlSpglOGiM8GYyOmjFqaMepMBGmNyJihQI2AfBQGwAwuAV1AkKifvTfObJOL81qA0b+v8a7QR1j6GpIEOLkSASuOJ9zpn4wksAri8Cjbtiuug1QsJdmdkyACzBWJsEWLjm2fBfZICPjsVPBxFCYCmJ6jQuhDC7KENYSHDhXkuGR0QsYyx4VhGpwPnw0okjs6JTzs0ylbolElz6P6NR5cipVzKro6q9cT4+NbtC7us4zHiJwX4jsXZ0GwgSfdNuj0eQdGYLK7QMAeSQFobobgArO6WPQcEleoTfnAqlSBSJFLcjA26aMXp5Qsk5KadzFpKN-wsrSRku1-TDGITPmsgIlgUDCKkQAKQgDQ9BgQJnSlkSc9KlQqiUmoi0BJf9XaPh9Ns4AAaoBwAgHJKAsx3WPORs84EcTM3ZtzfmlYAB1FgqTRYtAAEJYQUHAAA0l8d1fSHWzB5WrAgGsYBa2YbrZeXzDbiNKAAKzDWgaVoaeToPBTbMVULBEwopCPeF6BEWe2RVZVFi9gBqqSJiww2LR24vYbcECBLmBEt4aIXxrZShkoQFvaJlLwQ0ukUlfOzrFHKNyqy-KHLNG51KtcuuBj+UkqFb7UVfDxWvt0tK91e71Be0PeaQwKr0VJASfULVhh2g8kcLhzxPTpC9RjoKjdOkQKQq7n4LQoKQKwn1doTDpDKysa7PslAgpkgZFSMtBAWbKBVqSvBjd46VxGqnYu+dZq1D-QtbvADjKXUAV6Py31JMAheCzZ+TssBgDYG2YQCBKQ6bEW5rG9+AshYizFq0FOgD8iczgfOBWXnYnyakj8qdzHLEgG4HgbU0gFA1FscZDd08NhzzUKigJNx27mK7uFsz+qYtxaTpWGeyW3EBISRltdWWItQF9nlnjiXZ6uNSx4whGhAuGnkeCbLeBIm2JgF1uEq6SgxK+aUWRgHUY+n5UAA
