package com.example.andreacarballidop1di.UI;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.andreacarballidop1di.R;
import com.example.andreacarballidop1di.core.Tarea;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Tarea> items;
    private AccionesTarea accionesTarea;

    MyRecyclerViewAdapter(List<Tarea> items, AccionesTarea accionesTarea) {
        this.items=items;
        this.accionesTarea=accionesTarea;
        this.context = context;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private AccionesTarea accionesTarea;
        // Campos respectivos de un item
        private CardView cv;
        private TextView fecha;
        private TextView textotarea;
        private ImageView imagen;

        public ViewHolder(View v,AccionesTarea accionesTarea) {
            super(v);

            cv= (CardView) v.findViewById(R.id.cv);
            fecha = (TextView) v.findViewById(R.id.tv_card_Fecha);
            textotarea = (TextView) v.findViewById(R.id.tv_card_Tarea);
            imagen= (ImageView)v.findViewById(R.id.imagen);
            this.accionesTarea = accionesTarea;


        }

        public void mostrarTarea(final Tarea tarea) {

            fecha.setText(tarea.getFormatoFecha());
            textotarea.setText(tarea.getTextotarea());

            cv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v){
                    PopupMenu popup = new PopupMenu(cv.getContext(), itemView);

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.modificartarea:
                                    accionesTarea.modificar(tarea);
                                    return true;
                                case R.id.eliminartarea:
                                    accionesTarea.eliminar(tarea);
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    // here you can inflate your menu
                    popup.inflate(R.menu.context_menu);
                    popup.setGravity(Gravity.RIGHT);
                    popup.show();
                    return false;
                }

            });
        }
    }
    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_tarea, viewGroup, false);
        ViewHolder pvh = new ViewHolder(v,accionesTarea);
        return pvh;
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.mostrarTarea(items.get(i));
    }

//    public MyRecyclerViewAdapter(MainActivity mainActivity, ArrayList<Tarea> items) {
//        this.items = items;
//    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}