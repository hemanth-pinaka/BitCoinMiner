import akka.actor.{Actor,ActorRef,ActorSystem}
import akka.actor.Props
import java.security.MessageDigest
import akka.routing.RoundRobinPool
import scala.util.Random
import com.typesafe.config.ConfigFactory


/**
 * @author hemanth.pinaka
 * 
 * 
 */
case class initialize(masterIpAddress:String)
case class startRemoteMining(inputString:String,numberOfZeros:Integer)
case class receiveBitcoin(bitCoin:String,numberOfNonces:Integer)
case class remoteWorkerRequest(remoteWorkerCount:Integer)
case class workToRemoteMiner(inputString:String,numberOfZeros:Integer,master:ActorRef)

object RemoteMiner{
  def main(arg:Array[String]){
    val masterAddress=arg(0)
    
    val config = ConfigFactory.parseString(
      """akka{
          actor{
            provider = "akka.remote.RemoteActorRefProvider"
          }
          remote{
                   enabled-transports = ["akka.remote.netty.tcp"]
            netty.tcp{
            hostname = "127.0.0.1"
            port = 0
          }
        }     
      }""")

    implicit val system = ActorSystem("RemoteSystem", ConfigFactory.load(config))
  
  
   val remoteMaster: ActorRef=system.actorOf(Props[RemoteMaster],name="remoteMaster")
   
   println("remote is ready.")
   
   remoteMaster!initialize(masterAddress)
   //remoteMaster!initialize("127.0.0.1:1234")
   
  }
}

  class RemoteMaster extends Actor{
    def receive={
      
      case initialize(masterIpAddress)=>
        
      val master=context.actorSelection("akka.tcp://BitCoinWorld@"+masterIpAddress+"/user/master")
      master!remoteWorkerRequest(1)
      
    case startRemoteMining(inputString,numberOfZeros)=>
      val router: ActorRef =context.actorOf(RoundRobinPool(4).props(Props[Miner]), "remoteMaster")
   
         
          println("In remote master")
          sender!("Created router in Remote System with 4 Routees")          
            
          
          var i=0
    
          for(i<-1 to 4){
            router!workToRemoteMiner(inputString,numberOfZeros,sender)
          }
    }
      
  }
  
  class Miner extends Actor{
    
  var bitCoinCount:Integer=0
  def receive={
    
    
      
    
    case workToRemoteMiner(inputString,numberOfZeros,master)=>
      
      println("got the order to mine")
      mineCoins(inputString,numberOfZeros,master)
    case msg:String=>
        println(msg)
      
  }
  
  
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
        }
        else{
          number=number+1
        }
        
      }
  }
  }