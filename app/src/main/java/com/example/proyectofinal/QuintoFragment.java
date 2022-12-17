package com.example.proyectofinal;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.Executor;

public class QuintoFragment extends Fragment {

    Email e = new Email();

    private ImageView imgPerfil;
    private TextView txtNombre;
    private RecyclerView recyclerView;
    private EditText edMensaje;
    private Button btnMensaje;
    private AdapterMensajes adapter;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private ImageButton btnEnviarFoto;
    private static final int CODIGO_SELECCION_IMAGEN = 2; //se usa para darvalor a la foto
    private String fotoPerfilCadena;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_quinto, container, false);

        imgPerfil = view.findViewById(R.id.fotoPerfil);
        txtNombre = view.findViewById(R.id.txtNombre);
        recyclerView = view.findViewById(R.id.rvMensajes);
        edMensaje = view.findViewById(R.id.edMensajes);
        btnMensaje = view.findViewById(R.id.btnMensajes);
        btnEnviarFoto = view.findViewById(R.id.btnImagenButton);
        fotoPerfilCadena = "";
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("chat");//sala de chat(nombre donde se guardan los mensajes)
        storage = FirebaseStorage.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference();


        adapter = new AdapterMensajes(inflater.getContext());
        LinearLayoutManager l = new LinearLayoutManager(inflater.getContext());
        recyclerView.setLayoutManager(l);
        recyclerView.setAdapter(adapter);

        txtNombre.setText(e.getEmail());

        //boton enviar un mensaje
        btnMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mensaje = edMensaje.getText().toString().trim();
                if (TextUtils.isEmpty(mensaje)) {
                    edMensaje.setError("El mensaje esta vacío");
                    return;
                }

                databaseReference.push().setValue(new MensajeEnviar(edMensaje.getText().toString(), txtNombre.getText().toString(),
                        fotoPerfilCadena, "1", ServerValue.TIMESTAMP));
                edMensaje.setText("");
            }
        });
        //btn enviar una foto
        /*btnEnviarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/jpeg");
                i.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                startActivityForResult(Intent.createChooser(i,"SELECCIONA UNA FOTO"),1);
            }
        });*/
       /* btnEnviarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickcapturar=1;
                AlertDialog.Builder alerta = new AlertDialog.Builder(view.getContext());
                alerta.setTitle("Seleccione aplicacion");
                alerta.setMessage("' PETS TEAM ' quiere añadir una foto: ");
                alerta.setCancelable(false);
                alerta.setPositiveButton("Hacer foto", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        /*SOLICTUD DE PERMISOS PARA ACCEDER A LA CÁMARA DEL DISPOSITIVO MÓVIL Manifest.permission.CAMERA*/
                        /*SOLICITUD DE PERMISOS PARA ACCEDER AL ALMACENAMIENTO INTERNO DEL MÓVIL PARA GUARDAR LA FOTO: Manifest.permission.WRITE_EXTERNAL_STORAGE*/
                        /*SE COMPRUEBA SI LOS PERMISOS ESTÁN CONCEDIDOS*/
                        /*if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            /*EN CASO DE QUE NO ESTÉN CONCEDIDOS SE DEBEN PEDIR AL USUARIO*/
                            /*ESTE MÉTODO DE SOLICITUD DE PERMISOS TIENE ASOCIADO UN EVENTO, PARA COMPROBAR SI EL USUARIO HA APROBADO O NO LOS PERMISOS*/
                         /*   ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);        // Permission is not granted
                        }
                    }
                });
                alerta.setNegativeButton("Seleccionar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent =  new Intent(Intent.ACTION_PICK);
                        intent.setType("imagen2/*");
                        startActivityForResult(intent, 1);
                    }
                });
                alerta.show();
            }
        });*/

        //cambiar foo de perfil
       /* imgPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/jpeg");
                i.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                startActivityForResult(Intent.createChooser(i,"SELECCIONA UNA FOTO"),1);
            }
        });*/

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                setScrollbar();
            }
        });

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MensajeRecibir m = snapshot.getValue(MensajeRecibir.class);
                adapter.addMensaje(m);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    private void setScrollbar() {
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CODIGO_SELECCION_IMAGEN && resultCode == RESULT_OK) {
            Uri u = data.getData();
            storageReference = storage.getReference("imagenes_chat");//imagenes chat
            final StorageReference fotoRef = storageReference.child(u.getLastPathSegment());
            fotoRef.putFile(u).addOnSuccessListener((Executor) this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> u = taskSnapshot.getStorage().getDownloadUrl();

                    }
                }
            });
*/
    
}


