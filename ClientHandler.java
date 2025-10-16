import java.util.ArrayList;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.IOException;

public class ClientHandler implements Runnable {
    //so that instances will be executed by a separate thread



    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>(); 
    /*to keep track of all our clients
      so that whenever a clients sends a message we can loop through our arraylist
      of clients nd sent the message to each client
      this arraylist is bsically responsible for us allowing to communicate or send mesgs
      or broadcast to mulltiple clients instaead of just one or just a server*/

    //this is static bcoz we want it to belong to class not each object of the class
    

    //property ----

    private Socket socket;  // socket that is passed from our server class
    //this is ofcourse used to establish connection b/w the client and the server

    private BufferedReader bufferedReader;//this will be used to read data specifically
    //msgs that have been sent from the client 

    private BufferedWriter bufferedWriter;//to send data specifically msgs to our client

    private String clientUsername; //to represent each client 


    public ClientHandler(Socket socket){
        //now we'll setup our class properties inside try block
        try{
            this.socket = socket; /*this is the object that is being made from the class for 
                                    that object that's being made set the socket of it equal to 
                                    wt is passed into the constructor*/
           
                      //character stream         //byte stream   
          this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
           
           /*there are two types of stream there's byte stream nd there's character stream
           nd we want a character stream coz we are sending msgs 
           nd in java chracter streams end w the word 'writer' 
           while byte streams end w the word 'stream' */


           this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

           this.clientUsername = bufferedReader.readLine();
        /*when we reading a line bcoz when the client presses enter a string will be 
        sent over along eith a new line character 
        so we want to read from the stream up untill a new line character 
        which means the user has pressed the enter key */



           /*it takes client handlers this represents a client handler object so we're passing
           it into the arraylist of client handlers and we're then going to sent a msg 
           to any connected client that a new user has joined the chat along with the user's username*/
           clientHandlers.add(this);


           broadcastMessage("Server: " + clientUsername + " has entered the chat! ");

        }catch(IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }

    }
    
    public void run(){  /*everything oin this run method is wt is run on a separate thread nd wt want to do on a separate
                        thread is listen for mesgs
                        bcoz listening for msgs is a blocking operation and blocking operation
                        basically means that the program will be stuck utill the operatio is completed
    so if we weren't using multiple threads our program would be stuck waiting 
    waiting for a msg from a client 
    so instead we are gonna have thread a separate thread waiting for msgs and another one 
    working with the rest of our application bcoz if not our prgram would basically be 
    sitting here being like just waiting for msgs to come in */
    
             String messageFromClient;

             while(socket.isConnected()){

                try{
                    messageFromClient = bufferedReader.readLine();   //this is blocking operation
                    broadcastMessage(messageFromClient);


                }catch(IOException e){
                         closeEverything(socket, bufferedReader, bufferedWriter);
                         break;   /*when the client disconnect this will break out of this while loop
                                  bcoz if we didn't have break right here that would essentially mean that 
                                  it would just keep printing this */

                }
             }
             


    }

    //Broadcast message method which will be used send the message a client
    //wrote to everyone in the group chat

    public void broadcastMessage(String messageToSend){
        //loop through and send a message to each client connected 
        //using a arraylist of clientHandler
        for(ClientHandler clientHandler : clientHandlers){
                          //clientHandler -> this object will represent each clienhandler each time
                                                          //for each iteration through this arraylist

            //now we want broadcast a message to everyone except the user who sent it
            //bcoz we don't want a broadcasted message sent that we wrote ourselves 

            try{
                if(!clientHandler.clientUsername.equals(clientUsername)){
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();//
                }
            }
            catch (IOException e){
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }
    
    public void removeClientHandler(){

        clientHandlers.remove(this);
        broadcastMessage( "SERVER: " + clientUsername + "has left the chat");
    }
        public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
            try {
                if(bufferedReader != null){
                    bufferedReader.close();
                }
                if(bufferedWriter != null){
                    bufferedWriter.close();
                }
                if(socket != null){
                    socket.close();
                }
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }


