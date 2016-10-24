package server;

import exceptions.AuthFailException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

public class ClientHandler implements Runnable {
    private static int clientsCount = 0;
    private Socket socket;
    private Server server;
    private DataOutputStream out;
    private DataInputStream in;
    private String clientName;
    private boolean stopFlag = false;
    private String login;

    public ClientHandler(Socket socket, Server server) {
        try {
            this.socket = socket;
            this.server = server;

            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());

            clientsCount++;
            clientName = "client" + clientsCount;

            System.out.println("Client \"" + clientName + "\" ready!");
        } catch (IOException e) {
        }
    }

    @Override
    public void run() {
        waitForAuth();
        waitForMessage();
    }

    private void waitForMessage() {
        while (true) {
            String message = null;
            try {
                message = in.readUTF();
            } catch (EOFException e) {
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
            if(stopFlag){
                break;
            }
            System.out.println(clientName + ": " + message);
            if (processControlMessage(message)) {
                System.out.println("System command recieved");
            } else {
                new Thread(new MessagesSender(message, clientName, server)).start();
            }

        }
    }

    private void waitForAuth() {
        while (true) {
            String message = null;
            try {
                message = in.readUTF();
            } catch (EOFException e) {
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
            if (isAuthOk(message)) break;
        }
        System.out.println(clientName + " auth ok and is ready for chat!");
    }

    private boolean isAuthOk(String message) {
        System.out.println(clientName + "[NO AUTH]: " + message);

        if (message != null) {
            String[] parsedMessage = message.split("___");
            if (parsedMessage.length == 3) {
                try {
                    processAuthMessage(parsedMessage);
                } catch (AuthFailException e) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    private void processAuthMessage(String[] parsedMessage) throws AuthFailException {
        if (parsedMessage[0].equals("auth")) {
            System.out.println("Auth message from " + clientName);
            String login = parsedMessage[1];
            String password = parsedMessage[2];

            String nick = null;
            try {
                nick = SQLHandler.getNick(login, password);

            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (nick != null) {
                server.addClient(this, nick);
                this.login=login;
                return;
            }
        }

    }


    private boolean processControlMessage(String msg) {
        if (msg != null) {
            String[] parsedMessage = msg.split("___");

            if (parsedMessage[0].equals("changeNick")&&!server.isNickExists(parsedMessage[1])) {
                System.out.println("ChangeNick  message from " + clientName);
                String newNick = parsedMessage[1];
                try {
                    SQLHandler.setNick(this.clientName, newNick);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                System.out.println(this.clientName + " has changed nickname to " + newNick);
                String message = this.clientName + " changed nick to " + newNick;
                this.setNick(newNick);

                return true;
            }



            if (msg.equals("stopServer")) {

                if (stopServer()) {
                    System.out.println("Administrator stopped server");
                    String message = "Administrator stopped server";
                    new Thread(new MessagesSender(message, "[SYSTEM]", server)).start();
                    return true;
                }
            }
        }
        return false;
    }

    public DataOutputStream getOut() {
        return out;
    }

    public String getClientName() {
        return clientName;
    }

    public void setNick(String nick) {
        this.clientName = nick;
    }

    private boolean stopServer() {
        try {

            if (SQLHandler.checkAdminStatus(login)) {

                server.setStopFlag(true);
                return true;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void setStopFlag(boolean stopFlag){
        this.stopFlag=stopFlag;
    }

}
