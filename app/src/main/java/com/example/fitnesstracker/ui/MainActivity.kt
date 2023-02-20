package com.example.fitnesstracker.ui

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnesstracker.models.MainItem
import com.example.fitnesstracker.R

class MainActivity : AppCompatActivity(){

    // variavel que vai receber a referencia do recyclerview geral
    private lateinit var rvMain: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // criacao da lista de itens que serao exibidos na tela
        val mainItems = mutableListOf<MainItem>()
        // aqui adiciona-se os itens modelados no arquivo MainItem.kt
        mainItems.add(
            MainItem(
                id = 1,
                drawableId = R.drawable.ic_baseline_accessibility_35,
                textStringId = R.string.imc_label,
                color = Color.GREEN
            )
        )
        mainItems.add(
            MainItem(
                id = 2,
                drawableId = R.drawable.ic_baseline_balance_35,
                textStringId = R.string.tmb_label,
                color = Color.BLUE
            )
        )


        // ligamos o recyclerView a variavel RVmain
        rvMain = findViewById(R.id.rv_main)
        // setamos o adapter dele para o Mainadapter passando a lista de itens como parametro
        rvMain.adapter = mainAdapter(mainItems) { id ->
            when (id) {
                1 -> {
                    val intent = Intent(this, ImcActivity::class.java)
                    startActivity(intent)
                }
                2 -> {
                    val intent = Intent(this, TmbActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        // configura-se a forma como o layout sera exibido de maneira Linear usando o contexto desta activity
        rvMain.layoutManager = GridLayoutManager(this, 2)


    }

  /*  override fun onClick(id: Int) {
        when(id){
            1 -> {
                val intent = Intent(this, Imc::class.java)
                startActivity(intent)
            }
        }
    }*/

    // aqui temos uma classe interna do arquivo mainActivity.kt
    // essa classa servira como adapter do recyclerview RVmain e recebera uma lista de itens
    // ela herda caracteristicas da classe nativa RecyclerView.Adapter
    // e tem como parametro o <mainViewHolder> que sera a outra classe que coordena acoes da celula em especifico (o item)
    private inner class mainAdapter(private val mainItems: List<MainItem>,
                                    private val onItemClickListener: (Int) -> Unit): RecyclerView.Adapter<mainAdapter.mainViewHolder>() {
        // aqui sobreescrevemos 3 funcoes herdadas do RecyclerView.Adapter para configurar nossa lista

        // a primeira delas é a oncreateviewholder que herda caracteristicas do nosso viewholder depois de setado
        // essa funcao basicamente liga o layout do item que usaremos com o adapter
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): mainViewHolder {
            val view = layoutInflater.inflate(R.layout.list_item,parent,false)
            return mainViewHolder(view)
        }

        // essa funcao sera a responsavel por atualizar os itens conforme rolamos a lista no app
        // teremos sempre o nosso viewholder e sua respectiva posicao
        override fun onBindViewHolder(holder: mainViewHolder, position: Int) {
            val currentValue = mainItems[position]
            holder.bind(currentValue)
        }

        // por fim a nossa ultima funcao que retorna o tamanho da nossa lista e quantos itens serao exibidos
        override fun getItemCount(): Int {
           return mainItems.size
        }


        // aqui temos o nosso viewholder, como dito antes a classe responsavel pelo controle da view unica
        // do componente que sera listado no adapter
        // ele recebe uma view como parametro que sera setado no metodo oncreateviewholder do adapter
        inner class mainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            fun bind(item: MainItem){

                val img: ImageView = itemView.findViewById(R.id.item_img)
                img.setImageResource(item.drawableId)

                val name: TextView = itemView.findViewById(R.id.item_txt)
                name.setText(item.textStringId)

                val container: LinearLayout = itemView.findViewById(R.id.container_item)
                container.setBackgroundColor(item.color)

                container.setOnClickListener {
                    onItemClickListener.invoke(item.id)
                }
            }
        }

    }



}