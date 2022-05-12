package mx.tecnm.ladm_u4_ejercicio1_smspermisos_18401083

import android.app.Activity
import android.app.AlertDialog
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import mx.tecnm.ladm_u4_ejercicio1_smspermisos_18401083.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val siPermiso = 1
    val siPermisoReceive = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, arrayOf(
                android.Manifest.permission.RECEIVE_SMS
            ),siPermisoReceive)
        }

        binding.btnEnviar.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_DENIED){
                ActivityCompat.requestPermissions(this, arrayOf(
                    android.Manifest.permission.SEND_SMS
                ),siPermiso)
            }
            else{
                envioSMS()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == siPermiso){
            envioSMS()
        }
        if (requestCode == siPermisoReceive){
            receiveSMS()
        }
    }

    private fun receiveSMS() {
        AlertDialog.Builder(this)
            .setMessage("Se otorgó recibir")
            .show()
    }

    private fun envioSMS() {
        //5556
        SmsManager.getDefault().sendTextMessage(binding.txtCelular.text.toString(),null,
            binding.txtMensaje.text.toString(),null,null)
        Toast.makeText(this,"Enviado el SMS",Toast.LENGTH_LONG).show()
    }

}