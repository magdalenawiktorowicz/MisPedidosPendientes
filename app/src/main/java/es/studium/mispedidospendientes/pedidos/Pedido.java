package es.studium.mispedidospendientes.pedidos;

import android.util.Log;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import es.studium.mispedidospendientes.BDConexion;
import es.studium.mispedidospendientes.tiendas.Tienda;

public class Pedido {
    private int idPedido;
    private final LocalDate fechaPedido;
    private final LocalDate fechaEstimadaPedido;
    private final String descripcionPedido;
    private final double importePedido;
    private final int estadoPedido;
    private int idTiendaFK;

    public Pedido(int idPedido, LocalDate fechaPedido, LocalDate fechaEstimadaPedido, String descripcionPedido, double importePedido, int estadoPedido, int idTiendaFK) {
        this.idPedido = idPedido;
        this.fechaPedido = fechaPedido;
        this.fechaEstimadaPedido = fechaEstimadaPedido;
        this.descripcionPedido = descripcionPedido;
        this.importePedido = importePedido;
        this.estadoPedido = estadoPedido;
        this.idTiendaFK = idTiendaFK;
    }

    public Pedido(LocalDate fechaPedido, LocalDate fechaEstimadaPedido, String descripcionPedido, double importePedido, int estadoPedido, int idTiendaFK) {
        this.fechaPedido = fechaPedido;
        this.fechaEstimadaPedido = fechaEstimadaPedido;
        this.descripcionPedido = descripcionPedido;
        this.importePedido = importePedido;
        this.estadoPedido = estadoPedido;
        this.idTiendaFK = idTiendaFK;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public LocalDate getFechaPedido() {
        return fechaPedido;
    }

    public LocalDate getFechaEstimadaPedido() {
        return fechaEstimadaPedido;
    }

    public String getDescripcionPedido() {
        return descripcionPedido;
    }

    public double getImportePedido() {
        return importePedido;
    }

    public int getEstadoPedido() {
        return estadoPedido;
    }

    public int getIdTiendaFK() {
        return idTiendaFK;
    }

    @Override
    public String toString() {

        return (BDConexion.consultarTienda(getIdTiendaFK())).getNombreTienda() + " (" + getFechaPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ") " + getDescripcionPedido();
    }
}
