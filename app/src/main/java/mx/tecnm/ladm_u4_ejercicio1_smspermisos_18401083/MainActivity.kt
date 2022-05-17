package mx.tecnm.ladm_u4_ejercicio1_smspermisos_18401083

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.FirebaseDatabaseKtxRegistrar
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import mx.tecnm.ladm_u4_ejercicio1_smspermisos_18401083.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val siPermiso = 1
    val siPermisoReceive = 2
    var listaIds = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //---------------Consulta en tiempo real---------------------------

        val consulta = Firebase.database.getReference().child("mensajes")
        val postListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var datos = ArrayList<String>()
                listaIds.clear()
                for(data in snapshot.children){
                    val id = data.key
                    listaIds.add(id!!)
                    val origen = data.getValue<User>()!!.origen
                    val mensaje = data.getValue<User>()!!.mensaje
                    datos.add("Origen: ${origen} \nMensaje: ${mensaje}")
                    mostrarLista(datos)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        }

        //-----------------------------------------------------------------
        consulta.addValueEventListener(postListener) //El start
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(
                android.Manifest.permission.RECEIVE_SMS
            ),siPermisoReceive)
        }

        binding.btnEnviar.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(
                    android.Manifest.permission.SEND_SMS
                ),siPermiso)
            }
            else{
                envioSMS(this)
            }
        }
        binding.lvLista.setOnItemClickListener { adapterView, view, pos, l ->
            var idSeleccionado = listaIds.get(pos)
            AlertDialog.Builder(this)
                .setMessage("¿Que deseas hacer?\nID: ${idSeleccionado}")
                .setNegativeButton("Eliminar"){d,i->
                    eliminar(idSeleccionado)
                }
                .setNeutralButton("Cancelar"){d,i->
                    //Nada
                }
                .show()
        }
    }

    private fun eliminar(idSeleccionado: String) {
        var baseDatos = Firebase.database.reference
        baseDatos.child("mensajes").child(idSeleccionado).removeValue()
        Toast.makeText(this,"Eliminado",Toast.LENGTH_LONG).show()
    }

    private fun mostrarLista(datos: ArrayList<String>) {
        binding.lvLista.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,datos)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == siPermiso){
            envioSMS(this)
        }
        if (requestCode == siPermisoReceive){
            Toast.makeText(this,"PERMISO DE RECIBIR",Toast.LENGTH_LONG)
                .show()
            //receiveSMS()
        }
    }

    private fun receiveSMS() {
        AlertDialog.Builder(this)
            .setMessage("Se otorgó recibir")
            .show()
    }

    private fun envioSMS(context: Context) {
        //5556
        SmsManager.getDefault().sendTextMessage(binding.txtCelular.text.toString(),null,
            binding.txtMensaje.text.toString(),null,null)
        Toast.makeText(this,"Enviado el SMS",Toast.LENGTH_LONG).show()
        binding.txtMensaje.text.clear()
        binding.txtCelular.text.clear()

    }

}