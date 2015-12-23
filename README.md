#BitCoinMiner

The project mines "BitCoins" by generating the SHA-256 code of a given input string and outputs the BitCoins having the given number of input zeros.
The objective of this project is to mine bitcoins starting with the given number of zeros and mine them quickly using miners on different machines. They all report to a master
which keeps track of the number of BitCoins mined at any point. 

The project has been done using Akka actors. 
A master has several remote miners on different machines or instances running under it. Each remote machine itself has several miners running on it at any point.
