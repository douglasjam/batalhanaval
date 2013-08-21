package testes;

import inteligencia.Inteligencia;
import jogo.Tabuleiro;

public class Main {

    public static void main(String[] args) {
        String retorno;
        String retornoAlvo = null;
        Tabuleiro tabuleiro = new Tabuleiro("2A.txt");
        Tabuleiro tabtemp = new Tabuleiro();
        
        tabtemp.preencheTabuleiro("x");
        tabuleiro.imprimeTabuleiro();
        Inteligencia inte = new Inteligencia();
        retornoAlvo = tabuleiro.getPosicao(inte.atira("i"));
        tabtemp.setPosicao(0, 0, "a"); 
        for (int i =1; i<100; i++){
            retorno = inte.atira(retornoAlvo);
            if (!retorno.equals("v")){ 
                System.out.println(retorno);
                retornoAlvo = tabuleiro.getPosicao(retorno);
                tabtemp.setPosicao(Integer.parseInt(retorno.substring(0, 1)), Integer.parseInt(retorno.substring(1, 2)), retornoAlvo);
            }else
                break;
        }
        //tabtemp.imprimeTabuleiro();
        inte.imprimeTabuleiro();
     
    }
}