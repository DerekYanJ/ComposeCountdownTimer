/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.MainViewModel
import com.example.androiddevchallenge.ui.theme.purple700

enum class InputType {
    MIN,
    SEC
}

@ExperimentalAnimationApi
@Composable
fun MainUi() {
    val viewModel: MainViewModel = viewModel()
    var seconds = 0
    val height = 300
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Countdown Timer")
                },
            )
        },
    ) {
        Column(Modifier.padding(10.dp)) {
            OutlinedTextFieldDemo(viewModel, InputType.SEC) {
                viewModel.text = it
                seconds = if (it.isNotEmpty())
                    try {
                        it.toInt()
                    } catch (e: Exception) {
                        0
                    }
                else 0
            }
            Column(
                modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.Center
            ) {
                OutlinedButton(
                    {
                        if (viewModel.countDownRunning) {
                            // stop running
                            viewModel.stopCountdown()
                        } else {
                            // start running
                            viewModel.startCountdown(seconds.toLong())
                        }
                    },
                    Modifier.padding(top = 10.dp),
                ) {
                    Text(text = if (viewModel.countDownRunning) "Stop the countdown" else "Start the countdown", fontSize = 18.sp)
                }
            }
            Spacer(
                Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(purple700)
            )
            AnimatedVisibility(viewModel.countDownRunning) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(height = height.dp)
                        .animateContentSize(),
                    horizontalArrangement = Arrangement.Center,

                ) {
                    Text(
                        "${viewModel.currentSeconds}",
                        Modifier
                            .size(
                                60.dp,
                                if (viewModel.seconds != 0L)
                                    ((height / viewModel.seconds) * (viewModel.currentSeconds)).toInt().dp
                                else 0.dp
                            )
                            .padding(10.dp)
                            .background(purple700)
                            .animateContentSize(),
                        color = Color.Black,
                        fontSize = 24.sp,
                        fontStyle = FontStyle.Italic
                    )
                }
            }
        }
    }
}

@Composable
fun OutlinedTextFieldDemo(viewModel: MainViewModel, inputType: InputType, onValueChange: (String) -> Unit = {}) {
    OutlinedTextField(
        value = viewModel.text,
        onValueChange = {
            onValueChange.invoke(it)
        },
        label = {
            Text(if (inputType == InputType.MIN) "Minutes" else "Seconds")
        },
        placeholder = {
            Text(if (inputType == InputType.MIN) "Please enter minutes" else "Please enter seconds")
        },
        modifier = Modifier.padding(10.dp)
    )
}
