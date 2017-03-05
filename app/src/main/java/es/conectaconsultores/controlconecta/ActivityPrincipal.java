package es.conectaconsultores.controlconecta;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

public class ActivityPrincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private EditText mNumeroOrden, mEntrada, mSalida, mDescripcion;
    private Spinner mTipoOrden;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mNumeroOrden = (EditText) findViewById(R.id.edit_titulo);
        mEntrada = (EditText) findViewById(R.id.edit_entrada);
        mSalida = (EditText) findViewById(R.id.edit_salida);
        mDescripcion = (EditText) findViewById(R.id.edit_descripcion);

        Button boton = (Button) findViewById(R.id.button_enviar);
        boton.setOnClickListener(this);
        boton = (Button) findViewById(R.id.button_borrar);
        boton.setOnClickListener(this);

        mTipoOrden = (Spinner) findViewById(R.id.spinner_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tipo_orden, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTipoOrden.setAdapter(adapter);
        mTipoOrden.setSelection(0);

        requestPermissions();

        showSoftKeyboard(mNumeroOrden);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_principal, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_about) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_gallery) {
            
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onClick(View v) {
        final View view = v;

        switch (view.getId()) {
            case R.id.button_enviar:
            case R.id.button_borrar:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                if (view.getId() == R.id.button_enviar) {
                    builder.setTitle("¿Enviar datos?");
                } else if (view.getId() == R.id.button_borrar) {
                    builder.setTitle("¿Borrar datos?");
                }
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (view.getId() == R.id.button_enviar) {
                            insertEventCalendar();
                        } else if (view.getId() == R.id.button_borrar) {
                            clearEditText();
                        }
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {}
                });
                builder.setCancelable(false);
                builder.create().show();
                break;
        }
    }


    public void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }


    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR}, 444);
    }


    private void clearEditText() {
        mTipoOrden.setSelection(0);
        mNumeroOrden.setText("");
        mEntrada.setText("");
        mSalida.setText("");
        mDescripcion.setText("");
    }


    private void insertEventCalendar() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR}, 444);
        } else {

            int year, month, day, hourEntrada, minuteEntrada, hourSalida, minuteSalida;
            long startMillis, endMillis;

            long calID = getCalendar();

            if (calID == 99) {
                DialogError("Error Calendario", "El calendario Actuaciones no existe");
                return;
            }

            Calendar cal = Calendar.getInstance();

            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH);
            day = cal.get(Calendar.DAY_OF_MONTH);

            if (mNumeroOrden.getText().length() == 0) {
                DialogError("Error Datos", "Inserta un Titulo para continuar");
                return;
            }

            try {
                if (mEntrada.getText().length() == 5) {
                    hourEntrada = Integer.parseInt(mEntrada.getText().subSequence(0, 2).toString());
                    minuteEntrada = Integer.parseInt(mEntrada.getText().subSequence(3, 5).toString());
                } else if (mEntrada.getText().length() == 4) {
                    hourEntrada = Integer.parseInt(mEntrada.getText().subSequence(0, 1).toString());
                    minuteEntrada = Integer.parseInt(mEntrada.getText().subSequence(2, 4).toString());
                } else {
                    DialogError("Error Datos", "Hora de entrada incorrecta");
                    return;
                }
            } catch (NumberFormatException e) {
                DialogError("Error Datos", "Hora de entrada incorrecta");
                return;
            }

            try {
                if (mSalida.getText().length() == 5) {
                    hourSalida = Integer.parseInt(mSalida.getText().subSequence(0, 2).toString());
                    minuteSalida = Integer.parseInt(mSalida.getText().subSequence(3, 5).toString());
                } else if (mSalida.getText().length() == 4) {
                    hourSalida = Integer.parseInt(mSalida.getText().subSequence(0, 1).toString());
                    minuteSalida = Integer.parseInt(mSalida.getText().subSequence(2, 4).toString());
                } else {
                    DialogError("Error Datos", "Hora de salida incorrecta");
                    return;
                }
            } catch (NumberFormatException e) {
                DialogError("Error Datos", "Hora de salida incorrecta");
                return;
            }

            if (((hourEntrada*60)+minuteEntrada) <= ((hourSalida*60)+minuteSalida)) {
                if (hourEntrada >= 0 && hourEntrada <= 23 && minuteEntrada >= 0 && minuteEntrada <= 59) {
                    cal.set(year, month, day, hourEntrada, minuteEntrada);
                    startMillis = cal.getTimeInMillis();
                } else {
                    DialogError("Error Datos", "Hora de entrada incorrecta");
                    return;
                }
                if (hourSalida >= 0 && hourSalida <= 23 && minuteSalida >= 0 && minuteSalida <= 59) {
                    cal.set(year, month, day, hourSalida, minuteSalida);
                    endMillis = cal.getTimeInMillis();
                } else {
                    DialogError("Error Datos", "Hora de salida incorrecta");
                    return;
                }
            } else {
                DialogError("Error Datos", "Hora de entrada superior a Hora de salida");
                return;
            }

            final ContentValues values = new ContentValues();
            values.put(CalendarContract.Events.EVENT_TIMEZONE, "Europe/Madrid");
            values.put(CalendarContract.Events.CALENDAR_ID, calID);
            if (mTipoOrden.getSelectedItem().equals("")) {
                values.put(CalendarContract.Events.TITLE, mNumeroOrden.getText().toString());
            } else {
                values.put(CalendarContract.Events.TITLE, mTipoOrden.getSelectedItem() + " - " + mNumeroOrden.getText().toString());
            }
            values.put(CalendarContract.Events.DTSTART, startMillis);
            values.put(CalendarContract.Events.DTEND, endMillis);

            if (mDescripcion.getText().length() != 0) {
                values.put(CalendarContract.Events.DESCRIPTION, mDescripcion.getText().toString());
                getContentResolver().insert(CalendarContract.Events.CONTENT_URI, values);
                if (mTipoOrden.getSelectedItemPosition() == 6) {
                    sendEmailSpark();
                } else {
                    sendEmailFran();
                }
                DialogCorrect("Registro correcto");
            } else {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Faltan Datos");
                builder.setTitle("Descripcion en blanco ¿Continuar de todos modos?");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        getContentResolver().insert(CalendarContract.Events.CONTENT_URI, values);
                        if (mTipoOrden.getSelectedItemPosition() == 6) {
                            sendEmailSpark();
                        } else {
                            sendEmailFran();
                        }
                        DialogCorrect("Registro correcto");
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {}
                });
                builder.setCancelable(false);
                builder.create().show();
            }

        }
    }


    private int getCalendar() {
        int CalId = 99;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR}, 444);
        } else {
            String[] projection = new String[]{CalendarContract.Calendars._ID, CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,};
            Cursor cur = getContentResolver().query(CalendarContract.Calendars.CONTENT_URI, projection, null, null, null);
            if (cur != null && cur.moveToFirst()) {
                do {
                    if (cur.getString(1).equals("Produccion")) {
                        CalId = cur.getInt(0);
                    }
                } while (cur.moveToNext());
                cur.close();
            }
        }
        return CalId;
    }


    private void DialogError(String titulo, String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(mensaje);
        builder.setTitle(titulo);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {}
        });
        builder.setCancelable(false);
        builder.create().show();
    }


    private void DialogCorrect(String titulo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titulo);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                clearEditText();
                mNumeroOrden.requestFocus();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }


    private void sendEmailSpark() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "spark.telecom@sparkiberica.com" });
        intent.putExtra(Intent.EXTRA_CC, new String[] { "miguel.piqueras@fibrados.es", "francisco.rabazas@conectaconsultores.es" });
        intent.putExtra(Intent.EXTRA_SUBJECT, "CIERRE " + mTipoOrden.getSelectedItem() + " - " + mNumeroOrden.getText().toString());
        intent.putExtra(Intent.EXTRA_TEXT, "Actuacion completada OK\n\n" +
                "Hora entrada: " + mEntrada.getText().toString() + "\n" +
                "Hora salida: " + mSalida.getText().toString() + "\n\n" +
                mDescripcion.getText().toString());
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    private void sendEmailFran() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "francisco.rabazas@conectaconsultores.es" });
        if (mTipoOrden.getSelectedItem().equals("")) {
            intent.putExtra(Intent.EXTRA_SUBJECT, mNumeroOrden.getText().toString());
        } else {
            intent.putExtra(Intent.EXTRA_SUBJECT, mTipoOrden.getSelectedItem() + " - " + mNumeroOrden.getText().toString());
        }


        intent.putExtra(Intent.EXTRA_TEXT, "Hora entrada: " + mEntrada.getText().toString() + "\n" +
                "Hora salida: " + mSalida.getText().toString() + "\n\n" +
                mDescripcion.getText().toString());
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


}
