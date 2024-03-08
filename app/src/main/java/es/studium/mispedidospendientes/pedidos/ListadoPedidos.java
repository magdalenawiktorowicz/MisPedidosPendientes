package es.studium.mispedidospendientes.pedidos;

import android.graphics.drawable.ColorDrawable;
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
import es.studium.mispedidospendientes.tiendas.ListadoTiendas;

public class ListadoPedidos extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    List<Pedido> pedidos;
    RecyclerView recyclerView;
    Button btnTiendas;
    Button btnNuevoPedido;

    FragmentManager fm;
    FragmentTransaction ft;
    Fragment fragmentListadoTiendas;
    NuevoPedido dialogNuevoPedido;
    EdicionPedido dialogEdicionPedido;
    BorradoPedido dialogBorradoPedido;
    PedidoCallback pedidoCallback;

    public ListadoPedidos() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        // clase anómina - implementación de callback
        pedidoCallback = new PedidoCallback() {
            // si la operación se realiza correctamente, se re-establece el recyclerView con el listado de los pedidos
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
        return inflater.inflate(R.layout.fragment_listado_pedidos, container, false);
    }

    // método para obtener los pedidos de la BD y mostrarlos en recyclerView
    private void setUpRecyclerView() {
        recyclerView = requireView().findViewById(R.id.recyclerViewPedidos);
        // realizar la consulta de Pedidos
        pedidos = BDConexion.consultarPedidos();

        if (recyclerView != null) {
            recyclerView.setAdapter(new PedidosAdapter(pedidos, new RecyclerViewOnItemClickListener() {
                // al hacer click corto, se abrirá el dialog para editar el pedido
                @Override
                public void onClick(View v, int position) {
                    dialogEdicionPedido = new EdicionPedido(pedidos.get(position).getIdPedido(), pedidoCallback);
                    dialogEdicionPedido.setCancelable(false);
                    dialogEdicionPedido.show(getActivity().getSupportFragmentManager(), "Edición Pedido");
                }
                // al hacer click largo, se abrirá el dialog para borrar el pedido
                @Override
                public void onLongClick(View v, int position) {
                    dialogBorradoPedido = new BorradoPedido(pedidos.get(position).getIdPedido(), pedidoCallback);
                    dialogBorradoPedido.setCancelable(false);
                    dialogBorradoPedido.show(getActivity().getSupportFragmentManager(), "Borrado Pedido");
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
        btnTiendas = view.findViewById(R.id.btnTiendas);
        btnTiendas.setOnClickListener(this);
        btnNuevoPedido = view.findViewById(R.id.btnNuevoPedido);
        btnNuevoPedido.setOnClickListener(this);
        fm = getActivity().getSupportFragmentManager();
        setUpRecyclerView();
        // establecer el título en la barra superior
        if (getActivity() != null) {
            // establecer el color del fondo de la barra superior
            ((MainActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.olivine)));
            // establecer el título de la barra superior
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_action_bar_pedidos);
        }
    }

    @Override
    public void onClick(View v) {
        // al pulsar el botón 'Tiendas', se mostrará el fragment con el listado de tiendas
        if (v == btnTiendas) {
            fragmentListadoTiendas = new ListadoTiendas();
            ft = fm.beginTransaction();
            ft.replace(R.id.fragmentContainer, fragmentListadoTiendas, "listadoTiendas")
                    .addToBackStack(null)
                    .commit();
        }
        // al pulsar el botón 'Nuevo Pedido', se abrirá el dialog para crear un pedido nuevo
        else if (v == btnNuevoPedido) {
            dialogNuevoPedido = new NuevoPedido(pedidoCallback);
            dialogNuevoPedido.setCancelable(false);
            dialogNuevoPedido.show(getActivity().getSupportFragmentManager(), "Nuevo Pedido");
        }
    }
}