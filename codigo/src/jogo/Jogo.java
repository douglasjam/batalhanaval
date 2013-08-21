/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jogo;

import conexao.Conexao;
import gui.GUIConf;
import gui.GUITabuleiro;
import jogador.*;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import java.awt.Image;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Douglas J.A.M
 */
public class Jogo {

    static Jogo jogo;
    //
    Image icone;
    String titulo;
    Properties configuracoes;
    //
    GUIConf guiConfiguracoes;
    GUITabuleiro guiTabuleiro;
    //
    private Tabuleiro tabuleiroA;
    private Tabuleiro tabuleiroB;
    //
    private Jogador jogadorA;
    private Jogador jogadorB;
    //
    Conexao conexao;
    Boolean status;

    public static void main(String[] args) {

        jogo = Jogo.getInstance();
        jogo.iniciaDefinicoes();

        jogo.capturaConfiguracoes();
        System.out.println(jogo.getTipoJogoString());
        jogo.exibeTabuleiroGUI(jogo.getTipoJogoString());
        jogo.preparaJogo();
        //
        jogo.executaJogo();
    }

    public void iniciaDefinicoes() {

        // seta lookAndFeel com tema do Windows, se retirar isso alinhamentos podem desajustar
        try {
            javax.swing.UIManager.setLookAndFeel(new WindowsLookAndFeel());
        } catch (UnsupportedLookAndFeelException ex) {
            System.out.println("Erro ao definir o LookAndFeel");
            System.out.println(ex.getMessage());
        }

        icone = new ImageIcon(getClass().getResource("/imagens/mirado.png")).getImage();
        titulo = "Hidenove Batalha Naval";

        conexao = Conexao.getInstance();

        guiConfiguracoes = GUIConf.getInstance();
        guiConfiguracoes.setTitle(titulo);
        guiConfiguracoes.setIconImage(icone);
        guiConfiguracoes.setLocationRelativeTo(null);

        guiTabuleiro = GUITabuleiro.getInstance();
        guiTabuleiro.setTitle(titulo);
        guiTabuleiro.setIconImage(icone);
        guiTabuleiro.setLocationRelativeTo(null);

        status = false;

    }

