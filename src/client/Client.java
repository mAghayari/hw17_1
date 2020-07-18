package client;

import org.apache.log4j.Logger;
import util.Utility;

import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.*;

public class Client {
    public static final Logger LOGGER = Logger.getLogger(Client.class);

    public static void process(int portNumber, int clientNumber) {
        LOGGER.info("socket client" + clientNumber + " starting");
        try (Socket socket = new Socket("localhost", portNumber);
             Scanner scanner = new Scanner(socket.getInputStream());
             FileWriter result = new FileWriter("result.txt", true);
             FileWriter exceptions = new FileWriter("exceptions.txt", true);
             Formatter socketOut = new Formatter(socket.getOutputStream())) {

            while (scanner.hasNextLine()) {
                String command = scanner.nextLine();
                if (!Objects.equals(command, "ok")) {
                    List<String> operationContent = getOperationContent(command);
                    String date = Utility.getDate(new Date());
                    if (validateOperation(operationContent)) {
                        String operator = operationContent.get(0);
                        double operand1 = Double.parseDouble(operationContent.get(1));
                        double operand2 = Double.parseDouble(operationContent.get(2));
                        double operationResult = resultCalculator(operator, operand1, operand2);
                        result.write("command number " + operationContent.get(3) +
                                ", By client" + clientNumber + ", on date " + date +
                                ", with commandLine number " + operationContent.get(4) +
                                ", has result " + operationResult + "\n");
                        socketOut.format("result.txt\n");
                        socketOut.flush();
                        System.out.println("command Number " + operationContent.get(3) + " done");
                    } else {
                        System.out.println("failed command: " + operationContent.toString());
                        exceptions.write("Incorrect command: {" + operationContent.toString() +
                                ", on date " + date + "}\n");
                        socketOut.format("exceptions.txt\n");
                        socketOut.flush();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static double resultCalculator(String operator, double operand1, double operand2) {
        double result = 0;
        switch (operator) {
            case "+":
                result = operand1 + operand2;
                break;
            case "-":
                result = operand1 - operand2;
                break;
            case "*":
                result = operand1 * operand2;
                break;
            case "/":
                result = operand1 / operand2;
                break;
        }
        return result;
    }

    public static boolean validateOperation(List<String> operation) {
        if (operation.size() != 5)
            return false;
        else {
            String operator = operation.get(0);
            String operand1 = operation.get(1);
            String operand2 = operation.get(2);

            if (Objects.equals(operator, "/") && operand2.matches("[-+]?([0]*\\.[0]*|[0])"))
                return false;
            else
                return List.of("+", "-", "*", "/").contains(operator) &&
                        operand1.matches("[-+]?([0-9]*\\.[0-9]+|[0-9]+)") &&
                        operand2.matches("[-+]?([0-9]*\\.[0-9]+|[0-9]+)");
        }
    }

    public static List<String> getOperationContent(String operationString) {
        return Arrays.asList(operationString.split(" "));
    }
}