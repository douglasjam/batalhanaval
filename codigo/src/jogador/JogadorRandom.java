/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jogador;

import gui.GUIConf;
import gui.GUITabuleiro;
import jogo.Jogo;
import jogo.Tabuleiro;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Douglas J.A.M
 */
public class JogadorRandom extends Jogador {

    String apelido;
    Tabuleiro tabuleiro;

    public JogadorRandom(String apelido, Tabuleiro tabuleiro) {
        GUITabuleiro.getInstance().addLog("Jogador random " + apelido + " criado");
        this.apelido = apelido;
        this.tabuleiro = tabuleiro;
    }

    @Override
    public String fazJogada() {

        String retorno;
        Random random = new Random();
        random.setSeed(new Date().getTime());

        do {

            retorno = String.valueOf(random.nextInt(100));
            if (retorno.length() == 1) {
                retorno = "0" + retorno;
            }
        } while (!GUITabuleiro.getInstance().getValorAt(apelido, retorno).equals("?"));


        try {
            Thread.sleep(Jogo.getInstance().getDelay());
        } catch (InterruptedException ex) {
            Logger.getLogger(JogadorRandom.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        GUITabuleiro.getInstance().addLog("Jogador random " + apelido + " fez jogada " + retorno);
        return retorno;
    }

    @Override
    public String getApelido() {
        return this.apelido;
    }

    @Override
    public String encontrou(String posicao) {
        return super.encontrou(this.apelido, "random", posicao);
    }
}