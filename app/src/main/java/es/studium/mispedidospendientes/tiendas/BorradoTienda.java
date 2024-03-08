package es.studium.mispedidospendientes.tiendas;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;
import java.util.List;
import es.studium.mispedidospendientes.BDConexion;
import es.studium.mispedidospendientes.R;
import es.studium.mispedidospendientes.pedidos.Pedido;

public class BorradoTienda extends DialogFragment implements View.OnClickListener {
    private int idTienda;
    private String nombreTienda;
    private TextView mensajeConfirmacion;
    private Button btnCancelar;
    private Button btnAceptar;
    private TiendaCallback callback;
    Toast toast;

    public BorradoTienda(int idTienda, String nombreTienda, TiendaCallback callback) {
        this.idTienda = idTienda;
        this.nombreTienda = nombreTienda;
        this.callback = callback;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // crear un dialog y darle estilo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyDialogWindow);
        // establecer la ventana del dialog
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_borrado_tienda, null);
        // establecer el título del dialog
        builder.setTitle(getResources().getString(R.string.borradoTienda)).setView(dialogView);
        btnCancelar = dialogView.findViewById(R.id.btnCancelar4);
        btnAceptar = dialogView.findViewById(R.id.btnAceptar4);
        mensajeConfirmacion = dialogView.findViewById(R.id.textViewConfirmacion);
        mensajeConfirmacion.setText(mensajeConfirmacion.getText() + " " + nombreTienda + "?");
        btnCancelar.setOnClickListener(this);
        btnAceptar.setOnClickListener(this);
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        if (v == btnCancelar) {
            dismiss();
        } else if (v == btnAceptar) {
            // variable 'result' para controlar si la operación se ha realizado correctamente o no
            int result = 0;
            // obtener una lista de pedidos
            List<Pedido> pedidos = BDConexion.consultarPedidos();
            // comprobar si la tienda aparece en algún pedido como FK
            boolean esFK = pedidos.stream().anyMatch(p -> p.getIdTiendaFK() == idTienda);
            // si la tienda no aparece como FK en 'pedidos'
            if (!esFK) {
                // realizar la baja
                result = BDConexion.borradoTienda(idTienda);
                // si la baja se realiza correctamente, devuelve el código 200
                if (result == 200) {
                    toast = Toast.makeText(getContext(), R.string.borradoCorrecto, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    // llamar al método que actualiza el listado
                    callback.onOperacionCorrectaUpdated(true);
                }
            } else {
                toast = Toast.makeText(getContext(), R.string.esFK, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
            dismiss();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // bloquear en modo vertical
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onPause() {
        super.onPause();
        //set rotation to sensor dependent
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }
}
