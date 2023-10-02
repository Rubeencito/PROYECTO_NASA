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

public class pag_mecanico extends JFrame {
    private JButton ficharButton; // Declarar como variable de instancia
    private boolean entradaRegistrada = false; // Agregar una variable para el estado de fichaje

    private JButton adminButton; // Declarar como variable de instancia
    public pag_mecanico(String nombreUsuario) {
        // Configura la ventana
        setTitle("Página del Mecánico");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximiza la ventana a pantalla completa

        // Obtiene las dimensiones de la pantalla
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // Carga la imagen de fondo desde un archivo
        ImageIcon backgroundImage = new ImageIcon("src/main/java/com/example/nasa/fondoMecanico.jpg");
        Image img = backgroundImage.getImage();
        // Escala la imagen para que coincida con las dimensiones de la pantalla
        img = img.getScaledInstance(screenSize.width, screenSize.height, Image.SCALE_SMOOTH);
        backgroundImage = new ImageIcon(img);

        // Crea un JLabel con la imagen de fondo
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setBounds(0, 0, screenSize.width, screenSize.height);
        add(backgroundLabel);

        // Crea una etiqueta con el saludo personalizado
        JLabel saludoLabel = new JLabel("Hola Mecánico " + nombreUsuario);
        //saludoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        saludoLabel.setFont(new Font("Times New Roman", Font.BOLD, 36));
        saludoLabel.setForeground(Color.WHITE);
        saludoLabel.setBounds(450, 50, 400, 70); // Ajusta las coordenadas y dimensiones según tu diseño
        backgroundLabel.add(saludoLabel); // Agregamos la etiqueta al JLabel del fondo

        // -------------------------------- FICHA TECNICA -------------------------


        // Crea un panel para mostrar la ficha técnica del usuario ----------------------------------------------------------------
        JPanel fichaTecnicaPanel = new JPanel(new GridLayout(0, 2)); // Usamos un diseño de cuadrícula para mostrar los datos
        fichaTecnicaPanel.setBackground(Color.WHITE); // Fondo blanco para la ficha técnica
        fichaTecnicaPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10), // Márgenes externos
                BorderFactory.createLineBorder(Color.BLACK) // Borde negro
        ));
        fichaTecnicaPanel.setBounds(250, 170, 400, 400); // Ajusta las coordenadas y dimensiones según tu diseño
        backgroundLabel.add(fichaTecnicaPanel, Integer.valueOf(2)); // Ficha técnica

        // Conecta a la base de datos y consulta los datos del usuario ---------------------------------------------------------------------------
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

        // Agrega un botón para generar y mostrar el archivo de texto ------------------------------------------------------------
        JButton mostrarArchivoButton = new JButton("Mostrar vehiculos");
        mostrarArchivoButton.setBounds(700, 170, 200, 30); // Ajusta las coordenadas y dimensiones según tu diseño
        mostrarArchivoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generarYMostrarArchivoTexto();
            }
        });
        backgroundLabel.add(mostrarArchivoButton, Integer.valueOf(3)); // Botón

        // Crea un botón para fichar entrada o salida ------------------------------------------------------------------------
        ficharButton = new JButton("Fichar Entrada"); // Asigna el botón a la variable de instancia
        ficharButton.setBounds(700, 220, 200, 30); // Ajusta las coordenadas y dimensiones según tu diseño
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
        backgroundLabel.add(ficharButton, Integer.valueOf(4)); // Botón de fichar

        // Boton para el CRUD --------------------------------------------------
        adminButton = new JButton("Administrar Datos");
        adminButton.setBounds(700, 270, 200, 30); // Ajusta las coordenadas y dimensiones según tu diseño

// Verifica si el nombre de usuario es "admin" y muestra el botón "Admin" -------------------------------------------------------------------
        if (nombreUsuario.equals("admin")) {
            adminButton.setVisible(true);
        } else {
            adminButton.setVisible(false);
        }

        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (nombreUsuario.equals("admin")) {
                    // Abre la ventana de administración
                    SwingUtilities.invokeLater(() -> new AdminWindow(nombreUsuario));
                }
            }
        });
        backgroundLabel.add(adminButton, Integer.valueOf(5)); // Botón de administración



        // Agrega un botón "Salir" en el constructor de la clase pag_mecanico
        JButton salirButton = new JButton("Salir");
        salirButton.setBounds(700, 540, 200, 30); // Ajusta las coordenadas y dimensiones según tu diseño
        salirButton.setBorder(BorderFactory.createLineBorder(Color.white, 2));
        salirButton.setBackground(Color.BLUE);
        salirButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        salirButton.setForeground(Color.WHITE);
        backgroundLabel.add(salirButton, Integer.valueOf(6)); // Botón de Salir

        // Agrega un ActionListener para el botón "Salir"
        salirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirmacion = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que quieres salir?", "Confirmar Salida", JOptionPane.YES_NO_OPTION);
                if (confirmacion == JOptionPane.YES_OPTION) {
                    // Cierra la página actual (this)
                    dispose();

                    // Crea una instancia de inicio_session
                    SwingUtilities.invokeLater(() -> new inicio_session());
                }
            }
        });

        // ------------- Hace visible la ventana -----------------
        setVisible(true);

    } // ---------------- // ACABA CLASSE //-------------//

    // Método para agregar un campo a la ficha técnica ----------------------------------------------------------------------------
    private void agregarCampoFichaTecnica(JPanel panel, String etiqueta, String valor) {
        panel.add(new JLabel(etiqueta));
        panel.add(new JLabel(valor));
    }
    // Método para mostrar el archivo de texto ------------------------------------------------------------------------------------
    private void generarYMostrarArchivoTexto() {
        try {
            // Conecta a la base de datos
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/nasa", "root", "Admin123");
            String query = "SELECT * FROM Vehiculo";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Ruta del archivo de texto
            String rutaArchivo = "datos_vehiculos.txt";

            // Crea un BufferedWriter para escribir en el archivo de texto
            BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo));

            // Escribe los datos de la tabla en el archivo de texto
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String marca = resultSet.getString("marca");
                String modelo = resultSet.getString("modelo");
                String color = resultSet.getString("color");
                int cv = resultSet.getInt("cv");

                String linea = "ID: " + id + ", Marca: " + marca + ", Modelo: " + modelo + ", Color: " + color + ", CV: " + cv;
                writer.write(linea);
                writer.newLine();
            }

            // Cierra el BufferedWriter
            writer.close();

            // Muestra un cuadro de diálogo informando que se ha creado el archivo
            JOptionPane.showMessageDialog(this, "Archivo de texto generado: " + rutaArchivo, "Archivo Generado", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
    // Método para registrar un fichaje de entrada en la base de datos ----------------------------------------------------------------------
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
    // Método para registrar un fichaje de salida en la base de datos ------------------------------------------------------------------
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
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> new pag_mecanico("Nombre de Usuario"));
//    }
}
