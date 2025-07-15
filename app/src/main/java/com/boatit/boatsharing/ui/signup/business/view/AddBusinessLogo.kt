package com.boatit.boatsharing.ui.signup.business


import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.rememberAsyncImagePainter
import com.boatit.boatsharing.R
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.routes.NavigationManager
import com.boatit.boatsharing.routes.NavigationManager.DASHBOARD_SCREEN
import com.boatit.boatsharing.routes.popBack
import com.boatit.boatsharing.ui.signup.business.model.SaveBusinessAboutRequest
import com.boatit.boatsharing.ui.signup.business.viewmodel.BusinessAboutViewModel
import com.boatit.boatsharing.ui.signup.business.viewmodel.BusinessLogoViewModel
import com.boatit.boatsharing.uihelpers.CustomButton
import com.boatit.boatsharing.uihelpers.CustomTopBar
import com.boatit.boatsharing.uihelpers.FormStepsViews
import com.boatit.boatsharing.utils.AppConstants
import com.boatit.boatsharing.utils.permissions.PermissionsToAccessGallery
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import java.io.File


@Composable
fun AddBusinessLogo(navController: NavController,
    viewModel: BusinessLogoViewModel = koinViewModel()
) {
    val focusManager = LocalFocusManager.current
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var triggerGallery by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isButtonEnabled by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var isNetworkError by remember { mutableStateOf(false) }
    val isValidate = selectedImageUri!= null
    val context = LocalContext.current

    if (triggerGallery) {
        PermissionsToAccessGallery(
            onImageSelected = { uri ->
                selectedImageUri = uri
                triggerGallery = false
            },
            onPermissionGranted = {
                triggerGallery = false
            },
            onPermissionDenied = {
                triggerGallery = false
            }
        )
    }

    val registrationState by viewModel.registrationState.collectAsState()

    fun uriToFile(context: Context, uri: Uri): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val tempFile = File.createTempFile("upload", ".jpg", context.cacheDir)
            tempFile.outputStream().use { outputStream ->
                inputStream?.copyTo(outputStream)
            }
            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun performLogin(){
        navController.navigate(NavigationManager.BUSINESS_SCREEN)
    }

    when (registrationState) {
        is NetworkResponse.Success -> {
            if(isLoading){
                isLoading = false
                isNetworkError = false
                Toast.makeText(context, registrationState.data?.Message , Toast.LENGTH_SHORT).show()
                performLogin()
            }
        }
        is NetworkResponse.Error -> {
            if(isLoading){
                isLoading = false
                isNetworkError = true
                errorMessage = "Network error, please try again."
                Toast.makeText(context, (registrationState as NetworkResponse.Error).message, Toast.LENGTH_SHORT).show()
            }
        }
        else -> {}
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CustomTopBar(text = stringResource(R.string.add_your_business_info)+ " 4/4", onImageClick = {
                println("clicked...")
                navController.popBack()
            })
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(
                        top = innerPadding.calculateTopPadding() + 15.dp,
                        start = 20.dp,
                        end = 20.dp,
                        bottom = innerPadding.calculateTopPadding() + 25.dp,
                    )
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {

                FormStepsViews(
                    numberOfViews = 4,
                    activeColor = colorResource(id = R.color.button_normal),
                    inactiveColor = Color.Gray,
                    activeViewsCount = 4
                )

                Spacer(Modifier.height(30.dp))
                Text(
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    text = stringResource(R.string.add_business_logo_label)
                )

                Spacer(Modifier.height(10.dp))


                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
                        .background(Color.Transparent)
                        .clickable {
                            triggerGallery = true

                        },
                    contentAlignment = Alignment.Center
                ) {



                    if (selectedImageUri == null) {

                        Image(
                            painter = painterResource(R.drawable.upload_placeholder),
                            contentDescription = "Placeholder Image",
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Fit
                        )
                    } else {

                        Image(
                            painter = rememberAsyncImagePainter(model = selectedImageUri),
                            contentDescription = "Selected Image",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }


                Spacer(modifier = Modifier.height(40.dp))

                CustomButton(
                    text = stringResource(R.string.save_button_label),
                    isValidate = isValidate,
                    isLoading = isLoading,
                    onButtonClick = {
                        selectedImageUri?.let { uri ->
                            val file = uriToFile(context, uri)
                            if (file != null) {
                                isLoading = true
                                viewModel.uploadBusinessLogo(
                                        AppConstants.USER_ID!!,
                                        file)
                                focusManager.clearFocus()
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

            }
        },
        )


}

@Preview
@Composable
fun PreviewAddBusinessLogo() {
    AddBusinessLogo(navController = rememberNavController())
}