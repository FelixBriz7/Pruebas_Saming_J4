package com.example.aplicacion_para_samsug_j4

import android.Manifest
import android.app.ComponentCaller
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.aplicacion_para_samsug_j4.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding;
    private var telefonoSos: String? = null;

    //Lanzador para pedir un permiso a la aplicacion

    private lateinit var requesPermissionLauncher: ActivityResultLauncher<String>;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);

        mainBinding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(mainBinding.root);

        //Esta funcion espera a la raccion del usuario, es decir si decide dar permisos o no

        registerLauncher();

        if (!tienePermisoLLamada()) {
            requesPermissionLauncher.launch(Manifest.permission.CALL_PHONE);
        }

        mainBinding.mainBtnSos.setOnClickListener {
            if (tienePermisoLLamada()) {
                llamar();
            } else {
                requesPermissionLauncher.launch(Manifest.permission.CALL_PHONE);
            }
        }

    }

    override fun onResume() {
        super.onResume()
        telefonoSos = intent.getStringExtra("phone");
        mainBinding.mainTxtPhone.text = telefonoSos;
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent);
    }

    // Registra que hacer cuando el usuario pulse Permitir o Dengegar

    private fun registerLauncher() {
        requesPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                Toast.makeText(this, "Permiso concedido, pulsa SOS", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Necesitas habilitar los permisos", Toast.LENGTH_LONG).show()
            }
        }

    }

    //En caso de denegacion por parte del usuario en las llamada

    private fun tienePermisoLLamada(): Boolean{
        return ContextCompat.checkSelfPermission(
            this, Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED;
    }

    private fun llamar(){
        val telefono = telefonoSos;

        if (telefono == null){
            Toast.makeText(this, "No hay teléfono configurado", Toast.LENGTH_SHORT).show();
            return
        }
        val  intent = Intent(Intent.ACTION_CALL);
        intent.data = Uri.parse("tel:$telefono");
        startActivity(intent);
    }











}