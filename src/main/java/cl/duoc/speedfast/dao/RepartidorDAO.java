package cl.duoc.speedfast.dao;

import cl.duoc.speedfast.conexion.ConexionDB;
import cl.duoc.speedfast.modelo.Repartidor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepartidorDAO {

    /**
     * Inserta un nuevo repartidor en la base de datos.
     */
    public boolean create(Repartidor repartidor) {
        String sql = "INSERT INTO repartidores (nombre) VALUES (?)";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, repartidor.getNombre());
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al insertar repartidor: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene todos los repartidores registrados.
     */
    public List<Repartidor> readAll() {
        List<Repartidor> lista = new ArrayList<>();
        String sql = "SELECT * FROM repartidores";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Repartidor r = new Repartidor(
                        rs.getInt("id"),
                        rs.getString("nombre")
                );
                lista.add(r);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar repartidores: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Actualiza un repartidor existente.
     */
    public boolean update(Repartidor repartidor) {
        String sql = "UPDATE repartidores SET nombre=? WHERE id=?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, repartidor.getNombre());
            stmt.setInt(2, repartidor.getId());

            int filas = stmt.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar repartidor: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina un repartidor por su ID.
     */
    public boolean delete(int id) {
        String sql = "DELETE FROM repartidores WHERE id=?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int filas = stmt.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar repartidor: " + e.getMessage());
            return false;
        }
    }
}