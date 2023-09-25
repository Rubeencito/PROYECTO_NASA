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
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Crear un panel para la izquierda con fondo gris claro
        JPanel leftPanel = new JPanel();
        mainPanel = new JPanel(new BorderLayout());
        this.mainPanel = mainPanel;
        leftPanel.setBackground(new Color(200, 200, 200));
        leftPanel.setPreferredSize(new Dimension(100, getHeight()));
        mainPanel.add(leftPanel, BorderLayout.WEST);

        // Crear un panel para la derecha con fondo gris claro
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(new Color(200, 200, 200));
        rightPanel.setPreferredSize(new Dimension(100, getHeight()));
        mainPanel.add(rightPanel, BorderLayout.EAST);

        JLabel noDataLabel = new JLabel("No se encontraron datos para este usuario.");
        noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel noDataPanel = new JPanel(new BorderLayout());
        noDataPanel.add(noDataLabel, BorderLayout.CENTER);


        centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 10;
        gbc.weighty = 10;
        gbc.anchor = GridBagConstraints.NORTHWEST;  // Alineación en la esquina superior izquierda


        // Panel para el espacio arriba del texto y el botón
        JPanel topSpacePanel = new JPanel();
        topSpacePanel.setBackground(new Color(200, 200, 200));
        topSpacePanel.setPreferredSize(new Dimension(getWidth(), 50));
        mainPanel.add(topSpacePanel, BorderLayout.NORTH);

        // Panel para el espacio debajo del texto y el botón
        JPanel bottomSpacePanel = new JPanel();
        bottomSpacePanel.setBackground(new Color(200, 200, 200));
        bottomSpacePanel.setPreferredSize(new Dimension(getWidth(), 50));
        mainPanel.add(bottomSpacePanel, BorderLayout.SOUTH);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Crear el menú de navegación
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menú");

        JMenuItem fichaTecnicaItem = new JMenuItem("Ficha Técnica");
        fichaTecnicaItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Acción para abrir la página de Ficha Técnica
                abrirFichaTecnica(nombreUsuario);
            }
        });

        JMenuItem coordenadasItem = new JMenuItem("Coordenadas");
        coordenadasItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Acción para abrir la página de Coordenadas
                mostrarPaginaCoordenadas();
            }
        });

        menu.add(fichaTecnicaItem);
        menu.add(coordenadasItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        add(mainPanel);
        setVisible(true);
    }



    private void mostrarDireccionAleatoria() {
        Random random = new Random();
        int latitud = random.nextInt(90);
        int longitud = random.nextInt(180);

        JOptionPane.showMessageDialog(this, "Coordenadas: Latitud " + latitud + ", Longitud " + longitud);
    }

    private void abrirFichaTecnica(String nombreUsuario) {
        try {
            // Conectar a la base de datos
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/NASA", "root", "Admin123");
            Statement stmt = conn.createStatement();

            // Obtener los datos del astronauta a partir del nombre de usuario
            String query = "SELECT * FROM Astronauta INNER JOIN Usuario ON Astronauta.ID = Usuario.ID WHERE nombreUsuario = '" + nombreUsuario + "'";
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                // Crear un panel para mostrar los datos en el centro
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

                // Limpiar el panel central y agregar los datos
                mainPanel.remove(centerPanel);
                centerPanel = dataPanel;
                mainPanel.add(centerPanel, BorderLayout.CENTER);
            } else {
                // Si no se encuentra el astronauta
                JLabel noDataLabel = new JLabel("No se encontraron datos para este usuario.");
                noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);

                // Limpiar el panel central y agregar el mensaje de no datos
                mainPanel.remove(centerPanel);
                centerPanel = noDataPanel;
                mainPanel.add(centerPanel, BorderLayout.CENTER);
            }

            // Cerrar la conexión y la consulta
            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Actualizar la interfaz
        this.mainPanel.revalidate();
        this.mainPanel.repaint();
    }

    private void mostrarPaginaCoordenadas() {
        JFrame coordenadasFrame = new JFrame("Página de Coordenadas");
        coordenadasFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        coordenadasFrame.setSize(800, 600);
        coordenadasFrame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Crear un panel para la izquierda con fondo gris claro
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(200, 200, 200));
        leftPanel.setPreferredSize(new Dimension(100, getHeight()));
        mainPanel.add(leftPanel, BorderLayout.WEST);

        // Crear un panel para la derecha con fondo gris claro
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
        gbc.anchor = GridBagConstraints.NORTHWEST;  // Alineación en la esquina superior izquierda

        // Agregar texto "COORDENADAS" con formato
        JLabel coordenadasLabel = new JLabel("COORDENADAS");
        coordenadasLabel.setFont(new Font("Arial", Font.BOLD, 24));

        // Agregar espacio entre el texto y el botón
        gbc.insets = new Insets(10, 0, 0, 0);
        centerPanel.add(coordenadasLabel, gbc);

        JButton mostrarDireccionButton = new JButton("Enviar coordenadas");
        mostrarDireccionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarDireccionAleatoria();
            }
        });

        // Cambia la posición del botón y la alineación
        gbc.gridx = 2;  // Nueva posición
        gbc.anchor = GridBagConstraints.NORTHWEST;  // Alineación en la esquina superior izquierda
        gbc.insets = new Insets(10, 30, 0, 0);  // Cambia los márgenes
        centerPanel.add(mostrarDireccionButton, gbc);

        // Agregar imagen a la derecha del astronauta
        try {
            ImageIcon astronautIconRight = new ImageIcon("C:\\Users\\pauca\\IdeaProjects\\PROYECTO_NASA__\\src\\main\\java\\com\\example\\nasa\\astronauta.png");
            int imageWidthRight = astronautIconRight.getIconWidth() / 2;  // Cambiar el divisor para aumentar el tamaño
            int imageHeightRight = astronautIconRight.getIconHeight() / 2; // Cambiar el divisor para aumentar el tamaño
            astronautIconRight.setImage(astronautIconRight.getImage().getScaledInstance(imageWidthRight, imageHeightRight, Image.SCALE_DEFAULT)); // Escalar la imagen
            astronautLabel = new JLabel(astronautIconRight);
            astronautLabel.setBounds(60, 90, imageWidthRight, imageHeightRight);
            rightPanel.add(astronautLabel);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Agregar imagen a la izquierda del astronauta
        try {
            ImageIcon astronautIconLeft = new ImageIcon("C:\\Users\\pauca\\IdeaProjects\\PROYECTO_NASA__\\src\\main\\java\\com\\example\\nasa\\astronauta.png");
            int imageWidthLeft = astronautIconLeft.getIconWidth() / 2;  // Cambiar el divisor para aumentar el tamaño
            int imageHeightLeft = astronautIconLeft.getIconHeight() / 2; // Cambiar el divisor para aumentar el tamaño
            astronautIconLeft.setImage(astronautIconLeft.getImage().getScaledInstance(imageWidthLeft, imageHeightLeft, Image.SCALE_DEFAULT)); // Escalar la imagen
            JLabel astronautLabelLeft = new JLabel(astronautIconLeft);
            astronautLabelLeft.setBounds(60, 90, imageWidthLeft, imageHeightLeft);
            leftPanel.add(astronautLabelLeft);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Panel para el espacio arriba del texto y el botón
        JPanel topSpacePanel = new JPanel();
        topSpacePanel.setBackground(new Color(200, 200, 200));
        topSpacePanel.setPreferredSize(new Dimension(getWidth(), 50));
        mainPanel.add(topSpacePanel, BorderLayout.NORTH);

        // Panel para el espacio debajo del texto y el botón
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