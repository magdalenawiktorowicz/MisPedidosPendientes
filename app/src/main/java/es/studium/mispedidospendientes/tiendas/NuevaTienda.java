package es.studium.mispedidospendientes.tiendas;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;
import java.util.ArrayList;
import es.studium.mispedidospendientes.BDConexion;
import es.studium.mispedidospendientes.R;

public class NuevaTienda extends DialogFragment implements View.OnClickListener {
    private EditText editNombreTienda;
    private Button btnCancelar;
    private Button btnAceptar;
    private TiendaCallback callback;
    Toast toast;

    public NuevaTienda(TiendaCallback callback) {
        this.callback = callback;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // crear un dialog y darle estilo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyDialogWindow);
        // establecer la ventana del dialog
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_nueva_tienda, null);
        // establecer el título del dialog
        builder.setTitle(R.string.nuevaTienda).setView(dialogView);
        btnCancelar = dialogView.findViewById(R.id.btnCancelar);
        btnAceptar = dialogView.findViewById(R.id.btnAceptar);
        editNombreTienda = dialogView.findViewById(R.id.editNombreTienda);
        btnCancelar.setOnClickListener(this);
        btnAceptar.setOnClickListener(this);
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        if (v == btnCancelar) {
            dismiss();
        } else if (v == btnAceptar) {
            // obtener el nombre de la tienda que ha introducido el usuario
            String nombreTienda = editNombreTienda.getText().toString();
            // control de input del usuario
            if (nombreTienda.trim().isEmpty()) {
                toast = Toast.makeText(getContext(), R.string.nombreNoValido, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else {
                // variable 'result' para indicar si la operación se ha realizado correctamente
                int result = 0;
                // variable para asegurarse que la misma tienda no se introduzca en la BD
                boolean existe = false;
                try {
                    // comprobar si la tienda ya existe en la BD
                    ArrayList<Tienda> tiendas = BDConexion.consultarTiendas();
                    for (Tienda t : tiendas) {
                        if (nombreTienda.equals(t.getNombreTienda())) {
                            existe = true;
                        }
                    }
                    // si no existe todavía, realizar el alta
                    if (!existe) {
                        result = BDConexion.altaTienda(nombreTienda);
                    } else {
                        toast = Toast.makeText(getContext(), R.string.tiendaYaExiste, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                } catch (Exception ex) {
                    toast = Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                // si el alta se realiza correctamente, ejecuta el método establecido en ListadoTiendas pasándole true
                if (result == 200) {
                    toast = Toast.makeText(getContext(), R.string.altaCorrecta, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    callback.onOperacionCorrectaUpdated(true);
                }
                dismiss();
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
}
