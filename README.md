# tiralabra-rsa

## Documentation
- [Project specification](./documentation/projectspecification.md "Project specification")
- [User guide](./documentation/userguide.md "User guide")
- [Implementation report](./documentation/implementation.md "Implementation report")
- [Testing report](./documentation/testing.md "Testing report")

## Weekly reports
- [Weekly report 1](./documentation/weeklyreports/weeklyreport1.md "Weekly report 1")
- [Weekly report 2](./documentation/weeklyreports/weeklyreport2.md "Weekly report 2")
- [Weekly report 3](./documentation/weeklyreports/weeklyreport3.md "Weekly report 3")
- [Weekly report 4](./documentation/weeklyreports/weeklyreport4.md "Weekly report 4")
- [Weekly report 5](./documentation/weeklyreports/weeklyreport5.md "Weekly report 5")
- [Weekly report 6](./documentation/weeklyreports/weeklyreport6.md "Weekly report 6")

## How to run the program
All of the following commands should be run in the project root directory (i.e., the directory where the pom.xml file is located).

After cloning the project for the first time, navigate to the the project root directory and run
```
mvn clean compile test package exec:java
```
- clean: deletes the `target` folder (which shouldn't exist since the project has just been cloned)
- compile: compiles the source code
- test: runs tests
- package: packages everything into a JAR file
- exec:java: executes the program

If necessary, the tests can be skipped by running
```
mvn clean compile package exec:java -DskipTests
```

As long as the `target` directory exists with the required contents, the program can be executed by running
```
mvn exec:java
```

If the `target` directory gets removed for some reason, run `mvn compile` to generate it again.

Run tests and generate a JaCoCo report by running
```
mvn test jacoco:report
```
This will run the JUnit tests and generate a human-readable report to `target/site/jacoco/index.html`.

Run Checkstyle and generate a report by running
```
mvn checkstyle:checkstyle
```
This will run the checks configured in `checkstyle.xml` and generate a human-readable report to `target/site/checkstyle.html`.
