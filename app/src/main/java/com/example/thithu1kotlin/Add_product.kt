package com.example.thithu1kotlin

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.navigation.NavController


@Composable
fun Add_product(viewmodel: Product_Viewmodel, navController: NavController) {
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var status by remember { mutableStateOf(false) }
    var img by remember { mutableStateOf("") }
    var context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color("#f0f0f0".toColorInt()))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Name") })
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            value = price,
            onValueChange = { price = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Price") })
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            value = description,
            onValueChange = { description = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Description") })
        TextField(
            value = img,
            onValueChange = { img = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Image") })
        Button(onClick = {
            try {
                if (name.isNotEmpty() && price.isNotEmpty() && description.isNotEmpty()) {
                    var newProduct = Product(
                        name = name,
                        price = price,
                        description = description,
                        status = status,
                        id = "",
                        img = img
                    )
                    if (newProduct != null) {
                        viewmodel.addProduct(newProduct)
                        Toast.makeText(context, "Da them $name", Toast.LENGTH_SHORT).show()
                        navController.navigate(Route.Home)
                    }
                }else{
                    Toast.makeText(context, "Khong de trong du lieu", Toast.LENGTH_SHORT).show()
                }
            }catch (e:Exception){
                Log.d("Error",e.message.toString())
            }
        }) {
            Text(text = "ADD")
        }
    }
}
