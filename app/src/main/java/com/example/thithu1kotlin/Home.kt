package com.example.thithu1kotlin

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.graphics.toColorInt
import androidx.navigation.NavController
import coil.compose.AsyncImage

@Composable
fun Home(navController: NavController) {
    Column(
        modifier = Modifier.padding(top = 30.dp)
    ) {
        Button(onClick = {
            navController.navigate(Route.ADD)
        }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
        }
        ListProduct(viewmodel = Product_Viewmodel())
    }
}

@Composable
fun ListProduct(viewmodel: Product_Viewmodel) {
    val products = viewmodel._product.value
    LazyColumn(
    ) {
        items(products.size) { item ->
            ItemProduct(item = products[item], viewmodel)
        }
    }

    viewmodel.getProduct()
}

@Composable
fun ItemProduct(item: Product, viewmodel: Product_Viewmodel) {
    var selectedItem by remember { mutableStateOf<Product?>(null) }
    val openDialog = remember { mutableStateOf(false ) }
    val dialogDel = remember { mutableStateOf(false ) }
    val dialogUpdate = remember { mutableStateOf(false ) }
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable {
                selectedItem = item
                openDialog.value = true
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color("#f0f0f0".toColorInt()))
                .padding(10.dp)
                .clip(RoundedCornerShape(10.dp)),
            verticalArrangement = Arrangement.Center,
        ) {
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AsyncImage(model = item.img, contentDescription = null, modifier = Modifier
                        .width(100.dp)
                        .height(100.dp))
                    Column(
                        modifier = Modifier.padding(start = 10.dp)
                    ){
                        Text(text = item.name, color = Color.Black, fontSize = 20.sp)
                        Text(text = item.price, color = Color.Red, fontSize = 18.sp)
                    }
                }
                Text(text = "Delete" , color = Color.Red, fontSize = 18.sp, modifier = Modifier.clickable {
                    dialogDel.value = true
                })
                Text(text = "Edit" , color = Color.Green, fontSize = 18.sp, modifier = Modifier.clickable {
                    dialogUpdate.value = true
                })
            }
        }
    }
    if (dialogDel.value) {
        Dialog(onDismissRequest = { dialogDel.value }) {
            Card {
                Column(
                    modifier = Modifier
                        .width(320.dp)
                        .height(200.dp)
                        .padding(10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(text = "Are you sure you want to delete ${item.name}?", color = Color.Black, fontSize = 20.sp)
                    Row(
                        modifier = Modifier
                            .width(250.dp)
                            .height(100.dp)
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Button(onClick = {
                            viewmodel.deleteProduct(item)
                            viewmodel.getProduct()
                            dialogDel.value = false
                            Toast.makeText(context, "Delete ${item.name} success", Toast.LENGTH_SHORT).show()
                        }) {
                            Text(text = "Delete")
                        }
                        Button(onClick = { dialogDel.value = false }) {
                            Text(text = "Cancel")
                        }
                    }
                }
            }
        }
    }
    if (openDialog.value) {
        CustomDialog(selectedItem = selectedItem!!, openDialog = openDialog.value, click = {openDialog.value = false})
    }
    if (dialogUpdate.value) {
        var name by remember { mutableStateOf(item.name) }
        var price by remember { mutableStateOf(item.price) }
        var description by remember { mutableStateOf(item.description) }
        var img by remember { mutableStateOf(item.img) }
        Dialog(onDismissRequest = { dialogUpdate.value }) {
            Card {
                Column(
                    modifier = Modifier
                        .width(320.dp)
                        .height(400.dp)
                        .padding(10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(text = "Update product", color = Color.Black, fontSize = 20.sp)
                    TextField(value = name, onValueChange = { name = it })
                    TextField(value = price, onValueChange = { price = it })
                    TextField(value = description, onValueChange = { description = it })
                    TextField(value = img, onValueChange = { img = it })
                    Spacer(modifier = Modifier.height(10.dp))
                    Row {
                        Button(onClick = { dialogUpdate.value = false }){
                            Text(text = "Cancel")
                        }
                        Button(onClick = {
                            try {
                                if (name.isNotEmpty() && price.isNotEmpty() && description.isNotEmpty() && img.isNotEmpty()){
                                    viewmodel.updateProduct(Product(
                                        id = item.id,
                                        name = name,
                                        price = price,
                                        description = description,
                                        img = img,
                                        status = false
                                    ))
                                    dialogUpdate.value = false
                                    Toast.makeText(context, "Update success", Toast.LENGTH_SHORT).show()
                                }else{
                                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                                }
                            }catch (e: Exception){
                                Log.e("Error", e.message.toString())
                            }
                        }){
                            Text(text = "Update")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CustomDialog(selectedItem: Product, openDialog: Boolean, click: () -> Unit) {
    Dialog(onDismissRequest = { openDialog }) {
        Card {
            Column(
                modifier = Modifier
                    .width(200.dp)
                    .height(300.dp)
                    .padding(10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(model = selectedItem.img, contentDescription = null, modifier = Modifier.width(100.dp).height(100.dp))
                Text(text = selectedItem.name, color = Color.Black, fontSize = 20.sp)
                Text(text = selectedItem.price, color = Color.Red, fontSize = 18.sp)
                Text(text = selectedItem.description, color = Color.Black, fontSize = 17.sp)
                Text(
                    text = "${
                        when (selectedItem.status) {
                            true -> "Sản phẩm mới"
                            false -> "Sản phẩm cũ"
                            null -> TODO()
                        }
                    }", color = Color.Black, fontSize = 17.sp
                )
                Button(onClick = {
                    click()
                }) {
                    Text(text = "Đóng")
                }
            }
        }
    }
}