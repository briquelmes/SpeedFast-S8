package cl.duoc.speedfast.dao;

import cl.duoc.speedfast.conexion.ConexionDB;
import cl.duoc.speedfast.modelo.Pedido;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class PedidoDAO {

    /**
     * Inserta un nuevo pedido en la base de datos.
     */
    public boolean create(Pedido pedido) {
        String sql = "INSERT INTO pedidos (direccion, tipo, estado) VALUES (?, ?, ?)";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pedido.getDireccion());
            stmt.setString(2, pedido.getTipo());
            stmt.setString(3, pedido.getEstado());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al crear pedido: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lista todos los pedidos.
     */
    public List<Pedido> readAll() {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedidos";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Pedido p = new Pedido(
                        rs.getInt("id"),
                        rs.getString("direccion"),
                        rs.getString("tipo"),
                        rs.getString("estado")
                );
                lista.add(p);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar pedidos: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Lista pedidos con filtros opcionales por tipo o estado.
     */
    public List<Pedido> readByFilters(String tipo, String estado) {
        List<Pedido> lista = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT * FROM pedidos WHERE 1=1");

        if (tipo != null && !tipo.isEmpty()) {
            sql.append(" AND tipo = ?");
        }
        if (estado != null && !estado.isEmpty()) {
            sql.append(" AND estado = ?");
        }

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int index = 1;

            if (tipo != null && !tipo.isEmpty()) {
                stmt.setString(index++, tipo);
            }
            if (estado != null && !estado.isEmpty()) {
                stmt.setString(index++, estado);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Pedido p = new Pedido(
                        rs.getInt("id"),
                        rs.getString("direccion"),
                        rs.getString("tipo"),
                        rs.getString("estado")
                );
                lista.add(p);
            }

        } catch (SQLException e) {
            System.out.println("Error al filtrar pedidos: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Actualiza un pedido existente.
     */
    public boolean update(Pedido pedido) {
        String sql = "UPDATE pedidos SET direccion=?, tipo=?, estado=? WHERE id=?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pedido.getDireccion());
            stmt.setString(2, pedido.getTipo());
            stmt.setString(3, pedido.getEstado());
            stmt.setInt(4, pedido.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar pedido: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina un pedido por ID.
     */
    public boolean delete(int id) {
        String sql = "DELETE FROM pedidos WHERE id=?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar pedido: " + e.getMessage());
            return false;
        }
    }
}