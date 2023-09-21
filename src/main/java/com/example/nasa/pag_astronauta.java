package com.example.nasa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class pag_astronauta extends JFrame {

    private JLabel astronautLabel;

    public pag_astronauta(String nombreUsuario) {
        setTitle("Página del Astronauta");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Crear un panel para la izquierda con fondo gris claro
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(200, 200, 200));
        leftPanel.setPreferredSize(new Dimension(100, getHeight()));
        mainPanel.add(leftPanel, BorderLayout.WEST);

        // Crear un panel para la derecha con fondo gris claro
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(new Color(200, 200, 200));
        rightPanel.setPreferredSize(new Dimension(100, getHeight()));
        mainPanel.add(rightPanel, BorderLayout.EAST);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 10;
        gbc.weighty = 10;
        gbc.anchor = GridBagConstraints.NORTHWEST;  // Alineación en la esquina superior izquierda

        // Agregar texto "COORDENADAS" con formato
        JLabel coordenadasLabel = new JLabel("COORDENADAS");
        coordenadasLabel.setFont(new Font("Arial", Font.BOLD, 24));

        // Agregar espacio entre el texto y el botón
        gbc.insets = new Insets(10, 0, 0, 0);
        centerPanel.add(coordenadasLabel, gbc);

        JButton mostrarDireccionButton = new JButton("Enviar coordenadas");
        mostrarDireccionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarDireccionAleatoria();
            }
        });

        gbc.gridx = 1;  // Cambio de posición en la cuadrícula
        gbc.insets = new Insets(30, 30, 0, 30);
        centerPanel.add(mostrarDireccionButton, gbc);

        // Agregar imagen a la derecha del astronauta
        try {
            ImageIcon astronautIconRight = new ImageIcon("C:\\Users\\pauca\\IdeaProjects\\PROYECTO_NASA_\\src\\main\\java\\com\\example\\nasa\\astronauta.png");
            int imageWidthRight = astronautIconRight.getIconWidth() / 2;  // Cambiar el divisor para aumentar el tamaño
            int imageHeightRight = astronautIconRight.getIconHeight() / 2; // Cambiar el divisor para aumentar el tamaño
            astronautIconRight.setImage(astronautIconRight.getImage().getScaledInstance(imageWidthRight, imageHeightRight, Image.SCALE_DEFAULT)); // Escalar la imagen
            astronautLabel = new JLabel(astronautIconRight);
            astronautLabel.setBounds(60, 90, imageWidthRight, imageHeightRight);
            rightPanel.add(astronautLabel);
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Agregar imagen a la izquierda del astronauta
        try {
            ImageIcon astronautIconLeft = new ImageIcon("C:\\Users\\pauca\\IdeaProjects\\PROYECTO_NASA_\\src\\main\\java\\com\\example\\nasa\\astronauta.png");
            int imageWidthLeft = astronautIconLeft.getIconWidth() / 2;  // Cambiar el divisor para aumentar el tamaño
            int imageHeightLeft = astronautIconLeft.getIconHeight() / 2; // Cambiar el divisor para aumentar el tamaño
            astronautIconLeft.setImage(astronautIconLeft.getImage().getScaledInstance(imageWidthLeft, imageHeightLeft, Image.SCALE_DEFAULT)); // Escalar la imagen
            JLabel astronautLabelLeft = new JLabel(astronautIconLeft);
            astronautLabelLeft.setBounds(60, 90, imageWidthLeft, imageHeightLeft);
            leftPanel.add(astronautLabelLeft);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Panel para el espacio arriba del texto y el botón
        JPanel topSpacePanel = new JPanel();
        topSpacePanel.setBackground(new Color(200, 200, 200));
        topSpacePanel.setPreferredSize(new Dimension(getWidth(), 20));
        mainPanel.add(topSpacePanel, BorderLayout.NORTH);

        // Panel para el espacio debajo del texto y el botón
        JPanel bottomSpacePanel = new JPanel();
        bottomSpacePanel.setBackground(new Color(200, 200, 200));
        bottomSpacePanel.setPreferredSize(new Dimension(getWidth(), 20));
        mainPanel.add(bottomSpacePanel, BorderLayout.SOUTH);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    private void mostrarDireccionAleatoria() {
        Random random = new Random();
        int latitud = random.nextInt(90);
        int longitud = random.nextInt(180);

        JOptionPane.showMessageDialog(this, "Coordenadas: Latitud " + latitud + ", Longitud " + longitud);
    }

    public static void main(String[] args) {
        JButton iniciarSesionButton = new JButton("Iniciar Sesión");
        iniciarSesionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new pag_astronauta("Nombre de Usuario");
            }
        });

        JFrame loginFrame = new JFrame("Iniciar Sesión");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(300, 200);
        loginFrame.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));  // Alineación izquierda
        loginFrame.add(iniciarSesionButton);
        loginFrame.setVisible(true);
    }
}
