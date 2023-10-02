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

public class pag_espia extends JFrame {

    private JTextArea textoEditableArea;
    private JTextArea textoNoEditableArea;
    private JTextField telefonoTextField;
    private JTextField nomClauTextField; // Nuevo campo para mostrar "nom_clau"

    // Conexión a la base de datos MySQL
    private Connection conexion;
    private String nombreUsuario; // Nombre del usuario con sesión iniciada

    public pag_espia(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario; // Guardar el nombre de usuario con sesión iniciada
        setTitle("Página del espía");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        // Crear un panel de pestañas para alternar entre la página principal y la de mensaje
        JTabbedPane tabbedPane = new JTabbedPane();

        // Página principal
        JPanel principalPanel = new JPanel(new BorderLayout()) {
            // Sobreescribir el método paintComponent para establecer la imagen de fondo
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Cargar la imagen de fondo
                Image backgroundImage = new ImageIcon("src/main/java/com/example/nasa/espia.jpg").getImage();
                // Dibujar la imagen de fondo
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };

        // Panel para la parte superior de la página principal
        JPanel topMarginPanel = new JPanel();
        topMarginPanel.setOpaque(false); // Hacer el panel transparente
        topMarginPanel.setPreferredSize(new Dimension(getWidth(), 50));
        principalPanel.add(topMarginPanel, BorderLayout.NORTH);

        // Panel para la parte inferior de la página principal
        JPanel bottomMarginPanel = new JPanel();
        bottomMarginPanel.setOpaque(false); // Hacer el panel transparente
        bottomMarginPanel.setPreferredSize(new Dimension(getWidth(), 50));
        principalPanel.add(bottomMarginPanel, BorderLayout.SOUTH);

        // Crear un panel para la izquierda con fondo gris claro
        JPanel leftMarginPanel = new JPanel(new BorderLayout()) {
            // Sobreescribir el método paintComponent para establecer la imagen de fondo
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };
        leftMarginPanel.setOpaque(false); // Hacer el panel transparente
        leftMarginPanel.setPreferredSize(new Dimension(100, getHeight()));
        principalPanel.add(leftMarginPanel, BorderLayout.WEST);

        // Dividir el cuadro de texto en dos partes
        JPanel textPanel = new JPanel(new GridLayout(1, 2));

        // Cuadro de texto izquierdo con título "CIFRAR"
        JPanel leftTextAreaPanel = new JPanel(new BorderLayout());
        JLabel cifrarLabel = new JLabel("CIFRAR");
        cifrarLabel.setHorizontalAlignment(JLabel.CENTER);
        cifrarLabel.setFont(new Font("Arial", Font.BOLD, 24));
        leftTextAreaPanel.add(cifrarLabel, BorderLayout.NORTH);
        textoEditableArea = new JTextArea();
        textoEditableArea.setLineWrap(true);
        textoEditableArea.setWrapStyleWord(true);
        textoEditableArea.setFont(new Font("Arial", Font.PLAIN, 20));
        leftTextAreaPanel.add(new JScrollPane(textoEditableArea), BorderLayout.CENTER);

        // Cuadro de texto derecho con título "MENSAJE"
        JPanel rightTextAreaPanel = new JPanel(new BorderLayout());
        JLabel mensajeLabel = new JLabel("MENSAJE");
        mensajeLabel.setHorizontalAlignment(JLabel.CENTER);
        mensajeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        rightTextAreaPanel.add(mensajeLabel, BorderLayout.NORTH);
        textoNoEditableArea = new JTextArea();
        textoNoEditableArea.setEditable(false);
        textoNoEditableArea.setFont(new Font("Arial", Font.PLAIN, 20));
        rightTextAreaPanel.add(new JScrollPane(textoNoEditableArea), BorderLayout.CENTER);

        // Crear un panel para la derecha con fondo gris claro
        JPanel rightMarginPanel = new JPanel(new BorderLayout()) {
            // Sobreescribir el método paintComponent para establecer la imagen de fondo
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };
        rightMarginPanel.setOpaque(false); // Hacer el panel transparente
        rightMarginPanel.setPreferredSize(new Dimension(100, getHeight()));
        principalPanel.add(rightMarginPanel, BorderLayout.EAST);

        // Agregar los cuadros de texto con títulos a los paneles correspondientes
        textPanel.add(leftTextAreaPanel);
        textPanel.add(rightTextAreaPanel);

        principalPanel.add(textPanel, BorderLayout.CENTER);

        // Panel para los botones en la parte inferior
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false); // Hacer el panel transparente
        JButton cifrarButton = new JButton("Cifrar");
        JButton reiniciarButton = new JButton("Reiniciar");
        buttonPanel.add(cifrarButton);
        buttonPanel.add(reiniciarButton);

        cifrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String mensajeOriginal = textoEditableArea.getText();
                String mensajeCifrado = cifrarMensaje(mensajeOriginal);
                textoNoEditableArea.setText(mensajeCifrado);
            }
        });

        reiniciarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textoEditableArea.setText("");
                textoNoEditableArea.setText("");
            }
        });

        principalPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Agregar el contenido de la página principal a la pestaña "Mensaje"
        tabbedPane.addTab("Mensaje", principalPanel);

        // Pestaña de Datos
        JPanel datosPanel = new JPanel(new BorderLayout()) {
            // Sobreescribir el método paintComponent para establecer la imagen de fondo
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Cargar la imagen de fondo
                Image backgroundImage = new ImageIcon("src/main/java/com/example/nasa/datos.jpg").getImage();
                // Dibujar la imagen de fondo
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };

        // Panel para mostrar los datos con un borde redondeado
        JPanel datosMostrarPanel = new JPanel(new GridLayout(2, 1));
        datosMostrarPanel.setOpaque(false); // Hacer el panel transparente
        datosMostrarPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Campo de teléfono (No editable)
        JLabel telefonoLabel = new JLabel("Teléfono:");
        telefonoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        telefonoLabel.setForeground(Color.DARK_GRAY); // Color de texto más oscuro
        telefonoTextField = new JTextField(20);
        telefonoTextField.setEditable(false);
        telefonoTextField.setFont(new Font("Arial", Font.PLAIN, 18));

        // Campo de "nom_clau" (No editable)
        JLabel nomClauLabel = new JLabel("Nom Clau:");
        nomClauLabel.setFont(new Font("Arial", Font.BOLD, 18));
        nomClauLabel.setForeground(Color.DARK_GRAY); // Color de texto más oscuro
        nomClauTextField = new JTextField(20);
        nomClauTextField.setEditable(false);
        nomClauTextField.setFont(new Font("Arial", Font.PLAIN, 18));

        datosMostrarPanel.add(telefonoLabel);
        datosMostrarPanel.add(telefonoTextField);
        datosMostrarPanel.add(nomClauLabel);
        datosMostrarPanel.add(nomClauTextField);

        // Botón para cargar datos desde la base de datos
        JButton cargarDatosButton = new JButton("Cargar Datos");
        cargarDatosButton.setFont(new Font("Arial", Font.BOLD, 18));
        cargarDatosButton.setBackground(new Color(0, 128, 255)); // Color de fondo azul
        cargarDatosButton.setForeground(Color.WHITE); // Color de texto blanco
        cargarDatosButton.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2)); // Borde azul
        cargarDatosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarDatosDesdeBaseDeDatos();
            }
        });

        // Agregar el panel de mostrar datos y el botón al panel de Datos
        datosPanel.add(datosMostrarPanel, BorderLayout.CENTER);
        datosPanel.add(cargarDatosButton, BorderLayout.SOUTH);

        // Agregar la pestaña de Datos al panel de pestañas
        tabbedPane.addTab("Datos", datosPanel);

        // Agregar el panel de pestañas al marco
        add(tabbedPane);

        // Establecer la conexión a la base de datos MySQL
        establecerConexionBaseDeDatos();

        setVisible(true);
    }

    private void establecerConexionBaseDeDatos() {
        try {
            // Establecer la conexión a la base de datos MySQL (cambia la URL por la tuya)
            String url = "jdbc:mysql://localhost:3306/nasa";
            String usuario = "root";
            String contraseña = "Admin123";
            conexion = DriverManager.getConnection(url, usuario, contraseña);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void cargarDatosDesdeBaseDeDatos() {
        try {
            // Obtener los datos del usuario con sesión iniciada desde la base de datos
            String sql = "SELECT nom_clau, telefon FROM espia WHERE ID = (SELECT ID FROM Usuario WHERE nombreUsuario = ?)";
            PreparedStatement preparedStatement = conexion.prepareStatement(sql);
            preparedStatement.setString(1, nombreUsuario);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String telefono = resultSet.getString("telefon");
                String nomClau = resultSet.getString("nom_clau");
                telefonoTextField.setText(telefono);
                nomClauTextField.setText(nomClau);
            } else {
                JOptionPane.showMessageDialog(this, "No se encontraron datos en la base de datos.");
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar los datos desde la base de datos.");
        }
    }


    private String cifrarMensaje(String mensaje) {
        StringBuilder mensajeCifrado = new StringBuilder();
        for (char letra : mensaje.toCharArray()) {
            if (esVocal(letra)) {
                mensajeCifrado.append(letra);
            }
        }
        return mensajeCifrado.toString();
    }

    private boolean esVocal(char letra) {
        letra = Character.toLowerCase(letra);
        return letra == 'a' || letra == 'e' || letra == 'i' || letra == 'o' || letra == 'u' ||
                letra == 'á' || letra == 'é' || letra == 'í' || letra == 'ó' || letra == 'ú';
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                new pag_espia("Nombre de Usuario"); // Debes proporcionar el nombre de usuario aquí
//            }
//        });
//    }
}





























