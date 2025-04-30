package com.boatit.boatsharing.ui.voyager.dashbaord.view
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.RestrictionsManager.RESULT_ERROR
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.application.StripeSheetActivity
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.ui.voyager.dashbaord.model.BookedVoyage
import com.boatit.boatsharing.ui.voyager.dashbaord.model.CancelBookedVoyages
import com.boatit.boatsharing.ui.voyager.dashbaord.model.ConfirmBookedVoyages
import com.boatit.boatsharing.ui.voyager.dashbaord.model.PaymentConfirmationRequest
import com.boatit.boatsharing.ui.voyager.dashbaord.model.SponsorVoyagePaymentRequest
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.CancelBookedVoyageViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.ConfirmBookedVoyageViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.SponsorPaymentConfirmationViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.SponsorPaymentSheetConfigViewModel
import com.boatit.boatsharing.utils.AppConstants
import org.koin.androidx.compose.koinViewModel


@Composable
fun FutureVoyagerItems(navController: NavController, notification : BookedVoyage?,
                       viewModelConfirm: ConfirmBookedVoyageViewModel = koinViewModel(),
                       viewModelCancel: CancelBookedVoyageViewModel = koinViewModel(),
                       viewModelStripe: SponsorPaymentSheetConfigViewModel = koinViewModel(),
                       viewModelP: SponsorPaymentConfirmationViewModel = koinViewModel()

) {

    val ConfirmState by viewModelConfirm.nearbyPlaces.collectAsState()
    val stripeState by viewModelStripe.loginState.collectAsState()
    val paymentState by viewModelP.loginState.collectAsState()

    val context = LocalContext.current

    var loading by remember { mutableStateOf(false) }

    var showWaitingResponsePrompt by rememberSaveable { mutableStateOf(false) }
    var showConfirmBooking by rememberSaveable { mutableStateOf(false) }
    var showVoyageDetails by rememberSaveable { mutableStateOf(false) }
    var showFindBoat by rememberSaveable { mutableStateOf(false) }

    var paymentIntentClientSecret by remember { mutableStateOf<String?>(null) }
    var publishableKey by remember { mutableStateOf<String?>(null) }
    var id by remember { mutableStateOf<String?>(null) }
    var PaymentIntentid by remember { mutableStateOf<String?>(null) }
    var ephemeralKeySecret by remember { mutableStateOf<String?>(null) }

    val stripeLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ){  result: ActivityResult ->
        if(result.resultCode == RESULT_OK) {
            Toast.makeText(context, "Payment Successfull", Toast.LENGTH_LONG).show()
            viewModelP.payment(
                PaymentConfirmationRequest(
                    AppConstants.Voyage_ID!!,
                    PaymentIntentid!!,
                    ""
                )
            )
        }else if(result.resultCode == RESULT_CANCELED){
        }else if(result.resultCode == RESULT_ERROR){

        }else{ }
    }

    when (stripeState) {
        is NetworkResponse.Success -> {
            if (showWaitingResponsePrompt) {
                showWaitingResponsePrompt = false
                showConfirmBooking = false
                showVoyageDetails = false
                showFindBoat = false
                paymentIntentClientSecret = stripeState.data?.obj?.ClientSecret
                id = stripeState.data?.obj?.CustomerId
                ephemeralKeySecret = stripeState.data?.obj?.EphemeralKey_Secret
                publishableKey = stripeState.data?.obj?.PublishableKey
                PaymentIntentid = stripeState.data?.obj?.PaymentIntentId
                viewModelStripe.resetNearbyPlaces()
                val intent = Intent(context, StripeSheetActivity::class.java)
                intent.putExtra("publishableKey", publishableKey)
                intent.putExtra("ClientSecret", paymentIntentClientSecret)
                intent.putExtra("customerId", id)
                intent.putExtra("ephemeralKey", ephemeralKeySecret)
                stripeLauncher.launch(intent)
            }
        }
        is NetworkResponse.Error -> {
            if (showWaitingResponsePrompt) {
                showWaitingResponsePrompt = false
                showConfirmBooking = false
                Toast.makeText(context, stripeState.message, Toast.LENGTH_SHORT).show()
            }
        }
        else -> {}
    }

    when (ConfirmState) {
        is NetworkResponse.Success -> {}
        is NetworkResponse.Error -> {
            if(ConfirmState.message.equals("An error occurred: Please Pay Completely first then you can confirm your voyage"))
            {
                Toast.makeText(context, ConfirmState.message, Toast.LENGTH_SHORT).show()
                viewModelStripe.paymentConfig(SponsorVoyagePaymentRequest(notification?.Id!!,
                    AppConstants.USER_ID.toString(),""))
            }
            else{
                Toast.makeText(context, ConfirmState.message, Toast.LENGTH_SHORT).show()
            }
            viewModelConfirm.resetNearbyPlaces()
        }
        else -> {}
    }

    when (paymentState) {
        is NetworkResponse.Success -> {
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        }
        is NetworkResponse.Error -> {
            Toast.makeText(context, paymentState.message, Toast.LENGTH_SHORT).show()

        }
        else -> {}
    }

    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(
                    top = 50.dp,
                    start = 16.dp, end = 16.dp, bottom = 16.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Boat Info Card
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column() {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Normal
                            ),
                            text = "Sunday, 12 April | 10:00 am"
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.pending),
                                contentDescription = "Status Icon",
                                modifier = Modifier
                                    .size(25.dp) // Adjust size as needed
                                    .padding(end = 5.dp), // Add some space between text and icon
                                tint = Color.Blue // Change color of the icon
                            )
                            Text(
                                style = TextStyle(
                                    color = Color(0xFF797979),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.W500
                                ),
                                text = "Pending"
                            )

                        }
                    }

                    Spacer(Modifier.height(7.dp))

                    Text(
                        style = TextStyle(
                            color = Color(0xFF6A6969),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.W500
                        ),
                        text = "Event Conference"
                    )
                    Spacer(Modifier.height(7.dp))
                    Text(
                        style = TextStyle(
                            color = Color(0xFF6A6969),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.W500
                        ),
                        text = "2025"
                    )

                    Spacer(Modifier.height(10.dp))

                    Text(
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500
                        ),
                        text = "Voyagees details"
                    )
                    Spacer(Modifier.height(10.dp))

                    Box(
                        Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Card(
                                modifier = Modifier
                                    .padding(5.dp)
                                    .height(205.dp)
                                    .width(135.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxSize(),
                                    verticalArrangement = Arrangement.SpaceEvenly // Ensures space is even between the rows
                                ) {
                                    // First row with icon and text
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.passengers),
                                            contentDescription = "Status Icon",
                                            modifier = Modifier
                                                .size(30.dp)
                                                .padding(end = 10.dp),
                                            tint = Color.Blue
                                        )
                                        Text(
                                            style = TextStyle(
                                                color = Color.Black,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.W500
                                            ),
                                            text = "12"
                                        )
                                    }

                                    Divider(
                                        color = Color(0xFFA0A0A0),
                                        thickness = 1.dp
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.money_icon),
                                            contentDescription = "Status Icon",
                                            modifier = Modifier
                                                .size(30.dp)
                                                .padding(end = 10.dp),
                                            tint = Color.Blue
                                        )
                                        Text(
                                            style = TextStyle(
                                                color = Color.Black,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.W500
                                            ),
                                            text = notification?.AmountToPay.toString()
                                        )
                                    }

                                    Divider(
                                        color = Color(0xFFA0A0A0),
                                        thickness = 1.dp
                                    )

                                    // Third row with icon and text
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.clock),
                                            contentDescription = "Status Icon",
                                            modifier = Modifier
                                                .size(30.dp)
                                                .padding(end = 10.dp),
                                            tint = Color.Blue
                                        )
                                        Text(
                                            style = TextStyle(
                                                color = Color.Black,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.W500
                                            ),
                                            text = "12 PM"
                                        )
                                    }
                                }
                            }

                            Spacer(Modifier.width(5.dp))

                            Card(
                                modifier = Modifier
                                    .padding(5.dp)
                                    .height(205.dp)
                                    .width(175.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxSize(),
                                    verticalArrangement = Arrangement.SpaceEvenly // Ensures space is even between the rows
                                ) {
                                    // First row with icon and text
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.location_icon),
                                            contentDescription = "Status Icon",
                                            modifier = Modifier
                                                .size(30.dp)
                                                .padding(end = 10.dp),
                                            tint = Color.Blue
                                        )
                                        Text(
                                            style = TextStyle(
                                                color = Color.Black,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.W500
                                            ),
                                            text = notification?.PickupDock!!
                                        )
                                    }

                                    Divider(
                                        color = Color(0xFFA0A0A0),
                                        thickness = 1.dp
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.drop_off_loc_icon),
                                            contentDescription = "Status Icon",
                                            modifier = Modifier
                                                .size(30.dp)
                                                .padding(end = 10.dp),
                                            tint = Color.Red

                                        )
                                        Text(
                                            style = TextStyle(
                                                color = Color.Black,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.W500
                                            ),
                                            text = notification?.DropOffDock!!
                                        )
                                    }

                                    Divider(
                                        color = Color(0xFFA0A0A0),
                                        thickness = 1.dp
                                    )

                                    // Third row with icon and text
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.flag),
                                            contentDescription = "Status Icon",
                                            modifier = Modifier
                                                .size(30.dp)
                                                .padding(end = 10.dp),
                                            tint = Color.Blue
                                        )
                                        Text(
                                            style = TextStyle(
                                                color = Color.Black,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.W500
                                            ),
                                            text = "No Time Spent"
                                        )
                                    }
                                }
                            }
                        }
                    }

                }

            }



            Spacer(modifier = Modifier.height(16.dp))



            Text(
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500
                ),
                text = "Sponsors"
            )

            Spacer(Modifier.height(5.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp) // Set a fixed height for the inner Card
                    .background(color = Color.White)
                    .padding(5.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp) // Add elevation
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Person Icon",
                            modifier = Modifier
                                .size(25.dp) // Adjust icon size
                                .clip(CircleShape) // Make the icon circular
                                .background(Color.Gray) // Optional: Add background color to the circle
                        )
                        Spacer(Modifier.width(5.dp))
                        Text(
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.W400
                            ),
                            text = "Myself"
                        )
                        Spacer(Modifier.width(170.dp))
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Person Icon",
                            tint = Color.Green,
                            modifier = Modifier
                                .size(18.dp) // Adjust icon size
                                .clip(CircleShape) // Make the icon circular

                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp)) // Space between rows

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Phone Icon",
                            modifier = Modifier
                                .size(25.dp) // Adjust icon size
                                .clip(CircleShape) // Make the icon circular
                                .background(Color.Gray) // Optional: Add background color to the circle
                        )
                        Spacer(Modifier.width(5.dp))

                        Text(
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.W400
                            ),
                            text = "Chadwick"
                        )
                        Spacer(Modifier.width(150.dp))
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Person Icon",
                            tint = Color.Gray,
                            modifier = Modifier
                                .size(25.dp) // Adjust icon size
                                .clip(CircleShape) // Make the icon circular

                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp)) // Space between rows

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Email Icon",
                            modifier = Modifier
                                .size(25.dp) // Adjust icon size
                                .clip(CircleShape) // Make the icon circular
                                .background(Color.Gray) // Optional: Add background color to the circle
                        )
                        Spacer(Modifier.width(5.dp))


                        Text(
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.W400
                            ),
                            text = "Anderson"
                        )
                        Spacer(Modifier.width(150.dp))
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Person Icon",
                            tint = Color.Gray,
                            modifier = Modifier
                                .size(25.dp) // Adjust icon size
                                .clip(CircleShape) // Make the icon circular

                        )
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { viewModelCancel.fetchNearbyPlaces(CancelBookedVoyages(notification?.Id!!,""))
                              loading = true
                              },
                    shape = RoundedCornerShape(10.dp), // Corner radius
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .border(
                            width = 1.dp,
                            color = colorResource(id = R.color.button_normal), // Border color
                            shape = RoundedCornerShape(10.dp) // Apply same corner radius to border
                        ),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text(
                        text = "Cancel",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colorResource(id = R.color.button_normal) // Text color matches border
                    )
                }

                Button(
                    onClick = {
                        viewModelConfirm.fetchNearbyPlaces(ConfirmBookedVoyages(notification?.Id!!))
                        loading =true
                    },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(horizontal = 1.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.button_normal))
                ) {
                    Text(
                        text = "Confirm",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }
    }


}


