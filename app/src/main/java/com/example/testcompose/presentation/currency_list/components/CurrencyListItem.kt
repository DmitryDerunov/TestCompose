package com.example.testcompose.presentation.currency_list.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.*
import com.example.testcompose.domain.model.Currency

@Composable
fun CurrencyListItem(
        currency: Currency,
        modifier: Modifier,
        cornerRadius: Dp = 20.dp,
        onFavouriteCLick: ()-> Unit
){
    Card(
        modifier = modifier.padding(8.dp, 5.dp),
        border = BorderStroke(1.dp, MaterialTheme.colors.primary),
        shape = RoundedCornerShape(cornerRadius),
        elevation = 4.dp
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 8.dp)
        ) {

            Text(modifier = Modifier.align(CenterVertically),text = currency.description, style = MaterialTheme.typography.body1, fontSize = 20.sp)
            Spacer(modifier = Modifier.width(10.dp))
            Text(modifier = Modifier.align(CenterVertically),text = currency.value.toString(), style = MaterialTheme.typography.body1, fontSize = 20.sp)
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                modifier = Modifier.align(CenterVertically),
                onClick = onFavouriteCLick,
            ) {
                Icon(imageVector = if(currency.isFavourite) Icons.Default.StarRate else Icons.Default.StarOutline, contentDescription = "Add to favourite")
            }
        }
    }
}