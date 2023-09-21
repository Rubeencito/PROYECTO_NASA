package com.example.nasa;

import javax.swing.*;
import java.awt.*;

public class pag_fisico extends JFrame {

    public pag_fisico(String nombreUsuario) {
        // Configura la ventana
        setTitle("Página del Fisico");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300); // Establece el tamaño de la ventana
        setLocationRelativeTo(null); // Centra la ventana en la pantalla

        // Crea un panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Crea una etiqueta con el saludo personalizado
        JLabel saludoLabel = new JLabel("Hola Fisico " + nombreUsuario);
        saludoLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Establece la fuente y el tamaño
        saludoLabel.setHorizontalAlignment(JLabel.CENTER); // Centra el texto horizontalmente

        // Agrega la etiqueta al panel
        panel.add(saludoLabel, BorderLayout.CENTER);

        // Agrega el panel a la ventana
        add(panel);

        // Hace visible la ventana
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new pag_fisico("Nombre de Usuario");
            }
        });
    }
}

