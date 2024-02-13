# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

[Sequence Diagram](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWOZVYSnfoccKQCLAwwAIIgQKAM4TMAE0HAARsAkoYMhZkwBzKBACu2GAGI0wKgE8YAJRRakEsFEFIIaYwHcAFkjAdEqUgBaAD4WakoALhgAbQAFAHkyABUAXRgAej0VKAAdNABvLMpTAFsUABoYXCl3aBlKlBLgJAQAX0wKcNgQsLZxKKhbe18oAAoiqFKKquUJWqh6mEbmhABKDtZ2GB6BIVFxKSitFDAAVWzx7Kn13ZExSQlt0PUosgBRABk3uCSYCamYAAzXQlP7ZTC3fYPbY9Tp9FBRNB6BAIDbULY7eRQw4wECDQQoc6US7FYBlSrVOZ1G5Y+5SJ5qBRRACSADl3lZfv8ydNKfNFssWjA2Ul4mDaHCMaFIXSJFE8SgCcI9GBPCTJjyaXtZQyXsL2W9OeKNeSYMAVZ4khAANbofWis0WiG0g6PQKwzb9R2qq22tBo+Ew0JwyLey029AByhB+DIdBgKIAJgADMm8vlzT6I2h2ugZJodPpDEZoLxjjAPhA7G4jF4fH440Fg6xQ3FEqkMiopC40Onuaa+XV2iHus30V6EFWkGh1VMKbN+etJeIGTLXUcTkSxv2UFq7q7dUz3l8fsaAcCIKCJjAAOoACUNbxmNTqzu1B-dY-hUS3AalMDXB4IgVJULVGLc92xN1niZNkOS5K4eUqTNwz9e0xRQt992hT9enYKIUN9SNl1UXCRwIi0iP9EcY38eMIhTNMCkI7NczQfNtF0AxjB0FA7UrLR9GYWtvF8TA6KbXpWz4T43iSN40nSLsJB7PIWL9KNRzw-pJ0ElVRnU9Al09UjpRdICUK3AzKOzSCdR6F5j2+X5DLcC9QRQ28HysJ9XKwqCYxIiisw0kjV3MnEZBQBAThQZVVWskKjP8+yYKiGSvnkoEQTDLzHzDKjNBo3CJITRjMDzAsuOLQYZArYYYAAcR5R4RPrcTG2YD0uiiaJGrkxStB5NSbNClstKCmBBgARz0SQwGasoJES1D0AkYzx1MgCIrlSyLlcuyPzSpzT1c7LL1y+98r8wD6TIkzgtW6iTPC98gJgZAHEWyRRkOnC0vIWTT3cmAhqWlKP2679QZ5D5hj-Fd7p6mGyjhhxNNozqk1TdMwZQNGwDYjjC24oxsD0KBsBi+B8V8JqeQ8USGwCLqv1bBJkkG4bTFG9BcZ5VkeWHcaGUmma5ocVkUHcb6Vqoyo8cFsoNsDTE3pxPbiQOiH-sZCITpc3m3Jyzyrp8grsx1u6ofwvywrV7CcRA3xZcVpCUZQZk+D+u6Abgw1fjdsp0I96RbrdG2vTxr2EdIqHQylmWeV+jGSqxmBGP5sovaJqqi2McxosndwYAAKQgad6eDowFAQUBrQ6lnRfG3rYlODt0jxkakt7ApSrgCBJygBWeRz1Ov3w8X5vL6dZdckfs+92PXsduUwys7Xw8PfWgcNnvzo8i08vNm6dsCh67Zeh2oI3Bbk+jpet4co9d49r2D9D4+nwfq2I4nqPR58GXkjGgUAIgACsK5oFlv3Qe0AF6eyXsVHopVsbJgquxPOpMdDAEsIgRUsBgDYCpoQZwrhGbtVKs3ZG0QMpyQUhkdQ49tIIlxDFPAtwU72zMurNeIB2FQD4AAIRTk-AGSQrCnFZHAYQWUkjCCEV8XEngHi-3PptREyJUT23jmAmASIUSpxQencqlUgA)

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

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
| `mvn -pl shared tests`     | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

### Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
