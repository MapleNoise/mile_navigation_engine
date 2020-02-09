/*
 * Copyright 2018 Google LLC
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

package com.mile.mile_navigation_engine.common

import java.lang.IllegalArgumentException

/**
 * Generic holder for an object that either holds a result of a desired type,
 * or some exception.
 */

data class DataOrException<out T, out E: Exception?>(val data: T?, val exception: E?) {
    init {
        if (data == null && exception == null) {
            throw IllegalArgumentException("Both data and exception can't be null")
        }
        else if (data != null && exception != null) {
            throw IllegalArgumentException("Both data and exception can't be non-null")
        }
    }
}
