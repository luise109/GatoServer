package main.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import main.Main;
import main.model.ClientSocket;
import main.model.Nodo;
import main.model.Server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class ControllerRoot implements Observer {
    ServerSocket serverSocket = null;
    private final int PORT = 3001;
    private ArrayList<Nodo> poolSocket = new ArrayList<>();
    private boolean turno = true;

    @FXML
    private Button arribaIzquierda;

    @FXML
    private Button arriba;

    @FXML
    private Button arribaDerecha;

    @FXML
    private Button izquierda;

    @FXML
    private Button centro;

    @FXML
    private Button derecha;

    @FXML
    private Button abajoIzquierda;

    @FXML
    private Button abajo;

    @FXML
    private Button abajoDerecha;

    int[][] guardarMovimientos = new int[3][3];

    @FXML
    private Label victorias;

    @FXML
    private Label mensaje;

    private int contadorVictorias = 0;

    public void iniciarConexion() {
        //189.198.170.244
        byte[] ipBytes = {(byte) 192, (byte) 168, (byte) 0, (byte) 8};
        mensaje.setText("Esperando Rival");
        InetAddress ip = null;

        try {
            ip = InetAddress.getByAddress(ipBytes);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        try {
            serverSocket = new ServerSocket(PORT, 100, ip);

            Server server = new Server(serverSocket);
            server.addObserver(this);
            new Thread(server).start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @FXML
    private void initialize() {
        iniciarConexion();
    }

    @Override
    public void update(Observable o, Object arg) {

        if(o instanceof Server)
        {
            Socket socket = (Socket) arg;
            Nodo nodo = new Nodo("Jugador 2", socket);
            poolSocket.add(nodo);
            Platform.runLater(() ->mensaje.setText("Se encontro un jugador Iniciando partida..."));
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ClientSocket clientSocket = new ClientSocket(socket);
            clientSocket.addObserver(this);
            new Thread(clientSocket).start();
            Platform.runLater(() ->mensaje.setText("Jugador 1\nInician las O\nTu jugas con: O"));
        }
        if(o instanceof ClientSocket)
        {
            if(Integer.parseInt((String) arg) == 0)
            {
                Platform.runLater(()->reiniciar());
            }
            else if(Integer.parseInt((String) arg) == -1)
            {
                Platform.runLater(()->alerta("empate"));
                Platform.runLater(()->reiniciar());
            }
            if(espacioVacio(Integer.parseInt((String) arg)))
            {
                switch (Integer.parseInt((String) arg))
                {
                    case 1:
                        Platform.runLater(() -> arribaIzquierda());
                        break;
                    case 2:
                        Platform.runLater(() ->arriba());
                        break;
                    case 3:
                        Platform.runLater(() ->arribaDerecha());
                        break;
                    case 4:
                        Platform.runLater(() ->izquierda());
                        break;
                    case 5:
                        Platform.runLater(() ->centro());
                        break;
                    case 6:
                        Platform.runLater(() ->derecha());
                        break;
                    case 7:
                        Platform.runLater(() ->abajoIzquierda());
                        break;
                    case 8:
                        Platform.runLater(()->abajo());
                        break;
                    case 9:
                        Platform.runLater(() ->abajoDerecha());
                        break;
                }
            }
        }

    }

    public void reiniciar() {
        guardarMovimientos = new int[3][3];
        arribaIzquierda.setText(null);
        arriba.setText(null);
        arribaDerecha.setText(null);
        izquierda.setText(null);
        centro.setText(null);
        derecha.setText(null);
        abajoIzquierda.setText(null);
        abajo.setText(null);
        abajoDerecha.setText(null);
        arribaIzquierda.setStyle("-fx-background-color: #ffffff");
        arriba.setStyle("-fx-background-color: #ffffff");
        arribaDerecha.setStyle("-fx-background-color: #ffffff");
        izquierda.setStyle("-fx-background-color: #ffffff");
        centro.setStyle("-fx-background-color: #ffffff");
        derecha.setStyle("-fx-background-color: #ffffff");
        abajoIzquierda.setStyle("-fx-background-color: #ffffff");
        abajo.setStyle("-fx-background-color: #ffffff");
        abajoDerecha.setStyle("-fx-background-color: #ffffff");
        if (turno) {
            mensaje.setText("Jugador 1\nJugas con O\nInician las O");
        } else {
            mensaje.setText("Jugador 1\nJugas con O\nInician las X");
        }
    }

    public boolean espacioVacio(int posicion) {
        switch (posicion) {
            case 1:
                if (guardarMovimientos[0][0] == 1 || guardarMovimientos[0][0] == 2) {
                    return false;
                }
                break;
            case 2:
                if (guardarMovimientos[0][1] == 1 || guardarMovimientos[0][1] == 2) {
                    return false;
                }
                break;
            case 3:
                if (guardarMovimientos[0][2] == 1 || guardarMovimientos[0][2] == 2) {
                    return false;
                }
                break;
            case 4:
                if (guardarMovimientos[1][0] == 1 || guardarMovimientos[1][0] == 2) {
                    return false;
                }
                break;
            case 5:
                if (guardarMovimientos[1][1] == 1 || guardarMovimientos[1][1] == 2) {
                    return false;
                }
                break;
            case 6:
                if (guardarMovimientos[1][2] == 1 || guardarMovimientos[1][2] == 2) {
                    return false;
                }
                break;
            case 7:
                if (guardarMovimientos[2][0] == 1 || guardarMovimientos[2][0] == 2) {
                    return false;
                }
                break;
            case 8:
                if (guardarMovimientos[2][1] == 1 || guardarMovimientos[2][1] == 2) {
                    return false;
                }
                break;
            case 9:
                if (guardarMovimientos[2][2] == 1 || guardarMovimientos[2][2] == 2) {
                    return false;
                }
                break;
        }

        return true;
    }

    public boolean comprobarTiro() {
        if (turno == false) {
            return false;
        }
        return true;
    }


    @FXML
    void salir(ActionEvent event) {
        broadCast(-1);
        System.exit(0);
    }

    public boolean comprobarVictoria()
    {
        String status = "victoria";
        if(guardarMovimientos[0][0] == 1 && guardarMovimientos[0][1] == 1 && guardarMovimientos[0][2] == 1)
        {
            victorias.setText(String.valueOf(contadorVictorias += 1));
            alerta(status);
            reiniciar();
            return true;
        }
        if(guardarMovimientos[1][0] == 1 && guardarMovimientos[1][1] == 1 && guardarMovimientos[1][2] == 1)
        {
            victorias.setText(String.valueOf(contadorVictorias += 1));
            alerta(status);
            reiniciar();
            return true;
        }
        if(guardarMovimientos[2][0] == 1 && guardarMovimientos[2][1] == 1 && guardarMovimientos[2][2] == 1)
        {
            victorias.setText(String.valueOf(contadorVictorias += 1));
            alerta(status);
            reiniciar();
            return true;
        }
        if(guardarMovimientos[0][0] == 1 && guardarMovimientos[0][1] == 1 && guardarMovimientos[0][2] == 1)
        {
            victorias.setText(String.valueOf(contadorVictorias += 1));
            alerta(status);
            reiniciar();
            return true;
        }
        if(guardarMovimientos[0][0] == 1 && guardarMovimientos[1][0] == 1 && guardarMovimientos[2][0] == 1)
        {
            victorias.setText(String.valueOf(contadorVictorias += 1));
            alerta(status);
            reiniciar();
            return true;
        }
        if(guardarMovimientos[0][1] == 1 && guardarMovimientos[1][1] == 1 && guardarMovimientos[2][1] == 1)
        {
            victorias.setText(String.valueOf(contadorVictorias += 1));
            alerta(status);
            reiniciar();
            return true;
        }
        if(guardarMovimientos[0][2] == 1 && guardarMovimientos[1][2] == 1 && guardarMovimientos[2][2] == 1)
        {
            victorias.setText(String.valueOf(contadorVictorias += 1));
            alerta(status);
            reiniciar();
            return true;
        }
        if(guardarMovimientos[0][0] == 1 && guardarMovimientos[1][1] == 1 && guardarMovimientos[2][2] == 1)
        {
            victorias.setText(String.valueOf(contadorVictorias += 1));
            alerta(status);
            reiniciar();
            return true;
        }
        if(guardarMovimientos[2][0] == 1 && guardarMovimientos[1][1] == 1 && guardarMovimientos[0][2] == 1)
        {
            victorias.setText(String.valueOf(contadorVictorias += 1));
            alerta(status);
            reiniciar();
            return true;
        }

        return false;
    }


    public boolean comprobarDerrota()
    {
        String status = "derrota";
        if(guardarMovimientos[0][0] == 2 && guardarMovimientos[0][1] == 2 && guardarMovimientos[0][2] == 2)
        {
            reiniciar();
            alerta(status);
            return true;
        }
        if(guardarMovimientos[1][0] == 2 && guardarMovimientos[1][1] == 2 && guardarMovimientos[1][2] == 2)
        {
            reiniciar();
            alerta(status);
            return true;
        }
        if(guardarMovimientos[2][0] == 2 && guardarMovimientos[2][1] == 2 && guardarMovimientos[2][2] == 2)
        {
            reiniciar();
            alerta(status);
            return true;
        }
        if(guardarMovimientos[0][0] == 2 && guardarMovimientos[0][1] == 2 && guardarMovimientos[0][2] == 2)
        {
            reiniciar();
            alerta(status);
            return true;
        }
        if(guardarMovimientos[0][0] == 2 && guardarMovimientos[1][0] == 2 && guardarMovimientos[2][0] == 2)
        {
            reiniciar();
            alerta(status);
            return true;
        }
        if(guardarMovimientos[0][1] == 2 && guardarMovimientos[1][1] == 2 && guardarMovimientos[2][1] == 2)
        {
            reiniciar();
            alerta(status);
            return true;
        }
        if(guardarMovimientos[0][2] == 2 && guardarMovimientos[1][2] == 2 && guardarMovimientos[2][2] == 2)
        {
            reiniciar();
            alerta(status);
            return true;
        }
        if(guardarMovimientos[0][0] == 2 && guardarMovimientos[1][1] == 2 && guardarMovimientos[2][2] == 2)
        {
            reiniciar();
            alerta(status);
            return true;
        }
        if(guardarMovimientos[2][0] == 2 && guardarMovimientos[1][1] == 2 && guardarMovimientos[0][2] == 2)
        {
            reiniciar();
            alerta(status);
            return true;
        }

        return false;
    }

    public void alerta(String status)
    {
        Main.popUp(status,"Game Over");
    }


    //Local
    @FXML
    void abajo(ActionEvent event) {
        if (comprobarTiro()) {

            broadCast(8);
            if (espacioVacio(8)) {
                abajo.setText("O");
                abajo.setStyle("-fx-background-color: #ff3333");
                guardarMovimientos[2][1] = 1;
                comprobarVictoria();
                turno = false;
            }
        } else {
            Main.popUp("mensaje", "mensaje");
        }
    }

    @FXML
    void abajoDerecha(ActionEvent event) {
        if(comprobarTiro())
        {
            broadCast(9);
            if(espacioVacio(9))
            {
                abajoDerecha.setText("O");
                abajoDerecha.setStyle("-fx-background-color: #ff3333");
                guardarMovimientos[2][2] = 1;
                comprobarVictoria();
                turno = false;
            }

        }
        else
        {
            Main.popUp("mensaje","mensaje");
        }


    }

    @FXML
    void abajoIzquierda(ActionEvent event) {
        if(comprobarTiro())
        {
            broadCast(7);
            if(espacioVacio(7))
            {
                abajoIzquierda.setText("O");
                abajoIzquierda.setStyle("-fx-background-color: #ff3333");
                guardarMovimientos[2][0] = 1;
                comprobarVictoria();
                turno = false;
            }

        }
        else
        {
            Main.popUp("mensaje","mensaje");
        }


    }

    @FXML
    void arriba(ActionEvent event) {
        if(comprobarTiro())
        {
            broadCast(2);
            if(espacioVacio(2))
            {
                arriba.setText("O");
                arriba.setStyle("-fx-background-color: #ff3333");
                guardarMovimientos[0][1] = 1;
                comprobarVictoria();
                turno = false;
            }
        }
        else
        {
            Main.popUp("mensaje","mensaje");
        }


    }

    @FXML
    void arribaDerecha(ActionEvent event) {
        if(comprobarTiro())
        {
            broadCast(3);
            if(espacioVacio(3))
            {
                arribaDerecha.setText("O");
                arribaDerecha.setStyle("-fx-background-color: #ff3333");
                guardarMovimientos[0][2] = 1;
                comprobarVictoria();
                turno = false;
            }
        }
        else
        {
            Main.popUp("mensaje","mensaje");
        }


    }

    @FXML
    void arribaIzquierda(ActionEvent event) {
        if(comprobarTiro())
        {
            broadCast(1);
            if(espacioVacio(1))
            {
                arribaIzquierda.setText("O");
                arribaIzquierda.setStyle("-fx-background-color: #ff3333");
                guardarMovimientos[0][0] = 1;
                comprobarVictoria();
                turno = false;
            }
        }
        else
        {
            Main.popUp("mensaje","mensaje");
        }


    }

    @FXML
    void centro(ActionEvent event) {
        if(comprobarTiro())
        {
            broadCast(5);
            if(espacioVacio(5))
            {
                centro.setText("O");
                centro.setStyle("-fx-background-color: #ff3333");
                guardarMovimientos[1][1] = 1;
                comprobarVictoria();
                turno = false;
            }
        }
        else
        {
            Main.popUp("mensaje","mensaje");
        }


    }

    @FXML
    void derecha(ActionEvent event) {
        if(comprobarTiro())
        {
            broadCast(6);
            if(espacioVacio(6))
            {
                derecha.setText("O");
                derecha.setStyle("-fx-background-color: #ff3333");
                guardarMovimientos[1][2] = 1;
                comprobarVictoria();
                turno = false;
            }
        }
        else
        {
            Main.popUp("mensaje","mensaje");
        }


    }

    @FXML
    void izquierda(ActionEvent event) {
        if(comprobarTiro())
        {
            broadCast(4);
            if(espacioVacio(4))
            {
                izquierda.setText("O");
                izquierda.setStyle("-fx-background-color: #ff3333");
                guardarMovimientos[1][0] = 1;
                comprobarVictoria();
                turno = false;
            }
        }
        else
        {
            Main.popUp("mensaje","mensaje");
        }


    }

    private void broadCast(int pos){
        DataOutputStream bufferDeSalida = null;
        Nodo nodo = poolSocket.get(0);
            try {
                bufferDeSalida = new DataOutputStream(nodo.getSocket().getOutputStream());
                bufferDeSalida.flush();
                bufferDeSalida.writeUTF(String.valueOf(pos));
            } catch (IOException e) {
                e.printStackTrace();
            }
    }


    //Externo
    @FXML
    void abajo() {
        if(!comprobarTiro())
        {
            if(espacioVacio(8))
            {
                abajo.setText("X");
                abajo.setStyle("-fx-background-color: #0066ff");
                guardarMovimientos[2][1] = 2;
                comprobarDerrota();
                turno = true;
            }
        }


    }

    @FXML
    void abajoDerecha() {
        if(!comprobarTiro())
        {
            if(espacioVacio(9))
            {
                abajoDerecha.setText("X");
                abajoDerecha.setStyle("-fx-background-color: #0066ff");
                guardarMovimientos[2][2] = 2;
                comprobarDerrota();
                turno = true;
            }

        }

    }

    @FXML
    void abajoIzquierda() {
        if(!comprobarTiro())
        {

            if(espacioVacio(7))
            {
                abajoIzquierda.setText("X");
                abajoIzquierda.setStyle("-fx-background-color: #0066ff");
                guardarMovimientos[2][0] = 2;
                comprobarDerrota();
                turno = true;
            }

        }

    }

    @FXML
    void arriba() {
        if(!comprobarTiro())
        {

            if(espacioVacio(2))
            {
                arriba.setText("X");
                arriba.setStyle("-fx-background-color: #0066ff");
                guardarMovimientos[0][1] = 2;
                comprobarDerrota();
                turno = true;
            }

        }

    }

    @FXML
    void arribaDerecha() {
        if(!comprobarTiro())
        {

            if(espacioVacio(3))
            {
                arribaDerecha.setText("X");
                arribaDerecha.setStyle("-fx-background-color: #0066ff");
                guardarMovimientos[0][2] = 2;
                comprobarDerrota();
                turno = true;
            }

        }

    }

    @FXML
    void arribaIzquierda() {
        if(!comprobarTiro())
        {

            if(espacioVacio(1))
            {
                arribaIzquierda.setText("X");
                arribaIzquierda.setStyle("-fx-background-color: #0066ff");
                guardarMovimientos[0][0] = 2;
                comprobarDerrota();
                turno = true;
            }

        }

    }

    @FXML
    void centro() {
        if(!comprobarTiro())
        {

            if(espacioVacio(5))
            {
                centro.setText("X");
                centro.setStyle("-fx-background-color: #0066ff");
                guardarMovimientos[1][1] = 2;
                comprobarDerrota();
                turno = true;
            }

        }

    }

    @FXML
    void derecha() {
        if(!comprobarTiro())
        {

            if(espacioVacio(6))
            {
                derecha.setText("X");
                derecha.setStyle("-fx-background-color: #0066ff");
                guardarMovimientos[1][2] = 2;
                comprobarDerrota();
                turno = true;
            }

        }

    }

    @FXML
    void izquierda() {
        if(!comprobarTiro())
        {

            if(espacioVacio(4))
            {
                izquierda.setText("X");
                izquierda.setStyle("-fx-background-color: #0066ff");
                guardarMovimientos[1][0] = 2;
                comprobarDerrota();
                turno = true;
            }

        }

    }

}

