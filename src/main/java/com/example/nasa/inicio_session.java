package com.example.nasa;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class inicio_session {

    private JFrame frame;
    private JPanel panel;
    private JComboBox<String> rolesComboBox;
    private JTextField usuarioField;
    private JPasswordField contrasenaField;
    private JButton iniciarSesionButton;

    private Connection conexion;

    public inicio_session() {
        try {
            // Establecer la conexión a la base de datos MySQL
            conexion = DriverManager.getConnection("jdbc:mysql://localhost/nasa", "root", "patata");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos.");
            System.exit(1);
        }

        // Crear un frame sin decoración
        frame = new JFrame();
        frame.setUndecorated(true); // Sin bordes de ventana
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Obtener el tamaño de la pantalla
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // Establecer el tamaño y la posición manualmente (ajustar según sea necesario)
        frame.setSize(screenSize.width, screenSize.height);
        frame.setLocation(0, 0);

        // Establecer el estado de la ventana en maximizado
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Crear un panel para el fondo de la imagen con el mismo tamaño que la pantalla
        JPanel backgroundPanel = new JPanel() {
            private BufferedImage image;

            {
                try {
                    // Carga la imagen de fondo desde el archivo "fondo.jpg"
                    File imageFile = new File("src/main/java/com/example/nasa/fondo.jpg");
                    image = ImageIO.read(imageFile);
                } catch (Exception ex) {
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
        gbc.gridwidth = 2; // Ocupa dos columna
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
                String usuario = usuarioField.getText();
                char[] contrasenaChars = contrasenaField.getPassword();
                String contrasena = new String(contrasenaChars);
                String rolSeleccionado = (String) rolesComboBox.getSelectedItem();

                // Verificar las credenciales en la base de datos
                if (verificarCredenciales(usuario, contrasena, rolSeleccionado)) {
                    // Credenciales válidas, redirigir al usuario a la página correspondiente
                    redirigirSegunRol(rolSeleccionado, usuario);
                } else {
                    // Credenciales inválidas, mostrar un mensaje de error
                    JOptionPane.showMessageDialog(frame, "Credenciales inválidas. Inténtalo de nuevo.");
                }
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

    // Método para verificar las credenciales en la base de datos
    private boolean verificarCredenciales(String nombreUsuario, String contrasena, String professionName) {
        // Consulta SQL para verificar las credenciales
        String consulta = "SELECT * FROM Usuario WHERE nombreUsuario = ? AND contraseña = ? AND profession_ID = ?";

        try (PreparedStatement statement = conexion.prepareStatement(consulta)) {
            statement.setString(1, nombreUsuario);
            statement.setString(2, contrasena);
            statement.setString(3, getProfessionIDByName(professionName));

            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // Si hay resultados, las credenciales son válidas
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Método para obtener el profession_ID por nombre de profesión
    private String getProfessionIDByName(String professionName) {
        // Consulta SQL para obtener el profession_ID por nombre
        String consulta = "SELECT ID FROM Profesion WHERE nombre = ?";

        try (PreparedStatement statement = conexion.prepareStatement(consulta)) {
            statement.setString(1, professionName);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("ID");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null; // Si no se encuentra, devuelve null
    }

    // Método para redirigir según el rol
    private void redirigirSegunRol(String professionName, String usuario) {
        switch (professionName) {
            case "Astronauta":
                // Crea y muestra la página del astronauta
                SwingUtilities.invokeLater(() -> new pag_astronauta(usuario));
                break;
            case "Físico":
                // Crea y muestra la página del físico
                SwingUtilities.invokeLater(() -> new pag_fisico(usuario));
                break;
            case "Mecánico":
                // Crea y muestra la página del mecánico
                SwingUtilities.invokeLater(() -> new pag_mecanico(usuario));
                break;
            case "Espía":
                // Crea y muestra la página del espía
                SwingUtilities.invokeLater(() -> new pag_espia(usuario));
                break;
            default:
                // Si el professionName no coincide con ningún caso, muestra un mensaje de error
                JOptionPane.showMessageDialog(frame, "Rol no válido.");
                break;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new inicio_session();
            }
        });
    }
}






