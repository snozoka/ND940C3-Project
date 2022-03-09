package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    lateinit var radioButtonGroup: RadioGroup
    private var complete = false
    private val NOTIFICATION_ID = 0
    private val REQUEST_CODE = 0
    private val FLAGS = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        //Variables to pass to Detail Activity
        val fileName: String
        val fileStatus: String

        radioButtonGroup = findViewById(R.id.downloadSourceRadioButtonGroup)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
//        createChannel(
//            getString(R.string.download_notification_channel_id),
//            getString(R.string.download_notification_channel_name)
//        )




        custom_button.setOnClickListener {

            if (radioButtonGroup.getCheckedRadioButtonId() == -1)
            {
                custom_button.itemSelected(complete)
                Toast.makeText(applicationContext,"Please select the file to download", Toast.LENGTH_SHORT).show()
            }
            else
            {
                // one of the radio buttons is checked
                complete = true
                custom_button.itemSelected(complete)
                download()


            }


        }

    }
    private fun createChannel(channelId: String, channelName: String) {
        // TODO: Step 1.6 START create a channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                // TODO: Step 2.4 change importance
                NotificationManager.IMPORTANCE_LOW
            )
            // TODO: Step 2.6 disable badges for this channel

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Download Udacity Teachings"

            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }


    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if(id == downloadID){
                custom_button.hasCompletedDownload()

                // get selected radio button from radioGroup
                val selectedId = radioButtonGroup.getCheckedRadioButtonId()

                // find the radiobutton by returned id
                var radioButton = findViewById<RadioButton>(selectedId)

                //Create Detail Activity intent
                val contentIntent = Intent(applicationContext, DetailActivity::class.java)
                contentIntent.putExtra("FileName", radioButton.text).putExtra("Status", "Success")

                //Create a pendingIntent
                pendingIntent = PendingIntent.getActivity(
                    applicationContext,
                    NOTIFICATION_ID,
                    contentIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
                // Check Status intent
                val checkStatusIntent = Intent(applicationContext, DetailActivity::class.java)
                val checkStatusPendingIntent: PendingIntent = PendingIntent.getBroadcast(
                    applicationContext,
                    REQUEST_CODE,
                    checkStatusIntent,
                    FLAGS
                )

                notificationManager = ContextCompat.getSystemService(
                    application,
                    NotificationManager::class.java
                ) as NotificationManager

                val builder = NotificationCompat.Builder(
                    applicationContext,
                    applicationContext.getString(R.string.download_notification_channel_id)
                )
                    //Set title, text and icon to builder
                    .setSmallIcon(R.drawable.ic_assistant_black_24dp)
                    .setContentTitle(applicationContext
                        .getString(R.string.notification_title))
                    .setContentText(getText(R.string.notification_description))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    //Add check status action
                    .addAction(
                        R.drawable.ic_assistant_black_24dp,
                        applicationContext.getString(R.string.check_download_status),
                        checkStatusPendingIntent
                    )
                notificationManager.notify(NOTIFICATION_ID, builder.build())

                createChannel(
                    getString(R.string.download_notification_channel_id),
                    getString(R.string.download_notification_channel_name)
                )
            }
        }
    }

    private fun download() {
        val request =
            DownloadManager.Request(Uri.parse(URL))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
        val myDownloadQuery = DownloadManager.Query()
        //set the query filter to our previously Enqueued download
        myDownloadQuery.setFilterById(downloadID)
        var cursor = downloadManager.query(myDownloadQuery)
        //column for status
        if(cursor.moveToFirst()){
            val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
            val status = cursor.getInt(columnIndex)
            Log.i("Column Status Index:", status.toString())
            Log.i("Column Status String:", DownloadManager.COLUMN_STATUS)

            //get the download filename
            val filename =
                cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))
                    .replace("file://", "")
            //val filename = cursor.getString(filenameIndex)
            Log.i("File Name:", filename)
            Log.i("Column title String:", DownloadManager.COLUMN_TITLE)
        }
        else Log.i("Error in Cursor:", "Cursor is out of bounds")
    }

    companion object {
        private const val URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
    }

}
