package pt.isec.ans.amov.ui.Components.Cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isec.ans.amov.R
import pt.isec.ans.amov.ui.Components.Buttons.RoundIconButton
import pt.isec.ans.amov.ui.theme.BlueSoft


@Composable
fun ReviewCard(
    comment: String,
    rating: Int,

    modifier: Modifier = Modifier,
    onClick: () -> Unit = { }
){
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .padding(10.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .height(56.dp)
        ) {
            RoundIconButton(R.drawable.account)
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                //Attraction rating
                Text(
                    text = rating.toString(),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.inter_medium)),
                        color = BlueSoft,
                    )
                )

                //Star Icon
                Image(
                    painter = painterResource(id = R.drawable.star),
                    contentDescription = "Star icon",
                    contentScale = ContentScale.None,
                    modifier = Modifier
                        .padding(1.dp)
                        .width(14.dp)
                        .height(14.dp)
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .width(240.dp)
        ) {
            Text(
                text = "''$comment''",
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(600),
                    color = BlueSoft,
                    textAlign = TextAlign.Start
                ),
                modifier = Modifier
                    .height(56.dp)
            )
        }

    }
}

@Preview
@Composable
fun ReviewCardPreview() {
    ReviewCard(
        comment = "This is the comment of the category yyyyyyyyyyyyyyy sdkjnfi sdujbhfdv iusdabv siour dnvisdubvnsdiubvsdiuvcbsdiu",
        rating = 2,
    )
}