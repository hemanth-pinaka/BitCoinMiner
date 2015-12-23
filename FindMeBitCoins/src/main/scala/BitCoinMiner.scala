import akka.actor.{Actor,ActorRef,ActorSystem}
import akka.actor.Props
import java.security.MessageDigest
import akka.routing.RoundRobinPool
import scala.util.Random
import com.typesafe.config.ConfigFactory





case class workToMiner(inputString: String,numberOfZeros: Integer)
case class startMining(noOfWorkers:Integer,inputString: String,noOfZeros:Integer, runningSeconds: Integer)
case class startRemoteMining(inputString:String,numberOfZeros:Integer)
case class receiveBitcoin(bitCoin:String,numberOfNonces:Integer)
case class remoteWorkerRequest(remoteWorkerCount:Integer)

object BitCoin{
  def main(arg:Array[String]){
  //val system=ActorSystem("Bitcoin")
    
   
   val configfactory = ConfigFactory.parseString(
      """ 
     akka{ 
        actor{ 
          provider = "akka.remote.RemoteActorRefProvider" 
        } 
        remote{ 
                enabled-transports = ["akka.remote.netty.tcp"] 
            netty.tcp{ 
          hostname = "127.0.0.1"
          port = 1234
        } 
      }      
    }""")
    
   val BitCoinWorld = ActorSystem("BitCoinWorld", ConfigFactory.load(configfactory))
   
   
   val master: ActorRef=BitCoinWorld.actorOf(Props[MiningMaster], "master")
  
  val numberOfZeros:String=arg(0)
  val numberOfZerosInt:Integer=numberOfZeros.toInt
  master!startMining(4,"pinakah;jfutndd",numberOfZerosInt,20)
  
  

  
}
}




class MiningMaster extends Actor{
  var bitCoinCount=0
  var inputStringLocal:String=""
  var noOfZerosLocal:Integer=0
  var startElapsed:Long=System.currentTimeMillis()
  var totalNumberOfNonces:Integer=0
  def receive={
    case startMining(numberOfLocalWorkers,inputString,noOfZeros,runningMinutes)=>
            
          inputStringLocal=inputString
          noOfZerosLocal=noOfZeros
          println("About to create router")
          
          
          
          
          val router: ActorRef =context.actorOf(RoundRobinPool(numberOfLocalWorkers).props(Props[Miner]), "router")
   
         
          println("created router")          
            
          
          var i=0
    
          for(i<-1 to numberOfLocalWorkers){
            router!workToMiner(inputString,noOfZeros)
          }
          /*while(true){
            router!workToMiner(inputString,noOfZeros)
          }*/
          
          
        
    case  receiveBitcoin(bitCoin,numberOfNonces)=>
          bitCoinCount=bitCoinCount+1
          
          println("Input String= "+inputStringLocal)
          println("BitCoins Mined= "+bitCoinCount)
          println("BitCoin= "+bitCoin)
          println("Mined by " +sender.path)
          println("Time Elapsed= "+((System.currentTimeMillis()-startElapsed)/1000)+" Seconds")
          println("Number of Nonces Tried= "+(totalNumberOfNonces+numberOfNonces))
          
          println()
          
    case  remoteWorkerRequest(remoteWorkerNumber)=>
          println("Contacted by remote worker number "+remoteWorkerNumber)
          println("Starting mining on the remote worker "+remoteWorkerNumber)
          sender!startRemoteMining(inputStringLocal,noOfZerosLocal)
        
      
    case  str:String=>
      println(str)
  }
  
    
}

class Miner extends Actor{
  
  
  var bitCoinCount:Integer=0
  def receive={
    
    case workToMiner(inputString,numberOfZeros)=>
      println("Starting mining on the Local System")
      val utilities:Utilities=new Utilities()
      
      utilities.mineCoins(inputString,numberOfZeros,sender)
  }

}

class Utilities{ 
def mineCoins(inputStringToMiner: String,noOfZeroesMiner:Integer,masterActor:ActorRef){
    
    var nonce:String=Random.alphanumeric.take(6).mkString
      var number:Integer=0
         //   println( self.path.name)

      
     val md = MessageDigest.getInstance("SHA-256")
      var hexString:String= ""
      
      var i=0
      var zeroString:String="0"
      for (i <- 1 to noOfZeroesMiner-1){
        zeroString=zeroString+"0"
      }
      
      println("zeroString= "+zeroString)
      
      
      while(true){
        hexString= md.digest((inputStringToMiner+nonce+number).getBytes("UTF-8")).map("%02x".format(_)).mkString
      
        
        
        if(hexString.startsWith(zeroString)){
          
          masterActor!receiveBitcoin(hexString,number)
          number=number+1
        //  println(hexString)
        }
        else{
          number=number+1
          //println("boo")
        }
      }
     // sender!bitCoinCount
  }
}
  
