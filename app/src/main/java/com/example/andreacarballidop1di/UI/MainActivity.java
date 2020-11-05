package com.example.andreacarballidop1di.UI;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.andreacarballidop1di.R;
import com.example.andreacarballidop1di.core.Tarea;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {

    TextView fecha;
    Calendar calendar= Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);

    MyRecyclerViewAdapter adapter;
    ArrayList<Tarea> listaTareas;
    ArrayList<Tarea> tareas;
    ArrayList<Tarea> tareasfinalizadas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton btAdd = findViewById(R.id.btAdd);

        tareas  = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.rvTarea);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, tareas);
        adapter.setClickListener(this);

        this.registerForContextMenu( recyclerView );
        recyclerView.setLongClickable( true );
        recyclerView.setAdapter(adapter);




        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.onAdd();
            }
        });
    }


    @Override
    public void onItemClick(View view, int position) {
//        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();

    }


   private void onAdd() {

        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle("Nueva tarea");
        builder.setMessage( "Introduce el nombre de la tarea y la fecha límite asociada" );
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);


       String pattern = "dd-MM-yyyy";
       final View customLayout = getLayoutInflater().inflate(R.layout.dialog_tareanueva,null);
       builder.setView(customLayout);
//     builder.setTitle(R.string.alDiag_train);

       final EditText tarea = new EditText( this );
        layout.addView(tarea);
        tarea.setHint("Tarea");

       fecha = new TextView( this );
           fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(0);

            }
        });

        fecha.setHint("Fecha");
        layout.addView(fecha);

        builder.setView(layout );
        builder.setPositiveButton( "+", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final String text = tarea.getText().toString();
                final String text2 = fecha.getText().toString();
                String t= String.valueOf(text);


                String f= String.valueOf(text2);


                Tarea tarea1= new Tarea(t,f);
                tareas.add(tarea1);
                adapter.notifyDataSetChanged();

            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        return new DatePickerDialog(this, datePickerListener, year, month, day);

    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            day = selectedDay;
            month = selectedMonth;
            year = selectedYear;
            fecha.setText(day+"/"+(month+1)+"/"+year);

        }

    };


    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo cmi)
    {
        if ( view.getId() == R.id.rvTarea)
        {
            this.getMenuInflater().inflate( R.menu.context_menu, contextMenu );
            contextMenu.setHeaderTitle( "Menú contextual" );
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem menuItem)
    {
        final AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) menuItem.getMenuInfo();

        boolean toret = false;

        switch( menuItem.getItemId() ) {
            case R.id.modificartarea:

                final EditText editText = new EditText(MainActivity.this);
                editText.setText(MainActivity.this.tareas.get(info.position).toString());

                if (info.position>=0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Modificar");
                    builder.setView(editText);
                    builder.setNegativeButton("Cancelar",null);
                    builder.setPositiveButton("Modificar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //cambiar los datos en el arraylist,al cambiar los datos hacer de nuevo los calculos de las estadisticas
//
//                            MainActivity.this.tareas.set(info.position,editText.getText().toString());

                            MainActivity.this.adapter.notifyDataSetChanged();
                        }

                    });
                    builder.create().show();
                }
                toret = true;
                break;
            case R.id.eliminartarea:
                if (info.position >= 0 ) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Borrar Elemento");
                    builder.setMessage("Seguro que quieres borrar el elemento: '" + MainActivity.this.tareas.get(info.position).toString() +"'?");
                    builder.setPositiveButton("Borrar",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.this.tareas.remove( info.position );

                            MainActivity.this.adapter.notifyDataSetChanged();

                        }
                    });
                    builder.setNegativeButton("Cancelar", null);
                    builder.create().show();

                }
                toret = true;
                break;
        }

        return toret;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu( menu );

        this.getMenuInflater().inflate( R.menu.actions_menu, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {

        boolean toret = false;

        switch( menuItem.getItemId() ) {
            case R.id.añadirnuevatarea:
                onAdd();
                toret = true;
                break;
            case R.id.modificartarea:
               Modificar();
                toret = true;
                break;
            case R.id.eliminar:
                Eliminar();
                toret = true;
                break;

        }

        return toret;

    }

    private  void Modificar(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final CharSequence[] items = new CharSequence[tareas.size()];

        for(int i = 0; i < tareas.size(); i++){

            items[i] = "\nTarea: " + tareas.get(i).getTextotarea() + ", Fecha:  "
                    + tareas.get(i).getFecha();

        }

        builder.setTitle("Modificar tarea")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        MainActivity.this.tareas.set(items[which].getText().toString());

                        Toast.makeText(
                                MainActivity.this,
                                "Seleccionaste:" + items[which],
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                });
        builder.create().show();



    }


    private void Eliminar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final CharSequence[] items = new CharSequence[tareas.size()];

        for(int i = 0; i < tareas.size(); i++)
            items[i] = "\nTarea: " + tareas.get(i).getTextotarea() + ", Fecha:  "
                    + tareas.get(i).getFecha();


        final ArrayList itemsSeleccionados = new ArrayList();


        builder.setTitle("Selecciona la tarea que deseas eliminar")
                .setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                        if (isChecked) {
                            // Guardar indice seleccionado
                            itemsSeleccionados.add(which);
                            Toast.makeText(
                                    MainActivity.this,
                                    "Checks seleccionados:(" + itemsSeleccionados.size() + ")",
                                    Toast.LENGTH_SHORT)
                                    .show();
                        } else if (itemsSeleccionados.contains(which)) {
                            // Remover indice sin selección
                            itemsSeleccionados.remove(Integer.valueOf(which));
                        }
                    }
                });

        AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this);
         builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(final DialogInterface dialog, int which) {
                 AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                 builder.setTitle("¿Eliminar el elemento?");
                 AlertDialog.Builder buildereliminar = new AlertDialog.Builder(MainActivity.this);
                 buildereliminar.setMessage("¿Eliminar los elementos?");
                                    buildereliminar.setNegativeButton("Cancelar", null);
                                    buildereliminar.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int which) {

                                            Toast.makeText(MainActivity.this, "Tareas eliminadas", Toast.LENGTH_SHORT).show();
                                            MainActivity.this.adapter.notifyDataSetChanged();
                                        }
                                    });
                                    buildereliminar.create().show();
                                }
                            });
        builder.setNegativeButton("Cancelar", null);
        builder.create().show();
                        }




    @Override
    protected void onResume() {
        tareasfinalizadas = new ArrayList<>();
        super.onResume();

        for (Tarea t : tareas) {
            if (t.getFecha().compareTo(String.valueOf(new Date())) < 0) {
                tareasfinalizadas.add(t);
            }
        }
        if (!tareasfinalizadas.isEmpty()) {
            tareasCaducadas(tareasfinalizadas);
        }
    }



    private void tareasCaducadas(final List<Tarea> tareasFinalizadas) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tareas Finalizadas");
        ArrayList<String> listatareasfinalizadas = new  ArrayList<String>();
        String[] stringtareas = new String[tareasfinalizadas.size()];
        String tareaseliminar = null;
        final boolean[] tareaseleccion = new boolean[tareasfinalizadas.size()];

        for (int i = 0; i < tareasfinalizadas.size(); i++) {
           stringtareas[i] = " Tarea: " + tareasfinalizadas.get(i).getTextotarea() + " Fecha:  "
                    + tareasfinalizadas.get(i).getFecha();
            tareaseliminar =  stringtareas[i];
        }
        builder.setMultiChoiceItems(stringtareas, tareaseleccion, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i, boolean isChecked) {
                tareaseleccion[i] = isChecked;
            }
        });
        final String listaTareasElimino = tareaseliminar;
        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("¿Eliminar el elemento?");
                builder.setMessage(listaTareasElimino);
                AlertDialog.Builder buildereliminar = new AlertDialog.Builder(MainActivity.this);
                buildereliminar.setMessage("¿Eliminar los elementos?");
                buildereliminar.setNegativeButton("Cancelar", null);
                buildereliminar.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        for (int i = tareasfinalizadas.size() - 1; i >= 0; i--) {
                            if (tareaseleccion[i]) {
                                listaTareas.remove(tareasfinalizadas.get(i));
                            }
                        }
                        Toast.makeText(MainActivity.this, "Tareas eliminadas", Toast.LENGTH_SHORT).show();
                        MainActivity.this.adapter.notifyDataSetChanged();
                    }
                });
                buildereliminar.create().show();
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.create().show();
    }

}