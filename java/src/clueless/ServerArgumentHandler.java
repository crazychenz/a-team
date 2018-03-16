package clueless;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerArgumentHandler {

    private static final Logger logger = LogManager.getLogger(ServerArgumentHandler.class);

    private HashMap<String, Boolean> validArguments;

    public ServerArgumentHandler(String[] args, CluelessServerSocket serverSocket)
            throws Exception {
        // Valid arguments
        this.validArguments = new HashMap<>();
        this.validArguments.put("--port", true);
        this.validArguments.put("--backlog", true);
        this.validArguments.put("--address", true);

        // Note: This could easily be expanded to non-parameterized
        //       arguments with a second Hashtable.

        String lastArg = null;
        for (String arg : args) {
            if (lastArg != null) {
                switch (lastArg) {
                    case "--port":
                        try {
                            int port = Integer.parseInt(arg);
                            serverSocket.setPort(port);
                            System.out.format("Setting port to: %d\n", port);
                        } catch (NumberFormatException e) {
                            System.out.format("Failed to parse port: %s\n", arg);
                            throw e;
                        }
                        lastArg = null;
                        continue;
                    case "--backlog":
                        try {
                            int backlog = Integer.parseInt(arg);
                            serverSocket.setBacklog(backlog);
                            System.out.format("Setting backlog to: %d\n", backlog);
                        } catch (NumberFormatException e) {
                            System.out.format("Failed to parse backlog: %s\n", arg);
                            throw e;
                        }
                        lastArg = null;
                        continue;
                    case "--address":
                        InetAddress bindAddr;
                        try {
                            bindAddr = InetAddress.getByName(arg);
                        } catch (UnknownHostException e) {
                            System.out.format("Failed to parse address: %s\n", arg);
                            throw e;
                        }
                        serverSocket.setBindAddr(bindAddr);
                        System.out.format("Setting address to: %s\n", arg);
                        lastArg = null;
                    default:
                        break;
                }
            } else if (this.validArguments.containsKey(arg)) {
                lastArg = arg;
            }
        }
    }
}
