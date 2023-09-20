package com.example.nasa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class afegir_usuaris {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/nasa";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "admin123";

    public static void main(String[] args) {
        // Datos del usuario a insertar
        String nombreUsuario = "Dani";
        String contraseña = "admin123";
        int professionID = 1;  // Puedes cambiar este valor según tu necesidad

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            // Insertar usuario en la tabla Usuario
            String insertUsuarioQuery = "INSERT INTO Usuario (nombreUsuario, contraseña, profession_ID) VALUES (?, ?, ?)";
            PreparedStatement stmtUsuario = conn.prepareStatement(insertUsuarioQuery);
            stmtUsuario.setString(1, nombreUsuario);
            stmtUsuario.setString(2, contraseña);
            stmtUsuario.setInt(3, professionID);
            stmtUsuario.executeUpdate();

            // Insertar en la tabla correspondiente según profession_ID
            String insertQuery = getInsertQueryForProfession(professionID);
            if (insertQuery != null) {
                PreparedStatement stmtProfesion = conn.prepareStatement(insertQuery);

                // Generar valores aleatorios para campos específicos
                Random random = new Random();
                int edat_primer_vol = random.nextInt(100);
                int missions_ok = random.nextInt(100);
                int missions_ko = random.nextInt(100);
                String adreça = "Dirección usuario";
                String rang_militar = "Rango " + random.nextInt(100);

                // Agregar el bloque específico para cada tipo de usuario
                switch (professionID) {
                    case 1:  // Astronauta
                        stmtProfesion.setInt(1, generateUniqueID());
                        stmtProfesion.setInt(2, edat_primer_vol);
                        stmtProfesion.setString(3, nombreUsuario);
                        stmtProfesion.setInt(4, random.nextInt(100));
                        stmtProfesion.setInt(5, missions_ok);
                        stmtProfesion.setInt(6, missions_ko);
                        stmtProfesion.setString(7, adreça);
                        stmtProfesion.setString(8, rang_militar);
                        stmtProfesion.setInt(9, professionID);
                        break;
                    case 2:  // Físico
                        // Agregar el código para Físico
                        break;
                    case 3:  // Mecánico
                        // Agregar el código para Mecánico
                        break;
                    case 4:  // Espía
                        // Agregar el código para Espía
                        break;
                    default:
                        break;
                }

                stmtProfesion.executeUpdate();

                System.out.println("Usuario insertado correctamente en la tabla correspondiente.");
            } else {
                System.out.println("No se pudo determinar la tabla correspondiente para profession_ID " + professionID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String getInsertQueryForProfession(int professionID) {
        switch (professionID) {
            case 1:
                return "INSERT INTO Astronauta (ID, edat_primer_vol, nom, edat, missions_ok, missions_ko, adreça, rang_militar, profession_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            case 2:
                // Agrega el INSERT para Físico
                return null;
            case 3:
                // Agrega el INSERT para Mecánico
                return null;
            case 4:
                // Agrega el INSERT para Espía
                return null;
            default:
                return null;
        }
    }

    private static int generateUniqueID() {
        // Lógica para generar un ID único, puedes adaptarla según tus necesidades
        // En este ejemplo, se genera un ID aleatorio entre 1 y 1000
        Random random = new Random();
        return random.nextInt(1000) + 1;
    }
}
