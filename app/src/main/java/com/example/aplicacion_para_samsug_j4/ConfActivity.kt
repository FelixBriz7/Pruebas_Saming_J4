package com.example.aplicacion_para_samsug_j4

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aplicacion_para_samsug_j4.databinding.ActivityConfBinding
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import android.content.Intent


class ConfActivity : AppCompatActivity() {

    //Esta variable contiene los componentes básicos de Android para hacer
    // un programa
    private lateinit var confBinding: ActivityConfBinding;
    //Variable para guardar datos pequeños de otra app
    private lateinit var sharedFich: SharedPreferences;


    // Nombre del "cuaderno" y etiqueta del dato
    private val nameSharedFich = "shared_fich"
    private val nameSharedPhone = "phone_number"



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        //Funcion que hace que las vistas se muestren en pantalla
        confBinding = ActivityConfBinding.inflate(layoutInflater);
        setContentView(confBinding.root);

        /*
        Abrimos como el cuaderno privado de la app, es decir el fichero de la
        app que va a contener configuracion exclusiva de esta app
         */
        sharedFich = getSharedPreferences(nameSharedFich, Context.MODE_PRIVATE);


        //Revisa si existe un numero de tfn, en caso contrario devuelve null
        val telefonoGuardado : String? = sharedFich.getString(nameSharedPhone, null);
        telefonoGuardado?.let { numeroDeTelefono ->
            startMainActivity(numeroDeTelefono);
        }


        confBinding.confButton.setOnClickListener {
            val telefono = confBinding.confEditTextPhone1.text.toString();

            if (telefono.isEmpty()){
                Toast.makeText(this, "Debes de introducir un telefono", Toast.LENGTH_SHORT).show();

                //funcion que llamamos para verificar si es español, en caso contrario null

            }else if(!esTelefonoValido(telefono, "ES")){
                Toast.makeText(this, "Telefono no válido", Toast.LENGTH_SHORT).show();
            }else{
                sharedFich.edit().putString(nameSharedPhone,telefono).apply();
                startMainActivity(telefono);
            }

        }

    }

    /*
    Funcion con la que verificamos si el numero es valido o no
     */
    fun esTelefonoValido(telefono: String, pais: String): Boolean {
        val phoneUtil = PhoneNumberUtil.getInstance();
        return try {
            val numero = phoneUtil.parse(telefono,pais);
            phoneUtil.isValidNumber(numero);
        }catch (e: NumberParseException){
            false
        }

    }

    private fun startMainActivity(telefono: String) {
        val intent = Intent(this, MainActivity::class.java);
        intent.putExtra("phone", telefono);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }


}