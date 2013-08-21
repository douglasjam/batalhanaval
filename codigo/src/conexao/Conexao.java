package conexao;

import gui.GUIConf;
import gui.GUITabuleiro;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author douglasjam
 */
public class Conexao {

    static Conexao conexao;
    private Socket socket;
    private DataInputStream entrada;
    private DataOutputStream saida;
    private Boolean ready;
    private String tipo;
    private String host;
    private Integer porta;

    public static Conexao getInstance() {
        if (conexao == null) {
            conexao = new Conexao();
            conexao.setReady(false);
            return conexao;
        } else {
            return conexao;
        }
    }

    public void setDados(String tipo, String host, Integer porta) {
        this.tipo = tipo;
        this.host = host;
        this.porta = porta;
    }

    public void conectaServidor() {
        try {
            socket = new Socket(this.host, this.porta);
            this.entrada = new DataInputStream(this.socket.getInputStream());
            this.saida = new DataOutputStream(this.socket.getOutputStream());
            this.ready = true;
            System.out.println("Cliente iniciado");
            GUIConf.getInstance().escreveConfiguracoes();
            GUITabuleiro.getInstance().addLog("Cliente iniciado");
        } catch (UnknownHostException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void iniciaServidor() {
        try {
            socket = new ServerSocket(porta).accept();
            this.entrada = new DataInputStream(this.socket.getInputStream());
            this.saida = new DataOutputStream(this.socket.getOutputStream());
            this.ready = true;
            System.out.println("Servidor iniciado");
            GUITabuleiro.getInstance().addLog("Servidor iniciado");
        } catch (IOException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String recebe() {

        String mensagem = "";

        try {
            System.out.println("Aguardando resposta");
            GUITabuleiro.getInstance().addLog("Aguardando resposta");
            mensagem = new DataInputStream(this.socket.getInputStream()).readUTF();
        } catch (IOException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.ready = true;
        return mensagem;


    }

    public void envia(String mensagem) {

        try {

            new DataOutputStream(this.socket.getOutputStream()).writeUTF(mensagem);
            //this.saida.flush();

        } catch (IOException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.ready = true;
    }

    public void setReady(Boolean isReady) {
        this.ready = isReady;
    }

    public Boolean isReady() {
        return ready;
    }
}
