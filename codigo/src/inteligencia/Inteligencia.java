/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inteligencia;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Leandro e Douglas
 */
public class Inteligencia {

    private final static int TAMANHO = 10;
    private String[][] tabuleiroAdversario;
    private List<String> historicoTiros = new ArrayList<String>();
    private List<String> embarcacoesDestruidas = new ArrayList<String>();
    private int ultimaLinha;
    private int ultimaColuna;
    private String embarcacaoAcertada = null;
    private int pecasAcertadas;
    private final static int PECASTOTOTAL = (3 * 3) + (3 * 2) + (3 * 1) + (2 * 4) + (1 * 5);
    private String ultimoTiro;

    public Inteligencia() {
        tabuleiroAdversario = new String[TAMANHO][TAMANHO];
        preencheTabuleiro("x");
    }

    public String atira(String resposta) {

        //System.out.println("inteligencia recebeu " + resposta);
        String proximoTiro = null;

        //Começa jogo
        if (resposta.equals("i")) {
            ultimoTiro = "00";
            return ultimoTiro;
        }

        //Obtém último tiro
        ultimaLinha = Integer.parseInt(ultimoTiro.substring(0, 1)); //Integer.parseInt(historicoTiros.get(historicoTiros.size() - 1).substring(0, 1));
        ultimaColuna = Integer.parseInt(ultimoTiro.substring(1, 2)); //Integer.parseInt(historicoTiros.get(historicoTiros.size() - 1).substring(1, 2));

        //Preenche tabuleiro de controle o alvo do último tiro
        tabuleiroAdversario[ultimaLinha][ultimaColuna] = resposta;

        //Se tiro foi correto acrescenta na lista
        if (!resposta.equals("a")) {
            //System.out.println("tiro " + ultimoTiro);
            historicoTiros.add(ultimoTiro);
        }

        //Obtém última peça acertada no tabuleiro, caso haja alguma incompleta
        if (pecasAcertadas > 0) {
            ultimaLinha = Integer.parseInt(historicoTiros.get(historicoTiros.size() - 1).substring(0, 1));
            ultimaColuna = Integer.parseInt(historicoTiros.get(historicoTiros.size() - 1).substring(1, 2));
        }

        //Chama a função correta após a resposta
        if (resposta.equals("h")) {
            proximoTiro = tiroHidroaviao();
        } else if (resposta.equals("s")) {
            proximoTiro = tiroSubmarino();
        } else if (resposta.equals("d")) {
            proximoTiro = tiroDestroyer();
        } else if (resposta.equals("e")) {
            proximoTiro = tiroEncouracado();
        } else if (resposta.equals("c")) {
            proximoTiro = tiroCruzador();
        } else if (resposta.equals("a")) {
            if (pecasAcertadas == 0) {
                proximoTiro = tiroAgua();
            } else {
                pecasAcertadas--;

                if (embarcacaoAcertada.equals("h")) {
                    proximoTiro = tiroHidroaviao();
                } else if (embarcacaoAcertada.equals("s")) {
                    proximoTiro = tiroSubmarino();
                } else if (embarcacaoAcertada.equals("d")) {
                    proximoTiro = tiroDestroyer();
                } else if (embarcacaoAcertada.equals("e")) {
                    proximoTiro = tiroEncouracado();
                } else if (embarcacaoAcertada.equals("c")) {
                    proximoTiro = tiroCruzador();
                }
            }
        }

        ultimoTiro = proximoTiro;

        return proximoTiro;
    }

    public String tiroAgua() {
        int tempColuna = 0;
        int tempLinha = 0;

        //Calcula próximo tiro na água
        ultimaLinha = ((ultimaLinha + 2) % TAMANHO);
        ultimaColuna = ((ultimaColuna + 9) % TAMANHO);
        tempColuna = ultimaColuna;
        tempLinha = ultimaLinha;
        //System.out.println("linha" + ultimaLinha + " coluna: " + ultimaColuna);
        while (!tabuleiroAdversario[ultimaLinha][ultimaColuna].equals("x")) {
            ultimaColuna = ((ultimaColuna + 1) % 10);
            if (tempColuna == ultimaColuna) {
                ultimaLinha = ((ultimaLinha + 1) % TAMANHO);
                if (ultimaLinha == tempLinha) {
                    return "v";
                }
            }

        }
        return ultimaLinha + "" + ultimaColuna;
    }

