package es.studium.mispedidospendientes.pedidos;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import es.studium.mispedidospendientes.R;
import es.studium.mispedidospendientes.RecyclerViewOnItemClickListener;

public class PedidosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final TextView pedidoTextView;
    private final RecyclerViewOnItemClickListener listener;

    public PedidosViewHolder(@NonNull View itemView, @NonNull RecyclerViewOnItemClickListener listener) {
        super(itemView);
        pedidoTextView = itemView.findViewById(R.id.textViewPedidoItem);
        this.listener = listener;
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onLongClick(v, getAbsoluteAdapterPosition());
                return true;
            }
        });
    }

    public void bindRow(@NonNull Pedido pedido) {
        pedidoTextView.setText(pedido.toString());
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAbsoluteAdapterPosition());
    }
}
