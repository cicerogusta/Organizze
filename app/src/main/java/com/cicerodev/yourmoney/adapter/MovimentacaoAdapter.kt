package com.cicerodev.yourmoney.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cicerodev.yourmoney.R
import com.cicerodev.yourmoney.data.model.Movimentacao
import com.cicerodev.yourmoney.databinding.AdapterMovimentacaoBinding


class MovimentacaoAdapter(
    private var listaMovimentacao: MutableList<Movimentacao>, private val context: Context
) :
    RecyclerView.Adapter<MovimentacaoAdapter.MyViewHolder?>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovimentacaoAdapter.MyViewHolder {
        val itemLista = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_movimentacao, parent, false)
        return MyViewHolder(itemLista)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MovimentacaoAdapter.MyViewHolder, position: Int) {
        val movimentacao = listaMovimentacao[position]
        holder.binding.textCategoria.text = movimentacao.categoria
        holder.binding.textDescricao.text = movimentacao.descricao
        holder.binding.textValor.text = movimentacao.valor.toString()
        if (movimentacao.tipo == "d" ) {
            holder.binding.textValor.setTextColor(context.resources.getColor(R.color.colorAccentDespesa))
            holder.binding.textValor.text = "-" + movimentacao.valor.toString()

        } else {
            if (movimentacao.tipo == "r") {
                holder.binding.textValor.setTextColor(context.resources.getColor(R.color.colorAccentReceita))
                holder.binding.textValor.text = movimentacao.valor.toString()

            }
        }

    }

    override fun getItemCount(): Int {
        return listaMovimentacao.size
    }

    inner class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val binding: AdapterMovimentacaoBinding by lazy { AdapterMovimentacaoBinding.bind(itemView) }
    }

}