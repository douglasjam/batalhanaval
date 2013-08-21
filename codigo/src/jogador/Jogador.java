package jogador;

import conexao.Conexao;
import gui.GUITabuleiro;
import jogo.Jogo;

/**
 * @author douglasjam
 */
public abstract class Jogador {

    public abstract String getApelido();

    public abstract String fazJogada();

    public abstract String encontrou(String posicao);

    public String encontrou(String apelido, String tipo, String posicao) {

        String retorno = "";
        String adversario = apelido.equals("a") ? "b" : "a";

        // se o adversário não for remoto, olhamos local
        if (!(Jogo.getInstance().getJogador(adversario) instanceof JogadorRemoto)) {
            retorno = Jogo.getInstance().getTabuleiro(apelido).getPosicao(posicao);
            GUITabuleiro.getInstance().addLog("Jogador " + tipo + " " + apelido + " encontrou " + retorno);

        } else {

            // se for remoto
            // enviamos a jogada e capturamos o retorno

            Conexao.getInstance().setReady(false);
            while (!Conexao.getInstance().isReady()) {
                GUITabuleiro.getInstance().addLog("Enviou posição: " + posicao);
                System.out.println("Enviou posição: " + posicao);
                Conexao.getInstance().envia(posicao);
            }

            Conexao.getInstance().setReady(false);
            while (!Conexao.getInstance().isReady()) {
                retorno = Conexao.getInstance().recebe();
                GUITabuleiro.getInstance().addLog("Recebeu resposta: " + retorno);
                System.out.println("Recebeu resposta: " + retorno);
            }

            // atualiza ultima jogada da inteligencia quando remoto
            if (Jogo.getInstance().getJogadorA() instanceof JogadorInteligencia) {
                ((JogadorInteligencia) Jogo.getInstance().getJogadorA()).setUltimoTiro(retorno);
            }
        }
        return retorno;

    }
}
