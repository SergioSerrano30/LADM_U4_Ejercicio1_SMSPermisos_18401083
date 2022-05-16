package mx.tecnm.ladm_u4_ejercicio1_smspermisos_18401083

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.SmsMessage
import android.widget.Toast

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context, p1: Intent) {
        val extras = p1.extras

        if(extras!=null){
            val sms = extras.get("pdus") as Array<Any>
            for(indice in sms.indices){
                val formato = extras.getString("format")

                val mensajeSMS = if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    SmsMessage.createFromPdu(sms[indice] as ByteArray,formato)
                } else {
                    SmsMessage.createFromPdu(sms[indice] as ByteArray)
                }

                val celularOrigen = mensajeSMS.originatingAddress
                val contenidoSMS = mensajeSMS.displayMessageBody

                Toast.makeText(p0,"ORIGEN: ${celularOrigen}, TEXTO:\n${contenidoSMS}",
                    Toast.LENGTH_LONG).show()

            }
        }

    }
}