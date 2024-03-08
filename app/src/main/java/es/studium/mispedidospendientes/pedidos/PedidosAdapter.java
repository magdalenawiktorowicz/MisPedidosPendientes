package es.studium.mispedidospendientes.pedidos;


import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.studium.mispedidospendientes.R;
import es.studium.mispedidospendientes.RecyclerViewOnItemClickListener;

public class PedidosAdapter extends RecyclerView.Adapter<PedidosViewHolder> {
    private List<Pedido> data;
    private final RecyclerViewOnItemClickListener recyclerViewOnItemClickListener;

    public PedidosAdapter(@NonNull List<Pedido> data, @NonNull RecyclerViewOnItemClickListener recyclerViewOnItemClickListener) {
        this.data = data;
        this.recyclerViewOnItemClickListener = recyclerViewOnItemClickListener;
    }

    @Override
    @NonNull
    public PedidosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_pedidos, parent, false);
        return new PedidosViewHolder(row, recyclerViewOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(PedidosViewHolder holder, int position) {
        holder.bindRow(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
