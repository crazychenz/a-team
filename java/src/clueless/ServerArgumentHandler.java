package clueless;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import clueless.Server;

// required for arg parser
import java.lang.Integer;
import java.lang.NumberFormatException;
import java.lang.Boolean;
import java.util.Hashtable;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerArgumentHandler {

    private static final Logger logger = 
        LogManager.getLogger(ServerArgumentHandler.class);

    private Hashtable<String, Boolean> validArguments;

    public ServerArgumentHandler(
        String[] args, 
        CluelessServerSocket serverSocket) throws Exception
    {
        // Valid arguments
        this.validArguments = new Hashtable<String, Boolean>();
        this.validArguments.put("--port", new Boolean(true));
        this.validArguments.put("--backlog", new Boolean(true));
        this.validArguments.put("--address", new Boolean(true));
        
        // Note: This could easily be expanded to non-parameterized
        //       arguments with a second Hashtable.
        
        String lastArg = null;
        for (String arg: args) {
            if (lastArg != null)
            {
                if (lastArg.equals("--port")) {
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
                }
                else if (lastArg.equals("--backlog")) {
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
                }
                else if (lastArg.equals("--address")) {
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
                    continue;
                }
            }
            else if (this.validArguments.containsKey(arg)) {
                lastArg = arg;
                continue;
            }
        }
    }
}