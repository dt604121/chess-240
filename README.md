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
https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAHZM9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYHSQ4AAaxgotGMEwjOJLJOoNxKnKaB8CAQwAQUo4YXk0vQOOolAAPLlcmDNWBUt5kqzKN4oJhsuZrbbipcACxOADMMG6PSmo3UwHJ4ymAFEoG7yvrDcaZehJgd0BxzVQ8adlczSSApZl+XCboyGdoVeo2cZSgoOBw+R1GTm8wTq4XVKViyg5CgFD4HbDgMPUpWlV2SazTvXG82hw623bDDbCoCLmUIlCkVBIqodVhN8C1YUjpcYNcOndJuVVk8xw76hBTWg71N9jnKGf4MhzOUABMThOKGAw3mKYwwPejxTE+qQvm+H6rJmnCmF4vj+AE0DsOSMAADIQNESQBGkGRZP+eTnr627VHUTStAY6gJGgobyigswvG8HAHBeUAFCelxhuBQyQSsXxcUs4kwAC5zAgUq6lAgRE8rChHEai6KxNiq4FAWM6lOSVI0uxmLdFORLdgU9YAGY8s2FbaEKIoQeKq7evpLISlKsrsTAaAQMwkKIjCKDZu5NqeaS2q6t+UDeopDpOjArrQB6lFgAlNGlAGACMIa9OGLJRvccYJtA5Q+ME47QEgABeYUZmYaFRao1kmO2KgedOLKlGWgqdSgek9UWJaDuOo7jpOrXtQ2TYwEuE7aJggnxTainqTyB5HnF3qraU16iaM4mPuOiHphMfQHJ6GBZRaZTAU4BXCexJ1wWdr4XX0TVZuh3h+IEXgoOgBFEb4zCkekmSYAFmQwNwNnMBANlnECtA3cw6r3eUFTSLG+GxvUsbNC0TGqCx3TwedaBxQJcmXPBtV1fEiTlJaVOfWg+QrfT-H5Ipyn2ODalg8OmkYjpGpDfkrWGRSi3U5Nz6c1WlkzrNdm8otgrCjAHNvt5-a+a5hiwzANm+GhEW5LLMUIOZM35MYu3rVLpRJRAyQpeV7oY3dJQ5U4+WhkVkbRve8aJnK1UOkzjUyc14VS8Nau9RwKDcJki1K0t8iq0y6tOxykTDBANALVNy3W7L2vLat1obaLYDbQgYDmXxe286U6U5JlrvY49z2odmnj-VhkLNvh0IwAA4pBrKQ+RPfmGyfE4zPhMk-YkGUx9Bt8XTaNlIzUD1SzaBs-r6Dc6tClu8gsRzxGanQk-aji9pg0pwXvVGdnV9oHzjWNqRdSia2bAAmAPIYDyB0NwQ2Mo5Qm38oFc2lsk4iC6pFEaPY7Yux9JglA7tHSe29m6Zet1+4Bzys9UOqgSoxj6JHCq0cT71XjsPL+MscFKVfvPWEQCrKgMiBYVANBWT6gQDAbeEZBrdVTqSBQupZ6QTbPXAhuZNRT0fvPFuWAO42n2v0GRagYwVGMZBAAktIGMuVAKBj9E8MipYTZvT6HA0A0oyy3kuk8ExAA5SCl09iNGuhlf2lxB4hz6CY1QZiLGjGsbY+xjipjOJQN4sSvipgeJAF41x2SYmQUCaMYJMBQmJz+phQI2AfBQGwNweAY0VEKkXtDDGq9sqVFqA0LeO8Y4IU5qGAJkFeI0UPluA6RTRglPuN9WSR876EN7GNN+qhYRwFWbolAaIJZcNln2Acb9YQjNGIIwu9ZDlw2AP5FAXsTFQKSG-FcyduEKJ7H-CakA3znJZBreyes94g2gbA+BkojZIKOqbVBFsqoYM0SgeRP9oo6gQPgxKJCvapV9uEqh-og60L6BGeh4cypRyqmwhqywE6-Wrjw55ddeYNzdps-smQ356PwUY6ZKAknlDsQ4sJvcIkPRAoSkxfKYACr9DStCo9qkBEsBnZSXsABSEBoFv0CLk6UFDMbUWxt0ykDEWgmN3srN8oYGnACVVAOAEBlJQFmBK6QYz7oTOBFM61tr7WOpWAAdRYJYomLQABC+EFBwAANJfBdckwVsxKXn0vkCrmPNFn8zdgAKw1WgY56qeQcp2VpLE+yeFGWOS6356h-m8gdIYB57QeSOHrdIqx0hzbeGGC0lALzCFIuAYZSCZb3lyzANnb56Bq0gNsgCyBILdBgp8pC24kEUHMFhVbKWA7uylDtu3Gi-twQeyxT7PVIrA7B0KkS4qpKmE+0qgMuO1LOG6TecinsfgtDssgrCb1lBfXQGde26ds0v0Dh7YKZIGRUgwH-Xah1aU6WjoZfIdNW5mXLILXmyCnKDG5H2uevForQK9E4fKgGAQvA2r-P2WAwBsANMIOfFIUMKK906Ya3G+NCbE1aM7A++RVrofkpmwhtMhNdxE5QJZCLu5voOdwPA-UFA1AERZD9s0S5lzUHrZRNwNAKZ4SAJTUBnmqfU47es2nxF6akbEkdH7eyme1hZ0DwiNg6YkcogBGgEGym-bofQ0LmCgBnGFOR2DR14Pwxi5K2KiMaMuAAVjFdE4lDCI4PpgLCdOqhizEAIIkGAyMyHQExD9NC6jFImbo3ogRMBat4HU-hwjftiOlEeqGThQA
