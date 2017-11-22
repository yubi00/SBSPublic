#Coding Test – Blockchain/Java developer

Create a Java command line program that uses SHA256 hash functions to check if a set of database tables have changed in structure.

You may use your own database to develop this test, or use the MySQL RDS database reachable as follows:

```
mysql -u CodingTest  -h sbstest.chd1y6fipbcf.us-east-1.rds.amazonaws.com -P 3306 -pCodingTest
```

Your program will attach to a nominated MySQL datababe and execute a query specified on command line, input file standard input (Your choice as to which of these you implement, though some might be considered as better options).  The program will perform one the following activities as requested in invocation arguments:

* Create Proof of State:  
