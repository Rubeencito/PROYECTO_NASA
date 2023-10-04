package com.example.nasa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminWindow extends JFrame {
    private JTextField nombreField;
    private JTextField salarioField;
    private JTextField edadField;
    private JTextField tallerField;
    private JTextField direccionField;
    private JTextField experienciaField;
    private JTextField ciudadField;
    private JTextField sexoField;
    private JButton crearButton;
    private JButton leerButton;
    private JButton actualizarButton;
    private JButton eliminarButton;

    public AdminWindow(String nombreUsuario) {
        //  Ventana de administración
        setTitle("Ventana de Administración");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 400);
        setLayout(new BorderLayout());

        // Panel para ingresar datos
        JPanel inputPanel = new JPanel(new GridLayout(8, 2));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Nombre:"));
        nombreField = new JTextField();
        inputPanel.add(nombreField);

        inputPanel.add(new JLabel("Salario:"));
        salarioField = new JTextField();
        inputPanel.add(salarioField);

        inputPanel.add(new JLabel("Edad:"));
        edadField = new JTextField();
        inputPanel.add(edadField);

        inputPanel.add(new JLabel("Número de Taller:"));
        tallerField = new JTextField();
        inputPanel.add(tallerField);

        inputPanel.add(new JLabel("Dirección:"));
        direccionField = new JTextField();
        inputPanel.add(direccionField);

        inputPanel.add(new JLabel("Años de Experiencia:"));
        experienciaField = new JTextField();
        inputPanel.add(experienciaField);

        inputPanel.add(new JLabel("Ciudad:"));
        ciudadField = new JTextField();
        inputPanel.add(ciudadField);

        inputPanel.add(new JLabel("Sexo:"));
        sexoField = new JTextField();
        inputPanel.add(sexoField);

        add(inputPanel, BorderLayout.CENTER);

        // Panel para botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        crearButton = new JButton("Crear");
        leerButton = new JButton("Leer");
        actualizarButton = new JButton("Actualizar");
        eliminarButton = new JButton("Eliminar");

        buttonPanel.add(crearButton);
        buttonPanel.add(leerButton);
        buttonPanel.add(actualizarButton);
        buttonPanel.add(eliminarButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Agrega ActionListener para los botones
        crearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crearMecanico();
            }
        });

        leerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                leerMecanico();
            }
        });

        actualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarMecanico();
            }
        });

        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarMecanico();
            }
        });

        // Centra la ventana en la pantalla
        setLocationRelativeTo(null);

        // Haz visible la ventana de administración
        setVisible(true);
    }

    private void crearMecanico() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/nasa", "root", "Admin123")) {
            String query = "INSERT INTO Mecanico (nom, salari, edat, numero_taller, adreça, anys_experiencia, ciutat, sexe) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, nombreField.getText());
            preparedStatement.setBigDecimal(2, new BigDecimal(salarioField.getText()));
            preparedStatement.setInt(3, Integer.parseInt(edadField.getText()));
            preparedStatement.setInt(4, Integer.parseInt(tallerField.getText()));
            preparedStatement.setString(5, direccionField.getText());
            preparedStatement.setInt(6, Integer.parseInt(experienciaField.getText()));
            preparedStatement.setString(7, ciudadField.getText());
            preparedStatement.setString(8, sexoField.getText());
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Mecánico creado exitosamente.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al crear el mecánico.");
        }
    }

    private void leerMecanico() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/nasa", "root", "Admin123")) {
            String query = "SELECT * FROM Mecanico WHERE nom = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, nombreField.getText());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                nombreField.setText(resultSet.getString("nom"));
                salarioField.setText(resultSet.getBigDecimal("salari").toString());
                edadField.setText(Integer.toString(resultSet.getInt("edat")));
                tallerField.setText(Integer.toString(resultSet.getInt("numero_taller")));
                direccionField.setText(resultSet.getString("adreça"));
                experienciaField.setText(Integer.toString(resultSet.getInt("anys_experiencia")));
                ciudadField.setText(resultSet.getString("ciutat"));
                sexoField.setText(resultSet.getString("sexe"));
            } else {
                JOptionPane.showMessageDialog(this, "Mecánico no encontrado.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al leer el mecánico.");
        }
    }

    private void actualizarMecanico() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/nasa", "root", "Admin123")) {
            String query = "UPDATE Mecanico SET salari = ?, edat = ?, numero_taller = ?, adreça = ?, anys_experiencia = ?, ciutat = ?, sexe = ? WHERE nom = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setBigDecimal(1, new BigDecimal(salarioField.getText()));
            preparedStatement.setInt(2, Integer.parseInt(edadField.getText()));
            preparedStatement.setInt(3, Integer.parseInt(tallerField.getText()));
            preparedStatement.setString(4, direccionField.getText());
            preparedStatement.setInt(5, Integer.parseInt(experienciaField.getText()));
            preparedStatement.setString(6, ciudadField.getText());
            preparedStatement.setString(7, sexoField.getText());
            preparedStatement.setString(8, nombreField.getText());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Mecánico actualizado exitosamente.");
            } else {
                JOptionPane.showMessageDialog(this, "Mecánico no encontrado para actualizar.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al actualizar el mecánico.");
        }
    }

    private void eliminarMecanico() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/nasa", "root", "Admin123")) {
            String query = "DELETE FROM Mecanico WHERE nom = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, nombreField.getText());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Mecánico eliminado exitosamente.");
                // Limpiar los campos después de la eliminación
                nombreField.setText("");
                salarioField.setText("");
                edadField.setText("");
                tallerField.setText("");
                direccionField.setText("");
                experienciaField.setText("");
                ciudadField.setText("");
                sexoField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Mecánico no encontrado para eliminar.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al eliminar el mecánico.");
        }
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> new AdminWindow("Nombre de Usuario"));
//    }
}
