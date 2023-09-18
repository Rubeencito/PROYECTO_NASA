package com.example.nasa;

import java.awt.*;
import javax.swing.*;

public class FondoPanel extends JPanel {
    private Image imagenFondo;

    public FondoPanel(String rutaImagen) {
        // Carga la imagen de fondo
        imagenFondo = new ImageIcon(rutaImagen).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dibuja la imagen de fondo
        g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
    }
}
