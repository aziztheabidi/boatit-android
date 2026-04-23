@file:Suppress(
    "ktlint:standard:function-naming",
    "ktlint:standard:property-naming",
    "ktlint:standard:curly-spacing",
    "ktlint:standard:no-line-break-after-else",
    "ktlint:standard:if-else-wrapping",
    "ktlint:standard:discouraged-comment-location",
)

package com.boatit.boatsharing.features.voyager.dashboard.view
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.boatit.boatsharing.R
import com.boatit.boatsharing.application.StripeSheetActivity
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.domain.core.BusinessErrorCodes
import com.boatit.boatsharing.domain.core.ErrorType
import com.boatit.boatsharing.features.voyager.dashboard.model.BookedVoyage
import com.boatit.boatsharing.features.voyager.dashboard.model.CancelBookedVoyages
import com.boatit.boatsharing.features.voyager.dashboard.model.ConfirmBookedVoyages
import com.boatit.boatsharing.features.voyager.dashboard.model.PaymentConfirmationRequest
import com.boatit.boatsharing.features.voyager.dashboard.model.SponsorVoyagePaymentRequest
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.CancelBookedVoyageViewModel
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.ConfirmBookedVoyageViewModel
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.SponsorPaymentConfirmationViewModel
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.SponsorPaymentSheetConfigViewModel
import com.boatit.boatsharing.ui.components.MissingPaymentDialog
import com.boatit.boatsharing.ui.components.SessionDialog
import com.boatit.boatsharing.data.local.prefmanager.UserSessionStore
import org.koin.androidx.compose.koinViewModel
import org.koin.java.KoinJavaComponent.get

