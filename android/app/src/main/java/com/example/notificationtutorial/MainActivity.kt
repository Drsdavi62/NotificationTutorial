package com.example.notificationtutorial

import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.notificationtutorial.utils.NotificationConstants
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val notificationId = 101
    private var largeText = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_button.setOnClickListener {
            sendNotification()
        }

        large_text_switch.setOnCheckedChangeListener { _, isChecked -> largeText = isChecked }

        val crashButton = Button(this)
        crashButton.text = "Crash!"
        crashButton.setOnClickListener {
            throw RuntimeException("Test Crash") // Force a crash
        }

        addContentView(crashButton, ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT))
    }

    private fun sendNotification() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(this, notificationId, intent, 0)

        val bitmap =
            BitmapFactory.decodeResource(applicationContext.resources, R.drawable.androidtwo)
        val bitmapLargeIcon =
            BitmapFactory.decodeResource(applicationContext.resources, R.drawable.steamtwo)

        val builder =
            NotificationCompat.Builder(this, NotificationConstants.CHANNEL_LOCAL_NOTIFICATION_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Example Title")
                .setContentText("Example Description")
                .setLargeIcon(bitmapLargeIcon)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        builder.setStyle(
            if (largeText) NotificationCompat.BigTextStyle().bigText(
                "Acima de tudo, ?? fundamental ressaltar que a execu????o dos pontos do programa representa uma abertura para a melhoria do investimento em reciclagem t??cnica. Nunca ?? demais lembrar o peso e o significado destes problemas, uma vez que o julgamento imparcial das eventualidades estimula a padroniza????o dos procedimentos normalmente adotados. Percebemos, cada vez mais, que a hegemonia do ambiente pol??tico estende o alcance e a import??ncia do sistema de participa????o geral. Do mesmo modo, o entendimento das metas propostas cumpre um papel essencial na formula????o das formas de a????o. Desta maneira, o desafiador cen??rio globalizado desafia a capacidade de equaliza????o das novas proposi????es.\n" +
                        "\n" +
                        "          Por outro lado, a revolu????o dos costumes promove a alavancagem das posturas dos ??rg??os dirigentes com rela????o ??s suas atribui????es. A pr??tica cotidiana prova que a percep????o das dificuldades possibilita uma melhor vis??o global das diretrizes de desenvolvimento para o futuro. No entanto, n??o podemos esquecer que o desenvolvimento cont??nuo de distintas formas de atua????o obstaculiza a aprecia????o da import??ncia dos conhecimentos estrat??gicos para atingir a excel??ncia. Podemos j?? vislumbrar o modo pelo qual a consulta aos diversos militantes oferece uma interessante oportunidade para verifica????o do remanejamento dos quadros funcionais. O incentivo ao avan??o tecnol??gico, assim como o in??cio da atividade geral de forma????o de atitudes talvez venha a ressaltar a relatividade do processo de comunica????o como um todo.\n"
            )
            else NotificationCompat.BigPictureStyle().bigPicture(bitmap)
        )

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, builder.build())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == notificationId) {
            Toast.makeText(this, "LOCAL", Toast.LENGTH_LONG).show()
        }
    }
}