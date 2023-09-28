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

public class pag_fisico extends JFrame {

    private BufferedImage fondo;
    private JButton ficharButton;
    private JButton calcularDistanciaButton;
    private JButton calcularSuperficieButton;
    private JComboBox<String> planetasComboBox;
    private JPanel planetasPanel;
    private JLabel resultadoLabel;

    private boolean entradaRegistrada = false;

    public pag_fisico(String nombreUsuario) {

        setTitle("Pàgina del Físic");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        try {
            fondo = ImageIO.read(new File("C:\\Users\\ddiaz\\OneDrive\\Escritorio\\die.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(getWidth(), getHeight()));

        JLabel saludoLabel = new JLabel("Hola Doctor " + nombreUsuario);
        saludoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        saludoLabel.setVerticalAlignment(JLabel.TOP);
        saludoLabel.setBounds(50, 50, 300, 30);
        layeredPane.add(saludoLabel, Integer.valueOf(1));

        JPanel fichaTecnicaPanel = new JPanel(new GridLayout(0, 2));
        fichaTecnicaPanel.setBackground(Color.WHITE);
        fichaTecnicaPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createLineBorder(Color.BLACK)
        ));
        fichaTecnicaPanel.setBounds(50, 100, 300, 300);
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
                agregarCampoFichaTecnica(fichaTecnicaPanel, "Titulació acadèmica:", Integer.toString(resultSet.getInt("anys_experiencia")));
                agregarCampoFichaTecnica(fichaTecnicaPanel, "Adreça:", resultSet.getString("adreça"));
                agregarCampoFichaTecnica(fichaTecnicaPanel, "Ciutat:", resultSet.getString("ciutat"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        calcularDistanciaButton = new JButton("Calcular Distància");
        calcularDistanciaButton.setBounds(50, 450, 150, 30);
        calcularDistanciaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarRecuadroDatos();
            }
        });
        layeredPane.add(calcularDistanciaButton, Integer.valueOf(3));

        calcularSuperficieButton = new JButton("Calcular Superfície");
        calcularSuperficieButton.setBounds(210, 450, 150, 30);
        calcularSuperficieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implementa la lógica para calcular superfície aquí
            }
        });
        layeredPane.add(calcularSuperficieButton, Integer.valueOf(6));

        ficharButton = new JButton("Fichar Entrada");
        ficharButton.setBounds(470, 450, 150, 30);
        ficharButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!entradaRegistrada) {
                    ficharEntrada();
                    entradaRegistrada = true;
                    ficharButton.setText("Fichar Sortida");
                } else {
                    ficharSalida();
                    entradaRegistrada = false;
                    ficharButton.setText("Fichar Entrada");
                }
            }
        });
        layeredPane.add(ficharButton, Integer.valueOf(9));

        // Creación del panel para calcular distancia
        planetasPanel = new JPanel();
        planetasPanel.setBounds(600, 100, 400, 300);
        planetasPanel.setBackground(Color.WHITE);
        planetasPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createLineBorder(Color.BLACK)
        ));
        planetasPanel.setVisible(false);
        planetasPanel.setLayout(new BorderLayout());

        JLabel tituloCalculoLabel = new JLabel("Calcule la distància de la Terra a un planeta");
        tituloCalculoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        tituloCalculoLabel.setHorizontalAlignment(JLabel.CENTER);
        planetasPanel.add(tituloCalculoLabel, BorderLayout.NORTH);

        JPanel calculoPanel = new JPanel();
        calculoPanel.setLayout(new FlowLayout());
        planetasPanel.add(calculoPanel, BorderLayout.CENTER);

        String[] nombresPlanetas = {"Mercuri", "Venus", "Sol", "Marte", "Júpiter", "Saturn", "Urà", "Neptú"};
        planetasComboBox = new JComboBox<>(nombresPlanetas);
        calculoPanel.add(planetasComboBox);

        JButton calcularButton = new JButton("Calcular");
        calcularButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calcularDistancia();
            }
        });
        calculoPanel.add(calcularButton);

        resultadoLabel = new JLabel("");
        resultadoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        resultadoLabel.setHorizontalAlignment(JLabel.CENTER);
        planetasPanel.add(resultadoLabel, BorderLayout.SOUTH);

        layeredPane.add(planetasPanel, Integer.valueOf(8));

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
            JOptionPane.showMessageDialog(this, "Fichaje de Entrada registrado.", "Fichaje Registrado", JOptionPane.INFORMATION_MESSAGE);
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
            JOptionPane.showMessageDialog(this, "Fichaje de Salida registrado.", "Fichaje Registrado", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void mostrarRecuadroDatos() {
        planetasPanel.setVisible(true);
    }

    private void calcularDistancia() {
        String planetaSeleccionado = (String) planetasComboBox.getSelectedItem();
        String distancia = obtenerDistanciaTierra(planetaSeleccionado);
        resultadoLabel.setText("La Terra està a " + distancia + " km de " + planetaSeleccionado);
    }

    private String obtenerDistanciaTierra(String planeta) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/nasa", "root", "Admin123")) {
            String query = "SELECT distancia_terra FROM planetas WHERE nom = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, planeta);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("distancia_terra");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Desconeguda";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new pag_fisico("Nom d'Usuari"));
    }
}
