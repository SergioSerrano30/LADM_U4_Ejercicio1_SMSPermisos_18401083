package mx.tecnm.ladm_u4_ejercicio1_smspermisos_18401083

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.SmsMessage
import android.widget.Toast

class SmsReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, data: Intent) {
        val extras = data.extras

        if (extras !=null){
            val sms = extras.get("pdus") as Array<Any>
            for (indice in sms.indices){
                val formato = extras.getString("format")
                val smsMensaje =  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    SmsMessage.createFromPdu(sms[indice] as ByteArray,formato)
                }else{
                    SmsMessage.createFromPdu(sms[indice] as ByteArray)
                }
                var celularOrigen = smsMensaje.originatingAddress
                var contenidoSMS = smsMensaje.messageBody.toString()
                AlertDialog.Builder(context)
                    .setMessage("Entró mensaje: \n ${contenidoSMS}")
                    .show()
            }
        }
    }
}