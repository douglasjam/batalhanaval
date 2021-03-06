/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import conexao.Conexao;
import conexao.UIConectando;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @author douglasjam
 */
public class GUIConf extends javax.swing.JFrame {

    static GUIConf guiConfiguracoes;
    JFileChooser fileChooser = new JFileChooser(new File("."));
    String lastPath = "";
    static Boolean ready = false;
    private final static String CONFIG_FILE = System.getProperty("user.home").replace("\\", "/") + "/" + "hidenove_batalhanaval.ini";
    String tipoJogo;
    UIConectando uiConectando;
    Boolean aguardando;

    public static GUIConf getInstance() {
        if (guiConfiguracoes == null) {
            guiConfiguracoes = new GUIConf();
            guiConfiguracoes.setReady(false);
            return guiConfiguracoes;
        } else {
            return guiConfiguracoes;
        }
    }

    public GUIConf() {

        super();
        initComponents();
        this.aguardando = false;
        //
        uiConectando = UIConectando.getInstance();
        uiConectando.setGUI(this.ready, txtAreaLog, progressBar);
        //
        if (new File(CONFIG_FILE).exists()) {
            leConfiguracoes();
            habilitaCampos();
            btnIniciarJogo.requestFocus();
        } else {
            habilitaCampos();
        }

    }

    public void setReady(Boolean ready) {
        this.ready = ready;
    }

    public boolean isReady() {
        return this.ready;
    }

    public String getTipoJogo() {
        return (String) cmbTipoJogo.getSelectedItem();
    }

    public String getHost() {
        return txthost.getText();
    }

    public Integer getPorta() {
        return Integer.valueOf(txtporta.getText());
    }

    public Properties setConfiguracoes() {

        Properties configuracoes = new Properties();
        configuracoes.setProperty("tipo", (String) cmbTipoJogo.getSelectedItem());
        configuracoes.setProperty("host", txthost.getText());
        configuracoes.setProperty("porta", txtporta.getText());
        configuracoes.setProperty("tabuleiroA", txtPathTabuleiroA.getText());
        configuracoes.setProperty("tabuleiroB", txtPathTabuleiroB.getText());
        configuracoes.setProperty("chkSalvar", chkSalvarConfiguracoes.isSelected() ? "SIM" : "NÃO");
        configuracoes.setProperty("chkSalvar", chkSalvarConfiguracoes.isSelected() ? "SIM" : "NÃO");
        configuracoes.setProperty("delay", txtDelay.getText().isEmpty() ? "500" : txtDelay.getText());

        return configuracoes;
    }

    public void leConfiguracoes() {
        try {
            Properties configuracoes = new Properties();
            configuracoes.load(new FileInputStream(CONFIG_FILE));
            cmbTipoJogo.setSelectedItem(configuracoes.getProperty("tipo"));
            txthost.setText(configuracoes.getProperty("host"));
            txtporta.setText(configuracoes.getProperty("porta"));
            txtPathTabuleiroA.setText(configuracoes.getProperty("tabuleiroA"));
            txtPathTabuleiroB.setText(configuracoes.getProperty("tabuleiroB"));
            chkSalvarConfiguracoes.setSelected(configuracoes.getProperty("chkSalvar").equals("SIM"));
            txtDelay.setText(configuracoes.getProperty("delay", "500"));

        } catch (IOException ex) {
            Logger.getLogger(GUIConf.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void escreveConfiguracoes() {
        if (chkSalvarConfiguracoes.isSelected()) {
            try {

                setConfiguracoes().store(new FileOutputStream(CONFIG_FILE), null);
            } catch (IOException ex) {
                Logger.getLogger(GUIConf.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public Integer getDelay() {
        if (txtDelay.getText().isEmpty()) {
            return 500;
        } else {
            return Integer.valueOf(txtDelay.getText());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbltabuleiroA = new javax.swing.JLabel();
        txtPathTabuleiroA = new javax.swing.JTextField();
        btnEscolherTabuleiroA = new javax.swing.JButton();
        lbltabuleiroB = new javax.swing.JLabel();
        txtPathTabuleiroB = new javax.swing.JTextField();
        btnEscolherTabuleiroB = new javax.swing.JButton();
        lblhost = new javax.swing.JLabel();
        txthost = new javax.swing.JTextField();
        cmbTipoJogo = new javax.swing.JComboBox();
        lblTipoJogo = new javax.swing.JLabel();
        lblporta = new javax.swing.JLabel();
        btnIniciarJogo = new javax.swing.JButton();
        txtporta = new javax.swing.JTextField();
        chkSalvarConfiguracoes = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        scrollTxtAreaLog = new javax.swing.JScrollPane();
        txtAreaLog = new javax.swing.JTextArea();
        txtDelay = new javax.swing.JTextField();
        lblDelay = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        lbltabuleiroA.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbltabuleiroA.setText("Tab. Player A");

        btnEscolherTabuleiroA.setText("Escolher arquivo");
        btnEscolherTabuleiroA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEscolherTabuleiroAActionPerformed(evt);
            }
        });

        lbltabuleiroB.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbltabuleiroB.setText("Tab. Player B");

        btnEscolherTabuleiroB.setText("Escolher arquivo");
        btnEscolherTabuleiroB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEscolherTabuleiroBActionPerformed(evt);
            }
        });

        lblhost.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblhost.setText("Endereço");

        cmbTipoJogo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Selecione o tipo de jogo", "01 - Player 1 vs Player 2", "02 - Player 1 vs Inteligência", "03 - Player 1 vs Random", "04 - Inteligência vs Inteligência", "05 - Inteligência vs Random", "06 - Random vs Random", "07 - Player 1 vs Remoto (Cliente)", "08 - Player 1 vs Remoto (Servidor)", "09 - Inteligência vs Remoto (Cliente)", "10 - Inteligência vs Remoto (Servidor)", "11 - Random vs Remoto (Cliente)", "12 - Random vs Remoto (Servidor)" }));
        cmbTipoJogo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbTipoJogoItemStateChanged(evt);
            }
        });

