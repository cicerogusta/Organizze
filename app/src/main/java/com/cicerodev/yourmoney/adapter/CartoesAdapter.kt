package com.cicerodev.yourmoney.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cicerodev.yourmoney.R
import com.cicerodev.yourmoney.data.model.CartaoCredito
import com.cicerodev.yourmoney.databinding.CdcLayoutBinding
import com.cicerodev.yourmoney.ui.PrincipalActivityViewModel
import com.cicerodev.yourmoney.ui.SeusCartoesActivityViewModel
import com.cicerodev.yourmoney.util.formataData
import com.cicerodev.yourmoney.util.formataDataCartao


class CartoesAdapter(
    private var listaCartoes: MutableList<CartaoCredito>, private val viewModel: SeusCartoesActivityViewModel
) :
    RecyclerView.Adapter<CartoesAdapter.MyViewHolder?>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartoesAdapter.MyViewHolder {
        val itemLista = LayoutInflater.from(parent.context)
            .inflate(R.layout.cdc_layout, parent, false)
        return MyViewHolder(itemLista)
    }

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    override fun onBindViewHolder(holder: CartoesAdapter.MyViewHolder, position: Int) {
        val cartao = listaCartoes[position]
        holder.binding.textCardName.text = "Nome: ${cartao.nomeCartao}"
        holder.binding.textExpirationDate.text = "Data de vencimento: ${formataDataCartao(cartao.dataVencimento!!)}"
        holder.binding.textCardLimit.text = "Limite R$ ${cartao.limiteCartao}"
        holder.binding.button2.setOnClickListener {
            viewModel.removerCartao(cartao)
            viewModel.returnCards().value?.remove(cartao)
        }




    }


    override fun getItemCount(): Int {
        return listaCartoes.size
    }

    inner class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val binding: CdcLayoutBinding by lazy { CdcLayoutBinding.bind(itemView) }
    }

}