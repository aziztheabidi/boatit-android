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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.application.StripeSheetActivity
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.ui.voyager.dashbaord.model.PaymentConfirmationRequest
import com.boatit.boatsharing.ui.voyager.dashbaord.model.SponsorVoyagePaymentRequest
import com.boatit.boatsharing.ui.voyager.dashbaord.model.SponsorVoyagerPayment
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.FindBoatViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.PaymentSheetConfigViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.SponsorPaymentConfirmationViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.SponsorPaymentSheetConfigViewModel
import com.boatit.boatsharing.utils.AppConstants
import org.koin.androidx.compose.koinViewModel


@Composable
fun SponsorVoyagerItems(navController: NavController, notification : SponsorVoyagerPayment?,
                        viewModelStripe: SponsorPaymentSheetConfigViewModel = koinViewModel(),
                        viewModelP: SponsorPaymentConfirmationViewModel= koinViewModel()
) {
    var paymentIntentClientSecret by remember { mutableStateOf<String?>(null) }
    var publishableKey by remember { mutableStateOf<String?>(null) }
    var id by remember { mutableStateOf<String?>(null) }
    var PaymentIntentid by remember { mutableStateOf<String?>(null) }
    var ephemeralKeySecret by remember { mutableStateOf<String?>(null) }

    var showWaitingResponsePrompt by rememberSaveable { mutableStateOf(false) }
    var showConfirmBooking by rememberSaveable { mutableStateOf(false) }
    var showVoyageDetails by rememberSaveable { mutableStateOf(false) }
    var showFindBoat by rememberSaveable { mutableStateOf(false) }

    val stripeState by viewModelStripe.loginState.collectAsState()

    val context = LocalContext.current

    val stripeLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ){  result: ActivityResult ->
        if(result.resultCode == RESULT_OK) {
            Toast.makeText(context, "Payment Successfull", Toast.LENGTH_LONG).show()
            viewModelP.payment(
                PaymentConfirmationRequest(
                notification?.Id!!,
                "pi_3RJadkIiYO00MT0y1BsUCY5v",
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
                                            text = notification?.NoOfVoyagers.toString()
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
                                            text = notification?.VoyagerPhoneNumber.toString()
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

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {  },
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
                        text = "Ignore",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colorResource(id = R.color.button_normal) // Text color matches border
                    )
                }

                Button(
                    onClick = {
                        showWaitingResponsePrompt = true
                        viewModelStripe.paymentConfig(SponsorVoyagePaymentRequest(notification?.Id!!,"",AppConstants.USER_ID.toString()))
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
                        text = stringResource(R.string.pay_now_text),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }
    }


}


@Preview
@Composable
fun previewSponsorVoyageItems() {
    ConfirmBooking(
        navController = rememberNavController(),
        onCancelClick = {},
        onPayNowClick = {}
    )
}