    public void capturaConfiguracoes() {
        // exibe a tela de configurações e aguarda um retorno
        guiConfiguracoes.setReady(false);
        guiConfiguracoes.setVisible(true);

        // enquanto não estiver pronta as configurações não inicia
        while (!guiConfiguracoes.isReady()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(Jogo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        configuracoes = guiConfiguracoes.setConfiguracoes();
        guiConfiguracoes.dispose();
        this.titulo = this.titulo + getTipoJogoString();
        guiTabuleiro.setTitle(titulo);
    }

    public void preparaJogo() {

        jogo.setTabuleiroA(configuracoes.getProperty("tabuleiroA"));

        if (!getTipoJogoString().contains("Remoto")) {
            jogo.setTabuleiroB(configuracoes.getProperty("tabuleiroB"));
        } else {
            jogo.setTabuleiroB("");
        }

        // troca os tabuleiros dos players
        Tabuleiro temp = tabuleiroA;
        tabuleiroA = tabuleiroB;
        tabuleiroB = temp;

        String tabPlayerA = getTipoJogoString().contains("Remoto") ? tabuleiroB.getTabuleiroName() : tabuleiroA.getTabuleiroName();
        String tabPlayerB = getTipoJogoString().contains("Remoto") ? tabuleiroA.getTabuleiroName() : tabuleiroB.getTabuleiroName();

        // seta o jogador A
        if (getTipoJogo() == 1 || getTipoJogo() == 2 || getTipoJogo() == 3 || getTipoJogo() == 7 || getTipoJogo() == 8) {
            jogadorA = new JogadorHumano("a", tabuleiroA, guiTabuleiro.getTabPlayer("a"));
            guiTabuleiro.setPlayerA("Jogador A", tabPlayerA);
        } else if (getTipoJogo() == 4 || getTipoJogo() == 5 || getTipoJogo() == 9 || getTipoJogo() == 10) {
            jogadorA = new JogadorInteligencia("a", tabuleiroA);
            guiTabuleiro.setPlayerA("Inteligência A", tabPlayerA);
        } else {
            jogadorA = new JogadorRandom("a", tabuleiroA);
            guiTabuleiro.setPlayerA("Random A", tabPlayerA);
        }

        // seta o jogador B
        if (getTipoJogo() == 1) {
            jogadorB = new JogadorHumano("b", tabuleiroB, guiTabuleiro.getTabPlayer("b"));
            guiTabuleiro.setPlayerB("Jogador B", tabPlayerB);
        } else if (getTipoJogo() == 2 || getTipoJogo() == 4) {
            jogadorB = new JogadorInteligencia("b", tabuleiroB);
            guiTabuleiro.setPlayerB("Inteligência B", tabPlayerB);
        } else if (getTipoJogo() == 3 || getTipoJogo() == 5 || getTipoJogo() == 6) {
            jogadorB = new JogadorRandom("b", tabuleiroB);
            guiTabuleiro.setPlayerB("Random B", tabPlayerB);
        } else {
            jogadorB = new JogadorRemoto("b");
            guiTabuleiro.setPlayerB("Jogador Remoto", tabPlayerB);
        }

    }

    public void exibeTabuleiroGUI(String tipoJogo) {

        guiTabuleiro.setTipoJogo(tipoJogo);
        guiTabuleiro.reseta();
        guiTabuleiro.setVisible(true);

    }

    public void executaJogo() {

        status = true;
        Integer tipo = Integer.valueOf(configuracoes.getProperty("tipo").substring(0, 1));
        String posicaoJogada;
        String encontrado;

        if (!guiConfiguracoes.getTipoJogo().contains("Servidor")) {
            GUITabuleiro.getInstance().addLog("Ordem jogada: Player A, Adversario");
            GUITabuleiro.getInstance().addLog("---------------------------------------");
        }

        try {

            while (!jogo.terminou() && !guiTabuleiro.isAborted() && this.isJogando()) {


                if (!guiConfiguracoes.getTipoJogo().contains("Servidor")) {
                    //LOCAL OU CLIENTE

                    if (!jogo.terminou()) {
                        encontrado = "_";

                        while (!encontrado.equals("a")) {
                            if (jogo.terminou()) {
                                break;
                            }
                            guiTabuleiro.destacaPlayer("a");
                            posicaoJogada = jogo.getJogadorA().fazJogada();
                            encontrado = jogo.getJogadorA().encontrou(posicaoJogada);
                            guiTabuleiro.marcaTabuleiro("a", posicaoJogada, encontrado);
                        }

                        GUITabuleiro.getInstance().addLog("---------------------------------------");
                    }

                    if (!jogo.terminou()) {
                        encontrado = "_";
                        while (!encontrado.equals("a")) {
                            if (jogo.terminou()) {
                                break;
                            }
                            guiTabuleiro.destacaPlayer("b");
                            posicaoJogada = jogo.getJogadorB().fazJogada();
                            encontrado = jogo.getJogadorB().encontrou(posicaoJogada);
                            guiTabuleiro.marcaTabuleiro("b", posicaoJogada, encontrado);
                        }

                        GUITabuleiro.getInstance().addLog("---------------------------------------");
                    }


                } else {

                    // SERVIDOR

                    if (!jogo.terminou()) {
                        encontrado = "_";

                        while (!encontrado.equals("a")) {
                            if (jogo.terminou()) {
                                break;
                            }

                            guiTabuleiro.destacaPlayer("b");
                            posicaoJogada = jogo.getJogadorB().fazJogada();
                            encontrado = jogo.getJogadorB().encontrou(posicaoJogada);
                            guiTabuleiro.marcaTabuleiro("b", posicaoJogada, encontrado);
                        }

                        GUITabuleiro.getInstance().addLog("---------------------------------------");
                    }

                    if (!jogo.terminou()) {
                        encontrado = "_";
                        while (!encontrado.equals("a")) {
                            if (jogo.terminou()) {
                                break;
                            }
                            guiTabuleiro.destacaPlayer("a");
                            posicaoJogada = jogo.getJogadorA().fazJogada();
                            encontrado = jogo.getJogadorA().encontrou(posicaoJogada);
                            guiTabuleiro.marcaTabuleiro("a", posicaoJogada, encontrado);
                        }

                        GUITabuleiro.getInstance().addLog("---------------------------------------");
                    }

                }
            }

            // jogo terminou 
            status = false;

            if (guiTabuleiro.getPecasRestantes("a") == 0) {
                // recebe mensagem de vitória
                if (getTipoJogoString().contains("Remoto")) {
                    String parabens;
                    parabens = Conexao.getInstance().recebe();
                    guiTabuleiro.addLog("Recebeu " + parabens);
                    System.out.println("Recebeu " + parabens);
                }
                guiTabuleiro.addLog("Fim de jogo");
                GUITabuleiro.getInstance().addLog("Jogador A venceu");
                guiTabuleiro.exibeVencedor(jogadorA);

            } else {
                if (guiConfiguracoes.getTipoJogo().contains("Remoto")) {
                    guiTabuleiro.addLog("Envia parabens");
                    Conexao.getInstance().envia("parabens");
                    System.out.println("Envia parabens");
                }
                guiTabuleiro.addLog("Fim de jogo");
                System.out.println("Fim de jogo");
                GUITabuleiro.getInstance().addLog("Jogador B venceu");
                System.out.println("Jogador B venceu");
                guiTabuleiro.exibeVencedor(jogadorB);
            }


        } catch (Exception ex) {

            ex.printStackTrace();
            GUITabuleiro.getInstance().addLog("Erro no jogo, você deve abortar o jogo.\n" + ex.getMessage());
            while (!guiTabuleiro.isAborted()) {
            }
        }
    }

    public static Jogo getInstance() {
        if (jogo == null) {
            jogo = new Jogo();
            return jogo;
        } else {
            return jogo;
        }
    }

    public Integer getTipoJogo() {
        return Integer.valueOf(configuracoes.getProperty("tipo").substring(0, 2));
    }

    public String getTipoJogoString() {
        return configuracoes.getProperty("tipo");
    }

    public void setTabuleiroA(String pathArquivo) {
        tabuleiroA = new Tabuleiro(pathArquivo);

//        System.out.println("Carregou combo A: " + pathArquivo);
//        System.out.println("-------------------");
        tabuleiroA.imprimeTabuleiro();
    }

    public Tabuleiro getTabuleiro(String tabuleiro) {
        if (tabuleiro.equals("a")) {
            return tabuleiroA;
        } else {
            return tabuleiroB;
        }
    }

    public Tabuleiro getTabuleiroA() {
        return tabuleiroA;
    }

    public void setTabuleiroB(String pathArquivo) {

        if (pathArquivo.isEmpty() || pathArquivo == null) {
            tabuleiroB = new Tabuleiro();
        } else {
            tabuleiroB = new Tabuleiro(pathArquivo);
        }

//        System.out.println("Carregou combo B: " + pathArquivo);
//        System.out.println("-------------------");
        tabuleiroB.imprimeTabuleiro();
    }

    public Tabuleiro getTabuleiroB() {
        return tabuleiroB;
    }

    public GUITabuleiro getGuiJogo() {
        return guiTabuleiro;
    }

    public void setGuiJogo(GUITabuleiro guiJogo) {
        this.guiTabuleiro = guiJogo;
    }

    public Jogador getJogador(String jogador) {
        if (jogador.equals("a")) {
            return jogadorA;
        } else {
            return jogadorB;
        }
    }

    public Jogador getJogadorA() {
        return jogadorA;
    }

    public void setJogadorA(Jogador jogadorA) {
        this.jogadorA = jogadorA;
    }

    public Jogador getJogadorB() {
        return jogadorB;
    }

    public void setJogadorB(Jogador jogadorB) {
        this.jogadorB = jogadorB;
    }

    public boolean terminou() {
        int pecasRestantesA = guiTabuleiro.getPecasRestantes("a");
        int pecasRestantesB = guiTabuleiro.getPecasRestantes("b");
        Boolean retorno = pecasRestantesA == 0 || pecasRestantesB == 0;
        return (pecasRestantesA == 0 || pecasRestantesB == 00);
    }

    public Properties getConfiguracoes() {
        return configuracoes;
    }

    public void setConfiguracoes(Properties configuracoes) {
        this.configuracoes = configuracoes;
    }

    public Integer getDelay() {
        if (this.configuracoes.getProperty("delay") == null || this.configuracoes.getProperty("delay").isEmpty()) {
            return 500;
        } else {
            return Integer.valueOf(this.configuracoes.getProperty("delay"));
        }
    }

    public void atualizaVelocidade(Integer velocidade) {
        this.configuracoes.setProperty("delay", String.valueOf(velocidade));
        guiTabuleiro.addLog("Velocidade alterada para " + getDelay() + "ms");
    }

    public void setJogando(Boolean jogando) {
        this.status = jogando;
    }

    public Boolean isJogando() {
        return this.status;
    }

    public Image getIcon() {
        return icone;
    }
}
