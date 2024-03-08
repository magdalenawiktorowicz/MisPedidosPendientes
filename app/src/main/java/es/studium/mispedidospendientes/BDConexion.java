package es.studium.mispedidospendientes;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import es.studium.mispedidospendientes.pedidos.Pedido;
import es.studium.mispedidospendientes.tiendas.Tienda;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BDConexion {
    // Pedidos - Consulta
    public static ArrayList<Pedido> consultarPedidos() {
        ArrayList<Pedido> pedidos = new ArrayList<>();
        JSONArray result;
        int idPedido;
        LocalDate fechaPedido;
        LocalDate fechaEstimadaPedido;
        String descripcionPedido;
        double importePedido;
        int estadoPedido;
        int idTiendaFK;

        // Crear una instancia de OkHttpClient
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://192.168.1.131/ApiPedidosPendientes/pedidos.php")
                .build();
        try {
            // Realizar la solicitud
            Response response = client.newCall(request).execute();
            // Procesar la respuesta
            if (response.isSuccessful()) {
                result = new JSONArray(response.body().string());
                for (int i = 0; i < result.length(); i++) {
                    JSONObject jsonObject = result.getJSONObject(i);
                    idPedido = jsonObject.getInt("idPedido");
                    String[] fp = jsonObject.getString("fechaPedido").split("-");
                    fechaPedido = LocalDate.of(Integer.parseInt(fp[0]), Integer.parseInt(fp[1]), Integer.parseInt(fp[2]));
                    String[] fe = jsonObject.getString("fechaEstimadaPedido").split("-");
                    fechaEstimadaPedido = LocalDate.of(Integer.parseInt(fe[0]), Integer.parseInt(fe[1]), Integer.parseInt(fe[2]));
                    descripcionPedido = jsonObject.getString("descripcionPedido");
                    importePedido = jsonObject.getDouble("importePedido");
                    estadoPedido = jsonObject.getInt("estadoPedido");
                    idTiendaFK = jsonObject.getInt("idTiendaFK");

                    pedidos.add(new Pedido(idPedido, fechaPedido, fechaEstimadaPedido, descripcionPedido, importePedido, estadoPedido, idTiendaFK));
                }
                pedidos.sort(Comparator.comparing(Pedido::getFechaPedido));
            } else {
                Log.e("MainActivity", response.message());
            }
        } catch (IOException e) {
            Log.e("MainActivity", e.getMessage());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return pedidos;
    }

    // Pedidos - Consulta de un pedido por su id
    public static Pedido consultarPedido(int idPedido) {
        Pedido pedido = null;
        JSONArray jsonArray;
        JSONObject jsonobject;
        // Crear una instancia de OkHttpClient
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://192.168.1.131/ApiPedidosPendientes/pedidos.php?idPedido=" + idPedido)
                .build();
        try {
            // Realizar la solicitud
            Response response = client.newCall(request).execute();
            // Procesar la respuesta
            if (response.isSuccessful()) {
                jsonArray = new JSONArray(response.body().string());
                jsonobject = jsonArray.getJSONObject(0);
                LocalDate fechaPedido = LocalDate.parse(jsonobject.getString("fechaPedido"));
                LocalDate fechaEntrega = LocalDate.parse(jsonobject.getString("fechaEstimadaPedido"));
                String descripcion = jsonobject.getString("descripcionPedido");
                double importe = jsonobject.getDouble("importePedido");
                int estado = jsonobject.getInt("estadoPedido");
                int idTiendaFK = jsonobject.getInt("idTiendaFK");
                pedido = new Pedido(idPedido, fechaPedido, fechaEntrega, descripcion, importe, estado, idTiendaFK);
            } else {
                Log.e("MainActivity", response.message());
            }
        } catch (IOException e) {
            Log.e("MainActivity", e.getMessage());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage());
        }
        return pedido;
    }

    // Tiendas - Consulta
    public static ArrayList<Tienda> consultarTiendas() {
        ArrayList<Tienda> tiendas = new ArrayList<>();
        JSONArray result;
        int idTienda;
        String nombreTienda;

        // Crear una instancia de OkHttpClient
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://192.168.1.131/ApiPedidosPendientes/tiendas.php")
                .build();
        try {
            // Realizar la solicitud
            Response response = client.newCall(request).execute();
            // Procesar la respuesta
            if (response.isSuccessful()) {
                result = new JSONArray(response.body().string());
                for (int i = 0; i < result.length(); i++) {
                    JSONObject jsonObject = result.getJSONObject(i);
                    idTienda = jsonObject.getInt("idTienda");
                    nombreTienda = jsonObject.getString("nombreTienda");

                    tiendas.add(new Tienda(idTienda, nombreTienda));
                }
                tiendas.sort(
                        Comparator.comparing((Tienda tienda) -> tienda.getNombreTienda().toLowerCase()));
            } else {
                Log.e("MainActivity", response.message());
            }
        } catch (IOException e) {
            Log.e("MainActivity", e.getMessage());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return tiendas;
    }

    // Tiendas - Consulta de una tienda por su id
    public static Tienda consultarTienda(int idTienda) {
        Tienda tienda = null;
        JSONObject jsonobject;
        // Crear una instancia de OkHttpClient
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://192.168.1.131/ApiPedidosPendientes/tiendas.php?idTienda=" + idTienda)
                .build();
        try {
            // Realizar la solicitud
            Response response = client.newCall(request).execute();
            // Procesar la respuesta
            if (response.isSuccessful()) {
                jsonobject = new JSONObject(response.body().string());
                tienda = new Tienda(jsonobject.getInt("idTienda"), jsonobject.getString("nombreTienda"));
            } else {
                Log.e("MainActivity", response.message());
            }
        } catch (IOException e) {
            Log.e("MainActivity", e.getMessage());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return tienda;
    }

    // Tienda - Alta
    public static int altaTienda(String nombre) {
        int resultado = 0;
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("nombreTienda", nombre)
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.1.131/ApiPedidosPendientes/tiendas.php")
                .post(formBody)
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            resultado = response.code();
            Log.i("MainActivity", String.valueOf(response));
        } catch (IOException e) {
            Log.e("MainActivity", e.getMessage());
        }
        return resultado;
    }

    // Pedido - Alta
    public static int altaPedido(Pedido pedido) {
        int resultado = 0;
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("fechaPedido", pedido.getFechaPedido().toString())
                .add("fechaEstimadaPedido", pedido.getFechaEstimadaPedido().toString())
                .add("descripcionPedido", pedido.getDescripcionPedido())
                .add("importePedido", String.valueOf(pedido.getImportePedido()))
                .add("estadoPedido", "0")
                .add("idTiendaFK", String.valueOf(pedido.getIdTiendaFK()))
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.1.131/ApiPedidosPendientes/pedidos.php")
                .post(formBody)
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            resultado = response.code();
            Log.i("MainActivity", String.valueOf(response));
        } catch (IOException e) {
            Log.e("MainActivity", e.getMessage());
        }
        return resultado;
    }

    // Tienda - modificación
    public static int modificacionTienda(String nombreNuevo, int idTienda) {
        int resultado = 0;
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder queryUrlBuilder = HttpUrl.get(URI.create("http://192.168.1.131/ApiPedidosPendientes/tiendas.php")).newBuilder();
        queryUrlBuilder.addQueryParameter("idTienda", String.valueOf(idTienda));
        queryUrlBuilder.addQueryParameter("nombreTienda", nombreNuevo);

        // Las peticiones PUT requieren BODY, aunque sea vacío
        RequestBody formBody = new FormBody.Builder()
                .build();
        Request request = new Request.Builder()
                .url(queryUrlBuilder.build())
                .put(formBody)
                .build();
        Call call = client.newCall(request);
        try
        {
            Response response = call.execute();
            resultado = response.code();
            Log.i("MainActivity", String.valueOf(response));
        }
        catch (IOException e)
        {
            Log.e("MainActivity", e.getMessage());
        }
        return resultado;
    }

    // Pedido - modificación
    public static int modificacionPedido(Pedido pedidoEditado) {
        int resultado = 0;
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder queryUrlBuilder = HttpUrl.get(URI.create("http://192.168.1.131/ApiPedidosPendientes/pedidos.php")).newBuilder();
        queryUrlBuilder.addQueryParameter("idPedido", String.valueOf(pedidoEditado.getIdPedido()));
        queryUrlBuilder.addQueryParameter("fechaPedido", pedidoEditado.getFechaPedido().toString());
        queryUrlBuilder.addQueryParameter("fechaEstimadaPedido", pedidoEditado.getFechaEstimadaPedido().toString());
        queryUrlBuilder.addQueryParameter("descripcionPedido",pedidoEditado.getDescripcionPedido());
        queryUrlBuilder.addQueryParameter("importePedido", String.valueOf(pedidoEditado.getImportePedido()));
        queryUrlBuilder.addQueryParameter("estadoPedido", String.valueOf(pedidoEditado.getEstadoPedido()));
        queryUrlBuilder.addQueryParameter("idTiendaFK", String.valueOf(pedidoEditado.getIdTiendaFK()));

        // Las peticiones PUT requieren BODY, aunque sea vacío
        RequestBody formBody = new FormBody.Builder()
                .build();
        Request request = new Request.Builder()
                .url(queryUrlBuilder.build())
                .put(formBody)
                .build();
        Call call = client.newCall(request);
        try
        {
            Response response = call.execute();
            resultado = response.code();
            Log.i("MainActivity", String.valueOf(response));
        }
        catch (IOException e)
        {
            Log.e("MainActivity", e.getMessage());
        }
        return resultado;
    }

    // Tienda - baja
    public static int borradoTienda(int idTienda) {
        int resultado = 0;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://192.168.56.1/ApiPedidosPendientes/tiendas.php?idTienda=" + idTienda)
                .delete()
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            resultado = response.code();
            Log.i("MainActivity", String.valueOf(response));
        } catch (IOException e) {
            Log.e("MainActivity", e.getMessage());
        }
        return resultado;
    }

    // Pedido - baja
    public static int borradoPedido(int idPedido) {
        int resultado = 0;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://192.168.56.1/ApiPedidosPendientes/pedidos.php?idPedido=" + idPedido)
                .delete()
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            resultado = response.code();
            Log.i("MainActivity", String.valueOf(response));
        } catch (IOException e) {
            Log.e("MainActivity", e.getMessage());
        }
        return resultado;
    }
}
