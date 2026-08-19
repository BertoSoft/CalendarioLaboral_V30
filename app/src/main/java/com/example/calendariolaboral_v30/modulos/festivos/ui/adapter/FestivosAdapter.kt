package com.example.calendariolaboral_v30.modulos.festivos.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.calendariolaboral_v30.R
import com.example.calendariolaboral_v30.core.utils.Utils
import com.example.calendariolaboral_v30.databinding.ItemFestivosBinding
import com.example.calendariolaboral_v30.modulos.festivos.domain.model.DatosFestivos
import com.example.calendariolaboral_v30.modulos.festivos.domain.model.TipoFestivo
import com.example.calendariolaboral_v30.modulos.festivos.ui.extensions.toImagen
import com.example.calendariolaboral_v30.modulos.festivos.ui.extensions.toStringRes

class FestivosAdapter(
    private val onItemPulsado: (DatosFestivos) -> Unit
): ListAdapter<DatosFestivos, FestivosAdapter.FestivoViewHolder> (FestivoDiffCallBack) {

    private val utils = Utils()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FestivoViewHolder {
        val binding = ItemFestivosBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FestivoViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: FestivoViewHolder,
        position: Int
    ) {
        holder.render(getItem(position))
    }

    inner class FestivoViewHolder(
        private val binding: ItemFestivosBinding
    ): RecyclerView.ViewHolder(binding.root){
        val contexto = binding.root.context

        fun render(festivo: DatosFestivos){
            with(binding){
                tvItemTipo.text = contexto.getString(festivo.tipo.toStringRes())
                tvItemFecha.text = utils.fromLocalDateToFechaLarga(festivo.fecha)
                ivItemIcono.setImageResource(festivo.tipo.toImagen())

                val colorRes = when(festivo.tipo){
                    TipoFestivo.NACIONAL -> R.color.nacionel
                    TipoFestivo.EXCESO_JORNADA -> R.color.exceso_jornada
                    TipoFestivo.AUTONOMICO -> R.color.autonomico
                    TipoFestivo.LOCAL -> R.color.local
                    TipoFestivo.CONVENIO -> R.color.convenio
                }
                cardItem.setCardBackgroundColor(ContextCompat.getColor(contexto, colorRes))

                root.setOnClickListener {
                    onItemPulsado(festivo)
                }
            }
        }
    }

    object FestivoDiffCallBack: DiffUtil.ItemCallback<DatosFestivos>(){
        override fun areItemsTheSame(
            oldItem: DatosFestivos,
            newItem: DatosFestivos
        ): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: DatosFestivos,
            newItem: DatosFestivos
        ): Boolean =
            oldItem == newItem
    }
}

