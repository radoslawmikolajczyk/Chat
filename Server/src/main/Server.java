package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class Server {

    ArrayList clientArrayList;
    PrintWriter printWriter;

    //uruchomienie programu
    public static void main(String[] args) {
        Server server = new Server();
        server.startServer();
    }

    //start serwera
    public void startServer(){
        clientArrayList = new ArrayList();

        try{

            ServerSocket serverSocket = new ServerSocket(5000); //nasluch serwera
            //zeby serwer caly czas dzialal i odbieral polaczenia trzeba petli nieskonczonej
            while(true){
                Socket socket = serverSocket.accept();  // ta linia powoduje ze wszystkie polaczenia przychodzace na nasz serwer beda akceptowane
                System.out.println("Słucham: "+ serverSocket);
                printWriter = new PrintWriter(socket.getOutputStream()); //przekazujemy do niego komunikaty od klientow sieci
                clientArrayList.add(printWriter); // dodajemy klienta do listy aby go zapamietac

                Thread t = new Thread(new ServerClient(socket)); //odpalamy watek dla kazdego klienta z osobna
                t.start();
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }
    //tworzymy klase wewnetrzna do dodatkowego watku
    class ServerClient implements Runnable{

        Socket socket;
        BufferedReader bufferedReader; //bedziemy odczytywac to co klienci pisza

        //konstruktor
        public ServerClient(Socket socketClient){
            try {

                System.out.println("Połączony."); // jesli konstruktor zostanie wywolany to bedzie to znaczyc ze ktos sie podlaczyl
                // i wysylamy informacje o tym.
                socket = socketClient;
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // pozwala mi na odczytywanie tego co na wejsciu  w watku



            } catch (Exception ex){
                ex.printStackTrace();
            }
        }

        @Override
        public void run() {
            String str;
            PrintWriter pw = null;
            try {

                while ((str = bufferedReader.readLine()) != null){
                    System.out.println("Odebrano >> " + str);

                    Iterator it = clientArrayList.iterator();
                    while(it.hasNext()){
                        pw = (PrintWriter) it.next();
                        pw.println(str);
                        pw.flush();
                    }
                }

            } catch (Exception e){

            }
        }
    }
}
