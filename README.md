# RestAssured_TestNg_Allure
This demo represents a simplified framework for testing and validating REST APIs using the REST-assured library, TestNg, and Allure frameworks. The project includes examples:

1. Checking responses for requests such as GET and POST
2. Verifying JSON data consistency using JSON schema
3. Implementing the models layer.

**Before start**

You need to install Allure Report utility.
TestNG and  REST Assured dependencies has already added to the project pom file.

**Start**

Run all tests

```$ mvn clean test ```

Run a set of test methods from a test class

```mvn test -Dtest=BookingApiRequestTests#createBookingFromFile+getBookingByIdAndValidateOverJsonSchema```

**After tests**
If you want to see the test report locally:

```$ allure serve ./target/allure-results```

If you want just generate the test report without web service run:

```$ allure generate --clean target/allure-results```

Go to src/allure-report folder and click index.html


**Report**

Allure is a top-notch test report framework, known for its excellent support across languages and test runners, easy to integrate to the almost any CI.

The most popular annotation:

`@Step` - combine all steps (annotated methods) and generate a readable test report where you will see all the steps as text. Also, it adds variables with values to the step into the report.

`@DisplayName` - just JUnit 5 annotation witch means a test name

`@Issue` - add a link to Jira (or any other system) into your report for the test

`@Severity` - add a marker as BLOCKER or CRITICAL and etc into your report for the test

`@Flaky` - add a marker the test as flaky in the report system

`@Epic` -> `@Feature` -> `@Story` - combine your tests to a tree and split them by epic | feature | story

A sample of the Allure report can be found in the allure-report directory.
