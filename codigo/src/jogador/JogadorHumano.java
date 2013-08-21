package jogador;

import gui.GUITabuleiro;
import jogo.Jogo;
import jogo.Tabuleiro;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Douglas J.A.M
 */
public class JogadorHumano extends Jogador {

    String apelido;
    Tabuleiro tabuleiro;
    JPanel tabPlayer;
    static String retornoClique;

    public JogadorHumano(String apelido, Tabuleiro tabuleiro, JPanel tabPlayer) {
        GUITabuleiro.getInstance().addLog("Jogador humano " + apelido + " criado");
        this.apelido = apelido;
        this.tabuleiro = tabuleiro;
        this.tabPlayer = tabPlayer;
        this.retornoClique = "";
    }

    @Override
    public String fazJogada() {

        String retorno = "";

        // TODO: Não aceitar cliques em objetos ja descobertos
        MouseAdapter capturaClique = new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {

                Component componenteClicado = tabPlayer.getComponentAt(e.getPoint());

                if (componenteClicado.getClass().equals(JLabel.class)) {

                    JLabel jLabelClicado = (JLabel) tabPlayer.getComponentAt(e.getPoint());
                    Icon campoIcon = new ImageIcon(getClass().getResource("/imagens/campo.png"));

                    // se o campo já foi descoberto ignora clique
                    if ((jLabelClicado.getIcon().toString().equals(campoIcon.toString()))) {
                        JogadorHumano.retornoClique = jLabelClicado.getName().substring(1, 3);
                    }
                }

            }
        };


        tabPlayer.addMouseListener(capturaClique);
        // aguarda o cara clicar para continuar
        while (this.retornoClique.isEmpty()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(JogadorHumano.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        // o cara clicou, zeramos a variavel e removemos o listener
        tabPlayer.removeMouseListener(capturaClique);
        retorno = JogadorHumano.retornoClique;
        JogadorHumano.retornoClique = "";
        GUITabuleiro.getInstance().addLog("Jogador humano " + apelido + " fez jogada " + retorno);
        return retorno;
    }

    @Override
    public String getApelido() {
        return this.apelido;
    }

    @Override
    public String encontrou(String posicao) {
        return super.encontrou(this.apelido, "humano", posicao);
    }
}
