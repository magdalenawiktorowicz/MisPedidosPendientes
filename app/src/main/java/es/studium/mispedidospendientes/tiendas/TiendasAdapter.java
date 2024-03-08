package es.studium.mispedidospendientes.tiendas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import es.studium.mispedidospendientes.R;
import es.studium.mispedidospendientes.RecyclerViewOnItemClickListener;

public class TiendasAdapter extends RecyclerView.Adapter<TiendasViewHolder> {
    private List<Tienda> data;
    private final RecyclerViewOnItemClickListener recyclerViewOnItemClickListener;

    public TiendasAdapter(@NonNull List<Tienda> data, @NonNull RecyclerViewOnItemClickListener recyclerViewOnItemClickListener) {
        this.data = data;
        this.recyclerViewOnItemClickListener = recyclerViewOnItemClickListener;
    }

    @Override
    @NonNull
    public TiendasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_tiendas, parent, false);
        return new TiendasViewHolder(row, recyclerViewOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(TiendasViewHolder holder, int position) {
        holder.bindRow(data.get(position));
    }

    @Override
    public int getItemCount() { return data.size();}
}
