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
import com.google.android.gms.dynamic.IFragmentWrapper


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

            if (movimentacao.isDespesaCartao) {
                holder.binding.txtCardName.visibility = View.VISIBLE
                holder.binding.txtCardName.text = movimentacao.cartaoCredito.nomeCartao
                holder.binding.imageMoney.visibility = View.GONE
                holder.binding.imagePix.visibility = View.GONE
            } else {
                holder.binding.txtCardName.visibility = View.GONE
                if (movimentacao.isDespesaDinheiro) {
                    holder.binding.imageMoney.visibility = View.VISIBLE
                } else {
                    holder.binding.imageMoney.visibility = View.GONE
                    if (movimentacao.isDespesaPix) {
                        holder.binding.imagePix.visibility = View.VISIBLE
                    } else {
                        holder.binding.imageMoney.visibility = View.GONE
                    }
                }
            }
        } else {
            if (movimentacao.tipo == "r") {
                holder.binding.textValor.setTextColor(context.resources.getColor(R.color.colorAccentReceita))
                holder.binding.textValor.text = movimentacao.valor.toString()

                if (movimentacao.isReceitaCartao) {
                    holder.binding.txtCardName.visibility = View.VISIBLE
                    holder.binding.txtCardName.text = null
                    holder.binding.imageMoney.visibility = View.GONE
                    holder.binding.imagePix.visibility = View.GONE
                } else {
                    holder.binding.txtCardName.visibility = View.GONE
                    if (movimentacao.isDespesaDinheiro) {
                        holder.binding.imageMoney.visibility = View.VISIBLE
                    } else {
                        holder.binding.imageMoney.visibility = View.GONE
                        if (movimentacao.isDespesaPix) {
                            holder.binding.imagePix.visibility = View.VISIBLE
                        } else {
                            holder.binding.imageMoney.visibility = View.GONE
                        }
                    }
                }
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