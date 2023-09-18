package com.example.nasa;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


public class pag_mecanico extends JFrame {
    public pag_mecanico(String usuario) {
        // Configurar la ventana
        setTitle("Mecánico");
        setSize(800, 600); // Establecer el tamaño de la ventana a pantalla completa
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximizar la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Crear el panel de fondo personalizado y establecer la imagen de fondo
        //FondoPanel fondoPanel = new FondoPanel("C:\\Users\\Nerea\\Desktop\\ADAM2.2\\Projecte_enaitza\\src\\main\\java\\img\\fp");

        // Crear el panel principal con un BorderLayout
        JPanel panelPrincipal = new JPanel(new BorderLayout());

        // Crear el panel de la ficha técnica en el centro
        JPanel panelFichaTecnica = new JPanel();
        panelFichaTecnica.setLayout(new BoxLayout(panelFichaTecnica, BoxLayout.Y_AXIS));

        JLabel etiquetaFichaTecnica = new JLabel("Ficha Técnica del Mecánico:");
        etiquetaFichaTecnica.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea textoFichaTecnica = new JTextArea(10, 30);
        textoFichaTecnica.setEditable(false);
        textoFichaTecnica.setLineWrap(true);
        textoFichaTecnica.setWrapStyleWord(true);
        textoFichaTecnica.setAlignmentX(Component.CENTER_ALIGNMENT);

        JScrollPane scrollFichaTecnica = new JScrollPane(textoFichaTecnica);
        scrollFichaTecnica.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelFichaTecnica.add(etiquetaFichaTecnica);
        panelFichaTecnica.add(scrollFichaTecnica);

        // Crear el panel de botones a la izquierda
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));

        JButton botonVeiculos = new JButton("Veiculos");
        JButton botonFichar = new JButton("Fichar");
        JButton botonCerrar = new JButton("Cerrar");

        // Cambiar el tamaño de los botones
        Dimension botonDimension = new Dimension(150, 50); // Ancho y alto en píxeles
        botonVeiculos.setPreferredSize(botonDimension);
        botonFichar.setPreferredSize(botonDimension);
        botonCerrar.setPreferredSize(botonDimension);

        // Cambiar el diseño de los botones
        Font botonFont = new Font("Arial", Font.BOLD, 16);
        botonVeiculos.setFont(botonFont);
        botonFichar.setFont(botonFont);
        botonCerrar.setFont(botonFont);

        // Agregar los botones al panel de botones
        panelBotones.add(botonVeiculos);
        panelBotones.add(botonFichar);
        panelBotones.add(botonCerrar);

        // Agregar el panel de ficha técnica al centro y el panel de botones a la izquierda
        panelPrincipal.add(panelFichaTecnica, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.WEST);

        // Agregar el panel principal a la ventana
        add(panelPrincipal);
        //setContentPane(fondoPanel);
        // Manejar el evento de cerrar la ventana
        botonCerrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Cerrar la aplicación
            }
        });
    }

}