    public String tiroHidroaviao() {
        String alvo = null;
        int tempLinha;
        int tempColuna;
        pecasAcertadas++;

        if (pecasAcertadas == 1) {
            embarcacaoAcertada = "h";
            if (verificaDisponibilidade(ultimaLinha + 1, ultimaColuna + 1)) {
                alvo = (ultimaLinha + 1) + "" + (ultimaColuna + 1);
            } else if (verificaDisponibilidade(ultimaLinha + 1, ultimaColuna - 1)) {
                alvo = (ultimaLinha + 1) + "" + (ultimaColuna - 1);
            } else if (verificaDisponibilidade(ultimaLinha - 1, ultimaColuna + 1)) {
                alvo = (ultimaLinha - 1) + "" + (ultimaColuna + 1);
            } else if (verificaDisponibilidade(ultimaLinha - 1, ultimaColuna - 1)) {
                alvo = (ultimaLinha - 1) + "" + (ultimaColuna - 1);
            }
        } else if (pecasAcertadas == 2) {
            tempLinha = Integer.parseInt(historicoTiros.get(historicoTiros.size() - 2).substring(0, 1));
            tempColuna = Integer.parseInt(historicoTiros.get(historicoTiros.size() - 2).substring(1, 2));

            //Tenta próximas linhas
            if (tempLinha < ultimaLinha) {
                if (verificaDisponibilidade(tempLinha + 2, tempColuna)) {
                    return (tempLinha + 2) + "" + (tempColuna);
                } else if (verificaDisponibilidade(ultimaLinha - 2, ultimaColuna)) {
                    return (ultimaLinha - 2) + "" + (ultimaColuna);
                }
            } else {
                if (verificaDisponibilidade(ultimaLinha + 2, ultimaColuna)) {
                    return (ultimaLinha + 2) + "" + (ultimaColuna);
                } else if (verificaDisponibilidade(tempLinha - 2, tempColuna)) {
                    return (tempLinha - 2) + "" + (tempColuna);
                }
            }
            //Caso não esteja nas linhas, tenta as colunas
            if (tempColuna < ultimaColuna) {
                if (verificaDisponibilidade(tempLinha, tempColuna + 2)) {
                    return (tempLinha) + "" + (tempColuna + 2);
                } else if (verificaDisponibilidade(ultimaLinha, ultimaColuna - 2)) {
                    return (ultimaLinha) + "" + (ultimaColuna - 2);
                }
            } else {
                if (verificaDisponibilidade(tempLinha, tempColuna - 2)) {
                    return (tempLinha) + "" + (tempColuna - 2);
                } else if (verificaDisponibilidade(ultimaLinha, ultimaColuna + 2)) {
                    return (ultimaLinha) + "" + (ultimaColuna + 2);
                }

            }
        } else if (pecasAcertadas == 3) {
            //Finalizando embarcação
            embarcacoesDestruidas.add("Hidroaviao");
            embarcacaoAcertada = null;
            //System.out.println("Destruiu Hidroaviao");

            int tempAntLinha = 0;
            int tempAntColuna = 0;
            int linhaInicial = 0;
            int colunaInicial = 0;
            int linhaFinal = 0;
            int colunaFinal = 0;
            int linhaMeio = 0;
            int colunaMeio = 0;
            String posicao = "h";

            //Preenchendo água ao redor da embarcação
            tempLinha = Integer.parseInt(historicoTiros.get(historicoTiros.size() - 2).substring(0, 1));
            tempColuna = Integer.parseInt(historicoTiros.get(historicoTiros.size() - 2).substring(1, 2));
            tempAntLinha = Integer.parseInt(historicoTiros.get(historicoTiros.size() - 3).substring(0, 1));
            tempAntColuna = Integer.parseInt(historicoTiros.get(historicoTiros.size() - 3).substring(1, 2));

            if (ultimaLinha == tempAntLinha) {
                linhaMeio = tempLinha;
                colunaMeio = tempColuna;
                if (ultimaColuna < tempAntColuna) {
                    colunaInicial = ultimaColuna - 1;
                } else {
                    colunaInicial = tempAntColuna - 1;
                }
                //linha inicial
                if (ultimaLinha > tempLinha) {
                    linhaInicial = tempLinha - 1;
                } else {
                    linhaInicial = ultimaLinha - 1;
                }
            } else if (ultimaLinha == tempLinha) {
                linhaMeio = tempAntLinha;
                colunaMeio = tempAntColuna;
                if (ultimaColuna < tempColuna) {
                    colunaInicial = ultimaColuna - 1;
                } else {
                    colunaInicial = tempColuna - 1;
                }
                //linha inicial
                if (ultimaLinha > tempAntLinha) {
                    linhaInicial = tempAntLinha - 1;
                } else {
                    linhaInicial = ultimaLinha - 1;
                }
            }
            if (ultimaColuna == tempAntColuna) {
                linhaMeio = tempLinha;
                colunaMeio = tempColuna;
                posicao = "v";

                if (ultimaLinha < tempAntLinha) {
                    linhaInicial = ultimaLinha - 1;
                } else {
                    linhaInicial = tempAntLinha - 1;
                }
                //coluna inicial
                if (ultimaColuna > tempColuna) {
                    colunaInicial = tempColuna - 1;
                } else {
                    colunaInicial = ultimaColuna - 1;
                }
            } else if (ultimaColuna == tempColuna) {
                linhaMeio = tempAntLinha;
                colunaMeio = tempAntColuna;
                posicao = "v";

                if (ultimaLinha < tempLinha) {
                    linhaInicial = ultimaLinha - 1;
                } else {
                    linhaInicial = tempLinha - 1;
                }
                //coluna inicial
                if (ultimaColuna > tempAntColuna) {
                    colunaInicial = tempAntColuna - 1;
                } else {
                    colunaInicial = ultimaColuna - 1;
                }
            }
            if (posicao.equals("h")) {
                linhaFinal = linhaInicial + 3;
                colunaFinal = colunaInicial + 4;
            } else {
                linhaFinal = linhaInicial + 4;
                colunaFinal = colunaInicial + 3;
            }
            if (linhaMeio == linhaInicial + 1) {
                linhaInicial++;
                if (verificaDisponibilidade(linhaMeio - 1, colunaMeio - 1)) {
                    tabuleiroAdversario[linhaMeio - 1][colunaMeio - 1] = "a";
                }
                if (verificaDisponibilidade(linhaMeio - 1, colunaMeio)) {
                    tabuleiroAdversario[linhaMeio - 1][colunaMeio] = "a";
                }
                if (verificaDisponibilidade(linhaMeio - 1, colunaMeio + 1)) {
                    tabuleiroAdversario[linhaMeio - 1][colunaMeio + 1] = "a";
                }
            } else if (linhaMeio == linhaFinal - 1) {
                linhaFinal--;
                if (verificaDisponibilidade(linhaMeio + 1, colunaMeio - 1)) {
                    tabuleiroAdversario[linhaMeio + 1][colunaMeio - 1] = "a";
                }
                if (verificaDisponibilidade(linhaMeio + 1, colunaMeio)) {
                    tabuleiroAdversario[linhaMeio + 1][colunaMeio] = "a";
                }
                if (verificaDisponibilidade(linhaMeio + 1, colunaMeio + 1)) {
                    tabuleiroAdversario[linhaMeio + 1][colunaMeio + 1] = "a";
                }

            }
            if (colunaMeio == colunaInicial + 1) {
                colunaInicial++;
                if (verificaDisponibilidade(linhaMeio - 1, colunaMeio - 1)) {
                    tabuleiroAdversario[linhaMeio - 1][colunaMeio - 1] = "a";
                }
                if (verificaDisponibilidade(linhaMeio, colunaMeio - 1)) {
                    tabuleiroAdversario[linhaMeio][colunaMeio - 1] = "a";
                }
                if (verificaDisponibilidade(linhaMeio + 1, colunaMeio - 1)) {
                    tabuleiroAdversario[linhaMeio + 1][colunaMeio - 1] = "a";
                }
            } else if (colunaMeio == colunaFinal - 1) {
                colunaFinal--;
                if (verificaDisponibilidade(linhaMeio - 1, colunaMeio + 1)) {
                    tabuleiroAdversario[linhaMeio - 1][colunaMeio + 1] = "a";
                }
                if (verificaDisponibilidade(linhaMeio, colunaMeio + 1)) {
                    tabuleiroAdversario[linhaMeio][colunaMeio + 1] = "a";
                }
                if (verificaDisponibilidade(linhaMeio + 1, colunaMeio + 1)) {
                    tabuleiroAdversario[linhaMeio + 1][colunaMeio + 1] = "a";
                }
            }

            for (int i = linhaInicial; i <= linhaFinal; i++) {
                for (int j = colunaInicial; j <= colunaFinal; j++) {
                    if (verificaDisponibilidade(i, j)) {
                        tabuleiroAdversario[i][j] = "a";
                    }
                }
            }

            //System.out.println("linha inicial: " + linhaInicial + " coluna inicial: " + colunaInicial);
            imprimeTabuleiro();
            pecasAcertadas = 0;
            return tiroAgua();
        }
        return alvo;
    }

