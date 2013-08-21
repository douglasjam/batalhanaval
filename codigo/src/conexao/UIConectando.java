package conexao;

import gui.GUIConf;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

/**
 *
 * @author douglasjam
 */
public class UIConectando extends Thread {

    static UIConectando uiConectando;
    private Boolean ready;
    private JTextArea log;
    private JProgressBar progressBar;
    String tipo;
    Thread movimentaBarra;

    public static UIConectando getInstance() {
        if (uiConectando == null) {
            uiConectando = new UIConectando();
            return uiConectando;
        } else {
            return uiConectando;
        }
    }

    public void setGUI(Boolean ready, JTextArea log, JProgressBar progressBar) {
        this.ready = ready;
        this.log = log;
        this.progressBar = progressBar;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Boolean isReady() {
        return ready;
    }

    @Override
    public void run() {

        if (this.tipo.equals("servidor")) {

            progressBar.setString("Aguardando conexão...");
            movimentaBarra(true);
            Conexao.getInstance().iniciaServidor();


            movimentaBarra(false);
            GUIConf.getInstance().setReady(true);
            GUIConf.getInstance().escreveConfiguracoes();
            this.stop();

        } else {

            progressBar.setString("Conectando ao servidor...");
            movimentaBarra(true);
            Conexao.getInstance().conectaServidor();

            movimentaBarra(false);
            GUIConf.getInstance().setReady(true);
            this.stop();

        }

    }

    public void parar() {
        progressBar.setString("");
        movimentaBarra(false);
        this.stop();
    }

    public void movimentaBarra(Boolean inicia) {
        if (movimentaBarra == null) {

            movimentaBarra = new Thread() {

                @Override
                public void run() {
                    while (true) {
                        for (int i = 0; i < 100; i++) {
                            progressBar.setValue(i);
                            try {
                                Thread.sleep(25);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(UIConectando.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }

                }
            };
        }

        if (inicia) {
            movimentaBarra.start();
        } else {
            progressBar.setValue(0);
            movimentaBarra.stop();
        }
    }
}
