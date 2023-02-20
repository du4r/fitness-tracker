    package com.example.fitnesstracker.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnesstracker.App
import com.example.fitnesstracker.R
import com.example.fitnesstracker.models.CalcModel
import java.text.SimpleDateFormat
import java.util.*

    class ListCalcActivity : AppCompatActivity() {

    private lateinit var rvRegisters: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_calc)

        rvRegisters = findViewById(R.id.rv_registers)
        val result = mutableListOf<CalcModel>()
        val adapter = listCalcAdapter(result)
        rvRegisters.adapter = adapter
        rvRegisters.layoutManager = LinearLayoutManager(this)


        val type = intent?.extras?.getString("type") ?: throw IllegalStateException("type not found")

        Thread{
            val app = application as App
            val dao = app.db.calcDao()
            val response = dao.getRegisterByType(type)

            runOnUiThread {
                result.addAll(response)
                adapter.notifyDataSetChanged()
                    }
        }.start()

    }




   private inner class listCalcAdapter(val calcHistory: List<CalcModel>) : RecyclerView.Adapter<listCalcAdapter.listCalcViewHolder>() {


       override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): listCalcViewHolder {
            val view = layoutInflater.inflate(android.R.layout.simple_list_item_1,parent,false)
           return listCalcViewHolder(view)
       }

       override fun onBindViewHolder(holder: listCalcViewHolder, position: Int) {
          val currentValue = calcHistory[position]
           holder.bind(currentValue)
       }

       override fun getItemCount(): Int {
          return calcHistory.size
       }


       inner class listCalcViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
           fun bind(item: CalcModel){
              val tv = itemView as TextView
              val sdf = SimpleDateFormat("dd/MM/yyyy" , Locale("pt", "BR"))
              val data = sdf.format(item.createdDate)

              val res = item.result
              tv.text = getString(R.string.list_response, res, data)

           }
       }


   }
}
