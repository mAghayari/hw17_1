package server;

import org.apache.log4j.Logger;
import util.Utility;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Formatter;
import java.util.List;
import java.util.Scanner;

public class Server {

    public static int commandCounter = 0;
    private static final Logger LOGGER = Logger.getLogger(Server.class);

    public static void main(String[] args) {
        LOGGER.info("socket server starting");
        try {
            Path operationsPath = Paths.get("C:\\Users\\Codebanoo\\IdeaProjects\\serverSocket\\src\\operations.txt");
            List<String> operationList = Files.readAllLines(operationsPath);

            System.out.println("Enter report duration time(in minute):");
            int reportDuration = Utility.getInteger();
            System.out.println("Enter number of lines to send for each client each minute:");
            int numberOfLines = Utility.getInteger();

            ServerSocket serverSocket1 = new ServerSocket(3200);
            ServerSocket serverSocket2 = new ServerSocket(3201);

            Socket socket1 = serverSocket1.accept();
            Socket socket2 = serverSocket2.accept();

            Scanner scanner1 = new Scanner(socket1.getInputStream());
            Scanner scanner2 = new Scanner(socket2.getInputStream());

            Formatter clientFormatter1 = new Formatter(socket1.getOutputStream());
            Formatter clientFormatter2 = new Formatter(socket2.getOutputStream());

            int i;
            int j = 0;

            outer:
            while (j < reportDuration) {
                String command;
                for (i = j * numberOfLines; i < (j + 1) * numberOfLines; i++) {
                    if (i < operationList.size()) {
                        command = operationList.get(i) + " " + (++commandCounter) + " " + (i + 1) + "\n";

                        if (j % 2 == 0) {
                            sendToClient(scanner1, clientFormatter1, command, 1);

                        } else {
                            sendToClient(scanner2, clientFormatter2, command, 2);
                        }
                    } else
                        break outer;
                }
                Thread.sleep(5000);
                j++;
            }
            System.exit(0);

        } catch (IOException |
                InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void sendToClient(Scanner scanner, Formatter clientFormatter, String command, int clientNumber) {
        clientFormatter.format(command);
        clientFormatter.flush();
        System.out.println("command " + commandCounter + " sent to client" + clientNumber);
        String clientMessage = scanner.nextLine();
        clientFormatter.format("ok\n");
        System.out.println(clientMessage + " received");
    }
}