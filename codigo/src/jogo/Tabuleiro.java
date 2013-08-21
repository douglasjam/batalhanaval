/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jogo;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author douglasjam
 */
public class Tabuleiro {

    String arquivo;
    private String[][] tabuleiro;
    private final static int TAMANHO = 10;
    private final static int PECASTOTOTAL = (3 * 3) + (3 * 2) + (3 * 1) + (2 * 4) + (1 * 5);

    public Tabuleiro() {
        this.tabuleiro = new String[TAMANHO][TAMANHO];
        preencheTabuleiro("?");
    }

    public Tabuleiro(String arquivo) {
        this.arquivo = arquivo;
        this.tabuleiro = new String[TAMANHO][TAMANHO];
        carregaTabuleiro();
    }

    public boolean carregaTabuleiro() {

        BufferedReader in;
        String tipo;
        String[] temp;

        preencheTabuleiro("a");

        try {
            in = new BufferedReader(new FileReader(arquivo));
            while (in.ready()) {

                temp = in.readLine().split(" ");
                tipo = temp[0];

                for (int i = 1; i < temp.length; i++) {
                    tabuleiro[Integer.parseInt(temp[i].substring(0, 1))][Integer.parseInt(temp[i].substring(1, 2))] = tipo;
                }
            }
            in.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Tabuleiro.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Excessão gerada: Arquivo não encontrado. " + ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(Tabuleiro.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Excessão gerada: Erro na leitura do arquivo. " + ex.getMessage());
        }

        return false;
    }

    public void preencheTabuleiro(String valor) {
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                tabuleiro[i][j] = valor;
            }
        }
    }

    public boolean verificaFim() {
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                if ((!tabuleiro[i][j].equals("a")) && (!tabuleiro[i][j].equals("x"))) {
                    return false;
                }
            }
        }
        return true;
    }

    public void imprimeTabuleiro() {
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                System.out.print(tabuleiro[i][j] + " ");
            }
            System.out.println(i + " ");
        }
    }

    public void setPosicao(int linha, int coluna, String valor) {
        tabuleiro[linha][coluna] = valor;
    }

    public void setPosicao(String posicao, String valor) {
        Integer linha = Integer.valueOf(posicao.substring(0, 1));
        Integer coluna = Integer.valueOf(posicao.substring(1, 2));
        tabuleiro[linha][coluna] = valor;
    }

    public String getPosicao(int linha, int coluna) {
        return tabuleiro[linha][coluna];
    }

    public String getPosicao(String posicao) {
        //System.out.println("acessou tabuleiro, posicao " + posicao);
        String retorno = "";
        Integer linha = Integer.valueOf(posicao.substring(0, 1));
        Integer coluna = Integer.valueOf(posicao.substring(1, 2));
        retorno = tabuleiro[linha][coluna];
        return retorno;
    }

    public Integer getPecasRestantes() {

        Integer pecas = 0;

        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                String peca = tabuleiro[i][j];
                if (pecas.equals("c") || pecas.equals("d") || pecas.equals("d") || pecas.equals("e") || pecas.equals("h") || pecas.equals("s")) {
                    pecas++;
                }
            }
        }
        return PECASTOTOTAL - pecas;
    }

    public String getTabuleiroName() {
        if (arquivo == null) {
            return "remoto";
        } else {
            return (new File(arquivo).getName());
        }
    }
}
