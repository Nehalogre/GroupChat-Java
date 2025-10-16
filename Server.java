import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
//our server class is respoonsible for listening to clients who wish to connect and 
//they will spawn to new thread to handle them 


//first add some properties
// this class is going to have a server socket object
public class Server {

    private ServerSocket serverSocket;  // this object will be responsible for listening for incoming connectionsor clients
    //nd creating a socket bject to communicate with them.


    //now we'll make the constructor that will set up our server socket 
    public Server(ServerSocket serverSocket){
        this.serverSocket = serverSocket;

    }
    
    //now we'll make a method called start server which will be resonsible for keeping our server running 
    public void startServer(){

        //try block because what we'll do in here we'll need some input output error handling 

        try{

            //now we want our sever constantly running until the server socket is closed
            //so basically we want our server running indefinitely 

            //we are going to run server socket while the server socket is open so while it isn't closed and
            
            while(!serverSocket.isClosed()){

                //while it isn't closed wht we're gooing to be doing is waiting for a client to connect
               Socket socket =  serverSocket.accept();
               System.out.println("A new client has connected!");
                //this method is a blocking method meeans our program will be halted untill a client
                                       //connects. however, a client does connect a socket object is returned which can be used to 
                                       //communicate with client 

                  //when i run the program or typed in a username the server would say a new client
                  //has connecte 


                  //now we're goint to crete an object  and  it is called ClientHandler
                  ClientHandler clientHandler = new ClientHandler(socket);
                  /*So bsically each object of this class will be responsible for
                  communicating with a specific client

                  this class will implement runnable interface 
                  whose instances of the class will each be executed by separate thread and this is
                  vital to functioning this application bcoz we didn't spawn a new thread 
                  to handle the connection with each new client our application would oly be 
                  able to handle one client at a time*/


                  Thread  thread = new Thread(clientHandler);
                  thread.start();//to begin the execution  of this thread
              
            }
        }
        catch(IOException e ){

        }

    }
    public void closeServerSocket(){  // bcoz when error occurs we just want to shut down our server socket

        try{
            if(serverSocket != null){
                serverSocket.close();
            }

        }
        catch(IOException e){
            e.printStackTrace(); //print the error message to the console

        }
    }

// to run our program
    public static void main(String[] args) throws IOException{     
        

        ServerSocket serverSocket = new ServerSocket(1234);
        //our server will be listening for clients that are making a connection to this port

       Server server = new Server(serverSocket);

       server.startServer();//server constantly running we made the method start server
       
       

    }

    
}
