package io.github.takusan23.newradiosupporter.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.takusan23.newradiosupporter.R
import io.github.takusan23.newradiosupporter.tool.FinalNRType
import io.github.takusan23.newradiosupporter.tool.NrStandAloneType

/**
 * 一番上に出す情報
 *
 * @param nrStandAloneType 5G未接続時はnullでおｋ
 */
@Composable
fun TopInfo(
    modifier: Modifier = Modifier,
    finalNRType: FinalNRType,
    nrStandAloneType: NrStandAloneType?,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(100.dp),
            painter = painterResource(id = when (finalNRType) {
                FinalNRType.ANCHOR_BAND -> R.drawable.ic_android_anchor_lte_band
                FinalNRType.NR_SUB6 -> R.drawable.ic_android_nr_sub6
                FinalNRType.NR_MMW -> R.drawable.ic_android_nr_mmw
                FinalNRType.LTE -> R.drawable.ic_android_lte
                else -> R.drawable.ic_outline_info_24
            }),
            contentDescription = null
        )
        // メッセージ
        ConnectedStatusMessage(finalNRType = finalNRType)
        // 5Gの場合は NSA/SA どっちかを表示する
        if (nrStandAloneType != null && nrStandAloneType != NrStandAloneType.ERROR) {
            ConnectedNrStandAloneMessage(nrStandAloneType)
        }
    }
}

/** もし5Gに接続している場合は、スタンドアローン、ノンスタンドアローンのどっちに接続しているかを表示する */
@Composable
private fun ConnectedNrStandAloneMessage(nrStandAloneType: NrStandAloneType) {
    MessageCard(
        cardColor = MaterialTheme.colorScheme.secondaryContainer,
        iconRes = R.drawable.ic_outline_5g_24,
        text = when (nrStandAloneType) {
            NrStandAloneType.STAND_ALONE -> "スタンドアローン方式 (SA) の5Gに接続中です。"
            NrStandAloneType.NON_STAND_ALONE -> "ノンスタンドアローン方式 (NSA) の5Gに接続中です。"
            NrStandAloneType.ERROR -> "" // 多分こない
        }
    )
}

/** 接続中のネットワークを表示するやつ */
@Composable
private fun ConnectedStatusMessage(finalNRType: FinalNRType) {
    val isNr = finalNRType == FinalNRType.NR_MMW || finalNRType == FinalNRType.NR_SUB6
    MessageCard(
        cardColor = animateColorAsState(targetValue = if (isNr) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer).value,
        iconRes = if (isNr) R.drawable.ic_outline_5g_24 else R.drawable.ic_outline_error_outline_24,
        text = when (finalNRType) {
            FinalNRType.ANCHOR_BAND -> stringResource(id = R.string.type_lte_anchor_band)
            FinalNRType.NR_SUB6 -> stringResource(id = R.string.type_nr_sub6)
            FinalNRType.NR_MMW -> stringResource(id = R.string.type_nr_mmwave)
            FinalNRType.LTE -> stringResource(id = R.string.type_lte)
            else -> stringResource(id = R.string.loading)
        }
    )
}

/**
 * メッセージのカード
 *
 * @param cardColor カードの色
 * @param text メッセージ
 * @param iconRes アイコンのリソースID
 */
@Composable
private fun MessageCard(
    cardColor: Color,
    text: String,
    iconRes: Int,
) {
    Surface(
        modifier = Modifier.padding(10.dp),
        color = cardColor,
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 10.dp),
                painter = painterResource(id = iconRes),
                contentDescription = null
            )
            Text(text = text)
        }
    }
}