    public String tiroSubmarino() {
        embarcacoesDestruidas.add("Submarino");
        //Preenche aguas ao redor da embarcação
        for (int i = ultimaLinha - 1; i <= ultimaLinha + 1; i++) {
            if (i >= 0) {
                if (i == 10) {
                    break;
                }
                for (int j = ultimaColuna - 1; j <= ultimaColuna + 1; j++) {
                    if (j >= 0) {
                        if (j == 10) {
                            break;
                        }
                        if (tabuleiroAdversario[i][j].equals("x")) {
                            tabuleiroAdversario[i][j] = "a";
                        }

                    }
                }

            }
        }
        return tiroAgua();
    }

    public String tiroDestroyer() {
        pecasAcertadas++;

        if (pecasAcertadas == 1) {
            embarcacaoAcertada = "d";
            return acertouPrimeira(2);
        } else {
            embarcacoesDestruidas.add("Destroyer");
            embarcacaoAcertada = null;
            //System.out.println("Destruiu Destroyer");
            marcaAgua();
            pecasAcertadas = 0;
            return tiroAgua();
        }
    }

    public String tiroEncouracado() {
        pecasAcertadas++;
        if (pecasAcertadas == 1) {
            embarcacaoAcertada = "e";
            return acertouPrimeira(4);
        }
        if (pecasAcertadas > 1 && pecasAcertadas < 4) {
            return acertouIntermediarias();
        } else {
            //System.out.println("Destruiu Encouracado");
            embarcacaoAcertada = null;
            embarcacoesDestruidas.add("Encouraçado");
            marcaAgua();
            pecasAcertadas = 0;
            return tiroAgua();
        }
    }

