package es.studium.mispedidospendientes.tiendas;

public class Tienda {
    private final int idTienda;
    private final String nombreTienda;

    public Tienda(int idTienda, String nombreTienda) {
        this.idTienda = idTienda;
        this.nombreTienda = nombreTienda;
    }

    public int getIdTienda() {
        return idTienda;
    }

    public String getNombreTienda() {
        return nombreTienda;
    }

    @Override
    public String toString() {
        return getNombreTienda();
    }
}
