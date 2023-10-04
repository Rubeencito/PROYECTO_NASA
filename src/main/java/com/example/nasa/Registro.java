package com.example.nasa;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class Registro extends JFrame {
    private JTextField nombreUsuarioField;
    private JTextField contraseñaField;
    private JTextField professionIDField;
    private JButton crearButton;
    private JButton leerButton;
    private JButton actualizarButton;
    private JButton eliminarButton;
    public Registro(String nombreUsuario) {
        // Ventana de administración
        setTitle("Ventana de Administración");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 400);
        setLayout(new BorderLayout());

        // Panel para ingresar datos
        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Nombre de Usuario:"));
        nombreUsuarioField = new JTextField();
        inputPanel.add(nombreUsuarioField);

        inputPanel.add(new JLabel("Contraseña:"));
        contraseñaField = new JTextField();
        inputPanel.add(contraseñaField);

        inputPanel.add(new JLabel("ID de Profesión:"));
        professionIDField = new JTextField();
        inputPanel.add(professionIDField);

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

        // Agregar ActionListener para los botones
        crearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crearUsuario();
            }
        });

        leerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                leerUsuario();
            }
        });

        actualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarUsuario();
            }
        });

        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarUsuario();
            }
        });

        // Centra la ventana en la pantalla
        setLocationRelativeTo(null);

        // Haz visible la ventana de administración
        setVisible(true);
    }

    private void crearUsuario() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/nasa", "root", "Admin123")) {
            String query = "INSERT INTO Usuario (nombreUsuario, contraseña, profession_ID) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, nombreUsuarioField.getText());
            preparedStatement.setString(2, contraseñaField.getText());
            preparedStatement.setInt(3, Integer.parseInt(professionIDField.getText()));
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Usuario creado exitosamente.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al crear el usuario.");
        }
    }

    private void leerUsuario() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/nasa", "root", "Admin123")) {
            String query = "SELECT * FROM Usuario WHERE nombreUsuario = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, nombreUsuarioField.getText());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                contraseñaField.setText(resultSet.getString("contraseña"));
                professionIDField.setText(Integer.toString(resultSet.getInt("profession_ID")));
            } else {
                JOptionPane.showMessageDialog(this, "Usuario no encontrado.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al leer el usuario.");
        }
    }

    private void actualizarUsuario() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/nasa", "root", "Admin123")) {
            String query = "UPDATE Usuario SET contraseña = ?, profession_ID = ? WHERE nombreUsuario = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, contraseñaField.getText());
            preparedStatement.setInt(2, Integer.parseInt(professionIDField.getText()));
            preparedStatement.setString(3, nombreUsuarioField.getText());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Usuario actualizado exitosamente.");
            } else {
                JOptionPane.showMessageDialog(this, "Usuario no encontrado para actualizar.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al actualizar el usuario.");
        }
    }

    private void eliminarUsuario() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/nasa", "root", "Admin123")) {
            String query = "DELETE FROM Usuario WHERE nombreUsuario = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, nombreUsuarioField.getText());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Usuario eliminado exitosamente.");
                // Limpiar los campos después de la eliminación
                contraseñaField.setText("");
                professionIDField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Usuario no encontrado para eliminar.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al eliminar el usuario.");
        }
    }
}
