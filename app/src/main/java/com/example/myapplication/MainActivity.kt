package com.example.myapplication

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.myapplication.ui.theme.MyApplicationTheme
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.gson.Gson
import java.time.LocalDate


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val NvController = rememberNavController()
            NavHost(navController = NvController, startDestination = "mainScreen"){
                composable("mainScreen") {mainScreen(NvController)  }
                composable(route="nextScreen/{personJson}"){
                    Entry->val json = Entry.arguments?.getString("personJson")
                    val person= Gson().fromJson(json, Person::class.java)
                    nextScreen(NvController,person)
                }
            }
        }
    }
}
data class Person(
    val pName: String,
    val pYob: Int
)
@Composable
fun mainScreen(navControl: NavController){
    var txtName by remember { mutableStateOf("") }
    var txtYear by remember { mutableStateOf("") }
Column(modifier = Modifier.fillMaxSize().wrapContentHeight().padding(15.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy (15.dp )) {
    OutlinedTextField(
        label = {Text("Name")},
        value=txtName,
        onValueChange = {change -> txtName=change}
    )
    OutlinedTextField(
        label ={Text("Year of birth")},
        value=txtYear,
        onValueChange = {txtYear=it}
    )
    Button(onClick = {
        val person= Person(txtName.toString(),txtYear.toInt())
        val personJson=Uri.encode(Gson().toJson(person))

        navControl.navigate("nextscreen/$personJson")
    },
        colors = buttonColors(
            containerColor = Color(0xFF1565C0),   // background color
            contentColor = Color.White            // text (content) color
        )) { Text("Calculate Age")}
}
}
@Composable
fun nextScreen(nav: NavController, person: Person){
    val age:Int= LocalDate.now().year-person.pYob
            Column (modifier = Modifier.fillMaxSize().wrapContentHeight().padding(15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy (15.dp ))
            {
                    Text("Name:${person.pName}")
                    Text("Age:$age")
                    Button(
                            onClick = {nav.popBackStack()},
                            colors = buttonColors(
                            containerColor = Color(0xFF1565C0),   // background color
                            contentColor = Color.White            // text (content) color
                    ))
                    {Text("Go back") }

    }

}
