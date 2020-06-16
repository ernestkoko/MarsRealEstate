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

package com.example.android.marsrealestate.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

import retrofit2.http.GET

private const val BASE_URL = "https://mars.udacity.com/"

//create moshi object
//KotlinJsonAdapterfactory enables moshi object to work with json annotation properly
private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

//create retrofit object

private val retrofit = Retrofit.Builder()

        //lets retrofit to use moshi to convert response into kotlin objects
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        //call adapter enables coroutine to build APIs
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(BASE_URL)
        .build()

interface MarsApiService{
    //when GET is called, retrofit appends 'realestate' to the base url and create the call object
    //used to start the request
    @GET(value = "realestate")
    fun getProperties():
            //deferred is a kind of coroutine job that returns a result
            Deferred<List<MarsProperty>>
}

//MarsApi expose the retrofit to the rest of the application
object MarsApi{
    val retrofitService: MarsApiService by lazy {
        retrofit.create(MarsApiService::class.java)
    }
}
