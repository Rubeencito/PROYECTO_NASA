package com.example.nasa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class inicio_session {

    private JFrame frame;
    private JPanel panel;
    private JComboBox<String> rolesComboBox;
    private JTextField usuarioField;
    private JPasswordField contrasenaField;
    private JButton iniciarSesionButton;

    public inicio_session() {
        // Crear un frame sin decoración
        frame = new JFrame();
        frame.setUndecorated(true); // Sin bordes de ventana
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Obtener el tamaño de la pantalla
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // Establecer el tamaño y la posición manualmente (ajustar según sea necesario)
        frame.setSize(screenSize.width, screenSize.height);
        frame.setLocation(0, 0);

        // Crear un panel para el fondo de la imagen con el mismo tamaño que la pantalla
        JPanel backgroundPanel = new JPanel() {
            private BufferedImage image;

            {
                try {
                    // Carga la imagen de fondo desde el archivo "fondo.jpg"
                    File imageFile = new File("src/main/java/com/example/nasa/fondo.jpg");
                    image = ImageIO.read(imageFile);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (image != null) {
                    g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        backgroundPanel.setLayout(new GridBagLayout());

        // Crear el panel principal para los componentes
        panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(30, 30, 30, 30); // Espacio entre componentes (más grande)

        // Crear el título "NASA" y centrarlo en el recuadro
        JLabel titleLabel = new JLabel("NASA");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48)); // Fuente y tamaño personalizado
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Ocupa dos columnas
        gbc.anchor = GridBagConstraints.CENTER; // Centra el título en el recuadro
        panel.add(titleLabel, gbc);

        // Crear los campos de usuario y contraseña a la izquierda del cuadro de texto
        usuarioField = new JTextField(20); // Hacer el campo más ancho
        contrasenaField = new JPasswordField(20); // Hacer el campo más ancho
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1; // Restablecer a una columna
        gbc.anchor = GridBagConstraints.WEST; // Coloca los campos a la izquierda
        panel.add(new JLabel("Usuario:"), gbc);
        gbc.gridx = 1;
        panel.add(usuarioField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1;
        panel.add(contrasenaField, gbc);

        // Crear el desplegable de roles
        String[] roles = {"Físico", "Astronauta", "Espía", "Mecánico"};
        rolesComboBox = new JComboBox<>(roles);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2; // Ocupa dos columnas
        gbc.anchor = GridBagConstraints.CENTER; // Centra el desplegable en el recuadro
        panel.add(rolesComboBox, gbc);

        // Crear el botón de inicio de sesión
        iniciarSesionButton = new JButton("Iniciar Sesión");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2; // Ocupa dos columnas
        panel.add(iniciarSesionButton, gbc);

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

        // Agregar el panel principal al panel de fondo
        GridBagConstraints bgc = new GridBagConstraints();
        bgc.anchor = GridBagConstraints.CENTER;
        backgroundPanel.add(panel, bgc);

        // Agregar el panel de fondo al frame
        frame.add(backgroundPanel);

        // Hacer el frame visible
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






