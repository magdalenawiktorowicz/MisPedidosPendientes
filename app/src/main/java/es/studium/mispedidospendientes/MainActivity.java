package es.studium.mispedidospendientes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.StrictMode;
import es.studium.mispedidospendientes.pedidos.ListadoPedidos;

public class MainActivity extends AppCompatActivity {

    FragmentManager fm = getSupportFragmentManager();
    Fragment listadoPedidos;
    FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listadoPedidos = fm.findFragmentById(R.id.listadoPedidos);
        if (listadoPedidos == null) {
            ft = fm.beginTransaction();
            ft.replace(R.id.fragmentContainer, new ListadoPedidos(), "listadoPedidos");
            ft.commit();
        }

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

}