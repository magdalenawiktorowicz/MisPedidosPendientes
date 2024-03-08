package es.studium.mispedidospendientes.pedidos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import es.studium.mispedidospendientes.BDConexion;
import es.studium.mispedidospendientes.R;
import es.studium.mispedidospendientes.tiendas.Tienda;

public class NuevoPedido extends DialogFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Spinner spinnerTiendas;
    private EditText editTextFechaPedido;
    private EditText editTextFechaEntrega;
    private EditText editTextDescripcion;
    private EditText editTextImporte;
    private Button btnAceptar;
    private Button btnCancelar;
    private List<Tienda> tiendas;
    int result;
    Toast toast;

    private PedidoCallback callback;

    public NuevoPedido(PedidoCallback callback) { this.callback = callback;}

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // crear un dialog y darle estilo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyDialogWindow);
        // establecer la ventana del dialog
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_nuevo_pedido, null);
        // establecer el título del dialog
        builder.setTitle(R.string.nuevoPedido).setView(dialogView);
        // obtener un arrayList de las tiendas para rellenar el spinner
        tiendas = BDConexion.consultarTiendas();
        // crear un arrayList para guardar todas las Tiendas y mostrarlos en el Spinner
        List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add(getResources().getString(R.string.spinnerPrompt));
        // añadir cada tienda al Spinner
        for (Tienda t : tiendas) {
            spinnerArray.add(t.toString());
        }
        spinnerTiendas = (Spinner) dialogView.findViewById(R.id.spinner);
        // crear un adaptador para el Spinner y añadirle la lista de Tiendas
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, spinnerArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerTiendas.setAdapter(spinnerArrayAdapter);
        spinnerTiendas.setOnItemSelectedListener(this);
        btnAceptar = dialogView.findViewById(R.id.btnAceptar1);
        btnCancelar = dialogView.findViewById(R.id.btnCancelar1);
        editTextFechaPedido = dialogView.findViewById(R.id.editFechaPedido);
        editTextFechaEntrega = dialogView.findViewById(R.id.editFechaEntrega);
        editTextDescripcion = dialogView.findViewById(R.id.editDescripcion);
        editTextImporte = dialogView.findViewById(R.id.editImporte);
        btnAceptar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);
        // poner la fecha de hoy por defecto
        editTextFechaPedido.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")).toString());
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        if (v == btnCancelar) {
            dismiss();
        } else if (v == btnAceptar) {
            // comprobar si todos los campos contienen información
            if (spinnerTiendas.getSelectedItemPosition()!=0 && !editTextFechaPedido.getText().toString().trim().isEmpty() && !editTextFechaEntrega.getText().toString().trim().isEmpty() && !editTextDescripcion.getText().toString().trim().isEmpty() && !editTextImporte.getText().toString().trim().isEmpty()) {
                String nombreTiendaFK = spinnerTiendas.getSelectedItem().toString();
                int idTiendaFK = -1;
                LocalDate fechaPedido = null;
                LocalDate fechaEntrega = null;
                String descripcion = editTextDescripcion.getText().toString();
                double importe;

                String fechaPedidoString = editTextFechaPedido.getText().toString();
                String fechaEntregaString = editTextFechaEntrega.getText().toString();
                // convertir String con fecha de pedido en LocalDate
                fechaPedido = convertToDate(fechaPedidoString);
                // si la fecha del pedido es en formato incorrecto
                if (fechaPedido == null) {
                    toast = Toast.makeText(getContext(), R.string.fechaPedidoIncorrecta, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                // si la fecha del pedido es correcta
                else {
                    // convertir String con fecha de entrega en LocalDate
                    fechaEntrega = convertToDate(fechaEntregaString);
                    // si la fecha de entrega es incorrecta
                    if (fechaEntrega == null) {
                        toast = Toast.makeText(getContext(), R.string.fechaEntregaIncorrecta, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                    // si la fecha de entrega es correcta
                    else {
                        // comprobar que la fecha de pedido es antes de la de entrega
                        if (fechaPedido.isBefore(fechaEntrega)) {
                            // si es así, ajustar el formato de la fecha
                            fechaPedido.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                            fechaEntrega.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        } else {
                            fechaPedido = null;
                            fechaEntrega = null;
                            toast = Toast.makeText(getContext(), R.string.fechasIncorrectas, Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    }
                }
                String importeString = editTextImporte.getText().toString();
                importe = Double.valueOf(importeString);
                // obtener el id de la tienda seleccionada en spinner
                Optional<Tienda> tiendaOpt = tiendas.stream() // crear un stream de todas las tiendas (obtenidas en la consulta anterior)
                        .filter(t -> t.getNombreTienda().equals(nombreTiendaFK)) // filtrar - comparar los nombres de las tiendas con 'nombreTiendaFK'
                        .findFirst(); // obtener la tienda con este nombre
                if (tiendaOpt.isPresent()) {
                    idTiendaFK = tiendaOpt.get().getIdTienda(); // obtener el id de la tienda
                }
                if (fechaPedido != null && fechaEntrega != null && idTiendaFK != -1) {
                    // crear el objeto Pedido
                    Pedido pedido = new Pedido(fechaPedido, fechaEntrega, descripcion, importe, 0, idTiendaFK);
                    // dar de alta a un pedido (pasar el objeto Pedido al método altaPedido)
                    result = BDConexion.altaPedido(pedido);
                    if (result == 200) {
                        toast = Toast.makeText(getContext(), R.string.altaCorrecta, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        // si el alta se realiza correctamente, ejecuta el método establecido en ListadoPedidos pasándole true
                        callback.onOperacionCorrectaUpdated(true);
                    } else {
                        toast = Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                    dismiss();
                }
            } else {
                toast = Toast.makeText(getContext(), R.string.rellenaTodos, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }

    @Override public void onResume() {
        super.onResume();
        // bloquear en modo vertical
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override public void onPause() {
        super.onPause();
        //set rotation to sensor dependent
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private LocalDate convertToDate(String fecha) {
        LocalDate fechaLD = null;
        try {
            // dividir la fecha en día, mes y año
            String[] dateSplit = fecha.split("/");
            int day = Integer.parseInt(dateSplit[0]);
            int month = Integer.parseInt(dateSplit[1]);
            int year = Integer.parseInt(dateSplit[2]);
            // crear una fecha utilizando el día, mes y año
            fechaLD = LocalDate.of(year, month, day);
        } catch (Exception ex) {}
        return fechaLD;
    }
}
