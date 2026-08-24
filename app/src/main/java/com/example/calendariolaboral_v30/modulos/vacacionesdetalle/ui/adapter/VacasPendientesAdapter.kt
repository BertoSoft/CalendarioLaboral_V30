package com.example.calendariolaboral_v30.modulos.vacacionesdetalle.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.calendariolaboral_v30.databinding.ItemVacDetalleBinding
import com.example.calendariolaboral_v30.modulos.vacaciones.domain.model.DatosVacaciones

class VacasPendientesAdapter: ListAdapter<DatosVacaciones, VacasPendientesAdapter.VacasPendientesViewHolder>(VacasPendientesDiffCallBack){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VacasPendientesViewHolder {

        val binding = ItemVacDetalleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VacasPendientesViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: VacasPendientesViewHolder,
        position: Int
    ) {
        holder.render(getItem(position))
    }

    inner class  VacasPendientesViewHolder(
        private val binding: ItemVacDetalleBinding
    ): RecyclerView.ViewHolder(binding.root){
        val miContexto = binding.root.context
        fun render(dato: DatosVacaciones){
            var str = "Del ${dato.fecha_inicio} al ${dato.fecha_final}"
            binding.tvDetalleFechas.text = str
            str = "${dato.total_dias} días. consumidos"
            binding.tvDetalleTotalDias.text = str
        }
    }

    object VacasPendientesDiffCallBack : DiffUtil.ItemCallback<DatosVacaciones>() {
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