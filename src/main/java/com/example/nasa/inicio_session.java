package com.example.nasa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class inicio_session {

    private JFrame frame;
    private JPanel panel;
    private JComboBox<String> rolesComboBox;
    private JTextField usuarioField;
    private JPasswordField contrasenaField;
    private JButton iniciarSesionButton;

    public inicio_session() {
        // Crear el frame principal
        frame = new JFrame("Inicio de Sesión");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        // Crear el panel principal
        panel = new JPanel() {
            // Override para pintar la imagen de fondo
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon imagenFondo = new ImageIcon(System.getProperty("user.home") + "/Desktop/fondo.jpg"); // Ruta de la imagen en el escritorio
                Image image = imagenFondo.getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(new GridLayout(4, 2));

        // Crear el desplegable de roles
        String[] roles = {"Físico", "Astronauta", "Espía", "Mecánico"};
        rolesComboBox = new JComboBox<>(roles);

        // Crear los campos de usuario y contraseña
        usuarioField = new JTextField(10);
        contrasenaField = new JPasswordField(10);

        // Crear el botón de inicio de sesión
        iniciarSesionButton = new JButton("Iniciar Sesión");

        // Agregar componentes al panel
        panel.add(new JLabel("Rol:"));
        panel.add(rolesComboBox);
        panel.add(new JLabel("Usuario:"));
        panel.add(usuarioField);
        panel.add(new JLabel("Contraseña:"));
        panel.add(contrasenaField);
        panel.add(new JLabel()); // Espacio en blanco
        panel.add(iniciarSesionButton);

        // Agregar acción al botón de inicio de sesión
        iniciarSesionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String rolSeleccionado = (String) rolesComboBox.getSelectedItem();
                String usuario = usuarioField.getText();
                char[] contrasenaChars = contrasenaField.getPassword();
                String contrasena = new String(contrasenaChars);

                // Aquí puedes agregar lógica para verificar el inicio de sesión
                // Por ejemplo, verificar si el usuario y la contraseña son correctos

                JOptionPane.showMessageDialog(frame, "Rol: " + rolSeleccionado + "\nUsuario: " + usuario);
            }
        });

        // Agregar el panel al frame y hacerlo visible
        frame.add(panel);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new inicio_session();
            }
        });
    }
}



