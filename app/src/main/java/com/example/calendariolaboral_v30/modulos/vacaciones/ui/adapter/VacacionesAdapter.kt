package com.example.calendariolaboral_v30.modulos.vacaciones.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.calendariolaboral_v30.databinding.ItemVacacionesBinding
import com.example.calendariolaboral_v30.modulos.vacaciones.domain.model.DatosVacaciones

class VacacionesAdapter(
    private val onItemPulsado: (DatosVacaciones) -> Unit
) : ListAdapter<DatosVacaciones, VacacionesAdapter.VacacionesViewHolder>(VacacionesDiffCallBack) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VacacionesViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(
        holder: VacacionesViewHolder,
        position: Int
    ) {
        TODO("Not yet implemented")
    }

    inner class VacacionesViewHolder(
        private val binding: ItemVacacionesBinding
    ): RecyclerView.ViewHolder(binding.root){

        fun render(){

        }
    }

    object VacacionesDiffCallBack : DiffUtil.ItemCallback<DatosVacaciones>() {
        override fun areItemsTheSame(
            oldItem: DatosVacaciones,
            newItem: DatosVacaciones
        ): Boolean {
            TODO("Not yet implemented")
        }

        override fun areContentsTheSame(
            oldItem: DatosVacaciones,
            newItem: DatosVacaciones
        ): Boolean {
            TODO("Not yet implemented")
        }

    }
}