package es.studium.mispedidospendientes.tiendas;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import es.studium.mispedidospendientes.R;
import es.studium.mispedidospendientes.RecyclerViewOnItemClickListener;

public class TiendasViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final TextView tiendaTextView;
    private final RecyclerViewOnItemClickListener listener;

    public TiendasViewHolder(@NonNull View itemView, @NonNull RecyclerViewOnItemClickListener listener) {
        super(itemView);
        tiendaTextView = itemView.findViewById(R.id.textViewTiendaItem);
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

    public void bindRow(@NonNull Tienda tienda) {
        tiendaTextView.setText(tienda.toString());
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAbsoluteAdapterPosition());
    }

}
