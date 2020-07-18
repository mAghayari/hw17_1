package client;

public class Client1 extends Client {
    final static int portNumber = 3200;
    final static int clientNumber = 1;

    public static void main(String[] args) {
        process(portNumber, clientNumber);
    }
}