        lblTipoJogo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTipoJogo.setText("Tipo");

        lblporta.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblporta.setText("Porta");

        btnIniciarJogo.setText("Iniciar Jogo");
        btnIniciarJogo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIniciarJogoActionPerformed(evt);
            }
        });

        chkSalvarConfiguracoes.setSelected(true);
        chkSalvarConfiguracoes.setText("Salvar Configurações");

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/config_image.png"))); // NOI18N

        progressBar.setMaximumSize(new java.awt.Dimension(100, 100));
        progressBar.setMinimumSize(new java.awt.Dimension(0, 0));
        progressBar.setStringPainted(true);

        txtAreaLog.setColumns(20);
        txtAreaLog.setRows(5);
        scrollTxtAreaLog.setViewportView(txtAreaLog);

        txtDelay.setText("500");

        lblDelay.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDelay.setText("Delay(ms)");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lbltabuleiroA, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtPathTabuleiroA))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lbltabuleiroB, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtPathTabuleiroB)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnEscolherTabuleiroA, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnEscolherTabuleiroB, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnIniciarJogo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblhost, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txthost, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblporta, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtporta, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblDelay)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDelay, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkSalvarConfiguracoes, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(scrollTxtAreaLog, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblTipoJogo, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbTipoJogo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(lblTipoJogo))
                    .addComponent(cmbTipoJogo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblDelay)
                            .addComponent(txtDelay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(chkSalvarConfiguracoes, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txthost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblhost)
                        .addComponent(lblporta)
                        .addComponent(txtporta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbltabuleiroA, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtPathTabuleiroA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnEscolherTabuleiroA))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnEscolherTabuleiroB)
                            .addComponent(txtPathTabuleiroB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbltabuleiroB, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnIniciarJogo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollTxtAreaLog, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEscolherTabuleiroBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEscolherTabuleiroBActionPerformed

        if (fileChooser.showOpenDialog(this) == 0) {
            txtPathTabuleiroB.setText(fileChooser.getSelectedFile().getAbsolutePath());
            lastPath = fileChooser.getSelectedFile().getParentFile().getPath();

        }
    }//GEN-LAST:event_btnEscolherTabuleiroBActionPerformed

