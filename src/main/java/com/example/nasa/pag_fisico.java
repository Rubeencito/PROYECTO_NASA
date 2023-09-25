package com.example.nasa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class pag_fisico extends JFrame {

    private BufferedImage fondo;
    private  JButton ficharButton;
    private boolean entradaRegistrada = false;
    public pag_fisico(String nombreUsuario) {

        setTitle("Pàgina del Físic");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        try {
            fondo = ImageIO.read(new File("C:\\Users\\ddiaz\\OneDrive\\Escritorio\\die.jpg")); // Reemplaza con la ruta de tu imagen
        } catch (IOException e) {
            e.printStackTrace();
        }
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(getWidth(), getHeight()));

        JLabel saludoLabel = new JLabel("Hola Mecánico " + nombreUsuario);
        saludoLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Establece la fuente y el tamaño a 16
        saludoLabel.setVerticalAlignment(JLabel.TOP); // Alinea el texto arriba
        saludoLabel.setBounds(50, 50, 300, 30); // Ajusta las coordenadas y dimensiones según tu diseño
        layeredPane.add(saludoLabel, Integer.valueOf(1)); // Saludo

        JPanel fichaTecnicaPanel = new JPanel(new GridLayout(0, 2)); // Usamos un diseño de cuadrícula para mostrar los datos
        fichaTecnicaPanel.setBackground(Color.WHITE); // Fondo blanco para la ficha técnica
        fichaTecnicaPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10), // Márgenes externos
                BorderFactory.createLineBorder(Color.BLACK) // Borde negro
        ));
        fichaTecnicaPanel.setBounds(50, 100, 300, 300); // Ajusta las coordenadas y dimensiones según tu diseño
        layeredPane.add(fichaTecnicaPanel, Integer.valueOf(2)); // Ficha técnica


        JPanel fondoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (fondo != null) {
                    g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        fondoPanel.setBounds(0, 0, getWidth(), getHeight());
        layeredPane.add(fondoPanel, Integer.valueOf(0)); // Fondo

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/nasa", "root", "Admin123")) {
            String query = "SELECT * FROM Mecanico WHERE nom = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, nombreUsuario);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                agregarCampoFichaTecnica(fichaTecnicaPanel, "Nombre:", resultSet.getString("nom"));
                agregarCampoFichaTecnica(fichaTecnicaPanel, "Salario:", resultSet.getBigDecimal("salari").toString());
                agregarCampoFichaTecnica(fichaTecnicaPanel, "Edad:", Integer.toString(resultSet.getInt("edat")));
                agregarCampoFichaTecnica(fichaTecnicaPanel, "Número de Taller:", Integer.toString(resultSet.getInt("numero_taller")));
                agregarCampoFichaTecnica(fichaTecnicaPanel, "Dirección:", resultSet.getString("adreça"));
                agregarCampoFichaTecnica(fichaTecnicaPanel, "Años de Experiencia:", Integer.toString(resultSet.getInt("anys_experiencia")));
                agregarCampoFichaTecnica(fichaTecnicaPanel, "Ciudad:", resultSet.getString("ciutat"));
                agregarCampoFichaTecnica(fichaTecnicaPanel, "Sexo:", resultSet.getString("sexe"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ficharButton = new JButton("Fichar Entrada"); // Asigna el botón a la variable de instancia
        ficharButton.setBounds(300, 450, 150, 30); // Ajusta las coordenadas y dimensiones según tu diseño
        ficharButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!entradaRegistrada) {
                    ficharEntrada();
                    entradaRegistrada = true;
                    ficharButton.setText("Fichar Salida");
                } else {
                    ficharSalida();
                    entradaRegistrada = false;
                    ficharButton.setText("Fichar Entrada");
                }
            }
        });
        layeredPane.add(ficharButton, Integer.valueOf(4)); // Botón de fichar

        // Agrega el JLayeredPane al JFrame
        add(layeredPane);

        // Hace visible la ventana
        setVisible(true);
    }
    private void agregarCampoFichaTecnica(JPanel panel, String etiqueta, String valor) {
        panel.add(new JLabel(etiqueta));
        panel.add(new JLabel(valor));
    }

    private void ficharEntrada() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/nasa", "root", "Admin123");
            String query = "INSERT INTO Fitxar (fecha_creacion, hora_entrada) VALUES (?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            Date fechaActual = new Date();
            preparedStatement.setString(1, dateFormat.format(fechaActual));
            preparedStatement.setString(2, timeFormat.format(fechaActual));
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Fichaje de Entrada registrado.", "Fichaje Registrado", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void ficharSalida() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/nasa", "root", "Admin123");
            String query = "UPDATE Fitxar SET hora_salida = ? WHERE fecha_creacion = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            Date fechaActual = new Date();
            preparedStatement.setString(1, timeFormat.format(fechaActual));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            preparedStatement.setString(2, dateFormat.format(fechaActual));
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Fichaje de Salida registrado.", "Fichaje Registrado", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new pag_mecanico("Nombre de Usuario"));
    }

}
