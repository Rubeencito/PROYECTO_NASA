package com.example.nasa;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.sql.*;

public class pag_fisico extends JFrame {

    public pag_fisico(String nombreUsuario) {

        setTitle("Pàgina del Físic");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 350); // Tamaño más alargado
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Espaciado entre componentes

        try {
            Connection conn = ConexionDB.obtenerConexion();
            Statement stmt = conn.createStatement();

            String query = "SELECT * FROM Fisico INNER JOIN Usuario ON Fisico.ID = Usuario.ID WHERE nombreUsuario = '" + nombreUsuario + "'";
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                JPanel dataPanel = new JPanel(new GridLayout(7, 2));
                dataPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK), // Borde rectangular
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));

                dataPanel.add(new JLabel("Nom:"));
                dataPanel.add(new JLabel(rs.getString("nom")));

                dataPanel.add(new JLabel("Edat primer vol:"));
                dataPanel.add(new JLabel(String.valueOf(rs.getInt("anys_experiencia"))));

                dataPanel.add(new JLabel("Edat:"));
                dataPanel.add(new JLabel(String.valueOf(rs.getInt("edat"))));

                dataPanel.add(new JLabel("Experiència:"));
                dataPanel.add(new JLabel(String.valueOf(rs.getInt("anys_experiencia"))));

                dataPanel.add(new JLabel("Ciutat:"));
                dataPanel.add(new JLabel(rs.getString("ciutat")));

                dataPanel.add(new JLabel("Adreça:"));
                dataPanel.add(new JLabel(rs.getString("adreça")));

                dataPanel.add(new JLabel("Títol acadèmic:"));
                dataPanel.add(new JLabel(rs.getString("titulacio_academica")));

                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.weighty = 0.5; // Reducción del espacio vertical a la mitad
                gbc.fill = GridBagConstraints.VERTICAL;
                mainPanel.add(dataPanel, gbc);
            } else {
                JLabel noDataLabel = new JLabel("No s'han trobat dades per a aquest usuari.");
                noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
                gbc.gridx = 0;
                gbc.gridy = 0;
                mainPanel.add(noDataLabel, gbc);
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        add(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new pag_fisico("Nom d'usuari");
            }
        });
    }
}
