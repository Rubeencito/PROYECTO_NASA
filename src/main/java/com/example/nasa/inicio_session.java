package com.example.nasa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
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
    private boolean verificarCredenciales(String nombreUsuario, String contrasena, String professionID) {
        Connection conexion = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            conexion = ConexionDB.obtenerConexion();
            String consulta = "SELECT * FROM usuario WHERE nombreUsuario = ? AND contraseña = ? AND profession_ID = ?";
            statement = conexion.prepareStatement(consulta);
            statement.setString(1, nombreUsuario);
            statement.setString(2, contrasena);
            statement.setString(3, professionID);
            resultSet = statement.executeQuery();

            return resultSet.next(); // Si hay resultados, las credenciales son válidas
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            ConexionDB.cerrarConexion(conexion);
        }
    }

    // Método para redirigir según el rol
    private void redirigirSegunRol(String professionID, String usuario) {
        if (professionID.equals("1")) {
            // Redirigir a la página de Mecánico y pasar el nombre de usuario
            pag_mecanico paginaMecanico = new pag_mecanico(usuario);
            // Lógica adicional para mostrar la página de Mecánico
        } else if (professionID.equals("2")) {
            // Redirigir a la página de Espía y pasar el nombre de usuario
            pag_espia paginaEspia = new pag_espia(usuario);
            // Lógica adicional para mostrar la página de Espía
        } else if (professionID.equals("3")) {
            // Redirigir a la página de Físico y pasar el nombre de usuario
            pag_fisico paginaFisico = new pag_fisico(usuario);
            // Lógica adicional para mostrar la página de Físico
        } else if (professionID.equals("4")) {
            // Redirigir a la página de Astronauta y pasar el nombre de usuario
            pag_astronauta paginaAstronauta = new pag_astronauta(usuario);
            // Lógica adicional para mostrar la página de Astronauta
        }
        // Agrega más casos según los roles que tengas
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new inicio_session();
                pag_mecanico ventana = new pag_mecanico();
                ventana.setVisible(true);

            }
        });
    }
}







