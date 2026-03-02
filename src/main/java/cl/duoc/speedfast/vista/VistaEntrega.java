package cl.duoc.speedfast.vista;

import cl.duoc.speedfast.dao.EntregaDAO;
import cl.duoc.speedfast.dao.PedidoDAO;
import cl.duoc.speedfast.dao.RepartidorDAO;
import cl.duoc.speedfast.modelo.Entrega;
import cl.duoc.speedfast.modelo.Pedido;
import cl.duoc.speedfast.modelo.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class VistaEntrega extends JFrame {

    private JComboBox<Pedido> comboPedido;
    private JComboBox<Repartidor> comboRepartidor;
    private JTextField txtFecha;
    private JTextField txtHora;

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private EntregaDAO entregaDAO;
    private PedidoDAO pedidoDAO;
    private RepartidorDAO repartidorDAO;

    private int idSeleccionado = -1;

    public VistaEntrega() {

        entregaDAO = new EntregaDAO();
        pedidoDAO = new PedidoDAO();
        repartidorDAO = new RepartidorDAO();

        setTitle("Gestión de Entregas - SpeedFast");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        cargarCombos();
        cargarTabla();
    }

    private void initComponents() {

        setLayout(new BorderLayout());

        JPanel panelFormulario = new JPanel(new GridLayout(3, 4, 10, 10));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        comboPedido = new JComboBox<>();
        comboRepartidor = new JComboBox<>();
        txtFecha = new JTextField();
        txtHora = new JTextField();

        JButton btnRegistrar = new JButton("Registrar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");

        panelFormulario.add(new JLabel("Pedido:"));
        panelFormulario.add(comboPedido);

        panelFormulario.add(new JLabel("Repartidor:"));
        panelFormulario.add(comboRepartidor);

        panelFormulario.add(new JLabel("Fecha (YYYY-MM-DD):"));
        panelFormulario.add(txtFecha);

        panelFormulario.add(new JLabel("Hora (HH:MM:SS):"));
        panelFormulario.add(txtHora);

        panelFormulario.add(btnRegistrar);
        panelFormulario.add(btnActualizar);
        panelFormulario.add(new JLabel(""));
        panelFormulario.add(btnEliminar);

        add(panelFormulario, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(
                new Object[]{"ID", "Pedido", "Repartidor", "Fecha", "Hora"}, 0);

        tabla = new JTable(modeloTabla);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Eventos
        btnRegistrar.addActionListener(e -> registrarEntrega());
        btnActualizar.addActionListener(e -> actualizarEntrega());
        btnEliminar.addActionListener(e -> eliminarEntrega());

        tabla.getSelectionModel().addListSelectionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                idSeleccionado = (int) modeloTabla.getValueAt(fila, 0);
                txtFecha.setText(modeloTabla.getValueAt(fila, 3).toString());
                txtHora.setText(modeloTabla.getValueAt(fila, 4).toString());
            }
        });
    }

    private void registrarEntrega() {
        guardarEntrega(false);
    }

    private void actualizarEntrega() {

        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione una entrega",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        guardarEntrega(true);
    }

    private void guardarEntrega(boolean esActualizacion) {

        if (comboPedido.getSelectedItem() == null ||
                comboRepartidor.getSelectedItem() == null ||
                txtFecha.getText().isEmpty() ||
                txtHora.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Todos los campos son obligatorios",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {

            Pedido pedido = (Pedido) comboPedido.getSelectedItem();
            Repartidor repartidor = (Repartidor) comboRepartidor.getSelectedItem();

            LocalDate fecha = LocalDate.parse(txtFecha.getText());
            LocalTime hora = LocalTime.parse(txtHora.getText());

            Entrega entrega = new Entrega();
            entrega.setIdPedido(pedido.getId());
            entrega.setIdRepartidor(repartidor.getId());
            entrega.setFecha(fecha);
            entrega.setHora(hora);

            if (esActualizacion) {
                entrega.setId(idSeleccionado);
                entregaDAO.update(entrega);
                JOptionPane.showMessageDialog(this, "Entrega actualizada correctamente");
            } else {
                entregaDAO.create(entrega);
                JOptionPane.showMessageDialog(this, "Entrega registrada correctamente");
            }

            limpiarFormulario();
            cargarTabla();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Formato incorrecto.\nFecha: YYYY-MM-DD\nHora: HH:MM:SS",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarEntrega() {

        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione una entrega",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);

        entregaDAO.delete(id);
        JOptionPane.showMessageDialog(this, "Entrega eliminada");
        limpiarFormulario();
        cargarTabla();
    }

    private void cargarCombos() {

        comboPedido.removeAllItems();
        comboRepartidor.removeAllItems();

        for (Pedido p : pedidoDAO.readAll()) {
            comboPedido.addItem(p);
        }

        for (Repartidor r : repartidorDAO.readAll()) {
            comboRepartidor.addItem(r);
        }
    }

    private void cargarTabla() {

        modeloTabla.setRowCount(0);

        for (Entrega e : entregaDAO.readAll()) {
            modeloTabla.addRow(new Object[]{
                    e.getId(),
                    e.getIdPedido(),
                    e.getIdRepartidor(),
                    e.getFecha(),
                    e.getHora()
            });
        }
    }

    private void limpiarFormulario() {
        txtFecha.setText("");
        txtHora.setText("");
        idSeleccionado = -1;
        tabla.clearSelection();
    }
}