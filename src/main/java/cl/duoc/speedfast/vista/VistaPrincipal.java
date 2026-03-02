package cl.duoc.speedfast.vista;

import javax.swing.*;
import java.awt.*;

public class VistaPrincipal extends JFrame {

    public VistaPrincipal() {

        setTitle("SpeedFast - Sistema de Gestión");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 10, 10));

        JButton btnRepartidores = new JButton("Gestión de Repartidores");
        JButton btnPedidos = new JButton("Gestión de Pedidos");
        JButton btnEntregas = new JButton("Gestión de Entregas");

        panel.add(btnRepartidores);
        panel.add(btnPedidos);
        panel.add(btnEntregas);

        add(panel);

        btnRepartidores.addActionListener(e ->
                new VistaRepartidor().setVisible(true));

        btnPedidos.addActionListener(e ->
                new VistaPedido().setVisible(true));

        btnEntregas.addActionListener(e ->
                new VistaEntrega().setVisible(true));
    }
}