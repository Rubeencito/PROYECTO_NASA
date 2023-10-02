package com.example.nasa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.sql.ResultSet;
import java.util.Random;

class pag_astronauta extends JFrame {
    private JPanel mainPanel;
    private JLabel astronautLabel;
    private JPanel centerPanel;
    private JPanel noDataPanel;

    public pag_astronauta(String nombreUsuario) {
        setTitle("Página del Astronauta");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Hacer que la ventana sea maximizada
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menú");
        JMenuItem mensajesItem = new JMenuItem("Mensajes");
        mensajesItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarPaginaMensajes();
            }
        });

        JMenuItem fichaTecnicaItem = new JMenuItem("Ficha Técnica");
        fichaTecnicaItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirFichaTecnica(nombreUsuario);
            }
        });

        JMenuItem coordenadasItem = new JMenuItem("Coordenadas");
        coordenadasItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarPaginaCoordenadas();
            }
        });

        menu.add(mensajesItem);
        menu.add(fichaTecnicaItem);
        menu.add(coordenadasItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(200, 200, 200));
        mainPanel.setPreferredSize(new Dimension(800, 600));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(800, 600));

        layeredPane.setLayout(new GridBagLayout());

        GridBagConstraints textConstraints = new GridBagConstraints();
        textConstraints.gridx = 0;
        textConstraints.gridy = 0;
        textConstraints.weighty = 1.0;
        textConstraints.anchor = GridBagConstraints.NORTH;
        textConstraints.insets = new Insets(30, 0, 0, 0);

        JLabel welcomeLabel = new JLabel("¡Benvingut ASTRONAUTA " + nombreUsuario + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 30));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        layeredPane.add(welcomeLabel, textConstraints);

        GridBagConstraints imageConstraints = new GridBagConstraints();
        imageConstraints.gridx = 0;
        imageConstraints.gridy = 1;
        imageConstraints.weighty = 1.0;
        imageConstraints.anchor = GridBagConstraints.NORTH;
        imageConstraints.insets = new Insets(10, 0, 0, 0);

        try {
            ImageIcon astronautIcon = new ImageIcon("C:\\Users\\pauca\\IdeaProjects\\PROYECTO_NASA__\\src\\main\\java\\com\\example\\nasa\\astronauta.png");
            int imageWidth = astronautIcon.getIconWidth() / 2;
            int imageHeight = astronautIcon.getIconHeight() / 2;
            astronautIcon.setImage(astronautIcon.getImage().getScaledInstance(imageWidth, imageHeight, Image.SCALE_DEFAULT));
            JLabel astronautLabel = new JLabel(astronautIcon);
            astronautLabel.setHorizontalAlignment(SwingConstants.CENTER);
            layeredPane.add(astronautLabel, imageConstraints);
        } catch (Exception e) {
            e.printStackTrace();
        }

        add(layeredPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private void mostrarPaginaMensajes() {
        JFrame mensajesFrame = new JFrame("Página de Mensajes");
        mensajesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mensajesFrame.setSize(800, 600);
        mensajesFrame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel mensajesLabel = new JLabel("MENSAJES");
        mensajesLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(mensajesLabel, gbc);

        gbc.gridy++;
        JLabel mensajeLabel = new JLabel("Escribe tu mensaje:");
        mainPanel.add(mensajeLabel, gbc);

        gbc.gridy++;
        JTextField mensajeField = new JTextField(20);
        mainPanel.add(mensajeField, gbc);

        gbc.gridy++;
        JButton encriptarButton = new JButton("Encriptar Mensaje");
        encriptarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String mensaje = mensajeField.getText();
                String mensajeEncriptado = encriptarMensaje(mensaje);
                JOptionPane.showMessageDialog(mensajesFrame, "Mensaje encriptado y enviado: " + mensajeEncriptado);
            }
        });
        mainPanel.add(encriptarButton, gbc);

        mensajesFrame.add(mainPanel);
        mensajesFrame.setVisible(true);
    }


    private String encriptarMensaje(String mensaje) {
        StringBuilder mensajeEncriptado = new StringBuilder();
        for (char c : mensaje.toCharArray()) {
            if (esConsonante(Character.toLowerCase(c))) {
                mensajeEncriptado.append(c);
            }
        }
        return mensajeEncriptado.toString();
    }

    private boolean esConsonante(char c) {
        return !esVocal(c) && Character.isLetter(c);
    }

    private boolean esVocal(char c) {
        return c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u';
    }



    private void mostrarDireccionAleatoria() {
        Random random = new Random();
        int latitudGrados = random.nextInt(90);
        int latitudMinutos = random.nextInt(60);
        int latitudSegundos = random.nextInt(60);
        char latitudHemisferio = random.nextBoolean() ? 'N' : 'S';

        int longitudGrados = random.nextInt(180);
        int longitudMinutos = random.nextInt(60);
        int longitudSegundos = random.nextInt(60);
        char longitudHemisferio = random.nextBoolean() ? 'E' : 'W';

        String mensaje = "Coordenadas:\n" +
                "Latitud: " + latitudGrados + "° " + latitudMinutos + "' " + latitudSegundos + "'' " + latitudHemisferio + "\n" +
                "Longitud: " + longitudGrados + "° " + longitudMinutos + "' " + longitudSegundos + "'' " + longitudHemisferio;

        JOptionPane.showMessageDialog(this, mensaje);
    }

    private void abrirFichaTecnica(String nombreUsuario) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/NASA", "root", "admin123");
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM Astronauta INNER JOIN Usuario ON Astronauta.ID = Usuario.ID WHERE nombreUsuario = '" + nombreUsuario + "'";
            ResultSet rs = stmt.executeQuery(query);

            PaginaFichaTecnica fichaTecnicaPage = new PaginaFichaTecnica(nombreUsuario, rs);
            fichaTecnicaPage.setVisible(true);

            if (rs.next()) {
                JPanel dataPanel = new JPanel(new GridLayout(7, 2));
                dataPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                dataPanel.add(new JLabel("Nombre:"));
                dataPanel.add(new JLabel(rs.getString("nom")));

                dataPanel.add(new JLabel("Edad primer vuelo:"));
                dataPanel.add(new JLabel(String.valueOf(rs.getInt("edat_primer_vol"))));

                dataPanel.add(new JLabel("Edad:"));
                dataPanel.add(new JLabel(String.valueOf(rs.getInt("edat"))));

                dataPanel.add(new JLabel("Misiones exitosas:"));
                dataPanel.add(new JLabel(String.valueOf(rs.getInt("missions_ok"))));

                dataPanel.add(new JLabel("Misiones fallidas:"));
                dataPanel.add(new JLabel(String.valueOf(rs.getInt("missions_ko"))));

                dataPanel.add(new JLabel("Dirección:"));
                dataPanel.add(new JLabel(rs.getString("adreça")));

                dataPanel.add(new JLabel("Rango militar:"));
                dataPanel.add(new JLabel(rs.getString("rang_militar")));

                mainPanel.remove(centerPanel);
                centerPanel = dataPanel;
                mainPanel.add(centerPanel, BorderLayout.CENTER);
            } else {
                JLabel noDataLabel = new JLabel("No se encontraron datos para este usuario.");
                noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
                mainPanel.remove(centerPanel);
                centerPanel = noDataPanel;
                mainPanel.add(centerPanel, BorderLayout.CENTER);
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.mainPanel.revalidate();
        this.mainPanel.repaint();
    }

    class PaginaFichaTecnica extends JFrame {
        public PaginaFichaTecnica(String nombreUsuario, ResultSet rs) {
            setTitle("Ficha Técnica");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setSize(400, 300);
            setLocationRelativeTo(null);

            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.anchor = GridBagConstraints.WEST;
            gbc.insets = new Insets(10, 10, 10, 10);

            try {
                if (rs.next()) {
                    String nombre = rs.getString("nom");
                    int edadPrimerVuelo = rs.getInt("edat_primer_vol");
                    int edad = rs.getInt("edat");
                    int misionesExitosas = rs.getInt("missions_ok");
                    int misionesFallidas = rs.getInt("missions_ko");
                    String direccion = rs.getString("adreça");
                    String rangoMilitar = rs.getString("rang_militar");

                    gbc.gridx = 0;
                    gbc.gridy = 0;
                    panel.add(new JLabel("Nombre:"), gbc);
                    gbc.gridx = 1;
                    panel.add(new JLabel(nombre), gbc);

                    gbc.gridx = 0;
                    gbc.gridy++;
                    panel.add(new JLabel("Edad primer vuelo:"), gbc);
                    gbc.gridx = 1;
                    panel.add(new JLabel(String.valueOf(edadPrimerVuelo)), gbc);

                    gbc.gridx = 0;
                    gbc.gridy++;
                    panel.add(new JLabel("Edad:"), gbc);
                    gbc.gridx = 1;
                    panel.add(new JLabel(String.valueOf(edad)), gbc);

                    gbc.gridx = 0;
                    gbc.gridy++;
                    panel.add(new JLabel("Misiones exitosas:"), gbc);
                    gbc.gridx = 1;
                    panel.add(new JLabel(String.valueOf(misionesExitosas)), gbc);

                    gbc.gridx = 0;
                    gbc.gridy++;
                    panel.add(new JLabel("Misiones fallidas:"), gbc);
                    gbc.gridx = 1;
                    panel.add(new JLabel(String.valueOf(misionesFallidas)), gbc);

                    gbc.gridx = 0;
                    gbc.gridy++;
                    panel.add(new JLabel("Dirección:"), gbc);
                    gbc.gridx = 1;
                    panel.add(new JLabel(direccion), gbc);

                    gbc.gridx = 0;
                    gbc.gridy++;
                    panel.add(new JLabel("Rango militar:"), gbc);
                    gbc.gridx = 1;
                    panel.add(new JLabel(rangoMilitar), gbc);

                } else {
                    JLabel noDataLabel = new JLabel("No se encontraron datos para este usuario.");
                    noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    panel.add(noDataLabel, gbc);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            add(panel);
            setVisible(true);
        }
    }


    private void mostrarPaginaCoordenadas() {
        JFrame coordenadasFrame = new JFrame("Página de Coordenadas");
        coordenadasFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        coordenadasFrame.setSize(800, 600);
        coordenadasFrame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(200, 200, 200));
        leftPanel.setPreferredSize(new Dimension(100, getHeight()));
        mainPanel.add(leftPanel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(new Color(200, 200, 200));
        rightPanel.setPreferredSize(new Dimension(100, getHeight()));
        mainPanel.add(rightPanel, BorderLayout.EAST);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 10;
        gbc.weighty = 10;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        JLabel coordenadasLabel = new JLabel("COORDENADAS");
        coordenadasLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.insets = new Insets(100, 195, 0, 0);
        centerPanel.add(coordenadasLabel, gbc);

        JButton mostrarDireccionButton = new JButton("Enviar coordenadas");
        mostrarDireccionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarDireccionAleatoria();
            }
        });

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(-420, 0, 0, 10);
        centerPanel.add(mostrarDireccionButton, gbc);

        try {
            ImageIcon astronautIconRight = new ImageIcon("C:\\Users\\pauca\\IdeaProjects\\PROYECTO_NASA__\\src\\main\\java\\com\\example\\nasa\\astronauta.png");
            int imageWidthRight = astronautIconRight.getIconWidth() / 2;
            int imageHeightRight = astronautIconRight.getIconHeight() / 2;
            astronautIconRight.setImage(astronautIconRight.getImage().getScaledInstance(imageWidthRight, imageHeightRight, Image.SCALE_DEFAULT));
            astronautLabel = new JLabel(astronautIconRight);
            astronautLabel.setBounds(60, 90, imageWidthRight, imageHeightRight);
            rightPanel.add(astronautLabel);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            ImageIcon astronautIconLeft = new ImageIcon("C:\\Users\\pauca\\IdeaProjects\\PROYECTO_NASA__\\src\\main\\java\\com\\example\\nasa\\astronauta.png");
            int imageWidthLeft = astronautIconLeft.getIconWidth() / 2;
            int imageHeightLeft = astronautIconLeft.getIconHeight() / 2;
            astronautIconLeft.setImage(astronautIconLeft.getImage().getScaledInstance(imageWidthLeft, imageHeightLeft, Image.SCALE_DEFAULT));
            JLabel astronautLabelLeft = new JLabel(astronautIconLeft);
            astronautLabelLeft.setBounds(60, 90, imageWidthLeft, imageHeightLeft);
            leftPanel.add(astronautLabelLeft);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JPanel topSpacePanel = new JPanel();
        topSpacePanel.setBackground(new Color(200, 200, 200));
        topSpacePanel.setPreferredSize(new Dimension(getWidth(), 50));
        mainPanel.add(topSpacePanel, BorderLayout.NORTH);

        JPanel bottomSpacePanel = new JPanel();
        bottomSpacePanel.setBackground(new Color(200, 200, 200));
        bottomSpacePanel.setPreferredSize(new Dimension(getWidth(), 50));
        mainPanel.add(bottomSpacePanel, BorderLayout.SOUTH);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        coordenadasFrame.add(mainPanel);
        coordenadasFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new pag_astronauta("Nombre de Usuario");
        });
    }
}
