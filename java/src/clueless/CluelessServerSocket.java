package clueless;

import java.io.IOException;
import java.net.UnknownHostException;

import java.net.ServerSocket;
import java.net.InetAddress;

public class CluelessServerSocket {
    ServerSocket svcSocket;

    // Default values for ServerSocket options
    private int defSvcPort = 2323;
    private int defSvcBacklog = 6;
    private byte[] defBindAddr = new byte[] { 0x0, 0x0, 0x0, 0x0 };
    
    private int svcPort;
    private int svcBacklog;
    private InetAddress svcBindAddr;
    
    public CluelessServerSocket() throws Exception
    {
        this.svcPort = this.defSvcPort;
        this.svcBacklog = this.defSvcBacklog;
        try {
            this.svcBindAddr = InetAddress.getByAddress(defBindAddr);
        } catch (UnknownHostException e) {
            System.out.println("Couldn't get any inet address.");
            throw e;
        }
    }
    
    public void setPort(int port)
    {
        this.svcPort = port;
    }
    
    public void setBacklog(int backlog)
    {
        this.svcBacklog = backlog;
    }
    
    public void setBindAddr(InetAddress bindAddr)
    {
        this.svcBindAddr = bindAddr;
    }
    
    public ServerSocket getServerSocket() throws Exception
    {
        try {
            this.svcSocket = new ServerSocket(
                this.svcPort, this.svcBacklog, this.svcBindAddr);
        } catch (IOException e) {
            System.out.format("Couldn't open port %d\n", this.svcPort);
            throw e;
        }
        return this.svcSocket;
    }
}