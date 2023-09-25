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
    private BufferedImage fondo; // Para almacenar la imagen de fondo
    private  JButton ficharButton; // Declarar como variable de instancia
    private boolean entradaRegistrada = false; // Agregar una variable para el estado de fichaje

    public pag_mecanico(String nombreUsuario) {
        // Configura la ventana
        setTitle("Página del Mecánico");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximiza la ventana a pantalla completa

        // Carga la imagen de fondo desde un archivo
        try {
            fondo = ImageIO.read(new File("src/main/java/com/example/nasa/fondoMecanico.jpg")); // Reemplaza con la ruta de tu imagen
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Crea un JLayeredPane para superponer componentes
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(getWidth(), getHeight()));

        // Crea un panel para la foto de fondo
        JPanel fondoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (fondo != null) {
                    g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        fondoPanel.setBounds(0, 0, getWidth(), getHeight());
        layeredPane.add(fondoPanel, Integer.valueOf(0)); // Fondo

        // Crea una etiqueta con el saludo personalizado
        JLabel saludoLabel = new JLabel("Hola Mecánico " + nombreUsuario);
        saludoLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Establece la fuente y el tamaño a 16
        saludoLabel.setVerticalAlignment(JLabel.TOP); // Alinea el texto arriba
        saludoLabel.setBounds(50, 50, 300, 30); // Ajusta las coordenadas y dimensiones según tu diseño
        layeredPane.add(saludoLabel, Integer.valueOf(1)); // Saludo

        // Crea un panel para mostrar la ficha técnica del usuario
        JPanel fichaTecnicaPanel = new JPanel(new GridLayout(0, 2)); // Usamos un diseño de cuadrícula para mostrar los datos
        fichaTecnicaPanel.setBackground(Color.WHITE); // Fondo blanco para la ficha técnica
        fichaTecnicaPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10), // Márgenes externos
                BorderFactory.createLineBorder(Color.BLACK) // Borde negro
        ));
        fichaTecnicaPanel.setBounds(50, 100, 300, 300); // Ajusta las coordenadas y dimensiones según tu diseño
        layeredPane.add(fichaTecnicaPanel, Integer.valueOf(2)); // Ficha técnica



        // Conecta a la base de datos y consulta los datos del usuario
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



        // Agrega un botón para generar y mostrar el archivo de texto
        JButton mostrarArchivoButton = new JButton("Mostrar Archivo de Texto");
        mostrarArchivoButton.setBounds(50, 450, 200, 30); // Ajusta las coordenadas y dimensiones según tu diseño
        mostrarArchivoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generarYMostrarArchivoTexto();
            }
        });
        layeredPane.add(mostrarArchivoButton, Integer.valueOf(3)); // Botón

        // Crea un botón para fichar entrada o salida
        ficharButton = new JButton("Fichar Entrada"); // Asigna el botón a la variable de instancia
        ficharButton.setBounds(300, 450, 150, 30); // Ajusta las coordenadas y dimensiones según tu diseño
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
        layeredPane.add(ficharButton, Integer.valueOf(4)); // Botón de fichar

        // Agrega el JLayeredPane al JFrame
        add(layeredPane);

        // Hace visible la ventana
        setVisible(true);
    }

    // Método para agregar un campo a la ficha técnica
    private void agregarCampoFichaTecnica(JPanel panel, String etiqueta, String valor) {
        panel.add(new JLabel(etiqueta));
        panel.add(new JLabel(valor));
    }
    // Método para mostrar el archivo de texto
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
    // Método para registrar un fichaje de entrada en la base de datos
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

    // Método para registrar un fichaje de salida en la base de datos
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new pag_mecanico("Nombre de Usuario"));
    }
}
