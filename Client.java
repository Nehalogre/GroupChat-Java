//This class will actually be very similar to our client handler class
//except we're going to be using threads to deal with blocking operations.

import java.net.Socket;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.*;

public class Client {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;//
    private String clientUsername;//each client will have username
    private String username;

    //constructor
    public Client(Socket socket, String username){

        try{
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = username;
        }
        catch(IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    //send message probably tell what it's going to do
    //this will be used to send to send messages to our client handler
    public void sendMesssage(){
        try{
            bufferedWriter.write(clientUsername);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);//we can get it input from console

         while(socket.isConnected()){
            String messageToSend = scanner.nextLine();
            bufferedWriter.write(clientUsername + "; " +messageToSend);
            bufferedWriter.newLine();
            bufferedWriter.flush();

         }
        }
        catch(IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);

           
        }

    }

    public void listenForMessage() {
        //as we will be listening for mesgs
        new Thread(new Runnable(){
            public void run(){
                String messageFromGroupChat;


                while(socket.isConnected()){
                    try{
                        messageFromGroupChat = bufferedReader.readLine();
                        System.out.println(messageFromGroupChat);
                    }
                    catch(IOException e){
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
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

        public static void main(String[] args){
            Scanner sc = new Scanner(System.in);
            System.out.println("Eneter your username for the group chat: ");
            String username = sc.nextLine();
           // server.startServer();
            try{
                Socket socket = new Socket("localhost",  1234);
                Client client = new Client(socket, username);

                client.listenForMessage();
                client.sendMesssage();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }

}



    