    public String tiroCruzador() {
        pecasAcertadas++;
        if (pecasAcertadas == 1) {
            embarcacaoAcertada = "c";
            return acertouPrimeira(5);
        }
        if (pecasAcertadas > 1 && pecasAcertadas < 5) {
            return acertouIntermediarias();
        } else {
            //System.out.println("Destruiu Cruzador");
            embarcacaoAcertada = null;
            embarcacoesDestruidas.add("Cruzador");
            marcaAgua();
            pecasAcertadas = 0;
            return tiroAgua();
        }

    }

    public boolean verificaDisponibilidade(int linha, int coluna) {
        if ((linha <= 9 && linha >= 0) && (coluna <= 9 && coluna >= 0)) {
            if (tabuleiroAdversario[linha][coluna].equals("x")) {
                return true;
            }
        }
        return false;
    }

    public boolean verificaVitoria() {
        if (PECASTOTOTAL == embarcacoesDestruidas.size()) {
            return true;
        }
        return false;
    }

    public String acertouPrimeira(int tamanhoPeca) {
        //Verifica próximo passo após acertar a primeira parte da embarcação
        if (testaVertical(tamanhoPeca)) {
            if (verificaDisponibilidade(ultimaLinha + 1, ultimaColuna)) {
                return (ultimaLinha + 1) + "" + ultimaColuna;
            } else if (verificaDisponibilidade((ultimaLinha - 1), ultimaColuna)) {
                return (ultimaLinha - 1) + "" + ultimaColuna;
            }
        }
        if (verificaDisponibilidade(ultimaLinha, ultimaColuna + 1)) {
            return ultimaLinha + "" + (ultimaColuna + 1);
        } else if (verificaDisponibilidade(ultimaLinha, ultimaColuna - 1)) {
            return ultimaLinha + "" + (ultimaColuna - 1);
        }

        return "ee";
    }
    /*
     * public String acertouPrimeira() { //Verifica próximo passo após acertar a
     * primeira parte da embarcação
     *
     * if (verificaDisponibilidade(ultimaLinha, ultimaColuna + 1)) { return
     * ultimaLinha + "" + (ultimaColuna + 1); } else if
     * (verificaDisponibilidade(ultimaLinha, ultimaColuna - 1)) { return
     * ultimaLinha + "" + (ultimaColuna - 1); } * if
     * (verificaDisponibilidade(ultimaLinha + 1, ultimaColuna)) { return
     * (ultimaLinha + 1) + "" + ultimaColuna; } else if
     * (verificaDisponibilidade((ultimaLinha - 1), ultimaColuna)) { return
     * (ultimaLinha - 1) + "" + ultimaColuna; }
     *
     * return "ee"; }
     */

