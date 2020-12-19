package com.example.andreacarballidop1di.UI;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.andreacarballidop1di.R;
import com.example.andreacarballidop1di.core.Tarea;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements AccionesTarea{

    TextView fecha;
    Tarea tareaModifico;
    MyRecyclerViewAdapter adapter;
    ArrayList<Tarea> listaTareas;
    List<Tarea> tareas;
    ArrayList<Tarea> tareasfinalizadas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton btAdd = findViewById(R.id.btAdd);

        tareas  = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.rvTareas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(tareas,this);
        recyclerView.setAdapter(adapter);


        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.onAdd(null);
            }
        });
    }



   private void onAdd(final Tarea tareaModifico) {

     final AlertDialog.Builder builder = new AlertDialog.Builder(this);

       final View dialogLayout = getLayoutInflater().inflate(R.layout.dialog_tareanueva, null);
       builder.setView(dialogLayout);

       final TextView tvFecha = dialogLayout.findViewById(R.id.tvFecha);
       final EditText tvTarea = dialogLayout.findViewById(R.id.edTarea);

      final Calendar calendar = Calendar.getInstance();
       if (tareaModifico != null) {
           String tareaT = String.valueOf(tareaModifico.getTextotarea());
           String fecha = String.valueOf(tareaModifico.getFormatoFecha());
           tvFecha.setText(fecha);
           calendar.setTime(tareaModifico.getFecha());
           tvTarea.setText(tareaT);

       }

       tvFecha.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               int day = calendar.get(Calendar.DAY_OF_MONTH);
               int month = calendar.get(Calendar.MONTH);
               int year = calendar.get(Calendar.YEAR);

               final DatePickerDialog datePicker = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {

                   @Override
                   public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                       calendar.set(Calendar.YEAR, selectedYear);
                       calendar.set(Calendar.MONTH, selectedMonth);
                       calendar.set(Calendar.DAY_OF_MONTH, selectedDay);

                       SimpleDateFormat formatoFecha = new SimpleDateFormat("dd MMMM 'de' yyyy", Locale.getDefault());
                       tvFecha.setText(formatoFecha.format(calendar.getTime()));
                   }
               }, year, month, day);

               datePicker.show();
           }
       });


       builder.setPositiveButton( "Añadir tarea", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String tTarea = tvTarea.getText().toString();

                if ( tvFecha.getText().toString().length()<=0 && tTarea.equals("")){
                    Toast.makeText(MainActivity.this, "No se permiten los campos vacíos", Toast.LENGTH_SHORT).show();
                    return;

                }
                if(tvFecha.getText().toString().length()<=0){
                    Toast.makeText(MainActivity.this, "Debe escoger una fecha", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(tTarea.equals("")){

                    Toast.makeText(MainActivity.this,  "Campo de la tarea vacío", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (tareaModifico == null) {
                    Tarea tarea = new Tarea(calendar.getTime(),tTarea);
                    tareas.add(tarea);
                    Toast.makeText(MainActivity.this, "Tarea añadida", Toast.LENGTH_SHORT).show();
                } else {
                    tareaModifico.modificarTarea(calendar.getTime(),tTarea);

                    Toast.makeText(MainActivity.this, "Tarea modificada", Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();



            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.create().show();
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
                onAdd(null);
                toret = true;
                break;
            case R.id.modificar:
               modificarTareas();
                toret = true;
                break;
            case R.id.eliminar:
                eliminarTareas();
                toret = true;
                break;
        }
        return toret;

    }



    private void modificarTareas(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Modificar tarea");

        final String[] arrayTareas = new String[tareas.size()];
        final boolean[] eleccionTareas = new boolean[tareas.size()];
        for (int i = 0; i < tareas.size(); i++) {
            arrayTareas[i] = tareas.get(i).getFormatoFecha() + ", " + tareas.get(i).getTextotarea();
        }

        builder.setSingleChoiceItems(arrayTareas, -1, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int i) {
                tareaModifico = tareas.get(i);
            }
        });
        builder.setPositiveButton("Modificar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(final DialogInterface dialog, final int i) {
                if (tareaModifico == null) {
                    Toast.makeText(MainActivity.this, "No ha seleccionado una tarea", Toast.LENGTH_SHORT).show();
                } else {
                    onAdd(tareaModifico);
                }
            }

        });
        builder.setNegativeButton("Cancelar", null);
        builder.create().show();
    }


    @Override
    public void modificar(Tarea tarea) {
        onAdd(tarea);
    }

    @Override
    public void eliminar(final Tarea tarea) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Borrar Elemento");
        builder.setMessage("Está seguro de que desea eliminar este elemento?\n\n" + tarea.eliminarTarea());
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tareas.remove(tarea);
                Toast.makeText(MainActivity.this, "Tarea eliminada correctamente", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            }

        });

        builder.setNegativeButton("No", null);
        builder.create().show();
    }




    private void eliminarTareas() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar tareas");

        String[] stringTareas = new String[tareas.size()];
        final boolean[] tareaseleccion = new boolean[tareas.size()];
        for (int i = 0; i < tareas.size(); i++) {
            stringTareas[i] = tareas.get(i).getFormatoFecha() + ", " + tareas.get(i).getTextotarea();
        }
        builder.setMultiChoiceItems(stringTareas, tareaseleccion, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i, boolean isChecked) {
                tareaseleccion[i] = isChecked;
            }
        });
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                AlertDialog.Builder buildereliminar = new AlertDialog.Builder(MainActivity.this);
                buildereliminar.setMessage("¿Está seguro de que desea eliminar los elementos?");
                buildereliminar.setNegativeButton("No", null);
                buildereliminar.setPositiveButton("Sí", new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                        for (int i = tareas.size() - 1; i >= 0; i--) {
                            if (tareaseleccion[i]) {
                                tareas.remove(i);

                            }
                        }
                        Toast.makeText(MainActivity.this, "Tareas eliminadas correctamente", Toast.LENGTH_SHORT).show();
                        MainActivity.this.adapter.notifyDataSetChanged();

                    }
                });
                buildereliminar.create().show();
            }
        });
        builder.setNegativeButton("No", null);
        builder.create().show();

    }


    @Override
    protected void onResume() {
        tareasfinalizadas = new ArrayList<>();
        super.onResume();

        for (Tarea t : tareas) {
            if (t.getFecha().compareTo(new Date()) < 0) {
                tareasfinalizadas.add(t);
            }
        }
        if (!tareasfinalizadas.isEmpty()) {
            tareasCaducadas(tareasfinalizadas);
        }
    }


    private void tareasCaducadas(final List<Tarea> tareasfinalizadas) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tareas Finalizadas");
        ArrayList<String> listatareasfinalizadas = new  ArrayList<String>();
        String[] stringtareas = new String[tareasfinalizadas.size()];
        String tareaseliminar = null;
        final boolean[] tareaseleccion = new boolean[tareasfinalizadas.size()];

        for (int i = 0; i < tareasfinalizadas.size(); i++) {
           stringtareas[i] = "Tarea: " + tareasfinalizadas.get(i).getTextotarea() + "\nFecha: "
                    + tareasfinalizadas.get(i).getFormatoFecha();
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
                                tareas.remove(tareasfinalizadas.get(i));
                            }
                        }
                        Toast.makeText(MainActivity.this, "Tareas eliminadas correctamente", Toast.LENGTH_SHORT).show();
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