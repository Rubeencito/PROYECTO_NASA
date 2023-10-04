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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class pag_fisico extends JFrame {

    private boolean entradaRegistrada = false;

    //botons principals
    private JButton fitxarButton;
    private JButton calcularDistanciaButton;
    private JButton calcularSuperficieButton;
    private JButton sortirButton;


    // Components de la calculadora de distancia
    private JPanel calculadoraDistanciaPanel;
    private JComboBox<String> planetasComboBox;
    private JLabel resultatsLabel;

    public pag_fisico(String nomUsuari) {
        setTitle("Pàgina del Físic");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(getWidth(), getHeight()));

        // --------- Missatje entrada -----------------

        JLabel salutacioLabel = new JLabel("Hola Doctor " + nomUsuari);
        salutacioLabel.setFont(new Font("Times New Roman", Font.BOLD, 36));
        salutacioLabel.setVerticalAlignment(JLabel.TOP);
        salutacioLabel.setBounds(300, 50, 450, 50);
        layeredPane.add(salutacioLabel, Integer.valueOf(1));

        // ------------ FITXA TECNICA --------------------

        JPanel fitxaTecnicaPanel = new JPanel(new GridLayout(0, 2));
        fitxaTecnicaPanel.setBackground(Color.WHITE);
        fitxaTecnicaPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createLineBorder(Color.BLACK)
        ));
        fitxaTecnicaPanel.setBounds(250, 150, 400, 400);
        layeredPane.add(fitxaTecnicaPanel, Integer.valueOf(2));


        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/nasa", "root", "Admin123")) {
            String query = "SELECT * FROM Fisico WHERE nom = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, nomUsuari);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                afegirCampFitxaTecnica(fitxaTecnicaPanel, "Nom:", resultSet.getString("nom"));
                afegirCampFitxaTecnica(fitxaTecnicaPanel, "Sexe:", resultSet.getString("sexe"));
                afegirCampFitxaTecnica(fitxaTecnicaPanel, "Salari:", resultSet.getBigDecimal("salari").toString());
                afegirCampFitxaTecnica(fitxaTecnicaPanel, "Experiencia:", Integer.toString(resultSet.getInt("edat")));
                afegirCampFitxaTecnica(fitxaTecnicaPanel, "Titulació académica:", Integer.toString(resultSet.getInt("anys_experiencia")));
                afegirCampFitxaTecnica(fitxaTecnicaPanel, "Dirección:", resultSet.getString("adreça"));
                afegirCampFitxaTecnica(fitxaTecnicaPanel, "Ciutat:", resultSet.getString("ciutat"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }



        // -----------------------Botó "Sortir"--------------------------
        sortirButton = new JButton("Sortir");
        sortirButton.setBounds(70, 520, 150, 30); // Ajusta les coordenadas i dimensions
        sortirButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        layeredPane.add(sortirButton, Integer.valueOf(4));


        //  ActionListener Del botó "Sortir"
        sortirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirmacio = JOptionPane.showConfirmDialog(null, "Estàs segur de que vols sortir?", "Confirmar Sortida", JOptionPane.YES_NO_OPTION);
                if (confirmacio == JOptionPane.YES_OPTION) {
                    dispose();
                }
            }
        });

