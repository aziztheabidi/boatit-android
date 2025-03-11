package com.boatit.boatsharing.ui.chat.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.routes.popBack

import com.boatit.boatsharing.uihelpers.CustomTopBar


import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController


@Composable
fun VoyagerPastVoyages(navController: NavController) {

    Scaffold(
        topBar = {
            CustomTopBar(text = "Past Voyages", onImageClick = {
                println("clicked...")
                navController.popBack()
            })
        },
        containerColor = Color.White,
        content = { innerPadding ->

            Spacer(Modifier.height(20.dp))
            PastVoyagesList(innerPadding )
        },

        )
}


@Composable
fun PastVoyagesList(padding: PaddingValues) {

    LazyColumn(
        modifier = Modifier.padding(padding) // Apply padding to the LazyColumn
    ) {
        items(3) {
            PastVoyagesCard()
        }
    }
}
@Composable
fun PastVoyagesCard() {


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {

        Box {
            Icon(
                imageVector = Icons.Default.Place,
                contentDescription = "Map Pin",
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.Center)
                    .offset(x = (-10).dp, y = (-10).dp)
                    .alpha(0.3f),
                tint = Color.LightGray
            )
            Column(modifier = Modifier.padding(16.dp)) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Captain Name", fontWeight = FontWeight.Bold, fontSize = 18.sp)

                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = "$320", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)

                        Text(text = "18 Feb, 2025 12:00 pm", fontSize = 12.sp)

                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.passengers),
                        contentDescription = "Icon",
                        modifier = Modifier.size(20.dp),
                        tint = colorResource(R.color.button_normal)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "12 Past Voyages", fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.width(10.dp))


                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.location_icon),
                        contentDescription = "Icon",
                        modifier = Modifier.size(20.dp),
                        tint = colorResource(R.color.button_normal)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Long Island, NY", fontSize = 16.sp)
                }


                Spacer(modifier = Modifier.width(10.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.drop_off_loc_icon),
                        contentDescription = "Icon",
                        modifier = Modifier.size(20.dp),
                        tint = Color.Unspecified

                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Intercoastal Waterway", fontSize = 16.sp)
                }


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {

                        Text(text = "Status: Completed/Canceled", fontSize = 18.sp, fontWeight = FontWeight.Medium, fontStyle = FontStyle.Italic)

                }

            }

        }
    }


}


@Preview
@Composable
fun PreviewVoyagerPastVoyages() {
    VoyagerPastVoyages(navController = rememberNavController())
}