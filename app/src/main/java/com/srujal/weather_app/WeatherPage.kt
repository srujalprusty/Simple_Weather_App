package com.srujal.weather_app

import androidx.compose.foundation.hoverable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.resolveDefaults
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.srujal.weather_app.Api.NetworkResponse
import com.srujal.weather_app.Api.WeatherModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherPage(viewModel: WeatherViewModel){
    var city by remember {
        mutableStateOf("")
    }
    val  weatherResult = viewModel.weatherResult.observeAsState()
    val keyboard = LocalSoftwareKeyboardController.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
        ){
            OutlinedTextField(value = city , onValueChange = {
                city = it
            } , label = {
                Text(text = "Enter City Name")
            }, maxLines = 1,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions {
                // Handle the search action
                }, colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Black,
                    cursorColor = Color.Black,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Black,
                    focusedTextColor = Color.Black
                )
            )
            IconButton(onClick = {
                 viewModel.getData(city)
                keyboard?.hide()

            }){
                Icon(imageVector = Icons.Default.Search,
                    contentDescription = "Search" ,
                    Modifier.size(34.dp))
            }
        }

        when(val  result  = weatherResult.value){

            is NetworkResponse.Error -> {
                Text(text = result.message)
            }
            NetworkResponse.Loading -> {
                CircularProgressIndicator()
            }
            is NetworkResponse.Success -> {
                 WeatherDetails(data = result.data)
            }

            null -> {}

        }
    }
}


@Composable
fun WeatherDetails(data : WeatherModel){
         Column(
             modifier = Modifier
                 .fillMaxWidth()
                 .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
         ) {
             Row(
                 modifier = Modifier.fillMaxWidth(),
                 horizontalArrangement = Arrangement.Start,
                 verticalAlignment = Alignment.CenterVertically
             ){
                  Icon(imageVector = Icons.Default.LocationOn,
                      contentDescription = "Location Icon",
                      Modifier.size(39.dp)
                      )
                 Text(text = data.location.name ,
                     fontSize = 30.sp,
                     fontWeight = FontWeight.Bold,
                     color = Color.DarkGray
                 )
                 Spacer(modifier = Modifier.width(8.dp))
                 Text(text = data.location.country ,
                     fontSize = 18.sp,
                     fontWeight = FontWeight.Bold,
                     color = Color.Black
                 )
             }
             Spacer(modifier = Modifier.size(16.dp))

             Text(text = "${data.current.temp_c} °C" ,
                 fontSize = 60.sp,
                 fontWeight = FontWeight.Bold,
                 color = Color.Black,
                 textAlign = TextAlign.Center
             )

             AsyncImage(
                 modifier = Modifier.size(170.dp),
                 model = "https:${data.current.condition.icon}".replace("64x64", "128x128"),
                 contentDescription = "Weather Image" )


             Text(text = "${data.current.condition.text} °C" ,
                 fontSize = 20.sp,
                 color = Color.Gray,
                 textAlign = TextAlign.Center
             )
             Spacer(modifier = Modifier.height(16.dp))
             Card {
                 Column(
                     modifier = Modifier
                         .fillMaxWidth()
                         .padding(8.dp),
                     horizontalAlignment = Alignment.CenterHorizontally
                 ){
                     Row(
                         modifier = Modifier.fillMaxWidth(),
                         horizontalArrangement = Arrangement.SpaceAround

                     ) {
                       weathercardSection("Humidity" , data.current.humidity)
                       weathercardSection("Wind Speed" , data.current.wind_kph + " km/h")
                     }
                     Row(
                         modifier = Modifier.fillMaxWidth(),
                         horizontalArrangement = Arrangement.SpaceAround

                     ) {
                         weathercardSection("UV Index" , data.current.uv)
                         weathercardSection("Wind Degree" , data.current.wind_degree )
                     }
                     Row(
                         modifier = Modifier.fillMaxWidth(),
                         horizontalArrangement = Arrangement.SpaceAround

                     ) {
                         weathercardSection("Region" , data.location.region)
                         weathercardSection("Local TIme" , data.location.localtime.split(" ")[1])
                     }
                     Row(
                         modifier = Modifier.fillMaxWidth(),
                         horizontalArrangement = Arrangement.SpaceAround

                     ) {
                         weathercardSection("Altitude" , data.location.lat)
                         weathercardSection("Longitude" , data.location.lon)
                     }
                 }


                 }
             }
         }


@Composable
fun weathercardSection(key : String , value : String) {
    Column(
        modifier = Modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text(text = value,fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(text = key , fontSize = 16.sp, color = Color.Gray, fontWeight = FontWeight.SemiBold)
    }
}






