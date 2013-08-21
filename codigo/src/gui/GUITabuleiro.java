package gui;

import java.applet.Applet;
import jogador.Jogador;
import jogo.Jogo;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 * @author Douglas J.A.M
 */
public final class GUITabuleiro extends javax.swing.JFrame {

    static GUITabuleiro guiTabuleiro;
    private HashMap componentMap;
    private String tipoJogo;
    int PECASTOTOTAL = (3 * 3) + (3 * 2) + (3 * 1) + (2 * 4) + (1 * 5);
    Boolean abort;

    public void reseta() {
        abort = false;
        Set<String> chaves = componentMap.keySet();
        Integer pecasDescobertas = 0;
        for (Iterator<String> iterator = chaves.iterator(); iterator.hasNext();) {
            String chave = iterator.next();
            ((JLabel) componentMap.get(chave)).setIcon(getIconeEmbarcacao("?"));
        }
        lblVelocidade.setText("Velocidade " + String.valueOf(Jogo.getInstance().getDelay()) + "ms");
        sldVelocidade.setValue(Jogo.getInstance().getDelay());
    }

    public static GUITabuleiro getInstance() {
        if (guiTabuleiro == null) {
            guiTabuleiro = new GUITabuleiro();
            return guiTabuleiro;
        } else {
            return guiTabuleiro;
        }
    }

    public GUITabuleiro() {
        initComponents();
    }

    public void setTipoJogo(String tipoJogo) {

        componentMap = new HashMap<String, Component>();
        this.setLocationRelativeTo(null);
        this.tipoJogo = tipoJogo;

        trataCelulasTabuleiro(tabPlayerA);
        trataCelulasTabuleiro(tabPlayerB);
    }

    // adiciona capacidade de clique nos
    public void trataCelulasTabuleiro(Container container) {
        for (final Component c : container.getComponents()) {
            if (c instanceof JLabel) {

                // caso não seja JLabel de celula vaza fora
                if (((JLabel) c).getIcon() == null) {
                    continue;
                }
                // adiciona no map para acessar pelo nome
                componentMap.put(c.getName(), c);

                // adicione o icone de maozinha
                if (tipoJogo.contains("Player 1") && c.getName().contains("a")) {
                    ((JLabel) c).setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }
                if (tipoJogo.contains("Player 2") && c.getName().contains("b")) {
                    ((JLabel) c).setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }


            } else if (c instanceof Container) {
                trataCelulasTabuleiro((Container) c);
            }
        }
    }

    public JPanel getTabPlayer(String player) {
        if (player.equals("a")) {
            return tabPlayerA;
        } else {
            return tabPlayerB;
        }
    }

    // retorna o jlabel pelo nome a00, a23, b34
    public Component getCelula(String name) {
        if (componentMap.containsKey(name)) {
            return (Component) componentMap.get(name);
        } else {
            return null;
        }
    }

    public void destacaPlayer(String player) {

        Color padrao = new Color(240, 240, 240);
        Color destaque = Color.ORANGE;

        if (player.equals("a")) {

            bigboxPlayerA.setBackground(destaque);
            tabPlayerA.setBackground(destaque);

            bigboxPlayerB.setBackground(padrao);
            tabPlayerB.setBackground(padrao);

        } else if (player.equals("b")) {

            bigboxPlayerA.setBackground(padrao);
            tabPlayerA.setBackground(padrao);

            bigboxPlayerB.setBackground(destaque);
            tabPlayerB.setBackground(destaque);

        } else {

            bigboxPlayerA.setBackground(padrao);
            tabPlayerA.setBackground(padrao);

            bigboxPlayerB.setBackground(padrao);
            tabPlayerB.setBackground(padrao);
        }
    }

    // troca o icone do jlabel
    public void marcaTabuleiro(String tabuleiro, String posicao, String valor) {

        Integer linha = Integer.valueOf(posicao.substring(0, 1));
        Integer coluna = Integer.valueOf(posicao.substring(1, 2));
        String jLabelName = tabuleiro + posicao;

        JLabel jLabel = (JLabel) getCelula(jLabelName);
        jLabel.setIcon(getIconeEmbarcacao("DESTACA"));

        try {
            Thread.sleep(Jogo.getInstance().getDelay() / 4);
        } catch (InterruptedException ex) {
            Logger.getLogger(GUITabuleiro.class.getName()).log(Level.SEVERE, null, ex);
        }
        jLabel.setIcon(getIconeEmbarcacao(valor));

        jLabel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

        tocaSom(valor);

    }

    public Integer getPecasRestantes(String tabuleiro) {

        Integer retorno;
        Set<String> chaves = componentMap.keySet();
        Integer erros = 0;
        Integer acertos = 0;
        String eficiencia = "100";

        for (Iterator<String> iterator = chaves.iterator(); iterator.hasNext();) {
            String chave = iterator.next();
            if (chave.substring(0, 1).equals(tabuleiro)) {
                if (!((JLabel) componentMap.get(chave)).getIcon().toString().contains("campo.png") && !((JLabel) componentMap.get(chave)).getIcon().toString().contains("agua.png")) {
                    acertos++;
                } else if (((JLabel) componentMap.get(chave)).getIcon().toString().contains("agua.png")) {
                    erros++;
                }
            }
        }

        if (erros > 0) {
            eficiencia = (String.valueOf(((float) acertos / (float) (acertos + erros)) * 100) + "0000").substring(0, 5);
        }

        retorno = PECASTOTOTAL - acertos;

        if (tabuleiro.equals("a")) {
            lblAPecasRestantes.setText("(" + retorno + " peças rest.)");
            lblStatusA.setText("[" + (erros + acertos) + " tiros, " + acertos + " acertos, " + erros + " erros]");
            lblEficienciaA.setText("[Eficiência " + eficiencia + "%]");
        } else {
            lblBPecasRestantes.setText("(" + retorno + " peças rest.)");
            lblStatusB.setText("[" + (erros + acertos) + " tiros, " + acertos + " acertos, " + erros + " erros]");
            lblEficienciaB.setText("[Eficiência " + eficiencia + "%]");

        }


        return retorno;
    }

    public String getValorAt(String tabuleiro, String posicao) {

        String retorno;

        Integer linha = Integer.valueOf(posicao.substring(0, 1));
        Integer coluna = Integer.valueOf(posicao.substring(1, 2));
        String jLabelName = tabuleiro + posicao;

        JLabel jLabel = (JLabel) getCelula(jLabelName);
        ImageIcon iconeAtual = (ImageIcon) jLabel.getIcon();

        if (iconeAtual.toString().equals(new ImageIcon(getClass().getResource("/imagens/agua.png")).toString())) {
            retorno = "a";
        } else if (iconeAtual.toString().equals(new ImageIcon(getClass().getResource("/imagens/cruzador.png")).toString())) {
            retorno = "c";
        } else if (iconeAtual.toString().equals(new ImageIcon(getClass().getResource("/imagens/destroyer.png")).toString())) {
            retorno = "d";
        } else if (iconeAtual.toString().equals(new ImageIcon(getClass().getResource("/imagens/encouracador.png")).toString())) {
            retorno = "e";
        } else if (iconeAtual.toString().equals(new ImageIcon(getClass().getResource("/imagens/hidro.png")).toString())) {
            retorno = "h";
        } else if (iconeAtual.toString().equals(new ImageIcon(getClass().getResource("/imagens/submarino.png")).toString())) {
            retorno = "s";
        } else if (iconeAtual.toString().equals(new ImageIcon(getClass().getResource("/imagens/campo.png")).toString())) {
            retorno = "?";
        } else {
            System.out.println("RETORNO X TRATAR AGORA");
            new Exception("Tipo de embarcação desconhecido");
            retorno = "x";
        }

        return retorno;
    }

    public ImageIcon getIconeEmbarcacao(String tipo) {
        if (tipo.equals("a")) {
            return new ImageIcon(getClass().getResource("/imagens/agua.png"));
        } else if (tipo.equals("c")) {
            return new ImageIcon(getClass().getResource("/imagens/cruzador.png"));
        } else if (tipo.equals("d")) {
            return new ImageIcon(getClass().getResource("/imagens/destroyer.png"));
        } else if (tipo.equals("e")) {
            return new ImageIcon(getClass().getResource("/imagens/encouracador.png"));
        } else if (tipo.equals("h")) {
            return new ImageIcon(getClass().getResource("/imagens/hidro.png"));
        } else if (tipo.equals("s")) {
            return new ImageIcon(getClass().getResource("/imagens/submarino.png"));
        } else if (tipo.equals("DESTACA")) {
            return new ImageIcon(getClass().getResource("/imagens/destaca.png"));
        } else {
            return new ImageIcon(getClass().getResource("/imagens/campo.png"));
        }
    }

