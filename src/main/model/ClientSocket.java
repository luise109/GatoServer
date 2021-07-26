package main.model;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Observable;

public class ClientSocket extends Observable implements Runnable {
    private Socket socket;
    private DataInputStream bufferEntrada = null;

    public ClientSocket(Socket socket){
        this.socket = socket;
    }

    public ClientSocket() {

    }

    @Override
    public void run() {
        String mensaje = "";
        try {
            bufferEntrada = new DataInputStream(socket.getInputStream());
            do {
                mensaje = bufferEntrada.readUTF();
                this.setChanged();
                this.notifyObservers(mensaje);
            } while (mensaje != "exit");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
