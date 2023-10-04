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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class pag_fisico extends JFrame {

    private BufferedImage fondo;
    private JButton ficharButton;
    private JButton calcularDistanciaButton;
    private JButton calcularSuperficieButton;

    private JButton salirButton;
    private boolean entradaRegistrada = false;

    // Componentes de la calculadora de distancia
    private JPanel calculadoraDistanciaPanel;
    private JComboBox<String> planetasComboBox;
    private JLabel resultadoLabel;

    public pag_fisico(String nombreUsuario) {
        setTitle("Pàgina del Físic");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Cargar la imagen de fondo
        try {
            fondo = ImageIO.read(new File("C:\\Users\\ddiaz\\OneDrive\\Escritorio\\die.jpg")); // Reemplaza con la ruta de tu imagen
        } catch (IOException e) {
            e.printStackTrace();
        }

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(getWidth(), getHeight()));

        // --------- mensaje entrada -----------------

        JLabel saludoLabel = new JLabel("Hola Doctor " + nombreUsuario);
        //saludoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        saludoLabel.setFont(new Font("Times New Roman", Font.BOLD, 36));
        saludoLabel.setVerticalAlignment(JLabel.TOP);
        saludoLabel.setBounds(300, 50, 450, 50);
        layeredPane.add(saludoLabel, Integer.valueOf(1));

        // ------------ FIXA TECNICA --------------------

        JPanel fichaTecnicaPanel = new JPanel(new GridLayout(0, 2));
        fichaTecnicaPanel.setBackground(Color.WHITE);
        fichaTecnicaPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createLineBorder(Color.BLACK)
        ));
        fichaTecnicaPanel.setBounds(250, 150, 400, 400);
        layeredPane.add(fichaTecnicaPanel, Integer.valueOf(2));

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
        layeredPane.add(fondoPanel, Integer.valueOf(0));

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/nasa", "root", "Admin123")) {
            String query = "SELECT * FROM Fisico WHERE nom = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, nombreUsuario);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                agregarCampoFichaTecnica(fichaTecnicaPanel, "Nom:", resultSet.getString("nom"));
                agregarCampoFichaTecnica(fichaTecnicaPanel, "Sexe:", resultSet.getString("sexe"));
                agregarCampoFichaTecnica(fichaTecnicaPanel, "Salari:", resultSet.getBigDecimal("salari").toString());
                agregarCampoFichaTecnica(fichaTecnicaPanel, "Experiencia:", Integer.toString(resultSet.getInt("edat")));
                agregarCampoFichaTecnica(fichaTecnicaPanel, "Titulació académica:", Integer.toString(resultSet.getInt("anys_experiencia")));
                agregarCampoFichaTecnica(fichaTecnicaPanel, "Dirección:", resultSet.getString("adreça"));
                agregarCampoFichaTecnica(fichaTecnicaPanel, "Ciutat:", resultSet.getString("ciutat"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // ------------ Calcular Distancia ----------------------

        calcularDistanciaButton = new JButton("Calcular Distància");
        calcularDistanciaButton.setBounds(70, 150, 150, 30);
        calcularDistanciaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarCalculadoraDistancia();
            }
        });
        layeredPane.add(calcularDistanciaButton, Integer.valueOf(3));

        // Botón "Salir"
        salirButton = new JButton("Salir");
        salirButton.setBounds(70, 520, 150, 30); // Ajusta las coordenadas y dimensiones según tu diseño
        salirButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        layeredPane.add(salirButton, Integer.valueOf(4));


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


        calcularSuperficieButton = new JButton("Calcular Superfície");
        calcularSuperficieButton.setBounds(70, 220, 150, 30);
        calcularSuperficieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Crear un JComboBox vacío para los planetas
                JComboBox<String> planetasComboBox = new JComboBox<>();
                Map<String, Double> superficies = new HashMap<>(); // Mapa para almacenar las superficies

                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/nasa", "root", "Admin123")) {
                    String query = "SELECT nom, superficie FROM planetas";
                    PreparedStatement preparedStatement = conn.prepareStatement(query);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    // Llenar el JComboBox con los nombres de los planetas y almacenar sus superficies en el mapa
                    while (resultSet.next()) {
                        String nombrePlaneta = resultSet.getString("nom");
                        double superficiePlaneta = resultSet.getDouble("superficie");
                        planetasComboBox.addItem(nombrePlaneta);
                        superficies.put(nombrePlaneta, superficiePlaneta);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }



                // Crear un JPanel para colocar el JComboBox y el botón "Calcular"
                JPanel superficiePanel = new JPanel();
                superficiePanel.setLayout(new FlowLayout());
                superficiePanel.add(new JLabel("Calculi la superfície de:"));
                superficiePanel.add(planetasComboBox);

                // Crear un botón "Calcular"
                JButton calcularSuperficie = new JButton("Calcular");

                // ActionListener para el botón "Calcular"
                calcularSuperficie.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String planetaSeleccionado = (String) planetasComboBox.getSelectedItem();

                        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/nasa", "root", "Admin123")) {
                            String query = "SELECT superficie FROM planetas WHERE nom = ?";
                            PreparedStatement preparedStatement = conn.prepareStatement(query);
                            preparedStatement.setString(1, planetaSeleccionado);
                            ResultSet resultSet = preparedStatement.executeQuery();

                            if (resultSet.next()) {
                                double superficiePlaneta = resultSet.getDouble("superficie");
                                double precioCombustiblePorKm2 = 200.0; // Precio por km² de combustible
                                double precioViaje = superficiePlaneta * precioCombustiblePorKm2;

                                // Formatea la superficie para mostrar todos los ceros
                                String superficieFormateada = String.format("%.2f", superficiePlaneta);

                                // Formatea el precio para mostrarlo en formato de moneda
                                String precioFormateado = String.format("$%,.2f", precioViaje);

                                // Compara la superficie con el planeta más grande y el más pequeño
                                String mensaje = "Superficie de " + planetaSeleccionado + ": " + superficieFormateada + " km²\n"
                                        + "Preu d'exploració total: " + precioFormateado;

                                double superficieMaxima = Collections.max(superficies.values());
                                double superficieMinima = Collections.min(superficies.values());

                                if (superficiePlaneta == superficieMaxima) {
                                    mensaje += "\nEs el planeta més car d'explorar actualment";
                                } else if (superficiePlaneta == superficieMinima) {
                                    mensaje += "\nEs el planeta més economic per explorar";
                                }

                                // Muestra el resultado en un JOptionPane
                                JOptionPane.showMessageDialog(null, mensaje, "Preu del Viaje", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null, "No hi ha informació suficient " + planetaSeleccionado, "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                });

                // Agregar el botón "Calcular" al JPanel
                superficiePanel.add(calcularSuperficie);

                // Crear un JFrame para mostrar el JPanel
                JFrame superficieFrame = new JFrame("Calcular Superficie");
                superficieFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                superficieFrame.setSize(400, 100);
                superficieFrame.setLocationRelativeTo(null);
                superficieFrame.add(superficiePanel);

                // Hacer visible el JFrame
                superficieFrame.setVisible(true);
            }
        });


        layeredPane.add(calcularSuperficieButton, Integer.valueOf(4));



        ficharButton = new JButton("Fitxar Entrada");
        ficharButton.setBounds(70, 270, 150, 30);
        ficharButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!entradaRegistrada) {
                    ficharEntrada();
                    entradaRegistrada = true;
                    ficharButton.setText("Fitxar Sortida");
                } else {
                    ficharSalida();
                    entradaRegistrada = false;
                    ficharButton.setText("Fitxar Entrada");
                }
            }
        });
        layeredPane.add(ficharButton, Integer.valueOf(5));

        // --------------------- Configuración del panel de la calculadora de distancia -------------------------------
        calculadoraDistanciaPanel = new JPanel();
        calculadoraDistanciaPanel.setBackground(Color.WHITE);
        calculadoraDistanciaPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createLineBorder(Color.BLACK)
        ));
        calculadoraDistanciaPanel.setBounds(700, 150, 300, 400);
        calculadoraDistanciaPanel.setVisible(false);
        layeredPane.add(calculadoraDistanciaPanel, Integer.valueOf
                 (6));

        JLabel calculadoraDistanciaTitulo = new JLabel("Calculi la distància de la Terra a un planeta:");
        calculadoraDistanciaTitulo.setFont(new Font("Arial", Font.PLAIN, 11));
        calculadoraDistanciaTitulo.setBounds(10, 10, 280, 20);
        calculadoraDistanciaPanel.add(calculadoraDistanciaTitulo);

        String[] planetas = {"Mercuri", "Venus", "Sol", "Marte", "Júpiter", "Saturn", "Urà", "Neptú"};
        planetasComboBox = new JComboBox<>(planetas);
        planetasComboBox.setBounds(10, 40, 280, 30);
        calculadoraDistanciaPanel.add(planetasComboBox);

        JButton calcularDistanciaButton = new JButton("Calcular");
        calcularDistanciaButton.setBounds(10, 80, 280, 30);
        calcularDistanciaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calcularDistancia();
            }
        });
        calculadoraDistanciaPanel.add(calcularDistanciaButton);

        resultadoLabel = new JLabel();
        resultadoLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        resultadoLabel.setBounds(10, 120, 280, 20);
        calculadoraDistanciaPanel.add(resultadoLabel);

        // Agrega el JLayeredPane al JFrame
        add(layeredPane);

        // Hace visible la ventana
        setVisible(true);
    }

    private void agregarCampoFichaTecnica(JPanel panel, String etiqueta, String valor) {
        panel.add(new JLabel(etiqueta));
        panel.add(new JLabel(valor));
    }

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
            JOptionPane.showMessageDialog(this, "Fitxatge d'Entrada registrat.", "Fitxatge registrat", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
            JOptionPane.showMessageDialog(this, "Fitxatge de Sortida registrat.", "Fitxatge registrat", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void mostrarCalculadoraDistancia() {
        calculadoraDistanciaPanel.setVisible(true);
    }

    private void calcularDistancia() {
        String planetaSeleccionado = (String) planetasComboBox.getSelectedItem();
        double distancia = obtenerDistancia(planetaSeleccionado);
        double tiempoViaje = (distancia / obtenerDistancia("Sol")) * 2; // Utiliza el valor de 2 años como referencia

        DecimalFormat df = new DecimalFormat("#.##");
        resultadoLabel.setText("<html>La Terra està a " + df.format(distancia) + " km de " + planetaSeleccionado + ".<br>Duració del viatge: " + df.format(tiempoViaje) + " anys.</html>");
    }



    private double obtenerDistancia(String planeta) {
        double distancia = 0.0;
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/nasa", "root", "Admin123")) {
            String query = "SELECT distancia_terra FROM planetas WHERE nom = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, planeta);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                distancia = resultSet.getDouble("distancia_terra");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return distancia;
    }




    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new pag_fisico("Nombre de Usuario"));
    }
}