    public void exibeVencedor(Jogador jogador) {

        Set<String> chaves = componentMap.keySet();
        String mensagem;

        mensagem = "Clique em ok para encerrar o jogo";

        System.out.println(jogador.getApelido());
        System.out.println(tipoJogo);
        if (tipoJogo.contains("Remoto") && jogador.getApelido().toUpperCase().equals("B")) {
            tocaSom("derrota");
        } else {
            tocaSom("vitoria");
        }

        JDialog vencedor = new JOptionPane(mensagem, JOptionPane.PLAIN_MESSAGE, JOptionPane.CLOSED_OPTION).createDialog("Jogador " + jogador.getApelido().toUpperCase() + " venceu!!");
        vencedor.setIconImage(Jogo.getInstance().getIcon());
        vencedor.setModal(false);
        vencedor.show();
        this.repaint();

        Boolean destaca = true;

        while (vencedor.isVisible()) {
            destaca = !destaca;
            try {
                if (destaca) {
                    if (jogador.getApelido().equals("a")) {
                        destacaPlayer("a");
                    } else {
                        destacaPlayer("b");
                    }
                } else {
                    destacaPlayer("");
                }
                Thread.sleep(300);
            } catch (InterruptedException ex) {
                Logger.getLogger(GUITabuleiro.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

//        addLog("Jogador A: " + lblAPecasRestantes.getText().substring(1, -1) + ", " + lblStatusA.getText().substring(1, -1) + ", " + lblEficienciaA.getText().substring(1, -1));
        //      addLog("Jogador B: " + lblBPecasRestantes.getText().substring(1, -1) + ", " + lblStatusB.getText().substring(1, -1) + ", " + lblEficienciaB.getText().substring(1, -1));



        System.exit(0);
    }

    public void setPlayerA(String name, String tabuleiro) {
        lblPlayerA.setText(name + " (Tabuleiro: \"" + tabuleiro + "\")");
    }

    public void setPlayerB(String name, String tabuleiro) {
        lblPlayerB.setText(name + " (Tabuleiro: \"" + tabuleiro + "\")");
    }

    // converte DATE para string no formato 25/08/2010 23:45:10
    public static String fullDateToString(Date data) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String dateString = sdf.format(data).toString();
        return dateString;
    }

    public void addLog(String mensagem) {

        scrollLog.getVerticalScrollBar().setValue(scrollLog.getVerticalScrollBar().getMaximum() + 20);
        scrollLog.getVerticalScrollBar().setValue(scrollLog.getVerticalScrollBar().getMaximum() + 20);

        String newLog = fullDateToString(new Date()) + " - " + mensagem;
        if (txtLog.getText().isEmpty()) {
            txtLog.append(newLog);
        } else {
            txtLog.append("\n" + newLog);
        }

        scrollLog.getVerticalScrollBar().setValue(scrollLog.getVerticalScrollBar().getMaximum() + 20);
        scrollLog.getVerticalScrollBar().setValue(scrollLog.getVerticalScrollBar().getMaximum() + 20);
    }

    public Boolean isAborted() {
        return this.abort;

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        bigboxPlayerA = new javax.swing.JPanel();
        tabPlayerA = new javax.swing.JPanel();
        a00 = new javax.swing.JLabel();
        a01 = new javax.swing.JLabel();
        a02 = new javax.swing.JLabel();
        a03 = new javax.swing.JLabel();
        a04 = new javax.swing.JLabel();
        a05 = new javax.swing.JLabel();
        a06 = new javax.swing.JLabel();
        a07 = new javax.swing.JLabel();
        a08 = new javax.swing.JLabel();
        a09 = new javax.swing.JLabel();
        a10 = new javax.swing.JLabel();
        a11 = new javax.swing.JLabel();
        a12 = new javax.swing.JLabel();
        a13 = new javax.swing.JLabel();
        a14 = new javax.swing.JLabel();
        a15 = new javax.swing.JLabel();
        a16 = new javax.swing.JLabel();
        a17 = new javax.swing.JLabel();
        a18 = new javax.swing.JLabel();
        a19 = new javax.swing.JLabel();
        a20 = new javax.swing.JLabel();
        a34 = new javax.swing.JLabel();
        a33 = new javax.swing.JLabel();
        a25 = new javax.swing.JLabel();
        a36 = new javax.swing.JLabel();
        a35 = new javax.swing.JLabel();
        a30 = new javax.swing.JLabel();
        a32 = new javax.swing.JLabel();
        a24 = new javax.swing.JLabel();
        a31 = new javax.swing.JLabel();
        a23 = new javax.swing.JLabel();
        a26 = new javax.swing.JLabel();
        a22 = new javax.swing.JLabel();
        a27 = new javax.swing.JLabel();
        a37 = new javax.swing.JLabel();
        a38 = new javax.swing.JLabel();
        a39 = new javax.swing.JLabel();
        a28 = new javax.swing.JLabel();
        a29 = new javax.swing.JLabel();
        a21 = new javax.swing.JLabel();
        a49 = new javax.swing.JLabel();
        a47 = new javax.swing.JLabel();
        a48 = new javax.swing.JLabel();
        a43 = new javax.swing.JLabel();
        a44 = new javax.swing.JLabel();
        a46 = new javax.swing.JLabel();
        a40 = new javax.swing.JLabel();
        a45 = new javax.swing.JLabel();
        a42 = new javax.swing.JLabel();
        a41 = new javax.swing.JLabel();
        a70 = new javax.swing.JLabel();
        a83 = new javax.swing.JLabel();
        a84 = new javax.swing.JLabel();
        a86 = new javax.swing.JLabel();
        a75 = new javax.swing.JLabel();
        a80 = new javax.swing.JLabel();
        a85 = new javax.swing.JLabel();
        a55 = new javax.swing.JLabel();
        a74 = new javax.swing.JLabel();
        a82 = new javax.swing.JLabel();
        a81 = new javax.swing.JLabel();
        a54 = new javax.swing.JLabel();
        a53 = new javax.swing.JLabel();
        a52 = new javax.swing.JLabel();
        a51 = new javax.swing.JLabel();
        a79 = new javax.swing.JLabel();
        a71 = new javax.swing.JLabel();
        a89 = new javax.swing.JLabel();
        a78 = new javax.swing.JLabel();
        a87 = new javax.swing.JLabel();
        a88 = new javax.swing.JLabel();
        a72 = new javax.swing.JLabel();
        a77 = new javax.swing.JLabel();
        a73 = new javax.swing.JLabel();
        a76 = new javax.swing.JLabel();
        a56 = new javax.swing.JLabel();
        a57 = new javax.swing.JLabel();
        a58 = new javax.swing.JLabel();
        a59 = new javax.swing.JLabel();
        a50 = new javax.swing.JLabel();
        a95 = new javax.swing.JLabel();
        a92 = new javax.swing.JLabel();
        a91 = new javax.swing.JLabel();
        a93 = new javax.swing.JLabel();
        a64 = new javax.swing.JLabel();
        a94 = new javax.swing.JLabel();
        a63 = new javax.swing.JLabel();
        a96 = new javax.swing.JLabel();
        a66 = new javax.swing.JLabel();
        a90 = new javax.swing.JLabel();
        a65 = new javax.swing.JLabel();
        a60 = new javax.swing.JLabel();
        a99 = new javax.swing.JLabel();
        a97 = new javax.swing.JLabel();
        a62 = new javax.swing.JLabel();
        a98 = new javax.swing.JLabel();
        a61 = new javax.swing.JLabel();
        a67 = new javax.swing.JLabel();
        a68 = new javax.swing.JLabel();
        a69 = new javax.swing.JLabel();
        aj = new javax.swing.JLabel();
        af = new javax.swing.JLabel();
        ae = new javax.swing.JLabel();
        ac = new javax.swing.JLabel();
        ah = new javax.swing.JLabel();
        ag = new javax.swing.JLabel();
        ad = new javax.swing.JLabel();
        ai = new javax.swing.JLabel();
        ab = new javax.swing.JLabel();
        aa = new javax.swing.JLabel();
        a0 = new javax.swing.JLabel();
        a1 = new javax.swing.JLabel();
        a2 = new javax.swing.JLabel();
        a9 = new javax.swing.JLabel();
        a8 = new javax.swing.JLabel();
        a7 = new javax.swing.JLabel();
        a6 = new javax.swing.JLabel();
        a5 = new javax.swing.JLabel();
        a4 = new javax.swing.JLabel();
        a3 = new javax.swing.JLabel();
        bigboxPlayerB = new javax.swing.JPanel();
        tabPlayerB = new javax.swing.JPanel();
        b00 = new javax.swing.JLabel();
        b01 = new javax.swing.JLabel();
        b02 = new javax.swing.JLabel();
        b03 = new javax.swing.JLabel();
        b04 = new javax.swing.JLabel();
        b05 = new javax.swing.JLabel();
        b06 = new javax.swing.JLabel();
        b07 = new javax.swing.JLabel();
        b08 = new javax.swing.JLabel();
        b09 = new javax.swing.JLabel();
        b10 = new javax.swing.JLabel();
        b11 = new javax.swing.JLabel();
        b12 = new javax.swing.JLabel();
        b13 = new javax.swing.JLabel();
        b14 = new javax.swing.JLabel();
        b15 = new javax.swing.JLabel();
        b16 = new javax.swing.JLabel();
        b17 = new javax.swing.JLabel();
        b18 = new javax.swing.JLabel();
        b19 = new javax.swing.JLabel();
        b20 = new javax.swing.JLabel();
        b34 = new javax.swing.JLabel();
        b33 = new javax.swing.JLabel();
        b25 = new javax.swing.JLabel();
        b36 = new javax.swing.JLabel();
        b35 = new javax.swing.JLabel();
        b30 = new javax.swing.JLabel();
        b32 = new javax.swing.JLabel();
        b24 = new javax.swing.JLabel();
        b31 = new javax.swing.JLabel();
        b23 = new javax.swing.JLabel();
        b26 = new javax.swing.JLabel();
        b22 = new javax.swing.JLabel();
        b27 = new javax.swing.JLabel();
        b37 = new javax.swing.JLabel();
        b38 = new javax.swing.JLabel();
        b39 = new javax.swing.JLabel();
        b28 = new javax.swing.JLabel();
        b29 = new javax.swing.JLabel();
        b21 = new javax.swing.JLabel();
        b49 = new javax.swing.JLabel();
        b47 = new javax.swing.JLabel();
        b48 = new javax.swing.JLabel();
        b43 = new javax.swing.JLabel();
        b44 = new javax.swing.JLabel();
        b46 = new javax.swing.JLabel();
        b40 = new javax.swing.JLabel();
        b45 = new javax.swing.JLabel();
        b42 = new javax.swing.JLabel();
        b41 = new javax.swing.JLabel();
        b70 = new javax.swing.JLabel();
        b83 = new javax.swing.JLabel();
        b84 = new javax.swing.JLabel();
        b86 = new javax.swing.JLabel();
        b75 = new javax.swing.JLabel();
        b80 = new javax.swing.JLabel();
        b85 = new javax.swing.JLabel();
        b55 = new javax.swing.JLabel();
        b74 = new javax.swing.JLabel();
        b82 = new javax.swing.JLabel();
        b81 = new javax.swing.JLabel();
        b54 = new javax.swing.JLabel();
        b53 = new javax.swing.JLabel();
        b52 = new javax.swing.JLabel();
        b51 = new javax.swing.JLabel();
        b79 = new javax.swing.JLabel();
        b71 = new javax.swing.JLabel();
        b89 = new javax.swing.JLabel();
        b78 = new javax.swing.JLabel();
        b87 = new javax.swing.JLabel();
        b88 = new javax.swing.JLabel();
        b72 = new javax.swing.JLabel();
        b77 = new javax.swing.JLabel();
        b73 = new javax.swing.JLabel();
        b76 = new javax.swing.JLabel();
        b56 = new javax.swing.JLabel();
        b57 = new javax.swing.JLabel();
        b58 = new javax.swing.JLabel();
        b59 = new javax.swing.JLabel();
        b50 = new javax.swing.JLabel();
        b95 = new javax.swing.JLabel();
        b92 = new javax.swing.JLabel();
        b91 = new javax.swing.JLabel();
        b93 = new javax.swing.JLabel();
        b64 = new javax.swing.JLabel();
        b94 = new javax.swing.JLabel();
        b63 = new javax.swing.JLabel();
        b96 = new javax.swing.JLabel();
        b66 = new javax.swing.JLabel();
        b90 = new javax.swing.JLabel();
        b65 = new javax.swing.JLabel();
        b60 = new javax.swing.JLabel();
        b99 = new javax.swing.JLabel();
        b97 = new javax.swing.JLabel();
        b62 = new javax.swing.JLabel();
        b98 = new javax.swing.JLabel();
        b61 = new javax.swing.JLabel();
        b67 = new javax.swing.JLabel();
        b68 = new javax.swing.JLabel();
        b69 = new javax.swing.JLabel();
        b0 = new javax.swing.JLabel();
        b1 = new javax.swing.JLabel();
        b3 = new javax.swing.JLabel();
        b2 = new javax.swing.JLabel();
        b4 = new javax.swing.JLabel();
        b5 = new javax.swing.JLabel();
        b8 = new javax.swing.JLabel();
        b9 = new javax.swing.JLabel();
        b7 = new javax.swing.JLabel();
        b6 = new javax.swing.JLabel();
        ba = new javax.swing.JLabel();
        bb = new javax.swing.JLabel();
        bc = new javax.swing.JLabel();
        bf = new javax.swing.JLabel();
        be = new javax.swing.JLabel();
        bd = new javax.swing.JLabel();
        bi = new javax.swing.JLabel();
        bh = new javax.swing.JLabel();
        bg = new javax.swing.JLabel();
        bj = new javax.swing.JLabel();
        lblPlayerA = new javax.swing.JLabel();
        lblPlayerB = new javax.swing.JLabel();
        scrollLog = new javax.swing.JScrollPane();
        txtLog = new javax.swing.JTextArea();
        lblLog = new javax.swing.JLabel();
        btnReseta = new javax.swing.JButton();
        sldVelocidade = new javax.swing.JSlider();
        lblVelocidade = new javax.swing.JLabel();
        lblBPecasRestantes = new javax.swing.JLabel();
        lblAPecasRestantes = new javax.swing.JLabel();
        lblStatusA = new javax.swing.JLabel();
        lblStatusB = new javax.swing.JLabel();
        lblEficienciaA = new javax.swing.JLabel();
        lblEficienciaB = new javax.swing.JLabel();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        bigboxPlayerA.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        a00.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a00.setText("a00");
        a00.setName("a00");
        a00.setPreferredSize(new java.awt.Dimension(30, 30));

        a01.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a01.setText("a01");
        a01.setName("a01");
        a01.setPreferredSize(new java.awt.Dimension(30, 30));

        a02.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a02.setText("a02");
        a02.setName("a02");
        a02.setPreferredSize(new java.awt.Dimension(30, 30));

        a03.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a03.setText("a03");
        a03.setName("a03");
        a03.setPreferredSize(new java.awt.Dimension(30, 30));

        a04.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a04.setText("a04");
        a04.setName("a04");
        a04.setPreferredSize(new java.awt.Dimension(30, 30));

        a05.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a05.setText("a05");
        a05.setName("a05");
        a05.setPreferredSize(new java.awt.Dimension(30, 30));

        a06.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a06.setText("a06");
        a06.setName("a06");
        a06.setPreferredSize(new java.awt.Dimension(30, 30));

        a07.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a07.setText("a07");
        a07.setName("a07");
        a07.setPreferredSize(new java.awt.Dimension(30, 30));

        a08.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a08.setText("a08");
        a08.setName("a08");
        a08.setPreferredSize(new java.awt.Dimension(30, 30));

        a09.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a09.setText("a09");
        a09.setName("a09");
        a09.setPreferredSize(new java.awt.Dimension(30, 30));

        a10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a10.setText("a10");
        a10.setName("a10");
        a10.setPreferredSize(new java.awt.Dimension(30, 30));

        a11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a11.setText("a11");
        a11.setName("a11");
        a11.setPreferredSize(new java.awt.Dimension(30, 30));

        a12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a12.setText("a12");
        a12.setName("a12");
        a12.setPreferredSize(new java.awt.Dimension(30, 30));

        a13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a13.setText("a13");
        a13.setName("a13");
        a13.setPreferredSize(new java.awt.Dimension(30, 30));

        a14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a14.setText("a14");
        a14.setName("a14");
        a14.setPreferredSize(new java.awt.Dimension(30, 30));

        a15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a15.setText("a15");
        a15.setName("a15");
        a15.setPreferredSize(new java.awt.Dimension(30, 30));

        a16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a16.setText("a16");
        a16.setName("a16");
        a16.setPreferredSize(new java.awt.Dimension(30, 30));

        a17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a17.setText("a17");
        a17.setName("a17");
        a17.setPreferredSize(new java.awt.Dimension(30, 30));

        a18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a18.setText("a18");
        a18.setName("a18");
        a18.setPreferredSize(new java.awt.Dimension(30, 30));

        a19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a19.setText("a19");
        a19.setName("a19");
        a19.setPreferredSize(new java.awt.Dimension(30, 30));

        a20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a20.setText("a20");
        a20.setName("a20");
        a20.setPreferredSize(new java.awt.Dimension(30, 30));

        a34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a34.setText("a34");
        a34.setName("a34");
        a34.setPreferredSize(new java.awt.Dimension(30, 30));

        a33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a33.setText("a33");
        a33.setName("a33");
        a33.setPreferredSize(new java.awt.Dimension(30, 30));

        a25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a25.setText("a25");
        a25.setName("a25");
        a25.setPreferredSize(new java.awt.Dimension(30, 30));

        a36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a36.setText("a36");
        a36.setName("a36");
        a36.setPreferredSize(new java.awt.Dimension(30, 30));

        a35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a35.setText("a35");
        a35.setName("a35");
        a35.setPreferredSize(new java.awt.Dimension(30, 30));

        a30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a30.setText("a30");
        a30.setName("a30");
        a30.setPreferredSize(new java.awt.Dimension(30, 30));

        a32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a32.setText("a32");
        a32.setName("a32");
        a32.setPreferredSize(new java.awt.Dimension(30, 30));

        a24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a24.setText("a24");
        a24.setName("a24");
        a24.setPreferredSize(new java.awt.Dimension(30, 30));

        a31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a31.setText("a31");
        a31.setName("a31");
        a31.setPreferredSize(new java.awt.Dimension(30, 30));

        a23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a23.setText("a23");
        a23.setName("a23");
        a23.setPreferredSize(new java.awt.Dimension(30, 30));

        a26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a26.setText("a26");
        a26.setName("a26");
        a26.setPreferredSize(new java.awt.Dimension(30, 30));

        a22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a22.setText("a22");
        a22.setName("a22");
        a22.setPreferredSize(new java.awt.Dimension(30, 30));

        a27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a27.setText("a27");
        a27.setName("a27");
        a27.setPreferredSize(new java.awt.Dimension(30, 30));

        a37.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a37.setText("a37");
        a37.setName("a37");
        a37.setPreferredSize(new java.awt.Dimension(30, 30));

        a38.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a38.setText("a38");
        a38.setName("a38");
        a38.setPreferredSize(new java.awt.Dimension(30, 30));

        a39.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a39.setText("a39");
        a39.setName("a39");
        a39.setPreferredSize(new java.awt.Dimension(30, 30));

        a28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a28.setText("a28");
        a28.setName("a28");
        a28.setPreferredSize(new java.awt.Dimension(30, 30));

        a29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a29.setText("a29");
        a29.setName("a29");
        a29.setPreferredSize(new java.awt.Dimension(30, 30));

        a21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a21.setText("a21");
        a21.setName("a21");
        a21.setPreferredSize(new java.awt.Dimension(30, 30));

        a49.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a49.setText("a49");
        a49.setName("a49");
        a49.setPreferredSize(new java.awt.Dimension(30, 30));

        a47.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a47.setText("a47");
        a47.setName("a47");
        a47.setPreferredSize(new java.awt.Dimension(30, 30));

        a48.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a48.setText("a48");
        a48.setName("a48");
        a48.setPreferredSize(new java.awt.Dimension(30, 30));

        a43.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a43.setText("a43");
        a43.setName("a43");
        a43.setPreferredSize(new java.awt.Dimension(30, 30));

        a44.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a44.setText("a44");
        a44.setName("a44");
        a44.setPreferredSize(new java.awt.Dimension(30, 30));

        a46.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a46.setText("a46");
        a46.setName("a46");
        a46.setPreferredSize(new java.awt.Dimension(30, 30));

        a40.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a40.setText("a40");
        a40.setName("a40");
        a40.setPreferredSize(new java.awt.Dimension(30, 30));

        a45.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a45.setText("a45");
        a45.setName("a45");
        a45.setPreferredSize(new java.awt.Dimension(30, 30));

        a42.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a42.setText("a42");
        a42.setName("a42");
        a42.setPreferredSize(new java.awt.Dimension(30, 30));

        a41.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a41.setText("a41");
        a41.setName("a41");
        a41.setPreferredSize(new java.awt.Dimension(30, 30));

        a70.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a70.setText("a70");
        a70.setName("a70");
        a70.setPreferredSize(new java.awt.Dimension(30, 30));

        a83.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a83.setText("a83");
        a83.setName("a83");
        a83.setPreferredSize(new java.awt.Dimension(30, 30));

        a84.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a84.setText("a84");
        a84.setName("a84");
        a84.setPreferredSize(new java.awt.Dimension(30, 30));

        a86.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a86.setText("a86");
        a86.setName("a86");
        a86.setPreferredSize(new java.awt.Dimension(30, 30));

        a75.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a75.setText("a75");
        a75.setName("a75");
        a75.setPreferredSize(new java.awt.Dimension(30, 30));

        a80.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a80.setText("a80");
        a80.setName("a80");
        a80.setPreferredSize(new java.awt.Dimension(30, 30));

        a85.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a85.setText("a85");
        a85.setName("a85");
        a85.setPreferredSize(new java.awt.Dimension(30, 30));

        a55.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a55.setText("a55");
        a55.setName("a55");
        a55.setPreferredSize(new java.awt.Dimension(30, 30));

        a74.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a74.setText("a74");
        a74.setName("a74");
        a74.setPreferredSize(new java.awt.Dimension(30, 30));

        a82.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a82.setText("a82");
        a82.setName("a82");
        a82.setPreferredSize(new java.awt.Dimension(30, 30));

        a81.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a81.setText("a81");
        a81.setName("a81");
        a81.setPreferredSize(new java.awt.Dimension(30, 30));

        a54.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a54.setText("a54");
        a54.setName("a54");
        a54.setPreferredSize(new java.awt.Dimension(30, 30));

        a53.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a53.setText("a53");
        a53.setName("a53");
        a53.setPreferredSize(new java.awt.Dimension(30, 30));

        a52.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a52.setText("a52");
        a52.setName("a52");
        a52.setPreferredSize(new java.awt.Dimension(30, 30));

        a51.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a51.setText("a51");
        a51.setName("a51");
        a51.setPreferredSize(new java.awt.Dimension(30, 30));

        a79.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a79.setText("a79");
        a79.setName("a79");
        a79.setPreferredSize(new java.awt.Dimension(30, 30));

        a71.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a71.setText("a71");
        a71.setName("a71");
        a71.setPreferredSize(new java.awt.Dimension(30, 30));

        a89.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a89.setText("a89");
        a89.setName("a89");
        a89.setPreferredSize(new java.awt.Dimension(30, 30));

        a78.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a78.setText("a78");
        a78.setName("a78");
        a78.setPreferredSize(new java.awt.Dimension(30, 30));

        a87.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a87.setText("a87");
        a87.setName("a87");
        a87.setPreferredSize(new java.awt.Dimension(30, 30));

        a88.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a88.setText("a88");
        a88.setName("a88");
        a88.setPreferredSize(new java.awt.Dimension(30, 30));

        a72.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a72.setText("a72");
        a72.setName("a72");
        a72.setPreferredSize(new java.awt.Dimension(30, 30));

        a77.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a77.setText("a77");
        a77.setName("a77");
        a77.setPreferredSize(new java.awt.Dimension(30, 30));

        a73.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a73.setText("a73");
        a73.setName("a73");
        a73.setPreferredSize(new java.awt.Dimension(30, 30));

        a76.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a76.setText("a76");
        a76.setName("a76");
        a76.setPreferredSize(new java.awt.Dimension(30, 30));

        a56.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a56.setText("a56");
        a56.setName("a56");
        a56.setPreferredSize(new java.awt.Dimension(30, 30));

        a57.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a57.setText("a57");
        a57.setName("a57");
        a57.setPreferredSize(new java.awt.Dimension(30, 30));

        a58.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a58.setText("a58");
        a58.setName("a58");
        a58.setPreferredSize(new java.awt.Dimension(30, 30));

        a59.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a59.setText("a59");
        a59.setName("a59");
        a59.setPreferredSize(new java.awt.Dimension(30, 30));

        a50.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a50.setText("a50");
        a50.setName("a50");
        a50.setPreferredSize(new java.awt.Dimension(30, 30));

        a95.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a95.setText("a95");
        a95.setName("a95");
        a95.setPreferredSize(new java.awt.Dimension(30, 30));

        a92.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a92.setText("a92");
        a92.setName("a92");
        a92.setPreferredSize(new java.awt.Dimension(30, 30));

        a91.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a91.setText("a91");
        a91.setName("a91");
        a91.setPreferredSize(new java.awt.Dimension(30, 30));

        a93.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a93.setText("a93");
        a93.setName("a93");
        a93.setPreferredSize(new java.awt.Dimension(30, 30));

        a64.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a64.setText("a64");
        a64.setName("a64");
        a64.setPreferredSize(new java.awt.Dimension(30, 30));

        a94.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a94.setText("a94");
        a94.setName("a94");
        a94.setPreferredSize(new java.awt.Dimension(30, 30));

        a63.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a63.setText("a63");
        a63.setName("a63");
        a63.setPreferredSize(new java.awt.Dimension(30, 30));

        a96.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a96.setText("a96");
        a96.setName("a96");
        a96.setPreferredSize(new java.awt.Dimension(30, 30));

        a66.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a66.setText("a66");
        a66.setName("a66");
        a66.setPreferredSize(new java.awt.Dimension(30, 30));

        a90.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a90.setText("a90");
        a90.setName("a90");
        a90.setPreferredSize(new java.awt.Dimension(30, 30));

        a65.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a65.setText("a65");
        a65.setName("a65");
        a65.setPreferredSize(new java.awt.Dimension(30, 30));

        a60.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a60.setText("a60");
        a60.setName("a60");
        a60.setPreferredSize(new java.awt.Dimension(30, 30));

        a99.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a99.setText("a99");
        a99.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        a99.setName("a99");
        a99.setPreferredSize(new java.awt.Dimension(30, 30));

        a97.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a97.setText("a97");
        a97.setName("a97");
        a97.setPreferredSize(new java.awt.Dimension(30, 30));

        a62.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a62.setText("a62");
        a62.setName("a62");
        a62.setPreferredSize(new java.awt.Dimension(30, 30));

        a98.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a98.setText("a98");
        a98.setName("a98");
        a98.setPreferredSize(new java.awt.Dimension(30, 30));

        a61.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a61.setText("a61");
        a61.setName("a61");
        a61.setPreferredSize(new java.awt.Dimension(30, 30));

        a67.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a67.setText("a67");
        a67.setName("a67");
        a67.setPreferredSize(new java.awt.Dimension(30, 30));

        a68.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a68.setText("a68");
        a68.setName("a68");
        a68.setPreferredSize(new java.awt.Dimension(30, 30));

        a69.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        a69.setText("a69");
        a69.setName("a69");
        a69.setPreferredSize(new java.awt.Dimension(30, 30));

        aj.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        aj.setText("J");

        af.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        af.setText("F");

        ae.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ae.setText("E");

        ac.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ac.setText("C");

        ah.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ah.setText("H");

        ag.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ag.setText("G");

        ad.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ad.setText("D");

        ai.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ai.setText("I");

        ab.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ab.setText("B");

        aa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        aa.setText("A");

        a0.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        a0.setText("0");

        a1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        a1.setText("1");

        a2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        a2.setText("2");

        a9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        a9.setText("9");

        a8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        a8.setText("8");

        a7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        a7.setText("7");

        a6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        a6.setText("6");

        a5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        a5.setText("5");

        a4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        a4.setText("4");

        a3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        a3.setText("3");

        javax.swing.GroupLayout tabPlayerALayout = new javax.swing.GroupLayout(tabPlayerA);
        tabPlayerA.setLayout(tabPlayerALayout);
        tabPlayerALayout.setHorizontalGroup(
            tabPlayerALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabPlayerALayout.createSequentialGroup()
                .addGroup(tabPlayerALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabPlayerALayout.createSequentialGroup()
                        .addComponent(a0, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(a1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(a2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(a3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(a4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(a5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(a6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(a7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(a8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(a9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabPlayerALayout.createSequentialGroup()
                        .addGroup(tabPlayerALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(tabPlayerALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(aa, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(ab, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(ac, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabPlayerALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(tabPlayerALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(ad, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ae, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(af, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabPlayerALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(tabPlayerALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(ag, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ah, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(ai, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(aj, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(tabPlayerALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(tabPlayerALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(tabPlayerALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(tabPlayerALayout.createSequentialGroup()
                                        .addComponent(a20, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(5, 5, 5)
                                        .addComponent(a21, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a22, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a23, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a24, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a25, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a26, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a27, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a28, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a29, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(tabPlayerALayout.createSequentialGroup()
                                        .addComponent(a30, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(5, 5, 5)
                                        .addComponent(a31, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a32, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a33, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a34, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a35, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a36, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a37, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a38, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a39, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(tabPlayerALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(tabPlayerALayout.createSequentialGroup()
                                        .addComponent(a00, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(5, 5, 5)
                                        .addComponent(a01, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a02, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a03, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a04, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a05, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a06, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a07, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a08, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a09, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(tabPlayerALayout.createSequentialGroup()
                                        .addComponent(a10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(5, 5, 5)
                                        .addComponent(a11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a16, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a17, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a18, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a19, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(tabPlayerALayout.createSequentialGroup()
                                .addComponent(a40, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(5, 5, 5)
                                .addComponent(a41, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(a42, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(a43, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(a44, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(a45, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(a46, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(a47, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(a48, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(a49, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(tabPlayerALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(tabPlayerALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(tabPlayerALayout.createSequentialGroup()
                                        .addComponent(a70, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(5, 5, 5)
                                        .addComponent(a71, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a72, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a73, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a74, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a75, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a76, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a77, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a78, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a79, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(tabPlayerALayout.createSequentialGroup()
                                        .addComponent(a80, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(5, 5, 5)
                                        .addComponent(a81, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a82, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a83, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a84, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a85, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a86, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a87, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a88, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a89, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(tabPlayerALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(tabPlayerALayout.createSequentialGroup()
                                        .addComponent(a50, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(5, 5, 5)
                                        .addComponent(a51, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a52, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a53, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a54, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a55, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a56, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a57, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a58, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a59, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(tabPlayerALayout.createSequentialGroup()
                                        .addComponent(a60, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(5, 5, 5)
                                        .addComponent(a61, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a62, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a63, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a64, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a65, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a66, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a67, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a68, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(a69, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(tabPlayerALayout.createSequentialGroup()
                                .addComponent(a90, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(5, 5, 5)
                                .addComponent(a91, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(a92, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(a93, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(a94, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(a95, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(a96, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(a97, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(a98, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(a99, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        tabPlayerALayout.setVerticalGroup(
            tabPlayerALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabPlayerALayout.createSequentialGroup()
                .addGroup(tabPlayerALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(a0)
                    .addComponent(a1)
                    .addComponent(a2)
                    .addComponent(a3)
                    .addComponent(a4)
                    .addComponent(a5)
                    .addComponent(a6)
                    .addComponent(a7)
                    .addComponent(a8)
                    .addComponent(a9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tabPlayerALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(aa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(tabPlayerALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(a00, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a01, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a02, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a03, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a04, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a05, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a06, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a07, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a08, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a09, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tabPlayerALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ab, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(tabPlayerALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(a10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a16, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a17, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a18, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a19, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tabPlayerALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ac, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(tabPlayerALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(a20, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a21, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a22, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a23, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a24, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a25, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a26, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a27, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a28, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a29, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tabPlayerALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ad, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(tabPlayerALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(a30, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a31, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a32, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a33, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a34, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a35, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a36, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a37, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a38, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a39, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tabPlayerALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ae, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(tabPlayerALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(a40, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a41, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a42, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a43, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a44, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a45, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a46, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a47, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a48, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a49, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tabPlayerALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(af, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(tabPlayerALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(a50, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a51, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a52, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a53, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a54, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a55, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a56, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a57, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a58, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a59, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tabPlayerALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ag, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(tabPlayerALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(a60, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a61, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a62, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a63, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a64, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a65, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a66, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a67, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a68, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a69, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tabPlayerALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(tabPlayerALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(a70, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a71, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a72, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a73, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a74, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a75, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a76, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a77, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a78, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a79, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tabPlayerALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(tabPlayerALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(a80, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a81, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a82, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a83, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a84, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a85, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a86, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a87, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a88, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(a89, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tabPlayerALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(a90, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(a91, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(a92, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(a93, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(a94, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(a95, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(a96, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(a97, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(a98, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(a99, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(aj, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout bigboxPlayerALayout = new javax.swing.GroupLayout(bigboxPlayerA);
        bigboxPlayerA.setLayout(bigboxPlayerALayout);
        bigboxPlayerALayout.setHorizontalGroup(
            bigboxPlayerALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabPlayerA, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        bigboxPlayerALayout.setVerticalGroup(
            bigboxPlayerALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bigboxPlayerALayout.createSequentialGroup()
                .addComponent(tabPlayerA, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        bigboxPlayerB.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        b00.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b00.setText("b00");
        b00.setName("b00");
        b00.setPreferredSize(new java.awt.Dimension(30, 30));

        b01.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b01.setText("b01");
        b01.setName("b01");
        b01.setPreferredSize(new java.awt.Dimension(30, 30));

        b02.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b02.setText("b02");
        b02.setName("b02");
        b02.setPreferredSize(new java.awt.Dimension(30, 30));

        b03.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b03.setText("b03");
        b03.setName("b03");
        b03.setPreferredSize(new java.awt.Dimension(30, 30));

        b04.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b04.setText("b04");
        b04.setName("b04");
        b04.setPreferredSize(new java.awt.Dimension(30, 30));

        b05.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b05.setText("b05");
        b05.setName("b05");
        b05.setPreferredSize(new java.awt.Dimension(30, 30));

        b06.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b06.setText("b06");
        b06.setName("b06");
        b06.setPreferredSize(new java.awt.Dimension(30, 30));

        b07.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b07.setText("b07");
        b07.setName("b07");
        b07.setPreferredSize(new java.awt.Dimension(30, 30));

        b08.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b08.setText("b08");
        b08.setName("b08");
        b08.setPreferredSize(new java.awt.Dimension(30, 30));

        b09.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b09.setText("b09");
        b09.setName("b09");
        b09.setPreferredSize(new java.awt.Dimension(30, 30));

        b10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b10.setText("b10");
        b10.setName("b10");
        b10.setPreferredSize(new java.awt.Dimension(30, 30));

        b11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b11.setText("b11");
        b11.setName("b11");
        b11.setPreferredSize(new java.awt.Dimension(30, 30));

        b12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b12.setText("b12");
        b12.setName("b12");
        b12.setPreferredSize(new java.awt.Dimension(30, 30));

        b13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b13.setText("b13");
        b13.setName("b13");
        b13.setPreferredSize(new java.awt.Dimension(30, 30));

        b14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b14.setText("b14");
        b14.setName("b14");
        b14.setPreferredSize(new java.awt.Dimension(30, 30));

        b15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b15.setText("b15");
        b15.setName("b15");
        b15.setPreferredSize(new java.awt.Dimension(30, 30));

        b16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b16.setText("b16");
        b16.setName("b16");
        b16.setPreferredSize(new java.awt.Dimension(30, 30));

        b17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b17.setText("b17");
        b17.setName("b17");
        b17.setPreferredSize(new java.awt.Dimension(30, 30));

        b18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b18.setText("b18");
        b18.setName("b18");
        b18.setPreferredSize(new java.awt.Dimension(30, 30));

        b19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b19.setText("b19");
        b19.setName("b19");
        b19.setPreferredSize(new java.awt.Dimension(30, 30));

        b20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b20.setText("b20");
        b20.setName("b20");
        b20.setPreferredSize(new java.awt.Dimension(30, 30));

        b34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b34.setText("b34");
        b34.setName("b34");
        b34.setPreferredSize(new java.awt.Dimension(30, 30));

        b33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b33.setText("b33");
        b33.setName("b33");
        b33.setPreferredSize(new java.awt.Dimension(30, 30));

        b25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b25.setText("b25");
        b25.setName("b25");
        b25.setPreferredSize(new java.awt.Dimension(30, 30));

        b36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b36.setText("b36");
        b36.setName("b36");
        b36.setPreferredSize(new java.awt.Dimension(30, 30));

        b35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b35.setText("b35");
        b35.setName("b35");
        b35.setPreferredSize(new java.awt.Dimension(30, 30));

        b30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b30.setText("b30");
        b30.setName("b30");
        b30.setPreferredSize(new java.awt.Dimension(30, 30));

        b32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b32.setText("b32");
        b32.setName("b32");
        b32.setPreferredSize(new java.awt.Dimension(30, 30));

        b24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b24.setText("b24");
        b24.setName("b24");
        b24.setPreferredSize(new java.awt.Dimension(30, 30));

        b31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b31.setText("b31");
        b31.setName("b31");
        b31.setPreferredSize(new java.awt.Dimension(30, 30));

        b23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b23.setText("b23");
        b23.setName("b23");
        b23.setPreferredSize(new java.awt.Dimension(30, 30));

        b26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b26.setText("b26");
        b26.setName("b26");
        b26.setPreferredSize(new java.awt.Dimension(30, 30));

        b22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b22.setText("b22");
        b22.setName("b22");
        b22.setPreferredSize(new java.awt.Dimension(30, 30));

        b27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b27.setText("b27");
        b27.setName("b27");
        b27.setPreferredSize(new java.awt.Dimension(30, 30));

        b37.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b37.setText("b37");
        b37.setName("b37");
        b37.setPreferredSize(new java.awt.Dimension(30, 30));

        b38.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b38.setText("b38");
        b38.setName("b38");
        b38.setPreferredSize(new java.awt.Dimension(30, 30));

        b39.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b39.setText("b39");
        b39.setName("b39");
        b39.setPreferredSize(new java.awt.Dimension(30, 30));

        b28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b28.setText("b28");
        b28.setName("b28");
        b28.setPreferredSize(new java.awt.Dimension(30, 30));

        b29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b29.setText("b29");
        b29.setName("b29");
        b29.setPreferredSize(new java.awt.Dimension(30, 30));

        b21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b21.setText("b21");
        b21.setName("b21");
        b21.setPreferredSize(new java.awt.Dimension(30, 30));

        b49.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b49.setText("b49");
        b49.setName("b49");
        b49.setPreferredSize(new java.awt.Dimension(30, 30));

        b47.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b47.setText("b47");
        b47.setName("b47");
        b47.setPreferredSize(new java.awt.Dimension(30, 30));

        b48.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b48.setText("b48");
        b48.setName("b48");
        b48.setPreferredSize(new java.awt.Dimension(30, 30));

        b43.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b43.setText("b43");
        b43.setName("b43");
        b43.setPreferredSize(new java.awt.Dimension(30, 30));

        b44.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b44.setText("b44");
        b44.setName("b44");
        b44.setPreferredSize(new java.awt.Dimension(30, 30));

        b46.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b46.setText("b46");
        b46.setName("b46");
        b46.setPreferredSize(new java.awt.Dimension(30, 30));

        b40.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b40.setText("b40");
        b40.setName("b40");
        b40.setPreferredSize(new java.awt.Dimension(30, 30));

        b45.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b45.setText("b45");
        b45.setName("b45");
        b45.setPreferredSize(new java.awt.Dimension(30, 30));

        b42.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b42.setText("b42");
        b42.setName("b42");
        b42.setPreferredSize(new java.awt.Dimension(30, 30));

        b41.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b41.setText("b41");
        b41.setName("b41");
        b41.setPreferredSize(new java.awt.Dimension(30, 30));

        b70.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b70.setText("b70");
        b70.setName("b70");
        b70.setPreferredSize(new java.awt.Dimension(30, 30));

        b83.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b83.setText("b83");
        b83.setName("b83");
        b83.setPreferredSize(new java.awt.Dimension(30, 30));

        b84.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b84.setText("b84");
        b84.setName("b84");
        b84.setPreferredSize(new java.awt.Dimension(30, 30));

        b86.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b86.setText("b86");
        b86.setName("b86");
        b86.setPreferredSize(new java.awt.Dimension(30, 30));

        b75.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b75.setText("b75");
        b75.setName("b75");
        b75.setPreferredSize(new java.awt.Dimension(30, 30));

        b80.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b80.setText("b80");
        b80.setName("b80");
        b80.setPreferredSize(new java.awt.Dimension(30, 30));

        b85.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b85.setText("b85");
        b85.setName("b85");
        b85.setPreferredSize(new java.awt.Dimension(30, 30));

        b55.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b55.setText("g55");
        b55.setName("b55");
        b55.setPreferredSize(new java.awt.Dimension(30, 30));

        b74.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b74.setText("b74");
        b74.setName("b74");
        b74.setPreferredSize(new java.awt.Dimension(30, 30));

        b82.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b82.setText("b82");
        b82.setName("b82");
        b82.setPreferredSize(new java.awt.Dimension(30, 30));

        b81.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b81.setText("b81");
        b81.setName("b81");
        b81.setPreferredSize(new java.awt.Dimension(30, 30));

        b54.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b54.setText("b54");
        b54.setName("b54");
        b54.setPreferredSize(new java.awt.Dimension(30, 30));

        b53.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b53.setText("b53");
        b53.setName("b53");
        b53.setPreferredSize(new java.awt.Dimension(30, 30));

        b52.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b52.setText("b52");
        b52.setName("b52");
        b52.setPreferredSize(new java.awt.Dimension(30, 30));

        b51.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b51.setText("b51");
        b51.setName("b51");
        b51.setPreferredSize(new java.awt.Dimension(30, 30));

        b79.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b79.setText("b79");
        b79.setName("b79");
        b79.setPreferredSize(new java.awt.Dimension(30, 30));

        b71.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b71.setText("b71");
        b71.setName("b71");
        b71.setPreferredSize(new java.awt.Dimension(30, 30));

        b89.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b89.setText("b89");
        b89.setName("b89");
        b89.setPreferredSize(new java.awt.Dimension(30, 30));

        b78.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b78.setText("b78");
        b78.setName("b78");
        b78.setPreferredSize(new java.awt.Dimension(30, 30));

        b87.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b87.setText("b87");
        b87.setName("b87");
        b87.setPreferredSize(new java.awt.Dimension(30, 30));

        b88.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b88.setText("b88");
        b88.setName("b88");
        b88.setPreferredSize(new java.awt.Dimension(30, 30));

        b72.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b72.setText("b72");
        b72.setName("b72");
        b72.setPreferredSize(new java.awt.Dimension(30, 30));

        b77.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b77.setText("b77");
        b77.setName("b77");
        b77.setPreferredSize(new java.awt.Dimension(30, 30));

        b73.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b73.setText("b73");
        b73.setName("b73");
        b73.setPreferredSize(new java.awt.Dimension(30, 30));

        b76.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b76.setText("b76");
        b76.setName("b76");
        b76.setPreferredSize(new java.awt.Dimension(30, 30));

        b56.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b56.setText("b56");
        b56.setName("b56");
        b56.setPreferredSize(new java.awt.Dimension(30, 30));

        b57.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b57.setText("b57");
        b57.setName("b57");
        b57.setPreferredSize(new java.awt.Dimension(30, 30));

        b58.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b58.setText("b58");
        b58.setName("b58");
        b58.setPreferredSize(new java.awt.Dimension(30, 30));

        b59.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b59.setText("b59");
        b59.setName("b59");
        b59.setPreferredSize(new java.awt.Dimension(30, 30));

        b50.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b50.setText("b50");
        b50.setName("b50");
        b50.setPreferredSize(new java.awt.Dimension(30, 30));

        b95.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b95.setText("b95");
        b95.setName("b95");
        b95.setPreferredSize(new java.awt.Dimension(30, 30));

        b92.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b92.setText("b92");
        b92.setName("b92");
        b92.setPreferredSize(new java.awt.Dimension(30, 30));

        b91.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b91.setText("b91");
        b91.setName("b91");
        b91.setPreferredSize(new java.awt.Dimension(30, 30));

        b93.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b93.setText("b93");
        b93.setName("b93");
        b93.setPreferredSize(new java.awt.Dimension(30, 30));

        b64.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b64.setText("b64");
        b64.setName("b64");
        b64.setPreferredSize(new java.awt.Dimension(30, 30));

        b94.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b94.setText("b94");
        b94.setName("b94");
        b94.setPreferredSize(new java.awt.Dimension(30, 30));

        b63.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b63.setText("b63");
        b63.setName("b63");
        b63.setPreferredSize(new java.awt.Dimension(30, 30));

        b96.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b96.setText("b96");
        b96.setName("b96");
        b96.setPreferredSize(new java.awt.Dimension(30, 30));

        b66.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b66.setText("b66");
        b66.setName("b66");
        b66.setPreferredSize(new java.awt.Dimension(30, 30));

        b90.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b90.setText("b90");
        b90.setName("b90");
        b90.setPreferredSize(new java.awt.Dimension(30, 30));

        b65.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b65.setText("b65");
        b65.setName("b65");
        b65.setPreferredSize(new java.awt.Dimension(30, 30));

        b60.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b60.setText("b60");
        b60.setName("b60");
        b60.setPreferredSize(new java.awt.Dimension(30, 30));

        b99.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b99.setText("b99");
        b99.setName("b99");
        b99.setPreferredSize(new java.awt.Dimension(30, 30));

        b97.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b97.setText("b97");
        b97.setName("b97");
        b97.setPreferredSize(new java.awt.Dimension(30, 30));

        b62.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b62.setText("b62");
        b62.setName("b62");
        b62.setPreferredSize(new java.awt.Dimension(30, 30));

        b98.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b98.setText("b98");
        b98.setName("b98");
        b98.setPreferredSize(new java.awt.Dimension(30, 30));

        b61.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b61.setText("b61");
        b61.setName("b61");
        b61.setPreferredSize(new java.awt.Dimension(30, 30));

        b67.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b67.setText("b67");
        b67.setName("b67");
        b67.setPreferredSize(new java.awt.Dimension(30, 30));

        b68.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b68.setText("b68");
        b68.setName("b68");
        b68.setPreferredSize(new java.awt.Dimension(30, 30));

        b69.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/campo.png"))); // NOI18N
        b69.setText("b69");
        b69.setName("b69");
        b69.setPreferredSize(new java.awt.Dimension(30, 30));

        b0.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b0.setText("0");

        b1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b1.setText("1");

        b3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b3.setText("3");

        b2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b2.setText("2");

        b4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b4.setText("4");

        b5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b5.setText("5");

        b8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b8.setText("8");

        b9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b9.setText("9");

        b7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b7.setText("7");

        b6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b6.setText("6");

        ba.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ba.setText("A");

        bb.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        bb.setText("B");

        bc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        bc.setText("C");

        bf.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        bf.setText("F");

        be.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        be.setText("E");

        bd.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        bd.setText("D");

        bi.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        bi.setText("I");

        bh.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        bh.setText("H");

        bg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        bg.setText("G");

        bj.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        bj.setText("J");

        javax.swing.GroupLayout tabPlayerBLayout = new javax.swing.GroupLayout(tabPlayerB);
        tabPlayerB.setLayout(tabPlayerBLayout);
        tabPlayerBLayout.setHorizontalGroup(
            tabPlayerBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabPlayerBLayout.createSequentialGroup()
                .addGroup(tabPlayerBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tabPlayerBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(ba, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(bb, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(bc, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabPlayerBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(tabPlayerBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(bd, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(be, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(bf, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabPlayerBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(tabPlayerBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(bg, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bh, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(bi, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(bj, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tabPlayerBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(tabPlayerBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(tabPlayerBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(tabPlayerBLayout.createSequentialGroup()
                                .addComponent(b20, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(5, 5, 5)
                                .addComponent(b21, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b22, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b23, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b24, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b25, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b26, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b27, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b28, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b29, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(tabPlayerBLayout.createSequentialGroup()
                                .addComponent(b30, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(5, 5, 5)
                                .addComponent(b31, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b32, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b33, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b34, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b35, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b36, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b37, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b38, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b39, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(tabPlayerBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(tabPlayerBLayout.createSequentialGroup()
                                .addComponent(b00, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(5, 5, 5)
                                .addComponent(b01, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b02, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b03, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b04, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b05, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b06, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b07, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b08, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b09, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(tabPlayerBLayout.createSequentialGroup()
                                .addComponent(b10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(5, 5, 5)
                                .addComponent(b11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b16, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b17, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b18, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b19, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(tabPlayerBLayout.createSequentialGroup()
                        .addComponent(b40, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(b41, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b42, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b43, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b44, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b45, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b46, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b47, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b48, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b49, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(tabPlayerBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(tabPlayerBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(tabPlayerBLayout.createSequentialGroup()
                                .addComponent(b70, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(5, 5, 5)
                                .addComponent(b71, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b72, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b73, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b74, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b75, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b76, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b77, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b78, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b79, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(tabPlayerBLayout.createSequentialGroup()
                                .addComponent(b80, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(5, 5, 5)
                                .addComponent(b81, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b82, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b83, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b84, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b85, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b86, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b87, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b88, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b89, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(tabPlayerBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(tabPlayerBLayout.createSequentialGroup()
                                .addComponent(b50, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(5, 5, 5)
                                .addComponent(b51, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b52, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b53, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b54, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b55, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b56, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b57, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b58, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b59, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(tabPlayerBLayout.createSequentialGroup()
                                .addComponent(b60, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(5, 5, 5)
                                .addComponent(b61, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b62, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b63, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b64, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b65, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b66, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b67, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b68, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b69, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(tabPlayerBLayout.createSequentialGroup()
                        .addComponent(b90, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(b91, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b92, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b93, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b94, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b95, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b96, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b97, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b98, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b99, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(tabPlayerBLayout.createSequentialGroup()
                        .addComponent(b0, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(b6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        tabPlayerBLayout.setVerticalGroup(
            tabPlayerBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabPlayerBLayout.createSequentialGroup()
                .addGroup(tabPlayerBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b0)
                    .addComponent(b1)
                    .addComponent(b2)
                    .addComponent(b3)
                    .addComponent(b4)
                    .addComponent(b5)
                    .addComponent(b6)
                    .addComponent(b7)
                    .addComponent(b8)
                    .addComponent(b9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tabPlayerBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(ba, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(tabPlayerBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(b00, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b01, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b02, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b03, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b04, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b05, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b06, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b07, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b08, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b09, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tabPlayerBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b16, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b17, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b18, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b19, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tabPlayerBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b20, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b21, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b22, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b23, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b24, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b25, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b26, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b27, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b28, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b29, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tabPlayerBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(bd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(tabPlayerBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(b30, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b31, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b32, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b33, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b34, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b35, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b36, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b37, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b38, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b39, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tabPlayerBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(be, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(tabPlayerBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(b40, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b41, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b42, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b43, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b44, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b45, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b46, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b47, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b48, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b49, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tabPlayerBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b50, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b51, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b52, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b53, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b54, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b55, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b56, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b57, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b58, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b59, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bf, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tabPlayerBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(bg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(tabPlayerBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(b60, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b61, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b62, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b63, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b64, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b65, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b66, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b67, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b68, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b69, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tabPlayerBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(bh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(tabPlayerBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(b70, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b71, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b72, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b73, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b74, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b75, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b76, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b77, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b78, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b79, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tabPlayerBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b80, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b81, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b82, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b83, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b84, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b85, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b86, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b87, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b88, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b89, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tabPlayerBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tabPlayerBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(b90, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b91, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b92, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b93, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b94, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b95, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b96, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b97, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b98, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b99, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(bj, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout bigboxPlayerBLayout = new javax.swing.GroupLayout(bigboxPlayerB);
        bigboxPlayerB.setLayout(bigboxPlayerBLayout);
        bigboxPlayerBLayout.setHorizontalGroup(
            bigboxPlayerBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabPlayerB, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        bigboxPlayerBLayout.setVerticalGroup(
            bigboxPlayerBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabPlayerB, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        lblPlayerA.setText("Player A");

        lblPlayerB.setText("Player B");

        txtLog.setColumns(20);
        txtLog.setRows(5);
        scrollLog.setViewportView(txtLog);

        lblLog.setText("Log");

        btnReseta.setText("Abortar");
        btnReseta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetaActionPerformed(evt);
            }
        });

        sldVelocidade.setMaximum(5000);
        sldVelocidade.setMinimum(1);
        sldVelocidade.setValue(500);
        sldVelocidade.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                sldVelocidadeMouseReleased(evt);
            }
        });

        lblVelocidade.setText("Velocidade 5000ms");

        lblBPecasRestantes.setText("(31 peças rest.)");

        lblAPecasRestantes.setText("(31 peças rest.)");

        lblStatusA.setText("[X tiros, Y acertos, Z erros]");

        lblStatusB.setText("[XXX tiros, YX acertos, ZX erros]");

        lblEficienciaA.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblEficienciaA.setText("[Eficiência 100%]");

        lblEficienciaB.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblEficienciaB.setText("[Eficiência 100%]");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollLog)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblLog, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(196, 196, 196)
                                .addComponent(lblEficienciaA, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(bigboxPlayerA, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblAPecasRestantes)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblStatusA))
                            .addComponent(lblPlayerA, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(lblEficienciaB, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lblVelocidade)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(sldVelocidade, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(bigboxPlayerB, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(lblBPecasRestantes)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblStatusB)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnReseta, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(lblPlayerB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPlayerA, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPlayerB, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBPecasRestantes, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblStatusB, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAPecasRestantes, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblStatusA, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReseta))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(bigboxPlayerA, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bigboxPlayerB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblVelocidade, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sldVelocidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblEficienciaA, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblEficienciaB, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblLog, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollLog, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnResetaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetaActionPerformed

        Jogo.getInstance().setJogando(false);

        this.repaint();
        JOptionPane.showMessageDialog(this, "Clique em OK para fechar a janela.", "Jogo abortado", JOptionPane.ERROR_MESSAGE);

        System.exit(0);
    }//GEN-LAST:event_btnResetaActionPerformed

    public void tocaSom(String som) {



        String resource = "";

        if (som.equals("a")) {
            if (Jogo.getInstance().getDelay() < 1000) {
                return;
            } else {
                resource = "/sons/agua.wav";
            }
        } else if (som.equals("vitoria")) {
            resource = "/sons/vitoria.wav";
        } else if (som.equals("derrota")) {
            resource = "/sons/derrota.wav";
        } else {
            if (Jogo.getInstance().getDelay() < 1000) {
                return;
            } else {
                resource = "/sons/acertou.wav";
            }
        }

        Applet.newAudioClip(getClass().getResource(resource)).play();
    }

    public void setTabuleiroPath(String a, Integer pecasRestantes) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void sldVelocidadeMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sldVelocidadeMouseReleased
        lblVelocidade.setText("Velocidade " + String.valueOf(sldVelocidade.getValue()) + "ms");
        Jogo.getInstance().atualizaVelocidade(sldVelocidade.getValue());
    }//GEN-LAST:event_sldVelocidadeMouseReleased
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel a0;
    private javax.swing.JLabel a00;
    private javax.swing.JLabel a01;
    private javax.swing.JLabel a02;
    private javax.swing.JLabel a03;
    private javax.swing.JLabel a04;
    private javax.swing.JLabel a05;
    private javax.swing.JLabel a06;
    private javax.swing.JLabel a07;
    private javax.swing.JLabel a08;
    private javax.swing.JLabel a09;
    private javax.swing.JLabel a1;
    private javax.swing.JLabel a10;
    private javax.swing.JLabel a11;
    private javax.swing.JLabel a12;
    private javax.swing.JLabel a13;
    private javax.swing.JLabel a14;
    private javax.swing.JLabel a15;
    private javax.swing.JLabel a16;
    private javax.swing.JLabel a17;
    private javax.swing.JLabel a18;
    private javax.swing.JLabel a19;
    private javax.swing.JLabel a2;
    private javax.swing.JLabel a20;
    private javax.swing.JLabel a21;
    private javax.swing.JLabel a22;
    private javax.swing.JLabel a23;
    private javax.swing.JLabel a24;
    private javax.swing.JLabel a25;
    private javax.swing.JLabel a26;
    private javax.swing.JLabel a27;
    private javax.swing.JLabel a28;
    private javax.swing.JLabel a29;
    private javax.swing.JLabel a3;
    private javax.swing.JLabel a30;
    private javax.swing.JLabel a31;
    private javax.swing.JLabel a32;
    private javax.swing.JLabel a33;
    private javax.swing.JLabel a34;
    private javax.swing.JLabel a35;
    private javax.swing.JLabel a36;
    private javax.swing.JLabel a37;
    private javax.swing.JLabel a38;
    private javax.swing.JLabel a39;
    private javax.swing.JLabel a4;
    private javax.swing.JLabel a40;
    private javax.swing.JLabel a41;
    private javax.swing.JLabel a42;
    private javax.swing.JLabel a43;
    private javax.swing.JLabel a44;
    private javax.swing.JLabel a45;
    private javax.swing.JLabel a46;
    private javax.swing.JLabel a47;
    private javax.swing.JLabel a48;
    private javax.swing.JLabel a49;
    private javax.swing.JLabel a5;
    private javax.swing.JLabel a50;
    private javax.swing.JLabel a51;
    private javax.swing.JLabel a52;
    private javax.swing.JLabel a53;
    private javax.swing.JLabel a54;
    private javax.swing.JLabel a55;
    private javax.swing.JLabel a56;
    private javax.swing.JLabel a57;
    private javax.swing.JLabel a58;
    private javax.swing.JLabel a59;
    private javax.swing.JLabel a6;
    private javax.swing.JLabel a60;
    private javax.swing.JLabel a61;
    private javax.swing.JLabel a62;
    private javax.swing.JLabel a63;
    private javax.swing.JLabel a64;
    private javax.swing.JLabel a65;
    private javax.swing.JLabel a66;
    private javax.swing.JLabel a67;
    private javax.swing.JLabel a68;
    private javax.swing.JLabel a69;
    private javax.swing.JLabel a7;
    private javax.swing.JLabel a70;
    private javax.swing.JLabel a71;
    private javax.swing.JLabel a72;
    private javax.swing.JLabel a73;
    private javax.swing.JLabel a74;
    private javax.swing.JLabel a75;
    private javax.swing.JLabel a76;
    private javax.swing.JLabel a77;
    private javax.swing.JLabel a78;
    private javax.swing.JLabel a79;
    private javax.swing.JLabel a8;
    private javax.swing.JLabel a80;
    private javax.swing.JLabel a81;
    private javax.swing.JLabel a82;
    private javax.swing.JLabel a83;
    private javax.swing.JLabel a84;
    private javax.swing.JLabel a85;
    private javax.swing.JLabel a86;
    private javax.swing.JLabel a87;
    private javax.swing.JLabel a88;
    private javax.swing.JLabel a89;
    private javax.swing.JLabel a9;
    private javax.swing.JLabel a90;
    private javax.swing.JLabel a91;
    private javax.swing.JLabel a92;
    private javax.swing.JLabel a93;
    private javax.swing.JLabel a94;
    private javax.swing.JLabel a95;
    private javax.swing.JLabel a96;
    private javax.swing.JLabel a97;
    private javax.swing.JLabel a98;
    private javax.swing.JLabel a99;
    private javax.swing.JLabel aa;
    private javax.swing.JLabel ab;
    private javax.swing.JLabel ac;
    private javax.swing.JLabel ad;
    private javax.swing.JLabel ae;
    private javax.swing.JLabel af;
    private javax.swing.JLabel ag;
    private javax.swing.JLabel ah;
    private javax.swing.JLabel ai;
    private javax.swing.JLabel aj;
    private javax.swing.JLabel b0;
    private javax.swing.JLabel b00;
    private javax.swing.JLabel b01;
    private javax.swing.JLabel b02;
    private javax.swing.JLabel b03;
    private javax.swing.JLabel b04;
    private javax.swing.JLabel b05;
    private javax.swing.JLabel b06;
    private javax.swing.JLabel b07;
    private javax.swing.JLabel b08;
    private javax.swing.JLabel b09;
    private javax.swing.JLabel b1;
    private javax.swing.JLabel b10;
    private javax.swing.JLabel b11;
    private javax.swing.JLabel b12;
    private javax.swing.JLabel b13;
    private javax.swing.JLabel b14;
    private javax.swing.JLabel b15;
    private javax.swing.JLabel b16;
    private javax.swing.JLabel b17;
    private javax.swing.JLabel b18;
    private javax.swing.JLabel b19;
    private javax.swing.JLabel b2;
    private javax.swing.JLabel b20;
    private javax.swing.JLabel b21;
    private javax.swing.JLabel b22;
    private javax.swing.JLabel b23;
    private javax.swing.JLabel b24;
    private javax.swing.JLabel b25;
    private javax.swing.JLabel b26;
    private javax.swing.JLabel b27;
    private javax.swing.JLabel b28;
    private javax.swing.JLabel b29;
    private javax.swing.JLabel b3;
    private javax.swing.JLabel b30;
    private javax.swing.JLabel b31;
    private javax.swing.JLabel b32;
    private javax.swing.JLabel b33;
    private javax.swing.JLabel b34;
    private javax.swing.JLabel b35;
    private javax.swing.JLabel b36;
    private javax.swing.JLabel b37;
    private javax.swing.JLabel b38;
    private javax.swing.JLabel b39;
    private javax.swing.JLabel b4;
    private javax.swing.JLabel b40;
    private javax.swing.JLabel b41;
    private javax.swing.JLabel b42;
    private javax.swing.JLabel b43;
    private javax.swing.JLabel b44;
    private javax.swing.JLabel b45;
    private javax.swing.JLabel b46;
    private javax.swing.JLabel b47;
    private javax.swing.JLabel b48;
    private javax.swing.JLabel b49;
    private javax.swing.JLabel b5;
    private javax.swing.JLabel b50;
    private javax.swing.JLabel b51;
    private javax.swing.JLabel b52;
    private javax.swing.JLabel b53;
    private javax.swing.JLabel b54;
    private javax.swing.JLabel b55;
    private javax.swing.JLabel b56;
    private javax.swing.JLabel b57;
    private javax.swing.JLabel b58;
    private javax.swing.JLabel b59;
    private javax.swing.JLabel b6;
    private javax.swing.JLabel b60;
    private javax.swing.JLabel b61;
    private javax.swing.JLabel b62;
    private javax.swing.JLabel b63;
    private javax.swing.JLabel b64;
    private javax.swing.JLabel b65;
    private javax.swing.JLabel b66;
    private javax.swing.JLabel b67;
    private javax.swing.JLabel b68;
    private javax.swing.JLabel b69;
    private javax.swing.JLabel b7;
    private javax.swing.JLabel b70;
    private javax.swing.JLabel b71;
    private javax.swing.JLabel b72;
    private javax.swing.JLabel b73;
    private javax.swing.JLabel b74;
    private javax.swing.JLabel b75;
    private javax.swing.JLabel b76;
    private javax.swing.JLabel b77;
    private javax.swing.JLabel b78;
    private javax.swing.JLabel b79;
    private javax.swing.JLabel b8;
    private javax.swing.JLabel b80;
    private javax.swing.JLabel b81;
    private javax.swing.JLabel b82;
    private javax.swing.JLabel b83;
    private javax.swing.JLabel b84;
    private javax.swing.JLabel b85;
    private javax.swing.JLabel b86;
    private javax.swing.JLabel b87;
    private javax.swing.JLabel b88;
    private javax.swing.JLabel b89;
    private javax.swing.JLabel b9;
    private javax.swing.JLabel b90;
    private javax.swing.JLabel b91;
    private javax.swing.JLabel b92;
    private javax.swing.JLabel b93;
    private javax.swing.JLabel b94;
    private javax.swing.JLabel b95;
    private javax.swing.JLabel b96;
    private javax.swing.JLabel b97;
    private javax.swing.JLabel b98;
    private javax.swing.JLabel b99;
    private javax.swing.JLabel ba;
    private javax.swing.JLabel bb;
    private javax.swing.JLabel bc;
    private javax.swing.JLabel bd;
    private javax.swing.JLabel be;
    private javax.swing.JLabel bf;
    private javax.swing.JLabel bg;
    private javax.swing.JLabel bh;
    private javax.swing.JLabel bi;
    private javax.swing.JPanel bigboxPlayerA;
    private javax.swing.JPanel bigboxPlayerB;
    private javax.swing.JLabel bj;
    private javax.swing.JButton btnReseta;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblAPecasRestantes;
    private javax.swing.JLabel lblBPecasRestantes;
    private javax.swing.JLabel lblEficienciaA;
    private javax.swing.JLabel lblEficienciaB;
    private javax.swing.JLabel lblLog;
    private javax.swing.JLabel lblPlayerA;
    private javax.swing.JLabel lblPlayerB;
    private javax.swing.JLabel lblStatusA;
    private javax.swing.JLabel lblStatusB;
    private javax.swing.JLabel lblVelocidade;
    private javax.swing.JScrollPane scrollLog;
    private javax.swing.JSlider sldVelocidade;
    private javax.swing.JPanel tabPlayerA;
    private javax.swing.JPanel tabPlayerB;
    private javax.swing.JTextArea txtLog;
    // End of variables declaration//GEN-END:variables
}
