package clueless;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class ClientState {
	
	public AvailableSuspects availableSuspects;
	public boolean configured = false;
	public boolean gameStarted = false;
	
    private static final Logger logger
            = LogManager.getLogger(CLIEventHandler.class);
    
    ClientState() {
    }
}