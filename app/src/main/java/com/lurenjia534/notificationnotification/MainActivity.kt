package com.lurenjia534.notificationnotification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.lurenjia534.notificationnotification.ui.theme.NotificationNotificationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotificationNotificationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primary
                ) {
                    AppUI()
                }
            }
        }
    }
}

// class NotificationReceiver :

fun saveNotification(context: Context,title: String,message: String){
    val sharedPreferences = context.getSharedPreferences("NotificationPrefs",Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("title",title)
    editor.putString("message",message)
    editor.apply()
}
fun sendNotification(context: Context, message: String,title: String) {

    saveNotification(context, title, message)

    val deleteIntent = PendingIntent.getBroadcast(
        context,
        0,
        Intent(context,NotificationReceiver::class.java),
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val notificationManager = ContextCompat.getSystemService(
        context, NotificationManager::class.java
    ) as NotificationManager

    // 创建一个通知通道（在 Android Oreo 及以上版本中必须这样做）
    val channelId = "Notice_Memo"
    val channelName = "cno_Notice_Memo"
    val channel = NotificationChannel(
        channelId, channelName, NotificationManager.IMPORTANCE_HIGH
    )
    notificationManager.createNotificationChannel(channel)

    // 构建通知
    val notification = NotificationCompat.Builder(context, channelId)
        .setContentTitle(title)
        .setContentText(message)
        .setSmallIcon(R.drawable.ic_launcher_foreground) // 设置通知图标
        .setPriority(NotificationCompat.PRIORITY_HIGH) // 设置优先级
        .setOngoing(true) // 使通知成为持久性通知
        .setDeleteIntent(deleteIntent)
        .build()

    // 显示通知
    notificationManager.notify(1001, notification)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun AppUI(){


    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
       Column {
           OutlinedCard(modifier = Modifier
               .width(380.dp)
               .height(100.dp)) {
               Row {
                   Spacer(modifier = Modifier.width(45.dp))
                   Text(text = "👏 Welcome back Cno!", fontSize = 25.sp, fontWeight = FontWeight.Bold )
               }
               Spacer(modifier = Modifier.height(25.dp))
               Row {
                   Spacer(modifier = Modifier.width(75.dp))
                   Text(text = "❤ Is everything okay? Right!", fontSize = 15.sp, fontWeight = FontWeight.Light )
               }
           }
           Spacer(modifier = Modifier.height(50.dp))
           var text by remember { mutableStateOf(TextFieldValue("")) }
           var text2 by remember { mutableStateOf(TextFieldValue("")) }
           OutlinedCard(modifier = Modifier
               .height(280.dp)
               .width(380.dp), colors = CardDefaults.outlinedCardColors()) {
               Spacer(modifier = Modifier.height(50.dp))
               Row {
                   Spacer(modifier = Modifier.width(50.dp))
                   OutlinedTextField(value = text2, onValueChange = { newText -> text2 = newText },
                       label = { Text("message header") },
                       leadingIcon = { Icon(imageVector = Icons.Outlined.Create, contentDescription = null )},
                       colors = TextFieldDefaults.outlinedTextFieldColors(
                           focusedBorderColor = Color.Gray, // 获得焦点时的边框颜色
                           unfocusedBorderColor = Color.Gray // 未获得焦点时的边框颜色
                       )
                   )
               }
               Spacer(modifier = Modifier.height(15.dp))
               Row {
                   Spacer(modifier = Modifier.width(50.dp))
                   OutlinedTextField(value = text, onValueChange = { newText -> text = newText },
                       label = { Text("notification")},
                       leadingIcon = { Icon(imageVector = Icons.Default.Notifications, contentDescription = null) },
                       colors = TextFieldDefaults.outlinedTextFieldColors(
                           focusedBorderColor = Color.Gray, // 获得焦点时的边框颜色
                           unfocusedBorderColor = Color.Gray // 未获得焦点时的边框颜色
                       )
                   )
               }
               Spacer(modifier = Modifier.height(20.dp))
               Row {
                  Spacer(modifier = Modifier.width(130.dp))
                   val context = LocalContext.current
                   Button(onClick = {
                       sendNotification(context,text.text,text2.text)
                   }) {
                       Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = null)
                       Spacer(modifier = Modifier
                           .width(5.dp)
                           .height(10.dp))
                       Text(text = "Send!", fontWeight = FontWeight.Bold)
                   }
               }
           }
       }
    }
}