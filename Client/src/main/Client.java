package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    //klienta zaczynamy od zdefiniowania portu !!
    public static final int PORT = 5000;
    //kolejnym elementem jest zdefiniowanie IP serwera
    public static final String IP = "127.0.0.1";

    BufferedReader bufferedReader;
    String name;

    //start programu
    public static void main(String[] args) {
        Client client = new Client();
        client.startClient();
    }

    //uruchomienie klienta
    public void startClient(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Podaj imie: ");
        name = sc.nextLine();
        try{
            Socket socket = new Socket(IP,PORT);
            System.out.println("Podłączono do " + socket);

            PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Thread t = new Thread(new Addressee());
            t.start();

            while(true){
                System.out.println(">> ");
                String str = sc.nextLine();
                if (!str.equalsIgnoreCase("q")){  //equalsIgnoreCase  oznacza ze niewaze czy duza czy mala literka
                    printWriter.println(name + ":" + str);
                    printWriter.flush();
                } else {
                    printWriter.println("Użytkownik "+ name + " rozłączył się.");
                    printWriter.flush();
                    printWriter.close();
                    sc.close();
                    socket.close();
                }
            }
        } catch (Exception e){

        }
    }

    class Addressee implements Runnable{ //odbiorca

        @Override
        public void run() {
            String message;
            try {
                while ((message = bufferedReader.readLine()) != null){
                    String [] subString = message.split(":");
                    if (!subString[0].equals(name)){
                        System.out.println(message);
                        System.out.println(">> ");
                    }
                }
            } catch (Exception ex){
                System.out.println("Połączenie zostało zakończone.");
            }
        }
    }
}

