package com.example.calendariolaboral_v30.modulos.calendario.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.calendariolaboral_v30.R
import com.example.calendariolaboral_v30.databinding.ItemCalendarioDiaBinding
import com.example.calendariolaboral_v30.modulos.calendario.domain.model.DatosCalendario

class CalendarioAdapter():
    ListAdapter<DatosCalendario, CalendarioAdapter.CalendarioViewHolder>(CalendarioDiffCallBack){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CalendarioAdapter.CalendarioViewHolder {
        val binding = ItemCalendarioDiaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CalendarioViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: CalendarioAdapter.CalendarioViewHolder,
        position: Int
    ) {
        holder.render(getItem(position))
    }


    inner class CalendarioViewHolder(private val binding: ItemCalendarioDiaBinding):
        RecyclerView.ViewHolder(binding.root){

        fun render (dia: DatosCalendario){
            with(binding) {
                // 1. CONTROL DE VISIBILIDAD DE DÍAS VACÍOS
                // Comprobamos si la fecha no es nula (el último parámetro true/false del constructor)
                if (dia.fecha != null) {
                    tvNumeroDia.text = dia.fecha.dayOfMonth.toString()

                    // 2. CONTROL DE COLORES (Siempre debe llevar un else obligatorio)
                    if (dia.isSabado || dia.isDomingo) {
                        tvNumeroDia.setTextColor(Color.RED)
                    } else {
                        tvNumeroDia.setTextColor(itemView.context.getColor(R.color.text_main)) // Restablece color base para días de semana
                        if(dia.isVacaciones){
                            cardContenedorDia.setCardBackgroundColor(itemView.context.getColor(R.color.vacaciones))
                        }
                        else if(dia.isNacional){
                            cardContenedorDia.setCardBackgroundColor(itemView.context.getColor(R.color.nacionel))
                        }
                        else if(dia.isAutonomico){
                            cardContenedorDia.setCardBackgroundColor(itemView.context.getColor(R.color.autonomico))
                        }
                        else if(dia.isLocal){
                            cardContenedorDia.setCardBackgroundColor(itemView.context.getColor(R.color.local))
                        }
                        else if(dia.isConvenio){
                            cardContenedorDia.setCardBackgroundColor(itemView.context.getColor(R.color.convenio))
                        }
                        else{
                            cardContenedorDia.setCardBackgroundColor(itemView.context.getColor(R.color.bg_card_surface))
                        }
                    }

                    // Aquí puedes añadir tus otras validaciones visuales en el futuro:
                    // if (dia.isVacaciones) { ... } else { ... }

                } else {
                    // Si el día es vacío (huecos de inicio o fin), ocultamos la celda por completo
                    cardContenedorDia.setCardBackgroundColor(itemView.context.getColor(R.color.bg_card_surface))
                }
            }
        }

    }


    object CalendarioDiffCallBack: DiffUtil.ItemCallback<DatosCalendario>(){
        override fun areItemsTheSame(
            oldItem: DatosCalendario,
            newItem: DatosCalendario
        ): Boolean {
            return oldItem.fecha == newItem.fecha
        }

        override fun areContentsTheSame(
            oldItem: DatosCalendario,
            newItem: DatosCalendario
        ): Boolean {
            return oldItem == newItem
        }

    }
}
