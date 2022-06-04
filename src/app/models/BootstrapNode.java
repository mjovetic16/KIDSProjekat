package app.models;

public class BootstrapNode {

    private String ipAddress;
    private int listenerPort;


    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getListenerPort() {
        return listenerPort;
    }

    public void setListenerPort(int listenerPort) {
        this.listenerPort = listenerPort;
    }

    @Override
    public String toString() {
        return "BootstrapNode{" +
                "ipAddress='" + ipAddress + '\'' +
                ", listenerPort=" + listenerPort +
                '}';
    }
}
