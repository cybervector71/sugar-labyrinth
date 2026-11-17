package com.Famdiba1.Huqsoz.presentation.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.Famdiba1.Huqsoz.R
import com.Famdiba1.Huqsoz.core.di.AppViewModelFactory
import com.Famdiba1.Huqsoz.core.theme.Caramel
import com.Famdiba1.Huqsoz.core.theme.Copper
import com.Famdiba1.Huqsoz.core.theme.DarkScrim
import com.Famdiba1.Huqsoz.core.theme.DarkScrimDeep
import com.Famdiba1.Huqsoz.core.theme.DarkScrimMid
import com.Famdiba1.Huqsoz.core.theme.Pistachio
import com.Famdiba1.Huqsoz.core.theme.Raspberry
import com.Famdiba1.Huqsoz.core.theme.SplashCaption
import com.Famdiba1.Huqsoz.core.theme.SplashSubtitle
import com.Famdiba1.Huqsoz.core.theme.SurfaceLight
import com.Famdiba1.Huqsoz.core.theme.VanillaCream
import com.Famdiba1.Huqsoz.core.ui.ScreenBackground

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val splashViewModel: SplashViewModel = viewModel(factory = AppViewModelFactory)
    val finished by splashViewModel.finished.collectAsStateWithLifecycle()
    LaunchedEffect(finished) {
        if (finished) onFinished()
    }
    SplashContent()
}

@Composable
private fun SplashContent() {
    val transition = rememberInfiniteTransition(label = "splashLoop")
    val breath by transition.animateFloat(
        initialValue = 0.97f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breath"
    )
    val beadOne by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "beadOne"
    )
    val beadTwo by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "beadTwo"
    )
    val beadThree by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "beadThree"
    )

    ScreenBackground(
        overlay = listOf(
            DarkScrim.copy(alpha = 0.95f),
            DarkScrimMid.copy(alpha = 0.93f),
            DarkScrimDeep.copy(alpha = 0.97f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier.size(224.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val stroke = size.minDimension * 0.035f
                    val radii = listOf(0.92f, 0.76f, 0.60f)
                    val colors = listOf(Raspberry, Pistachio, Caramel)
                    val phases = listOf(beadOne, beadTwo, beadThree)
                    for (ring in radii.indices) {
                        val diameter = size.minDimension * radii[ring]
                        val topLeft = Offset(
                            (size.width - diameter) / 2f,
                            (size.height - diameter) / 2f
                        )
                        drawArc(
                            color = Copper.copy(alpha = 0.42f),
                            startAngle = 120f + ring * 40f,
                            sweepAngle = 210f,
                            useCenter = false,
                            topLeft = topLeft,
                            size = Size(diameter, diameter),
                            style = Stroke(width = stroke, cap = StrokeCap.Round)
                        )
                        val angle = Math.toRadians(
                            (120f + ring * 40f + 210f * phases[ring]).toDouble()
                        )
                        val center = Offset(size.width / 2f, size.height / 2f)
                        drawCircle(
                            color = colors[ring],
                            radius = stroke * 1.15f,
                            center = Offset(
                                center.x + diameter / 2f * kotlin.math.cos(angle).toFloat(),
                                center.y + diameter / 2f * kotlin.math.sin(angle).toFloat()
                            )
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .size(190.dp)
                        .graphicsLayer {
                            scaleX = breath
                            scaleY = breath
                        }
                        .background(
                            brush = Brush.radialGradient(
                                listOf(
                                    VanillaCream.copy(alpha = 0.16f),
                                    Color.Transparent
                                )
                            ),
                            shape = RoundedCornerShape(32.dp)
                        )
                        .border(
                            width = 2.dp,
                            color = Copper.copy(alpha = 0.55f),
                            shape = RoundedCornerShape(32.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = stringResource(R.string.cd_logo),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(132.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = stringResource(R.string.app_title),
                style = MaterialTheme.typography.displaySmall,
                color = VanillaCream,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(R.string.app_tagline),
                style = MaterialTheme.typography.labelSmall,
                color = SplashSubtitle,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(44.dp))

            LinearProgressIndicator(
                modifier = Modifier
                    .width(180.dp)
                    .height(5.dp),
                color = Raspberry,
                trackColor = SurfaceLight.copy(alpha = 0.16f)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = stringResource(R.string.loading),
                style = MaterialTheme.typography.labelSmall,
                color = SplashCaption,
                textAlign = TextAlign.Center
            )
        }
    }
}
