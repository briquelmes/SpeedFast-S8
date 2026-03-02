package cl.duoc.speedfast.vista;

import cl.duoc.speedfast.dao.RepartidorDAO;
import cl.duoc.speedfast.modelo.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Ventana para gestionar repartidores.
 */
public class VistaRepartidor extends JFrame {

    private JTextField txtNombre;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private RepartidorDAO repartidorDAO;
    private int idSeleccionado = -1;

    public VistaRepartidor() {
        repartidorDAO = new RepartidorDAO();

        setTitle("Gestión de Repartidores - SpeedFast");
        setSize(550, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        cargarTabla();
    }

    private void initComponents() {

        JPanel panel = new JPanel(new BorderLayout());
        // Panel formulario con flow layout para el orden
        JPanel panelFormulario = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        panelFormulario.add(new JLabel("Nombre:"));

        txtNombre = new JTextField(15);
        panelFormulario.add(txtNombre);

        JButton btnAgregar = new JButton("Agregar");
        panelFormulario.add(btnAgregar);

        JButton btnActualizar = new JButton("Actualizar");
        panelFormulario.add(btnActualizar);

        JButton btnEliminar = new JButton("Eliminar");
        panelFormulario.add(btnEliminar);

        panel.add(panelFormulario, BorderLayout.NORTH);

        // Tabla
        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Nombre"}, 0);
        tabla = new JTable(modeloTabla);

        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        add(panel);

        // Eventos botones
        btnAgregar.addActionListener(e -> agregarRepartidor());
        btnEliminar.addActionListener(e -> eliminarRepartidor());
        btnActualizar.addActionListener(e -> actualizarRepartidor());

        // Evento al seleccionar una fila
        tabla.getSelectionModel().addListSelectionListener(e -> {
            int fila = tabla.getSelectedRow();

            if (fila >= 0) {
                idSeleccionado = (int) modeloTabla.getValueAt(fila, 0);
                String nombre = (String) modeloTabla.getValueAt(fila, 1);
                txtNombre.setText(nombre);
            }
        });
    }

    private void agregarRepartidor() {
        String nombre = txtNombre.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El nombre es obligatorio",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Repartidor r = new Repartidor(nombre);

        if (repartidorDAO.create(r)) {
            JOptionPane.showMessageDialog(this,
                    "Repartidor agregado correctamente");
            txtNombre.setText("");
            cargarTabla();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Error al agregar repartidor",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarRepartidor() {

        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un repartidor",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nombre = txtNombre.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El nombre es obligatorio",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Repartidor r = new Repartidor();
        r.setId(idSeleccionado);
        r.setNombre(nombre);

        if (repartidorDAO.update(r)) {
            JOptionPane.showMessageDialog(this,
                    "Repartidor actualizado correctamente");
            cargarTabla();
            txtNombre.setText("");
            idSeleccionado = -1;
            tabla.clearSelection();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Error al actualizar",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarRepartidor() {

        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un repartidor",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);

        if (repartidorDAO.delete(id)) {
            JOptionPane.showMessageDialog(this,
                    "Repartidor eliminado");
            cargarTabla();
            txtNombre.setText("");
            idSeleccionado = -1;
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo eliminar",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarTabla() {

        modeloTabla.setRowCount(0);

        List<Repartidor> lista = repartidorDAO.readAll();

        for (Repartidor r : lista) {
            modeloTabla.addRow(new Object[]{
                    r.getId(),
                    r.getNombre()
            });
        }
    }
}