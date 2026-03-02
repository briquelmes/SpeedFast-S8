package cl.duoc.speedfast.vista;

import cl.duoc.speedfast.dao.PedidoDAO;
import cl.duoc.speedfast.modelo.Pedido;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VistaPedido extends JFrame {

    private JTextField txtDireccion;
    private JComboBox<String> comboTipo;
    private JComboBox<String> comboEstado;

    private JComboBox<String> comboFiltroTipo;
    private JComboBox<String> comboFiltroEstado;

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private PedidoDAO pedidoDAO;
    private int idSeleccionado = -1;

    public VistaPedido() {

        pedidoDAO = new PedidoDAO();

        setTitle("Gestión de Pedidos - SpeedFast");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        cargarTabla(null, null);
    }

    private void initComponents() {

        JPanel panel = new JPanel(new BorderLayout());

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelCampos = new JPanel(new GridLayout(2, 4, 10, 10));

        txtDireccion = new JTextField(15);

        comboTipo = new JComboBox<>(new String[]{
                "COMIDA", "ENCOMIENDA", "EXPRESS"
        });

        comboEstado = new JComboBox<>(new String[]{
                "PENDIENTE", "EN_REPARTO", "ENTREGADO"
        });

        panelCampos.add(new JLabel("Dirección:"));
        panelCampos.add(txtDireccion);
        panelCampos.add(new JLabel("Tipo:"));
        panelCampos.add(comboTipo);

        panelCampos.add(new JLabel("Estado:"));
        panelCampos.add(comboEstado);
        panelCampos.add(new JLabel(""));
        panelCampos.add(new JLabel(""));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));

        JButton btnAgregar = new JButton("Agregar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");

        panelBotones.add(btnAgregar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);

        panelSuperior.add(panelCampos, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        panel.add(panelSuperior, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(
                new Object[]{"ID", "Dirección", "Tipo", "Estado"}, 0);

        tabla = new JTable(modeloTabla);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel panelFiltros = new JPanel(new FlowLayout());

        comboFiltroTipo = new JComboBox<>(new String[]{
                "TODOS", "COMIDA", "ENCOMIENDA", "EXPRESS"
        });

        comboFiltroEstado = new JComboBox<>(new String[]{
                "TODOS", "PENDIENTE", "EN_REPARTO", "ENTREGADO"
        });

        JButton btnFiltrar = new JButton("Filtrar");
        JButton btnLimpiarFiltro = new JButton("Limpiar Filtros");

        panelFiltros.add(new JLabel("Filtrar Tipo:"));
        panelFiltros.add(comboFiltroTipo);
        panelFiltros.add(new JLabel("Filtrar Estado:"));
        panelFiltros.add(comboFiltroEstado);
        panelFiltros.add(btnFiltrar);
        panelFiltros.add(btnLimpiarFiltro);

        panel.add(panelFiltros, BorderLayout.SOUTH);

        add(panel);

        btnAgregar.addActionListener(e -> agregarPedido());
        btnActualizar.addActionListener(e -> actualizarPedido());
        btnEliminar.addActionListener(e -> eliminarPedido());
        btnFiltrar.addActionListener(e -> aplicarFiltro());
        btnLimpiarFiltro.addActionListener(e -> limpiarFiltros());

        tabla.getSelectionModel().addListSelectionListener(e -> {
            int fila = tabla.getSelectedRow();

            if (fila >= 0) {
                idSeleccionado = (int) modeloTabla.getValueAt(fila, 0);
                txtDireccion.setText((String) modeloTabla.getValueAt(fila, 1));
                comboTipo.setSelectedItem(modeloTabla.getValueAt(fila, 2));
                comboEstado.setSelectedItem(modeloTabla.getValueAt(fila, 3));
            }
        });
    }

    private void agregarPedido() {

        String direccion = txtDireccion.getText().trim();

        if (direccion.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "La dirección es obligatoria",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Pedido p = new Pedido(
                direccion,
                comboTipo.getSelectedItem().toString(),
                comboEstado.getSelectedItem().toString()
        );

        if (pedidoDAO.create(p)) {
            JOptionPane.showMessageDialog(this,
                    "Pedido agregado correctamente");
            limpiarFormulario();
            cargarTabla(null, null);
        }
    }

    private void actualizarPedido() {

        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un pedido",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String direccion = txtDireccion.getText().trim();

        if (direccion.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "La dirección es obligatoria",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Pedido p = new Pedido();
        p.setId(idSeleccionado);
        p.setDireccion(direccion);
        p.setTipo(comboTipo.getSelectedItem().toString());
        p.setEstado(comboEstado.getSelectedItem().toString());

        if (pedidoDAO.update(p)) {
            JOptionPane.showMessageDialog(this,
                    "Pedido actualizado correctamente");
            limpiarFormulario();
            cargarTabla(null, null);
        }
    }

    private void eliminarPedido() {

        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un pedido",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);

        if (pedidoDAO.delete(id)) {
            JOptionPane.showMessageDialog(this,
                    "Pedido eliminado");
            limpiarFormulario();
            cargarTabla(null, null);
        }
    }

    private void aplicarFiltro() {

        String tipo = comboFiltroTipo.getSelectedItem().toString();
        String estado = comboFiltroEstado.getSelectedItem().toString();

        if (tipo.equals("TODOS")) tipo = null;
        if (estado.equals("TODOS")) estado = null;

        cargarTabla(tipo, estado);
    }

    private void limpiarFiltros() {
        comboFiltroTipo.setSelectedIndex(0);
        comboFiltroEstado.setSelectedIndex(0);
        cargarTabla(null, null);
    }

    private void cargarTabla(String tipo, String estado) {

        modeloTabla.setRowCount(0);

        List<Pedido> lista;

        if (tipo == null && estado == null) {
            lista = pedidoDAO.readAll();
        } else {
            lista = pedidoDAO.readByFilters(tipo, estado);
        }

        for (Pedido p : lista) {
            modeloTabla.addRow(new Object[]{
                    p.getId(),
                    p.getDireccion(),
                    p.getTipo(),
                    p.getEstado()
            });
        }
    }

    private void limpiarFormulario() {
        txtDireccion.setText("");
        comboTipo.setSelectedIndex(0);
        comboEstado.setSelectedIndex(0);
        idSeleccionado = -1;
        tabla.clearSelection();
    }
}