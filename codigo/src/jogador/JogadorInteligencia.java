/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jogador;

import gui.GUITabuleiro;
import inteligencia.BKPInteligencia;
import inteligencia.Inteligencia;
import jogo.Jogo;
import jogo.Tabuleiro;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Douglas J.A.M
 */
public class JogadorInteligencia extends Jogador {

    String apelido;
    Tabuleiro tabuleiro;
    //BKPInteligencia inteligencia;
    Inteligencia inteligencia;
    public String ultimoEncontrado;

    public JogadorInteligencia(String apelido, Tabuleiro tabuleiro) {
        GUITabuleiro.getInstance().addLog("Jogador inteligencia " + apelido + " criado");
        this.apelido = apelido;
        this.tabuleiro = tabuleiro;
        inteligencia = new Inteligencia();
        this.ultimoEncontrado = "i";
    }

    @Override
    public String fazJogada() {
        String retorno = inteligencia.atira(ultimoEncontrado);

        if (!(Jogo.getInstance().getJogadorB() instanceof JogadorRemoto)) {
            ultimoEncontrado = encontrou(retorno);
        }

        try {
            Thread.sleep(Jogo.getInstance().getDelay());
        } catch (InterruptedException ex) {
            Logger.getLogger(JogadorInteligencia.class.getName()).log(Level.SEVERE, null, ex);
        }
        GUITabuleiro.getInstance().addLog("Jogador inteligencia \"" + apelido + "\" fez jogada " + retorno);
        return retorno;
    }

    @Override
    public String getApelido() {
        return this.apelido;
    }

    @Override
    public String encontrou(String posicao) {
        return super.encontrou(this.apelido, "inteligencia", posicao);
    }
    
    public void setUltimoTiro(String ultimoTiro){
        this.ultimoEncontrado = ultimoTiro;
    }
}