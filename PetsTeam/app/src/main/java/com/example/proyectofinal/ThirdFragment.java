package com.example.proyectofinal;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class ThirdFragment extends Fragment {

    Uri imagen;
    ImageView vista;
    ImageButton capturar;
    Button subir;
    EditText titulo, desc, precio, talla;
    int clickcapturar=0;
    Email e= new Email();
    DatabaseReference mDatabase;
    StorageReference mStorage;
    Producto p;
    ProgressDialog bprogress;
    Uri downloadUri;
    private FirebaseAuth firebaseAuth;

    public ThirdFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_third, container, false);

        capturar = (ImageButton) view.findViewById(R.id.b_foto);
        vista = (ImageView) view.findViewById(R.id.imag_vista);
        titulo = (EditText) view.findViewById(R.id.txt_titulo);
        desc = (EditText) view.findViewById(R.id.txt_des);
        precio = (EditText) view.findViewById(R.id.txt_precio);
        talla = (EditText) view.findViewById(R.id.txt_talla);
        subir = (Button) view.findViewById(R.id.b_subir);

        bprogress = new ProgressDialog(view.getContext());

        //PASAMOS EL EMAIL DEL LOGIN
        TextView a = (TextView) view.findViewById(R.id.txtmail2);
        String mostrarmail = e.getEmail();
        a.setText(mostrarmail);

        capturar.setOnClickListener(new View.OnClickListener() {
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
                        if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            /*EN CASO DE QUE NO ESTÉN CONCEDIDOS SE DEBEN PEDIR AL USUARIO*/
                            /*ESTE MÉTODO DE SOLICITUD DE PERMISOS TIENE ASOCIADO UN EVENTO, PARA COMPROBAR SI EL USUARIO HA APROBADO O NO LOS PERMISOS*/
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);        // Permission is not granted
                        } else {
                            /*Llamamos al método que hemos implementado para abrir la cámara*/
                            opencamera();
                        }
                    }
                });
                alerta.setNegativeButton("Seleccionar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent =  new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(intent, 1);
                    }
                });
                alerta.show();
            }
        });

        subir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String title = titulo.getText().toString().trim();
                final String des = desc.getText().toString().trim();
                final String price = precio.getText().toString().trim();
                final String size = talla.getText().toString().trim();

                if (TextUtils.isEmpty(title)) {
                    titulo.setError("El campo Nombre está vacío");
                    return;
                }
                if (des.length() < 10) {
                    desc.setError("Añade una descripción más larga");
                    return;
                }
                if (des.length() > 78) {
                    desc.setError("Descripción demasiado larga");
                    return;
                }
                if (TextUtils.isEmpty(size)) {
                    talla.setError("El campo Talla está vacío");
                    return;
                }

                if (TextUtils.isEmpty(price)) {
                    precio.setError("El campo Precio está vacío");
                    return;
                }

                if(clickcapturar==0) {
                    Toast.makeText(view.getContext(), "Debe subir una foto", Toast.LENGTH_SHORT).show();

                }else {
                    //INSERTAMOS
                    //para insertar la img
                    mStorage = FirebaseStorage.getInstance().getReference();
                    p = new Producto();
                    p.setId(UUID.randomUUID().toString());
                    p.setNombre(titulo.getText().toString().trim());
                    p.setDescripcion(desc.getText().toString().trim());
                    p.setTalla(talla.getText().toString().trim());
                    p.setPrecio(String.valueOf(precio.getText()));
                    p.setUsuario(e.getEmail());
                    p.setUrlFoto(downloadUri.toString());
                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    mDatabase.child("Producto").child(p.getId()).setValue(p);
                    Toast.makeText(view.getContext(), "¡Hecho!",  Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(view.getContext(), PaginaPrincipal.class);
                    startActivity(i);
                }
            }
        });

        return view;
    }

    private void opencamera() {
        ContentValues values = new ContentValues();

        /*El nombre de las imágenes se asignan automáticamente*/
        imagen = getActivity().getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        /*Abre la aplicación de la cámara. Se abre en un nuevo Intent*/
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        /*Se pasa a la cámara dónde debe guardar las imagenes que tome.*/
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imagen);
        /*Se inicia la aplicación cámara*/
        resultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        InputStream inputStream = null;
                        try {
                            inputStream = getActivity().getApplicationContext().getContentResolver().openInputStream(imagen);
                            bprogress.setTitle("Subiendo...");
                            bprogress.setCancelable(false);
                            bprogress.show();
                            mStorage = FirebaseStorage.getInstance().getReference();
                            StorageReference filePath = mStorage.child("fotos").child(imagen.getLastPathSegment());
                            filePath.putFile(imagen).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    bprogress.dismiss();
                                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                    while(!uriTask.isSuccessful());
                                    downloadUri = uriTask.getResult();
                                }
                            });
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        vista.setImageURI(imagen);
                    }
                }
            });

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        clickcapturar = 1;
        if(resultCode == RESULT_OK){
            bprogress.setTitle("Subiendo...");
            bprogress.setCancelable(true);
            bprogress.show();
            imagen = data.getData();
            mStorage = FirebaseStorage.getInstance().getReference();
            StorageReference filePath = mStorage.child("fotos").child(imagen.getLastPathSegment());
            filePath.putFile(imagen).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    bprogress.dismiss();
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while(!uriTask.isSuccessful());
                    downloadUri = uriTask.getResult();

                }
            });
            vista.setImageURI(imagen);
        }
    }
}