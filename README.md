#BitCoinMiner

The project mines "BitCoins" by generating the SHA-256 code of a given input string and outputs the BitCoins having the given number of input zeros.
The objective of this project is to mine bitcoins starting with the given number of zeros and mine them quickly using miners on different machines. They all report to a master
which keeps track of the number of BitCoins mined at any point. 

The project has been done using Akka actors. 
A master has several remote miners on different machines or instances running under it. Each remote machine itself has several miners running on it at any point.

------------------------------------------------------------------------------------------------------------------------------

Input :
On the server side: An integer k. Example: "run 3" or "run 5"

On the client side: An IP address that points to the server

The input string is appended with the gatorlink Id. 
Gatorlink Id used : pinakah

Output : 
Input string and its bitcoins value.
Total number of bitcoins mined.
Total number of nonces used.
Time elapsed
-------------------------------------------------------------------------------------------------------------------------------
The project contains two scala files. "BitCoinMiner" in the scala project "FindMeBitCoins" which serves as the Server and "RemoteMiner" in the project "RemoteMiner"

"FindMeBitCoins" takes the number of zeros as input
"RemoteMiner" takes the ipaddress of the server as input

The server when started starts 4 local workers under itself which start mining for coins.

When the RemoteMiner is started on a remote system, it contacts the server and the server sends it a signal to start mining the bitcoins remotely.
Instructions {How to run}
----------------------------------------------------------------

Copy the folder on client and server machine. 
Go to project rootFolder ,FindMeBitCoins

Run the following commands for client(Worker) and server(Master) 
------------------------------------------------------------------
The server can be run by navigating to the "FindMeBitCoins" folder and running sbt there. The input is the number of zeros.

example: "run 3" or "run 4"

Now from the 

Similarly the Client "RemoteMiner" can also be launched by navigating to its root folder and runnng sbt. 
The input is the ip address along with the port of the server.

Example: run 127.0.0.1:1234
----------------------------------------------------------------

Note
----------------------------------------------------------------------------

Please refer to excel sheet in the project root folder for our test results and the number of bitcoins mined for various inputs

Details:
----------------------------------------------------------------------------
1. Size of the work unit has been decided on the basis of the number of cores in a machine. 
   Thus, each core in a machine is treated to be equal in performance and allotted work assigned to the machine/number of cores. 
   
2. Result of running the program for k = 4 :
	Number of BitCoins Found: 223
	Time run for: 		     5 minutes
	The distribution of work:
	4 local Actors mining on the server and 3 clients with 4 remote miners each

   
3.  
Running time for k=5.
BitCoins mined=15
One server and 3 clients with 4 miners on each.
Real time: 300 seconds
System Time: 1034
CPU time: 43

CPUtime/realtime= (1212+46)/300=4.19	

4. After running one server with 3 clients each with 4 miners, 
The largest bitcoin found was one with 6 zeros after 15.3 minutes. Number of nonces tried is 2.3 million.

5. Largest number of machines the program was run on: 2 machines with 8 cores each 

