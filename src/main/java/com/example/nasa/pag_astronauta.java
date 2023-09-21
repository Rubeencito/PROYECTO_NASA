package com.example.nasa;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class pag_astronauta extends JFrame {

    public pag_astronauta(String nombreUsuario) {
        // Configura la ventana
        setTitle("Página del Astronauta");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 300); // Establece el tamaño de la ventana
        setLocationRelativeTo(null); // Centra la ventana en la pantalla

        // Crea un panel principal con GridLayout
        JPanel panel = new JPanel(new GridLayout(1, 2));

        try {
            // Conecta a la base de datos
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/NASA", "root", "patata");
            Statement stmt = conn.createStatement();

            // Obtén los datos del astronauta a partir del nombre de usuario
            String query = "SELECT * FROM Astronauta INNER JOIN Usuario ON Astronauta.ID = Usuario.ID WHERE nombreUsuario = '" + nombreUsuario + "'";
            ResultSet rs = stmt.executeQuery(query);

            // Procesa los resultados y muestra los datos
            if (rs.next()) {
                // Panel para mostrar los datos a la izquierda
                JPanel dataPanel = new JPanel(new GridLayout(7, 2));
                dataPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                dataPanel.add(new JLabel("Nombre:"));
                dataPanel.add(new JLabel(rs.getString("nom")));

                dataPanel.add(new JLabel("Edad primer vuelo:"));
                dataPanel.add(new JLabel(String.valueOf(rs.getInt("edat_primer_vol"))));

                dataPanel.add(new JLabel("Edad:"));
                dataPanel.add(new JLabel(String.valueOf(rs.getInt("edat"))));

                dataPanel.add(new JLabel("Misiones exitosas:"));
                dataPanel.add(new JLabel(String.valueOf(rs.getInt("missions_ok"))));

                dataPanel.add(new JLabel("Misiones fallidas:"));
                dataPanel.add(new JLabel(String.valueOf(rs.getInt("missions_ko"))));

                dataPanel.add(new JLabel("Dirección:"));
                dataPanel.add(new JLabel(rs.getString("adreça")));

                dataPanel.add(new JLabel("Rango militar:"));
                dataPanel.add(new JLabel(rs.getString("rang_militar")));

                // Agregar el panel de datos a la izquierda
                panel.add(dataPanel);

                // Agregar imagen de astronauta en la parte derecha
                ImageIcon astronautIcon = new ImageIcon("C:\\Users\\pauca\\IdeaProjects\\PROYECTO_NASA_\\src\\main\\java\\com\\example\\nasa\\astronauta.png");
                JLabel astronautLabel = new JLabel(astronautIcon);
                astronautLabel.setHorizontalAlignment(SwingConstants.CENTER);
                panel.add(astronautLabel);
            } else {
                // Si no se encuentra el astronauta
                JLabel noDataLabel = new JLabel("No se encontraron datos para este usuario.");
                noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
                panel.add(noDataLabel);
            }

            // Cierra la conexión y la consulta
            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Agrega el panel a la ventana
        add(panel);

        // Hace visible la ventana
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Modifica "Nombre de Usuario" con el nombre real de usuario que deseas mostrar
            new pag_astronauta("Nombre de Usuario");
        });
    }
}