@Composable
fun FutureVoyagerItems(
    navController: NavController,
    notification: BookedVoyage?,
    viewModelConfirm: ConfirmBookedVoyageViewModel = koinViewModel(),
    viewModelCancel: CancelBookedVoyageViewModel = koinViewModel(),
    viewModelStripe: SponsorPaymentSheetConfigViewModel = koinViewModel(),
    viewModelP: SponsorPaymentConfirmationViewModel = koinViewModel(),
    userSessionStore: UserSessionStore = get(UserSessionStore::class.java),
) {
    val confirmUi by viewModelConfirm.uiState.collectAsState()
    val ConfirmState = confirmUi.confirmationState
    val stripeVm by viewModelStripe.uiState.collectAsState()
    val stripeState = stripeVm.paymentSheetConfigState
    val paymentUi by viewModelP.uiState.collectAsState()
    val paymentState = paymentUi.networkState

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
    var showDialog by remember { mutableStateOf(false) }
    var showDialogForCancel by remember { mutableStateOf(false) }

    val stripeLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult(),
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                Toast.makeText(context, "Payment Done Please Confirm Your Voyage", Toast.LENGTH_LONG).show()
                viewModelP.payment(
                    PaymentConfirmationRequest(
                        notification?.Id.orEmpty(),
                        PaymentIntentid.orEmpty(),
                        "",
                    ),
                )
            } else if (result.resultCode == RESULT_CANCELED)
                {
                } else if (result.resultCode == RESULT_ERROR)
                {
                } else
                { }
        }

    when (stripeState) {
        is NetworkResponse.Success -> {
            paymentIntentClientSecret = stripeState.data?.obj?.ClientSecret
            id = stripeState.data?.obj?.CustomerId
            ephemeralKeySecret = stripeState.data?.obj?.EphemeralKey_Secret
            publishableKey = stripeState.data?.obj?.PublishableKey
            PaymentIntentid = stripeState.data?.obj?.PaymentIntentId
            viewModelStripe.resetPaymentSheetState()
            val intent = Intent(context, StripeSheetActivity::class.java)
            intent.putExtra("publishableKey", publishableKey)
            intent.putExtra("ClientSecret", paymentIntentClientSecret)
            intent.putExtra("customerId", id)
            intent.putExtra("ephemeralKey", ephemeralKeySecret)
            stripeLauncher.launch(intent)
        }
        is NetworkResponse.Error -> {
            Toast.makeText(context, stripeState.message, Toast.LENGTH_SHORT).show()
        }
        else -> {}
    }

    when (val confirmState = ConfirmState) {
        is NetworkResponse.Success -> {}
        is NetworkResponse.Error -> {
            val errType = confirmState.errorType
            val payIncomplete =
                errType is ErrorType.Validation &&
                    errType.field == BusinessErrorCodes.PAY_BEFORE_CONFIRM
            if (payIncomplete) {
                showDialog = true
            } else {
                Toast.makeText(context, confirmState.message, Toast.LENGTH_SHORT).show()
            }
            viewModelConfirm.resetConfirmationState()
        }
        else -> {}
    }

    when (paymentState) {
        is NetworkResponse.Success -> {}
        is NetworkResponse.Error -> {}
        else -> {}
    }

    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(
                        top = 5.dp,
                        start = 5.dp,
                        end = 5.dp,
                        bottom = 5.dp,
                    ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                buildAnnotatedString {
                    append("Hey ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(userSessionStore.currentUserName())
                    }
                    append(" you are invited to sponsor the Voyage starting.Please pay now to confirm the Voyage.")
                },
                color = Color.Black,
                fontSize = 12.sp,
            )

            Spacer(modifier = Modifier.height(24.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp),
                shape = RoundedCornerShape(10.dp),
                modifier =
                    Modifier.fillMaxWidth()
                        .border(0.5.dp, colorResource(id = R.color.button_normal), RoundedCornerShape(8.dp)),
            ) {
                Column(
                    modifier = Modifier.padding(15.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            style =
                                TextStyle(
                                    color = Color.Black,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Normal,
                                ),
                            text = notification?.BookingDateTime.toString(),
                        )
                    }

                    Spacer(Modifier.height(7.dp))

                    Text(
                        style =
                            TextStyle(
                                color = Color(0xFF6A6969),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.W500,
                            ),
                        text = notification?.Name.orEmpty(),
                    )
                    Spacer(Modifier.height(7.dp))
                    Text(
                        style =
                            TextStyle(
                                color = Color(0xFF6A6969),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.W500,
                            ),
                        text = notification?.BookingDateTime.orEmpty(),
                    )

                    Spacer(Modifier.height(10.dp))

                    Text(
                        style =
                            TextStyle(
                                color = Color.Black,
                                fontSize = 14.sp,
                            ),
                        text = "Voyagees details",
                    )
                    Spacer(Modifier.height(10.dp))

                    Box(
                        Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Card(
                                modifier =
                                    Modifier
                                        .padding(5.dp)
                                        .height(205.dp)
                                        .weight(1f),
                                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                            ) {
                                Column(
                                    modifier =
                                        Modifier
                                            .padding(10.dp)
                                            .fillMaxSize(),
                                    verticalArrangement = Arrangement.SpaceEvenly, // Ensures space is even between the rows
                                ) {
                                    // First row with icon and text
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.passengers),
                                            contentDescription = "Status Icon",
                                            modifier =
                                                Modifier
                                                    .size(30.dp)
                                                    .padding(end = 10.dp),
                                            tint = Color.Unspecified,
                                        )
                                        Text(
                                            style =
                                                TextStyle(
                                                    color = Color.Black,
                                                    fontSize = 12.sp,
                                                ),
                                            text = notification?.NoOfVoyagers.toString(),
                                        )
                                    }

                                    HorizontalDivider(
                                        color = Color(0xFFA0A0A0),
                                        thickness = 1.dp,
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.money_icon),
                                            contentDescription = "Status Icon",
                                            modifier =
                                                Modifier
                                                    .size(30.dp)
                                                    .padding(end = 10.dp),
                                            tint = Color.Unspecified,
                                        )
                                        Text(
                                            style =
                                                TextStyle(
                                                    color = Color.Black,
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.Bold,
                                                ),
                                            text = notification?.AmountToPay.toString(),
                                        )
                                    }

                                    HorizontalDivider(
                                        color = Color(0xFFA0A0A0),
                                        thickness = 1.dp,
                                    )

                                    // Third row with icon and text
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.clock),
                                            contentDescription = "Status Icon",
                                            modifier =
                                                Modifier
                                                    .size(30.dp)
                                                    .padding(end = 10.dp),
                                            tint = Color.Unspecified,
                                        )
                                        Text(
                                            style =
                                                TextStyle(
                                                    color = Color.Black,
                                                    fontSize = 12.sp,
                                                ),
                                            text =
                                                notification?.Duration.toString().takeIf { it.isNotBlank() }
                                                    ?: "---",
                                        )
                                    }
                                }
                            }

                            Spacer(Modifier.width(5.dp))

                            Card(
                                modifier =
                                    Modifier
                                        .padding(5.dp)
                                        .height(205.dp)
                                        .weight(1f),
                                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                            ) {
                                Column(
                                    modifier =
                                        Modifier
                                            .padding(10.dp)
                                            .fillMaxSize(),
                                    verticalArrangement = Arrangement.SpaceEvenly, // Ensures space is even between the rows
                                ) {
                                    // First row with icon and text
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.location_icon),
                                            contentDescription = "Status Icon",
                                            modifier =
                                                Modifier
                                                    .size(30.dp)
                                                    .padding(end = 10.dp),
                                            tint = Color.Unspecified,
                                        )
                                        Text(
                                            style =
                                                TextStyle(
                                                    color = Color.Black,
                                                    fontSize = 12.sp,
                                                ),
                                            text = notification?.PickupDock.orEmpty(),
                                        )
                                    }

                                    HorizontalDivider(
                                        color = Color(0xFFA0A0A0),
                                        thickness = 1.dp,
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.drop_off_loc_icon),
                                            contentDescription = "Status Icon",
                                            modifier =
                                                Modifier
                                                    .size(30.dp)
                                                    .padding(end = 10.dp),
                                            tint = Color.Unspecified,
                                        )
                                        Text(
                                            style =
                                                TextStyle(
                                                    color = Color.Black,
                                                    fontSize = 12.sp,
                                                ),
                                            text = notification?.DropOffDock.orEmpty(),
                                        )
                                    }

                                    HorizontalDivider(
                                        color = Color(0xFFA0A0A0),
                                        thickness = 1.dp,
                                    )

                                    // Third row with icon and text
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.flag),
                                            contentDescription = "Status Icon",
                                            modifier =
                                                Modifier
                                                    .size(30.dp)
                                                    .padding(end = 10.dp),
                                            tint = Color.Unspecified,
                                        )
                                        Text(
                                            style =
                                                TextStyle(
                                                    color = Color.Black,
                                                    fontSize = 15.sp,
                                                ),
                                            text = notification?.PickupDock.orEmpty(),
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Spacer(Modifier.height(10.dp))

            SponsorsList(notification?.sponsors.orEmpty())

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    style =
                        TextStyle(
                            color = Color.Black,
                            fontSize = 12.sp,
                        ),
                    text = "To Confirm the Voyage, please",
                )
            }
            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Button(
                    onClick = {
                        showDialogForCancel = true
                    },
                    shape = RoundedCornerShape(10.dp), // Corner radius
                    modifier =
                        Modifier
                            .weight(1f)
                            .height(50.dp)
                            .border(
                                width = 1.dp,
                                color = colorResource(id = R.color.button_normal), // Border color
                                shape = RoundedCornerShape(10.dp), // Apply same corner radius to border
                            ),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                ) {
                    Text(
                        text = "Cancel Voyage",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = colorResource(id = R.color.button_normal), // Text color matches border
                    )
                }

                Button(
                    onClick = {
                        viewModelConfirm.submitConfirmation(ConfirmBookedVoyages(notification?.Id.orEmpty()))
                        loading = true
                    },
                    shape = RoundedCornerShape(10.dp),
                    modifier =
                        Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.button_normal)),
                ) {
                    Text(
                        text = "Confirm Voyage",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                    )
                }
            }

            if (showDialog) {
                val sponsorNames =
                    remember(notification) {
                        notification?.sponsors.orEmpty().joinToString(", ") { it.VoyagerUserName }
                    }
                MissingPaymentDialog(
                    name = sponsorNames,
                    onCancel = { showDialog = false },
                    onPayNow = {
                        showDialog = false
                        viewModelStripe.loadPaymentSheetConfig(
                            SponsorVoyagePaymentRequest(
                                notification?.Id.orEmpty(),
                                userSessionStore.currentUserId(),
                                "",
                            ),
                        )
                    },
                    onDismissRequest = { showDialog = false },
                )
            }

            if (showDialogForCancel)
                {
                    SessionDialog(
                        text = "Are you sure, you want to cancel voyage",
                        onCancel = {
                            showDialogForCancel = false
                        },
                        onPressOk = {
                            showDialogForCancel = false
                            viewModelCancel.fetchNearbyPlaces(CancelBookedVoyages(notification?.Id.orEmpty(), ""))
                            loading = true
                        },
                        showCancelButton = true,
                    )
                }
        }
    }
}
