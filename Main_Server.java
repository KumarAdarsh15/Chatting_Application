public class Main_Server {
    public static void main(String[] args) {
        Server s = new Server();    //invoke GUI part
        s.waitingForClient();       //wait's for client
        s.setIOStreams();           //set's streams through which data will be transferred
    }
}
