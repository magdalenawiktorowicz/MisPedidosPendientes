package es.studium.mispedidospendientes.tiendas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import java.util.List;
import es.studium.mispedidospendientes.BDConexion;
import es.studium.mispedidospendientes.MainActivity;
import es.studium.mispedidospendientes.R;
import es.studium.mispedidospendientes.RecyclerViewOnItemClickListener;
import es.studium.mispedidospendientes.pedidos.ListadoPedidos;

public class ListadoTiendas extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    List<Tienda> tiendas;
    RecyclerView recyclerView;
    Button btnVolver;
    Button btnNuevaTienda;
    FragmentManager fm;
    FragmentTransaction ft;
    Fragment fragmentListadoPedidos;
    NuevaTienda dialogNuevaTienda;
    EdicionTienda dialogEdicionTienda;
    BorradoTienda dialogBorradoTienda;
    TiendaCallback tiendaCallback;

    public ListadoTiendas() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        // clase anónima - implementación de callback
        tiendaCallback = new TiendaCallback() {
            // si la operación se realiza correctamente, se re-establece el recyclerView con el listado de las tiendas
            @Override
            public void onOperacionCorrectaUpdated(boolean operacionCorrecta) {
                if (operacionCorrecta) {
                    setUpRecyclerView();
                }
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_listado_tiendas, container, false);
    }

    // método para obtener las tiendas de la BD y mostrarlas en recyclerView
    void setUpRecyclerView() {
        recyclerView = requireView().findViewById(R.id.recyclerViewTiendas);
        // realizar la consulta de Tiendas
        tiendas = BDConexion.consultarTiendas();
        if (recyclerView != null) {
            recyclerView.setAdapter(new TiendasAdapter(tiendas, new RecyclerViewOnItemClickListener() {
                // al hacer click corto, se abrirá el dialog para editar la tienda
                @Override
                public void onClick(View v, int position) {
                    dialogEdicionTienda = new EdicionTienda(tiendas.get(position).toString(), tiendas.get(position).getIdTienda(), tiendaCallback);
                    dialogEdicionTienda.setCancelable(false);
                    dialogEdicionTienda.show(getActivity().getSupportFragmentManager(), "Edición Tienda");
                }
                // al hacer click largo, se abrirá el dialog para borrar la tienda
                @Override
                public void onLongClick(View v, int position) {
                    dialogBorradoTienda = new BorradoTienda(tiendas.get(position).getIdTienda(), tiendas.get(position).getNombreTienda(), tiendaCallback);
                    dialogBorradoTienda.setCancelable(false);
                    dialogBorradoTienda.show(getActivity().getSupportFragmentManager(), "Borrado Tienda");
                }
            }));
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), ((LinearLayoutManager) recyclerView.getLayoutManager()).getOrientation());
            recyclerView.addItemDecoration(dividerItemDecoration);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnVolver = view.findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(this);
        btnNuevaTienda = view.findViewById(R.id.btnNuevaTienda);
        btnNuevaTienda.setOnClickListener(this);
        fm = getActivity().getSupportFragmentManager();
        setUpRecyclerView();
        // establecer el título en la barra superior
        if (getActivity() != null) {
            // establecer el título de la barra superior
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_action_bar_tiendas);
        }
    }

    @Override
    public void onClick(View v) {
        // al pulsar el botón 'Volver', se mostrará el fragment con el listado de pedidos
        if (v == btnVolver) {
            fragmentListadoPedidos = new ListadoPedidos();
            ft = fm.beginTransaction();
            ft.replace(R.id.fragmentContainer, fragmentListadoPedidos, "listadoPedidos")
                    .addToBackStack(null)
                    .commit();
        }
        // al pulsar el botón 'Nueva Tienda', se abrirá el dialog para dar de baja a una tienda nueva
        else if (v == btnNuevaTienda) {
            dialogNuevaTienda = new NuevaTienda(tiendaCallback);
            dialogNuevaTienda.setCancelable(false);
            dialogNuevaTienda.show(getActivity().getSupportFragmentManager(), "Nueva Tienda");
        }
    }
}