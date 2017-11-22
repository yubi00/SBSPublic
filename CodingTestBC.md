# Coding Test – Blockchain/Java developer

Create a Java command line program that uses  hash functions to check if a set of database tables have changed in content.

You may use your own database to develop this test, or use the MySQL RDS database reachable as follows:

```
mysql -u CodingTest  -h sbstest.chd1y6fipbcf.us-east-1.rds.amazonaws.com -P 3306 -pCodingTest
```
This database contains the example `Sakila` database which you can use as sample data (read only) and also a `SBSTest` database that can be used to create/modify data.

Your program will attach to a nominated MySQL database and execute a query specified on command line, input file standard input (Your choice as to which of these you implement, though some might be considered as better options).  The program will perform one the following activities as requested in invocation arguments:

* Create Proof of State: For the data returned by the query, calculate a `sha256` hash and store that hash together with the query on the local filesystem
* Validate State: For a given query, check the stored hash and check to see if that hash is still valid (eg, that the data has not changed)
* List hashes - list all hashes and queries executed

Write a test case that exercises the hashing, and which confirms that the validate function works correctly.  

Upload the test results to a github repository.  We should be able to execute your code by using `git clone` and `mvn` or `gradle` commands.

Notes:

* Performance is a consideration in this test.  In particular, try to minimise network traffic in your implementation.
* Only MySQL code need be provided, but the overall code should be as database-independent as possible.
* It is OK if the hashing function requires an _exact_ match of SQL statements including whitespace.  However, two tables that contain the same data should not return the same hash.