    public String acertouIntermediarias() {
        int tempLinha = 0;
        int tempColuna = 0;
        int penultimaLinha;
        int penultimaColuna;

        penultimaLinha = Integer.parseInt(historicoTiros.get(historicoTiros.size() - 2).substring(0, 1));
        penultimaColuna = Integer.parseInt(historicoTiros.get(historicoTiros.size() - 2).substring(1, 2));


        //Procura passo na sequencia corrente
        if (ultimaLinha == penultimaLinha) {
            tempLinha = ultimaLinha;
            if (ultimaColuna < penultimaColuna) {
                tempColuna = ultimaColuna - 1;
            } else {
                tempColuna = ultimaColuna + 1;
            }
        } else if (ultimaColuna == penultimaColuna) {
            tempColuna = ultimaColuna;
            if (ultimaLinha < penultimaLinha) {
                tempLinha = ultimaLinha - 1;
            } else {
                tempLinha = ultimaLinha + 1;
            }
        }

        if (verificaDisponibilidade(tempLinha, tempColuna)) {
            return tempLinha + "" + tempColuna;
        } else {
            //Inverte sequencia
            if (ultimaLinha == penultimaLinha) {
                tempLinha = ultimaLinha;
                if (ultimaColuna < penultimaColuna) {
                    tempColuna = ultimaColuna + pecasAcertadas;
                } else {
                    tempColuna = ultimaColuna - pecasAcertadas;
                }
            } else if (ultimaColuna == penultimaColuna) {
                tempColuna = ultimaColuna;
                if (ultimaLinha < penultimaLinha) {
                    tempLinha = ultimaLinha + pecasAcertadas;
                } else {
                    tempLinha = ultimaLinha - pecasAcertadas;
                }
            }
        }

        return tempLinha + "" + tempColuna;
    }

    public boolean testaVertical(int tamanhoPeca) {
        int linhaAcima = 1;
        int linhaAbaixo = 1;

        //testa pra baixo
        while (verificaDisponibilidade(ultimaLinha + linhaAcima, ultimaColuna)) {
            linhaAcima++;
            if (linhaAcima == tamanhoPeca) {
                return true;
            }
        }
        if (linhaAcima == 1) {
            linhaAcima = ultimaLinha;
        } else {
            //linhaAcima--;
            linhaAcima = ultimaLinha + linhaAcima;
        }

        //testa  para cima a partir do ultimo ponto
        while (verificaDisponibilidade(linhaAcima - linhaAbaixo, ultimaColuna)) {
            linhaAbaixo++;
            if (linhaAbaixo == tamanhoPeca) {
                return true;
            }
            if (ultimaLinha == (linhaAcima - linhaAbaixo)) {
                linhaAbaixo++;
                if (linhaAbaixo == tamanhoPeca) {
                    return true;
                }
            }
        }


        return false;
    }

    public void marcaAgua() {
        int tempLinha = 10;
        int tempColuna = 10;
        int ultimaLinhaMarcada = ultimaLinha;
        int ultimaColunaMarcada = ultimaColuna;

        tempLinha = Integer.parseInt(historicoTiros.get(historicoTiros.size() - 2).substring(0, 1));
        tempColuna = Integer.parseInt(historicoTiros.get(historicoTiros.size() - 2).substring(1, 2));

        if (ultimaLinha == tempLinha) {
            if (ultimaColuna < tempColuna) {
                ultimaColunaMarcada = ultimaColuna + (pecasAcertadas - 1);
                tempColuna = ultimaColuna;
            } else {
                ultimaColunaMarcada = ultimaColuna;
                tempColuna = ultimaColuna - (pecasAcertadas - 1);
            }

        } else if (ultimaColuna == tempColuna) {
            if (ultimaLinha < tempLinha) {
                ultimaLinhaMarcada = ultimaLinha + (pecasAcertadas - 1);
                tempLinha = ultimaLinha;
            } else {
                ultimaLinhaMarcada = ultimaLinha;
                tempLinha = ultimaLinha - (pecasAcertadas - 1);
            }

        }

        for (int i = tempLinha - 1; i <= ultimaLinhaMarcada + 1; i++) {
            for (int j = tempColuna - 1; j <= ultimaColunaMarcada + 1; j++) {
                if (verificaDisponibilidade(i, j)) {
                    String u = i + "" + j;
                    tabuleiroAdversario[i][j] = "a";
                }
            }
        }
//        imprimeTabuleiro();
    }

    public final void preencheTabuleiro(String valor) {
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                tabuleiroAdversario[i][j] = valor;
            }
        }
    }

    public void imprimeTabuleiro() {
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                //System.out.print(tabuleiroAdversario[i][j] + " ");
            }
            //System.out.println(i + " ");
        }
    }
}
