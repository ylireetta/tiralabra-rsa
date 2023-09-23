# Testing documentation

## Should contain: 
- What has been tested and how
- What types of input were used (especially important for comparative analysis)
- How can the tests be repeated
- Results of empirical testing presented in graphical form
- Tests should ideally be a runnable program. This makes repeating the tests easy
- For Java it is recommended to do unit testing with JUnit

## Test situation on week 3
At this point, tests have been written for `PrimeHelper` and `FileHelper` classes.

`PrimeHelper` tests verify that prime as well as composite numbers are detected successfully. More attention has to be paid to testing the `PrimeHelper` class, but because most of the code in the class is in private helper methods, there is little to be tested at the moment. In addition, I am still not completely sure what I can use as test data - for example, if I wanted to test the `millerRabinTest` method, I would need some sensible random witnesses and the `d` from writing `n - 1` as `2^r * d`. Of course, I could get the values by calling the methods available in the `PrimeHelper` class, but since I have written the methods myself, it probably isn't the best practice to use that data in the tests.

The tests for the `FileHelper` class verify that files can be created, written to, and read successfully. Also unsuccessful results are tested, e.g. by attempting to create multiple files of the same publicity class for the same user, and by attempting to read files that do not exist. Not all methods and paths in the `FileHelper` class have been tested yet, but the test coverage of this class will increase in the following weeks.

There will be a lecture on testing at the beginning of week 4, after which I will hopefully be wiser when it comes to performance testing. As of now, not much thought has been paid to that.