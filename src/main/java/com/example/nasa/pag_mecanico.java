package com.example.nasa;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


public class pag_mecanico extends JFrame {
    public pag_mecanico() {
        // Configurar la ventana
        setTitle("Mecánico");
        setSize(800, 600); // Establecer el tamaño de la ventana a pantalla completa
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximizar la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Crear el panel principal con un BorderLayout
        JPanel panelPrincipal = new JPanel(new BorderLayout());

        // Crear el panel de la ficha técnica en la izquierda
        JPanel panelFichaTecnica = new JPanel();
        panelFichaTecnica.setLayout(new BoxLayout(panelFichaTecnica, BoxLayout.Y_AXIS));

        JLabel etiquetaFichaTecnica = new JLabel("Ficha Técnica del Mecánico:");
        etiquetaFichaTecnica.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea textoFichaTecnica = new JTextArea(10, 30);
        textoFichaTecnica.setEditable(false);
        textoFichaTecnica.setLineWrap(true);
        textoFichaTecnica.setWrapStyleWord(true);
        textoFichaTecnica.setAlignmentX(Component.LEFT_ALIGNMENT);

        JScrollPane scrollFichaTecnica = new JScrollPane(textoFichaTecnica);
        scrollFichaTecnica.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelFichaTecnica.add(etiquetaFichaTecnica);
        panelFichaTecnica.add(scrollFichaTecnica);

        // Crear el panel de botones en la derecha
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));

        JButton botonveiculos = new JButton("Veiculos");
        botonveiculos.setAlignmentX(Component.RIGHT_ALIGNMENT);
        JButton botonfichar = new JButton("Fichar");
        botonfichar.setAlignmentX(Component.RIGHT_ALIGNMENT);
        JButton botonCerrar = new JButton("Cerrar");
        botonCerrar.setAlignmentX(Component.RIGHT_ALIGNMENT);

        panelBotones.add(botonveiculos);
        panelBotones.add(botonfichar);
        panelBotones.add(botonCerrar);

        // Agregar el panel de ficha técnica a la izquierda y el panel de botones a la derecha
        panelPrincipal.add(panelFichaTecnica, BorderLayout.WEST);
        panelPrincipal.add(panelBotones, BorderLayout.EAST);

        // Agregar el panel principal a la ventana
        add(panelPrincipal);

        // Manejar el evento de cerrar la ventana
        botonCerrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Cerrar la aplicación
            }
        });
    }

}
