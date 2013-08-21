/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jogador;

import conexao.Conexao;
import gui.GUITabuleiro;
import jogo.Jogo;

/**
 * @author Douglas J.A.M
 */
public class JogadorRemoto extends Jogador {

    String apelido;

    // sem tabuleiro, o metodo faz jogada puxara do servidor
    public JogadorRemoto(String apelido) {
        this.apelido = apelido;
        GUITabuleiro.getInstance().addLog("Jogador remoto  " + apelido + " criado");
    }

    @Override
    public String fazJogada() {

        String jogada = "";
        String adversario = this.apelido.equals("a") ? "b" : "a";
        //String adversario = this.apelido.equals("a") ? "a" : "b";
        String retornoPeca = "";

        // recebe a jogada do remoto

        Conexao.getInstance().setReady(false);
        while (!Conexao.getInstance().isReady()) {
            jogada = Conexao.getInstance().recebe();
            GUITabuleiro.getInstance().addLog("Recebeu jogada: " + jogada);
            System.out.println("Recebeu jogada: " + jogada);
        }

        // pegamos a peça e respondemos
        retornoPeca = Jogo.getInstance().getTabuleiroB().getPosicao(jogada);

        // envia o retorno
        Conexao.getInstance().setReady(false);
        while (!Conexao.getInstance().isReady()) {
            Conexao.getInstance().envia(retornoPeca);
            GUITabuleiro.getInstance().addLog("Enviou retorno: " + retornoPeca);
            System.out.println("Enviou retorno: " + retornoPeca);
        }
        
        GUITabuleiro.getInstance().addLog("Jogador remoto " + apelido + " fez jogada " + jogada);
        return jogada;
    }

    @Override
    public String getApelido() {
        return this.apelido;
    }

    @Override
    public String encontrou(String posicao) {
        //System.out.println("PEDE POSICAO AO REMOTO: " + posicao);
        return super.encontrou(this.apelido, "remoto", posicao);
    }
}
