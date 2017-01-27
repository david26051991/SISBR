
package com.dyd.sisbr.swing;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.UIManager;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SuppressWarnings("serial")
public class GeneradorClasificador extends javax.swing.JFrame {

//    ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
//	ApplicationContext ctx = new AnnotationConfigApplicationContext(ApplicationConfig.class);
//    Main main = (Main) ctx.getBean("main");
	
	ConfigurableApplicationContext ctx = SpringApplication.run(ApplicationConfig.class);
	Main main = ctx.getBean(Main.class);
	
    String rutaAlmacen = main.getPathRepositoryFiles();
        
    public GeneradorClasificador() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            Logger.getLogger(GeneradorClasificador.class.getName()).log(Level.SEVERE, null, ex);
        }
        initComponents();
        this.setLocationRelativeTo(null);
        pbarProceso.setVisible(false);
    }

    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    	
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtRutaResEjemplo = new javax.swing.JTextField();
        btnSeleccionarEjemplos = new javax.swing.JButton();
//        btnSeleccionarAlmacen = new javax.swing.JButton();
        txtRutaResAlmacen = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        btnGenerar = new javax.swing.JButton();
        pbarProceso = new javax.swing.JProgressBar();
        scrollPane = new javax.swing.JScrollPane();
        txtLog = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Generador del Clasificador de Resoluciones");

        jLabel2.setText("Ruta de almacenamiento de Resoluciones:");

        btnSeleccionarEjemplos.setText("Seleccionar");
        btnSeleccionarEjemplos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSeleccionarEjemplosActionPerformed(evt);
            }
        });

//        btnSeleccionarAlmacen.setText("Seleccionar");
//        btnSeleccionarAlmacen.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                btnSeleccionarAlmacenActionPerformed(evt);
//            }
//        });

        jLabel3.setText("Ruta de la Colección de Resoluciones:");

        btnGenerar.setBackground(new java.awt.Color(255, 255, 255));
        btnGenerar.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnGenerar.setText("Generar Clasificador");
        btnGenerar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarActionPerformed(evt);
            }
        });

        pbarProceso.setStringPainted(true);

        txtLog.setEditable(false);
        scrollPane.setViewportView(txtLog);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(132, 132, 132))
            .addGroup(layout.createSequentialGroup()
                .addGap(225, 225, 225)
                .addComponent(btnGenerar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addContainerGap(357, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(scrollPane, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pbarProceso, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(txtRutaResAlmacen)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//                                .addComponent(btnSeleccionarAlmacen))
                                )
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(txtRutaResEjemplo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSeleccionarEjemplos)))
                        .addGap(39, 39, 39))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel1)
                .addGap(38, 38, 38)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtRutaResEjemplo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSeleccionarEjemplos))
                .addGap(17, 17, 17)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
//                    .addComponent(btnSeleccionarAlmacen)
                    .addComponent(txtRutaResAlmacen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(pbarProceso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnGenerar)
                .addGap(18, 18, 18)
                .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        pack();
        
        txtRutaResAlmacen.setEnabled(false);
        txtRutaResAlmacen.setText(rutaAlmacen);
    }// </editor-fold>//GEN-END:initComponents

    private void btnSeleccionarEjemplosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSeleccionarEjemplosActionPerformed
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = fc.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            txtRutaResEjemplo.setText(file.getPath());
        }
    }//GEN-LAST:event_btnSeleccionarEjemplosActionPerformed

//    private void btnSeleccionarAlmacenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSeleccionarAlmacenActionPerformed
//        JFileChooser fc = new JFileChooser();
//        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//        int returnVal = fc.showOpenDialog(this);
//
//        if (returnVal == JFileChooser.APPROVE_OPTION) {
//            File file = fc.getSelectedFile();
//            txtRutaResAlmacen.setText(file.getPath());
//        }
//    }

    private void btnGenerarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarActionPerformed
        
    	if(txtRutaResEjemplo.getText().trim().isEmpty()){
    		JOptionPane.showMessageDialog(this, "Debe seleccionar la ruta de la colección de resoluciones");
    	} else if(txtRutaResAlmacen.getText().trim().isEmpty()){
    		JOptionPane.showMessageDialog(this, "Debe seleccionar la ruta donde se almacenará las resoluciones");
    	} else{
    		main.setFormulario(this);
            main.start();
    	}
        
    }//GEN-LAST:event_btnGenerarActionPerformed

    public JButton getBtnGenerar() {
        return btnGenerar;
    }

//    public JButton getBtnSeleccionarAlmacen() {
//        return btnSeleccionarAlmacen;
//    }

    public JButton getBtnSeleccionarEjemplos() {
        return btnSeleccionarEjemplos;
    }

    public JTextField getTxtRutaResAlmacen() {
        return txtRutaResAlmacen;
    }

    public JTextField getTxtRutaResEjemplo() {
        return txtRutaResEjemplo;
    }

    public JProgressBar getPbarProceso() {
        return pbarProceso;
    }

    public JTextPane getTxtLog() {
        return txtLog;
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }
    
    public static void main(String args[]) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GeneradorClasificador().setVisible(true);
            }
        });
    }
    
    public void bloquearForm(){
    	txtRutaResAlmacen.setEnabled(false);
    	txtRutaResEjemplo.setEnabled(false);
    	btnGenerar.setEnabled(false);
//    	btnSeleccionarAlmacen.setEnabled(false);
    	btnSeleccionarEjemplos.setEnabled(false);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGenerar;
//    private javax.swing.JButton btnSeleccionarAlmacen;
    private javax.swing.JButton btnSeleccionarEjemplos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JProgressBar pbarProceso;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTextPane txtLog;
    private javax.swing.JTextField txtRutaResAlmacen;
    private javax.swing.JTextField txtRutaResEjemplo;
    // End of variables declaration//GEN-END:variables
}
