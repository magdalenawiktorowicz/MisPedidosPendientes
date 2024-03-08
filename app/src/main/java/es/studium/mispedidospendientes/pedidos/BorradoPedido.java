package es.studium.mispedidospendientes.pedidos;

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

import es.studium.mispedidospendientes.BDConexion;
import es.studium.mispedidospendientes.R;

public class BorradoPedido extends DialogFragment implements View.OnClickListener {
    private int idPedido;
    private TextView mensajeConfirmacion;
    private Button btnCancelar;
    private Button btnAceptar;
    private PedidoCallback callback;
    Toast toast;

    public BorradoPedido(int idPedido, PedidoCallback callback) {
        this.idPedido = idPedido;
        this.callback = callback;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // crear un dialog y darle estilo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyDialogWindow);
        // establecer la ventana del dialog
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_borrado_pedido, null);
        // establecer el título del dialog
        builder.setTitle(getResources().getString(R.string.borradoPedido) + idPedido).setView(dialogView);

        btnCancelar = dialogView.findViewById(R.id.btnCancelar5);
        btnAceptar = dialogView.findViewById(R.id.btnAceptar5);
        mensajeConfirmacion = dialogView.findViewById(R.id.textViewConfirmacion2);
        mensajeConfirmacion.setText(mensajeConfirmacion.getText() + " " + idPedido + "?");
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
            // llamar al método para realizar la baja
            result = BDConexion.borradoPedido(idPedido);
            // si la baja se ha realizado correctamente devolverá el código 200
            if (result == 200) {
                toast = Toast.makeText(getContext(), R.string.borradoCorrecto, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                // llamar al método que actualiza el listado
                callback.onOperacionCorrectaUpdated(true);
            } else {
                toast = Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT);
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
