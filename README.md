# tiralabra-rsa

## Weekly reports
- [Weekly report 1](./documentation/weeklyreport1.md "Weekly report 1")
- Week 2
- Week 3
- Week 4
- Week 5
- Week 6

## How to run the program
Generate the executable JAR file by running `mvn package` in the project root directory (i.e., where the project pom.xml file is located). Still in the project root directory, the program can then be executed by running the following command (this will probably work on my machine only, looking at the path; leaving this just as a note for myself):
```
java -cp target/tiralabraproject-rsa-1.0-SNAPSHOT.jar:/home/ylireett/.m2/repository/commons-lang/commons-lang/2.5/commons-lang-2.5.jar com.ylireetta.tiralabraproject_rsa.Main
```

If the Commons Lang JAR is not included in the classpath, ToStringBuilder class is not found. I will investigate the problem and try to fix it before the end of this course.


Run tests and generate a JaCoCo report by running `mvn test jacoco:report` in the project root directory. This will run the JUnit tests and generate a human-readable report to `target/site/jacoco/index.html`.
