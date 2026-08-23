package com.example.calendariolaboral_v30.modulos.vacaciones.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.calendariolaboral_v30.core.utils.Utils
import com.example.calendariolaboral_v30.databinding.ItemVacacionesBinding
import com.example.calendariolaboral_v30.modulos.vacaciones.domain.model.DatosVacaciones

class VacacionesAdapter() : ListAdapter<DatosVacaciones, VacacionesAdapter.VacacionesViewHolder>(VacacionesDiffCallBack) {

    val utils = Utils()
    var onItemPulsado: ((DatosVacaciones) -> Unit)? = null
    var onItemDeletePulsado: ((DatosVacaciones) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VacacionesViewHolder {
        val binding = ItemVacacionesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VacacionesViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: VacacionesViewHolder,
        position: Int
    ) {
        holder.render(getItem(position))
    }

    inner class VacacionesViewHolder(
        private val binding: ItemVacacionesBinding
    ): RecyclerView.ViewHolder(binding.root){
        val miContexto = binding.root.context
        fun render(vacaciones: DatosVacaciones){
            with(binding){
                var str_vacaciones = ""
                if(vacaciones.total_dias == 1){
                    str_vacaciones = "Dias laborables: (${vacaciones.total_dias} día)"
                }
                else{
                    str_vacaciones = "Dias laborables: (${vacaciones.total_dias} días)"
                }
                val str_fecha_inicio = utils.fromLocalDateToFechaCorta(vacaciones.fecha_inicio)
                val str_fecha_final = utils.fromLocalDateToFechaCorta(vacaciones.fecha_final)
                val str_fechas = "Del ${str_fecha_inicio} al ${str_fecha_final}"
                tvVacacionesDias.text = str_vacaciones
                tvVacacionesFechas.text = str_fechas

                cardItemVacaciones.setOnClickListener {
                    onItemPulsado?.invoke(vacaciones)
                }
                ivDeleteVacaciones.setOnClickListener {
                    onItemDeletePulsado?.invoke(vacaciones)
                }
            }
        }
    }

    object VacacionesDiffCallBack : DiffUtil.ItemCallback<DatosVacaciones>() {
        override fun areItemsTheSame(
            oldItem: DatosVacaciones,
            newItem: DatosVacaciones
        ): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: DatosVacaciones,
            newItem: DatosVacaciones
        ): Boolean =
            oldItem == newItem
    }
}