//    public void trataProcessing() {
//        if (uiConectando.isProcessing()) {
//            if (getTipoJogo().contains("Servidor")) {
//                JOptionPane.showMessageDialog(this, "O jogo está aguardando a conexão de um cliente, clique em cancelar para iniciar uma nova configuração.", "ERRO", JOptionPane.ERROR_MESSAGE);
//            } else {
//                JOptionPane.showMessageDialog(this, "O jogo está se conectando ao servidor, clique em cancelar para iniciar uma nova configuração.", "ERRO", JOptionPane.ERROR_MESSAGE);
//            }
//            btnIniciarJogo.requestFocus();
//        }
//    }
    private void btnIniciarJogoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIniciarJogoActionPerformed

        if (this.aguardando) {
            System.exit(0);
            return;
        }

        String opcao = (String) cmbTipoJogo.getSelectedItem();
        Integer opcaoN = Integer.valueOf(opcao.substring(0, 2));

        if (txtDelay.getText().isEmpty()) {
            txtAreaLog.append("Preencha o campo delay\n");
            JOptionPane.showMessageDialog(this, "Preencha o campo delay.", "ERRO", JOptionPane.ERROR_MESSAGE);
            txtDelay.requestFocus();
            return;
        } else if (Integer.valueOf(txtDelay.getText()) <= 0 || Integer.valueOf(txtDelay.getText()) > 5000) {
            txtAreaLog.append("O delay deve estar entre 1 e 5000ms\n");
            JOptionPane.showMessageDialog(this, "O delay deve estrar entre 1 e 5000ms.", "ERRO", JOptionPane.ERROR_MESSAGE);
            txtDelay.requestFocus();
            return;
        }
        if (opcaoN >= 1 && opcaoN <= 6) {

            if (txtPathTabuleiroA.getText().isEmpty()) {
                txtAreaLog.append("Selecione um arquivo do tabuleiro B antes de iniciar este tipo de jogo\n");
                JOptionPane.showMessageDialog(this, "Selecione um arquivo do tabuleiro B antes de iniciar este tipo de jogo.", "ERRO", JOptionPane.ERROR_MESSAGE);
                txthost.requestFocus();
                return;
            } else if (txtPathTabuleiroB.getText().isEmpty()) {
                txtAreaLog.append("Selecione um arquivo do tabuleiro A antes de iniciar este tipo de jogo\n");
                JOptionPane.showMessageDialog(this, "Selecione um arquivo do tabuleiro A antes de iniciar este tipo de jogo.", "ERRO", JOptionPane.ERROR_MESSAGE);
                txthost.requestFocus();
                return;
            } else if (!new File(txtPathTabuleiroA.getText()).exists()) {
                txtAreaLog.append("O arquivo selecionado para o tabuleiro A não existe!\n");
                JOptionPane.showMessageDialog(this, "O arquivo selecionado para o tabuleiro A não existe!.", "ERRO", JOptionPane.ERROR_MESSAGE);
                txtPathTabuleiroA.setText("");
                btnEscolherTabuleiroA.requestFocus();
                return;
            } else if (!new File(txtPathTabuleiroB.getText()).exists()) {
                txtAreaLog.append("O arquivo selecionado para o tabuleiro B não existe!\n");
                JOptionPane.showMessageDialog(this, "O arquivo selecionado para o tabuleiro B não existe!.", "ERRO", JOptionPane.ERROR_MESSAGE);
                txtPathTabuleiroB.setText("");
                txthost.requestFocus();
                return;
            }

        } else {

            if (txthost.getText().isEmpty() && getTipoJogo().contains("Cliente")) {
                txtAreaLog.append("Preencha o campo \"HOST\" antes de iniciar este tipo de jogo.\n");
                JOptionPane.showMessageDialog(this, "Preencha o campo \"HOST\" antes de iniciar este tipo de jogo.", "ERRO", JOptionPane.ERROR_MESSAGE);
                txthost.requestFocus();
                return;
            } else if (txtporta.getText().isEmpty()) {
                txtAreaLog.append("Preencha o campo \"PORTA\" antes de iniciar este tipo de jogo.\n");
                JOptionPane.showMessageDialog(this, "Preencha o campo \"PORTA\" antes de iniciar este tipo de jogo.", "ERRO", JOptionPane.ERROR_MESSAGE);
                txthost.requestFocus();
                return;
            } else if (txtPathTabuleiroA.getText().isEmpty()) {
                txtAreaLog.append("Selecione um arquivo de tabuleiro antes de iniciar este tipo de jogo.\n");
                JOptionPane.showMessageDialog(this, "Selecione um arquivo de tabuleiro antes de iniciar este tipo de jogo.", "ERRO", JOptionPane.ERROR_MESSAGE);
                txthost.requestFocus();
                return;
            } else if (!new File(txtPathTabuleiroA.getText()).exists()) {
                txtAreaLog.append("O arquivo selecionado para o tabuleiro A não existe!\n");
                JOptionPane.showMessageDialog(this, "O arquivo selecionado para o tabuleiro A não existe!", "ERRO", JOptionPane.ERROR_MESSAGE);
                txtPathTabuleiroA.setText("");
                txthost.requestFocus();
                return;
            }


        }

        // caso o jogo seja remoto, faz conexão agora
        if (getTipoJogo().contains("Remoto")) {

            btnIniciarJogo.setText("Abortar jogo");
            this.aguardando = true;

            if (getTipoJogo().contains("Servidor")) {

                Conexao.getInstance().setDados("servidor", getHost(), getPorta());
                uiConectando.setTipo("servidor");
                uiConectando.start();

            } else {

                Conexao.getInstance().setDados("cliente", getHost(), getPorta());
                uiConectando.setTipo("cliente");
                uiConectando.start();

            }

        } else {

            // escreve configurações caso necessário
            if (chkSalvarConfiguracoes.isSelected()) {
                escreveConfiguracoes();
            }

            this.ready = true;
        }




    }//GEN-LAST:event_btnIniciarJogoActionPerformed

    private void cmbTipoJogoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbTipoJogoItemStateChanged

        String opcao = (String) cmbTipoJogo.getSelectedItem();
        habilitaCampos();
        this.repaint();
    }//GEN-LAST:event_cmbTipoJogoItemStateChanged

    private void btnEscolherTabuleiroAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEscolherTabuleiroAActionPerformed

        if (fileChooser.showOpenDialog(this) == 0) {
            txtPathTabuleiroA.setText(fileChooser.getSelectedFile().getAbsolutePath());
            lastPath = fileChooser.getSelectedFile().getParentFile().getPath();
            //atualizaChooser();

            if (btnEscolherTabuleiroB.isEnabled() && btnEscolherTabuleiroB.getText().isEmpty()) {
                btnEscolherTabuleiroB.requestFocus();
            } else if (txthost.isEnabled() && txthost.getText().isEmpty()) {
                txthost.requestFocus();
            } else if (btnIniciarJogo.isEnabled()) {
                btnIniciarJogo.requestFocus();
            }

        }
    }//GEN-LAST:event_btnEscolherTabuleiroAActionPerformed

    public void atualizaChooser() {


        //if (lastPath.equals("")) {
        fileChooser.setCurrentDirectory(new File("."));
        //} else {
        //  fileChooser.setCurrentDirectory(new File(lastPath));
        //}
        fileChooser.setDialogTitle("Selecione o arquivo do tabuleiro");
        btnEscolherTabuleiroA.setText("Selecionar Arquivo");
        btnEscolherTabuleiroB.setText("Selecionar Arquivo");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Tabuleiro (*.txt)", "txt"));
        fileChooser.setAcceptAllFileFilterUsed(false);
        txtPathTabuleiroA.setText("");
        txtPathTabuleiroB.setText("");
    }

    private void habilitaCampos() {

        Integer opcao = 0;

        if (cmbTipoJogo.getSelectedIndex() > 0) {
            opcao = Integer.valueOf(((String) cmbTipoJogo.getSelectedItem()).substring(0, 2));
        }

        if (opcao >= 1 && opcao <= 6) {
            // dois locais

            txthost.setEnabled(false);
            txtporta.setEnabled(false);

            lbltabuleiroA.setText("Tab. Player A");

            txtPathTabuleiroA.setEnabled(true);
            btnEscolherTabuleiroA.setEnabled(true);

            txtPathTabuleiroB.setEnabled(true);
            btnEscolherTabuleiroB.setEnabled(true);

            btnIniciarJogo.setEnabled(true);

            btnEscolherTabuleiroA.requestFocus();

        } else if (opcao >= 7 && opcao <= 12) {

            // habilita ou nao host
            if (((String) cmbTipoJogo.getSelectedItem()).contains("Servidor")) {
                txthost.setEnabled(false);
            } else {
                txthost.setEnabled(true);
            }

            txtporta.setEnabled(true);

            lbltabuleiroA.setText("Tab. Pla. Rem");

            txtPathTabuleiroA.setEnabled(true);
            btnEscolherTabuleiroA.setEnabled(true);

            btnEscolherTabuleiroB.setEnabled(false);
            txtPathTabuleiroB.setEnabled(false);

            btnIniciarJogo.setEnabled(true);

            txthost.requestFocus();

        } else {

            txthost.setEnabled(false);
            txtporta.setEnabled(false);

            lbltabuleiroA.setText("Tab. Player A");

            txtPathTabuleiroA.setEnabled(false);
            btnEscolherTabuleiroA.setEnabled(false);

            txtPathTabuleiroB.setEnabled(false);
            btnEscolherTabuleiroB.setEnabled(false);

            btnIniciarJogo.setEnabled(false);

        }


    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEscolherTabuleiroA;
    private javax.swing.JButton btnEscolherTabuleiroB;
    private javax.swing.JButton btnIniciarJogo;
    private javax.swing.JCheckBox chkSalvarConfiguracoes;
    private javax.swing.JComboBox cmbTipoJogo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblDelay;
    private javax.swing.JLabel lblTipoJogo;
    private javax.swing.JLabel lblhost;
    private javax.swing.JLabel lblporta;
    private javax.swing.JLabel lbltabuleiroA;
    private javax.swing.JLabel lbltabuleiroB;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JScrollPane scrollTxtAreaLog;
    private javax.swing.JTextArea txtAreaLog;
    private javax.swing.JTextField txtDelay;
    private javax.swing.JTextField txtPathTabuleiroA;
    private javax.swing.JTextField txtPathTabuleiroB;
    private javax.swing.JTextField txthost;
    private javax.swing.JTextField txtporta;
    // End of variables declaration//GEN-END:variables
}
