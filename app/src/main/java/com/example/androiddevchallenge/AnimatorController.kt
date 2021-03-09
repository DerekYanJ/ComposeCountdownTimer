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
package com.example.androiddevchallenge

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator

class AnimatorController(private val viewModel: MainViewModel) {

    private var valueAnimator: ValueAnimator? = null

    fun start() {
        if (viewModel.seconds == 0L) return
        if (valueAnimator == null) {
            // Animator: totalTime -> 0
            valueAnimator = ValueAnimator.ofInt(viewModel.seconds.toInt(), 0)
            valueAnimator?.interpolator = LinearInterpolator()
            // Update timeLeft in ViewModel
            valueAnimator?.addUpdateListener {
                viewModel.currentSeconds = (it.animatedValue as Int).toLong()
            }
            valueAnimator?.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    complete()
                }
            })
        } else {
            valueAnimator?.setIntValues(viewModel.seconds.toInt(), 0)
        }
        // (LinearInterpolator + duration) aim to set the interval as 1 second.
        valueAnimator?.duration = viewModel.seconds * 1000L
        valueAnimator?.start()
    }

    fun pause() {
        valueAnimator?.pause()
    }

    fun resume() {
        valueAnimator?.resume()
    }

    fun stop() {
        valueAnimator?.cancel()
        viewModel.currentSeconds = 0
    }

    fun complete() {
        viewModel.stopCountdown()
    }
}