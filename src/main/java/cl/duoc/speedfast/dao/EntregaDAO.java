package cl.duoc.speedfast.dao;

import cl.duoc.speedfast.conexion.ConexionDB;
import cl.duoc.speedfast.modelo.Entrega;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EntregaDAO {

    /**
     * Inserta una nueva entrega.
     */
    public boolean create(Entrega entrega) {
        String sql = "INSERT INTO entregas (id_pedido, id_repartidor, fecha, hora) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, entrega.getIdPedido());
            stmt.setInt(2, entrega.getIdRepartidor());
            stmt.setDate(3, Date.valueOf(entrega.getFecha()));
            stmt.setTime(4, Time.valueOf(entrega.getHora()));

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al crear entrega: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lista todas las entregas.
     */
    public List<Entrega> readAll() {
        List<Entrega> lista = new ArrayList<>();
        String sql = "SELECT * FROM entregas";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Entrega e = new Entrega();
                e.setId(rs.getInt("id"));
                e.setIdPedido(rs.getInt("id_pedido"));
                e.setIdRepartidor(rs.getInt("id_repartidor"));
                e.setFecha(rs.getDate("fecha").toLocalDate());
                e.setHora(rs.getTime("hora").toLocalTime());

                lista.add(e);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar entregas: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Actualiza una entrega existente.
     */
    public boolean update(Entrega entrega) {
        String sql = "UPDATE entregas SET id_pedido=?, id_repartidor=?, fecha=?, hora=? WHERE id=?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, entrega.getIdPedido());
            stmt.setInt(2, entrega.getIdRepartidor());
            stmt.setDate(3, Date.valueOf(entrega.getFecha()));
            stmt.setTime(4, Time.valueOf(entrega.getHora()));
            stmt.setInt(5, entrega.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar entrega: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina una entrega por ID.
     */
    public boolean delete(int id) {
        String sql = "DELETE FROM entregas WHERE id=?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar entrega: " + e.getMessage());
            return false;
        }
    }
}