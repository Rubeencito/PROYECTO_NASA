package com.example.nasa;
import javax.swing.*;

public class main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               // new inicio_session();
                String usuario = null;
                pag_mecanico ventana = new pag_mecanico(usuario);
                ventana.setVisible(true);

            }
        });
    }
}
