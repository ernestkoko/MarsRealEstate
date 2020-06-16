/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.marsrealestate.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.marsrealestate.network.MarsApi
import com.example.android.marsrealestate.network.MarsProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

enum class MarsApiStatus{LOADING, ERROR, DONE}
/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */
class OverviewViewModel : ViewModel() {

    // The internal MutableLiveData String that stores the status of the most recent request
    private val _status = MutableLiveData<MarsApiStatus>()

    // The external immutable LiveData for the request status String
    val status: LiveData<MarsApiStatus>
        get() = _status

    private val _properties = MutableLiveData<List<MarsProperty>>()

    val  properties: LiveData<List<MarsProperty>>
    get() = _properties

    // we create a job
    private var viewModelJob = Job()

    //use the job to create coroutine scope with Main dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    /**
     * Call getMarsRealEstateProperties() on init so we can display status immediately.
     */
    init {
        getMarsRealEstateProperties()
    }

    /**
     * Sets the value of the status LiveData to the Mars API status.
     */
    private fun getMarsRealEstateProperties() {
        //launch the coroutine
        coroutineScope.launch {
            //make a network call on the background thread
            val getPropertiesDeferred = MarsApi.retrofitService.getProperties()
            try {
                //loading state
                _status.value = MarsApiStatus.LOADING
                //returns the result from the network call when the value is ready
                val listResult = getPropertiesDeferred.await()
                //done when we are finished
                _status.value = MarsApiStatus.DONE
                _properties.value = listResult

            } catch (t: Throwable) {
                //handles the failure
                _status.value = MarsApiStatus.ERROR
                //set the recycler view list to zero on error
                _properties.value = ArrayList()

            }

        }

       // _status.value = "Set the Mars API Response here!"
    }

    //called when the viewModel dies
    override fun onCleared() {
        super.onCleared()
        //cancels the job when the viewModel dies
        viewModelJob.cancel()
    }
}