//----------------- FUNCIONAMENT CALCULAR SUPERFICIE---------------
        calcularSuperficieButton = new JButton("Calcular Superfície");
        calcularSuperficieButton.setBounds(70, 220, 150, 30);
        calcularSuperficieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox<String> planetesComboBox = new JComboBox<>();
                Map<String, Double> superficies = new HashMap<>(); // Mapa per guardar les superficies

                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/nasa", "root", "Admin123")) {
                    String query = "SELECT nom, superficie FROM planetas";
                    PreparedStatement preparedStatement = conn.prepareStatement(query);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    // Emplenar el JComboBox amb els planetes y guardars les superficies al mapa
                    while (resultSet.next()) {
                        String nomPlaneta = resultSet.getString("nom");
                        double superficiePlaneta = resultSet.getDouble("superficie");
                        planetesComboBox.addItem(nomPlaneta);
                        superficies.put(nomPlaneta, superficiePlaneta);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                JPanel superficiePanel = new JPanel();
                superficiePanel.setLayout(new FlowLayout());
                superficiePanel.add(new JLabel("Calculi la superfície de:"));
                superficiePanel.add(planetesComboBox);
                JButton calcularSuperficie = new JButton("Calcular");

                // ActionListener del botó "Calcular"
                calcularSuperficie.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String planetaSeleccionat = (String) planetesComboBox.getSelectedItem();

                        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/nasa", "root", "Admin123")) {
                            String query = "SELECT superficie FROM planetas WHERE nom = ?";
                            PreparedStatement preparedStatement = conn.prepareStatement(query);
                            preparedStatement.setString(1, planetaSeleccionat);
                            ResultSet resultSet = preparedStatement.executeQuery();

                            if (resultSet.next()) {
                                double superficiePlaneta = resultSet.getDouble("superficie");
                                double precuCombustibleKm2 = 200.0; // Preu per km² de combustible
                                double preuViatje = superficiePlaneta * precuCombustibleKm2;

                                //  Mostrar tots els zeros de la superficie
                                String superficieFormateada = String.format("%.2f", superficiePlaneta);

                                // Mostrar el preu en formato de moneda
                                String preuFormateado = String.format("$%,.2f", preuViatje);


                                String missatge = "Superficie de " + planetaSeleccionat + ": " + superficieFormateada + " km²\n"
                                        + "Preu d'exploració total: " + preuFormateado;

                                // Compara la superficie amb el planeta més gran i el més petit
                                double superficieMaxima = Collections.max(superficies.values());
                                double superficieMinima = Collections.min(superficies.values());

                                if (superficiePlaneta == superficieMaxima) {
                                    missatge += "\nEs el planeta més car d'explorar actualment";
                                } else if (superficiePlaneta == superficieMinima) {
                                    missatge += "\nEs el planeta més economic per explorar";
                                }

                                // Mostra el resultat en un JOptionPane
                                JOptionPane.showMessageDialog(null, missatge, "Preu del Viaje", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null, "No hi ha informació suficient " + planetaSeleccionat, "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                });

                // Afegir el botó "Calcular" al JPanel
                superficiePanel.add(calcularSuperficie);

                // Crear un JFrame per mostrar el JPanel
                JFrame superficieFrame = new JFrame("Calcular Superficie");
                superficieFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                superficieFrame.setSize(400, 100);
                superficieFrame.setLocationRelativeTo(null);
                superficieFrame.add(superficiePanel);
                superficieFrame.setVisible(true);
            }
        });
        layeredPane.add(calcularSuperficieButton, Integer.valueOf(4));

        // --------------------- Configuración del panel de la calculadora de distancia -------------------------------
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

        JLabel calculadoraDistanciaTitol = new JLabel("Calculi la distància de la Terra a un planeta:");
        calculadoraDistanciaTitol.setFont(new Font("Arial", Font.PLAIN, 11));
        calculadoraDistanciaTitol.setBounds(10, 10, 280, 20);
        calculadoraDistanciaPanel.add(calculadoraDistanciaTitol);

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

        resultatsLabel = new JLabel();
        resultatsLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        resultatsLabel.setBounds(10, 120, 280, 20);
        calculadoraDistanciaPanel.add(resultatsLabel);


        add(layeredPane);

        setVisible(true);

        //-------------------------Configuració botó de fitxar --------------------------------------

        fitxarButton = new JButton("Fitxar Entrada");
        fitxarButton.setBounds(70, 270, 150, 30);
        fitxarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!entradaRegistrada) {
                    fitxarEntrada();
                    entradaRegistrada = true;
                    fitxarButton.setText("Fitxar Sortida");
                } else {
                    fitxarSortida();
                    entradaRegistrada = false;
                    fitxarButton.setText("Fitxar Entrada");
                }
            }
        });
        layeredPane.add(fitxarButton, Integer.valueOf(5));


    }

    private void afegirCampFitxaTecnica(JPanel panel, String etiqueta, String valor) {
        panel.add(new JLabel(etiqueta));
        panel.add(new JLabel(valor));
    }


    //----------------LOGICA FITXAR ENTRADA I SORTIDA -----------------------------
    private void fitxarEntrada() {
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

    private void fitxarSortida() {
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

    //-----------------------CALCULAR DISTANCIA ENTRE ELS PLANETES I LA TERRA--------------------

    private void mostrarCalculadoraDistancia() {
        calculadoraDistanciaPanel.setVisible(true);
    }

    private void calcularDistancia() {
        String planetaSeleccionat = (String) planetasComboBox.getSelectedItem();
        double distancia = obtenirDistancia(planetaSeleccionat);
        double tempsViatge = (distancia / obtenirDistancia("Sol")) * 2; // Utiliza el valor de 2 anys com a referencia

        DecimalFormat df = new DecimalFormat("#.##");
        resultatsLabel.setText("<html>La Terra està a " + df.format(distancia) + " km de " + planetaSeleccionat + ".<br>Duració del viatge: " + df.format(tempsViatge) + " anys.</html>");
    }


    private double obtenirDistancia(String planeta) {
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




//accés rapid a la pagina per fer proves concretes
/*    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new pag_fisico("Nombre de Usuario"));
    }

 */
}
