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


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.marsrealestate.databinding.GridViewItemBinding
import com.example.android.marsrealestate.network.MarsProperty

class PhotoGridAdapter(private val onclickListener: OnclickListener) :
        ListAdapter<MarsProperty, PhotoGridAdapter.MarsPropertyViewModel>(DiffCallBack) {

    //view Holder
    class MarsPropertyViewModel(private var binding: GridViewItemBinding) :
            RecyclerView.ViewHolder(binding.root) {
        fun bind(marsProperty: MarsProperty) {
            //bind the marProperty to the property variable from the xml file
            binding.property = marsProperty
            //ensures the views are redrawn immediately
            binding.executePendingBindings()
        }

    }

    //a class for comparing the items to know whether to update the view
    companion object DiffCallBack : DiffUtil.ItemCallback<MarsProperty>() {
        override fun areItemsTheSame(oldItem: MarsProperty, newItem: MarsProperty): Boolean {
            return oldItem === newItem

        }

        override fun areContentsTheSame(oldItem: MarsProperty, newItem: MarsProperty): Boolean {
            return oldItem.id == newItem.id
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoGridAdapter.MarsPropertyViewModel {
        return MarsPropertyViewModel(GridViewItemBinding.inflate(LayoutInflater.from(parent.context)))

    }

    override fun onBindViewHolder(holder: PhotoGridAdapter.MarsPropertyViewModel, position: Int) {
        //get the item at the position
        val marsProperty = getItem(position)
        // bind click listener
        holder.itemView.setOnClickListener{
            onclickListener.onclick(marsProperty)
        }

        //bind it
        holder.bind(marsProperty)

    }
    class OnclickListener(val clickListener: (marsProperty: MarsProperty) -> Unit){
        fun onclick(marsProperty: MarsProperty) = clickListener(marsProperty)
    }

}

