package clueless;

import java.nio.ByteBuffer;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Poller;
import org.zeromq.ZMQ.Socket;

public class Client implements Runnable {
    
    private static final Logger logger =
        LogManager.getLogger(Client.class);

    Context zmqContext;
    Socket socket;
    //Socket chatSocket;
    UUID uuid;
    ConcurrentLinkedQueue<Message> chatMessageQueue;
    
    public Client()
    {
        uuid = UUID.randomUUID();
        // Grab a context object with one I/O thread.
        zmqContext = ZMQ.context(1);
        socket = zmqContext.socket(ZMQ.DEALER);
        socket.setIdentity(uuid.toString().getBytes());
        //chatSocket.setIdentity(("chat-" + uuid).getBytes());
        chatMessageQueue = new ConcurrentLinkedQueue<Message>();
    }
    
    public void connect() throws Exception
    {
        socket.connect("tcp://localhost:2323");
        //chatSocket.connect("tcp://localhost:2323");
        return;
    }
    
    public void sendMessage(Message msg) throws Exception
    {
        ByteBuffer buf;
        try {
            buf = Message.toBuffer(msg);
        } catch (Exception e) {
            logger.error("Failed to sendMessage.");
            throw e;
        }
        socket.sendMore("");
        socket.send(buf.array());
    }
    
    public void sendClientConnect() throws Exception {
        Message msg;
        logger.debug("Connecting to Game");
        msg = new Message(MessagesEnum.MESSAGE_CLIENT_CONNECTED, "");
        sendMessage(msg);
    }
        
    // TODO: Add a parameter
    public void sendClientConfig(CardsEnum suspect) throws Exception
    {
        Message msg;
        logger.debug("Configuring client");
        msg = new Message(MessagesEnum.MESSAGE_CLIENT_CONFIG, CardsEnum.SUSPECT_SCARLET);
        sendMessage(msg);
    }
    
    public void sendChatMessage(String chatMessage) throws Exception
    {
        Message msg;
        logger.debug("Sending chat to others");
        msg = new Message(MessagesEnum.MESSAGE_CHAT_FROM_CLIENT, chatMessage);
        sendMessage(msg);
    }
    
    public void run()
    {
        Poller items = zmqContext.poller(1);
        items.register(socket, Poller.POLLIN);
        //items.register(chatSocket, Poller.POLLIN);
        
        //while (!Thread.currentThread().isInterrupted()) {
        while (true) {
            try {
            
                if (items.poll() < 0)
                {
                    return; // Interrupted
                }

                // On gameSocket Event
                if (items.pollin(0)) {
                    
                    
                    // Delimiter frame is empty
                    String empty = socket.recvStr();
                    assert (empty.length() == 0);
                    
                    // Get the payload frame
                    ByteBuffer buf = ByteBuffer.wrap(socket.recv());
                    Message msg = Message.fromBuffer(buf);
                    switch (msg.getMessageID()) {
                        case MESSAGE_CHAT_FROM_SERVER:
     
                            logger.info("chat: " + msg);
                            break;
                        case MESSAGE_SERVER_AVAILABLE_SUSPECTS:
                            AvailableSuspects suspects = (AvailableSuspects)msg.getMessageData();
                            logger.info("Count: " + suspects.list.size());
                            for (CardsEnum suspect: suspects.list) {
                                logger.info(suspect);
                            }  
                            break;
                        default:
                            logger.info("Message: " + msg);
                            break;
                    }
                }

            } catch (Exception e) {
                logger.error(e);
                return;
            }
        }
    }
    
    public void disconnect() throws Exception
    {
        //gameThread.interrupt();
        //chatThread.interrupt();
        // TODO: Should we join here?
        socket.close();
        //chatSocket.close();
        return;
    }

}