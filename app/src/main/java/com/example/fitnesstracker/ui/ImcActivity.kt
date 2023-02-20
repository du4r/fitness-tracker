package com.example.fitnesstracker.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.fitnesstracker.App
import com.example.fitnesstracker.R
import com.example.fitnesstracker.models.CalcModel

class ImcActivity : AppCompatActivity() {
    private lateinit var imcBtn: Button
    private lateinit var weightField: EditText
    private lateinit var heightField: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imc)

        imcBtn = findViewById(R.id.btn_imc_send)
        weightField = findViewById(R.id.edit_imc_weight)
        heightField = findViewById(R.id.edit_imc_height)

        imcBtn.setOnClickListener {
            if(Validate()){
                Toast.makeText(this, "preencha todos os campos com valores maiores que 0", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }else{

               val result =  CalculateImc(weightField.text.toString().toInt(),heightField.text.toString().toInt())
                Log.d("Test", "Result: $result")
                val response = imcResponse(result)
                //Toast.makeText(this, response, Toast.LENGTH_LONG).show()

                val title = getString(R.string.imc_response, result)

                AlertDialog.Builder(this)
                    .setTitle(title)
                    .setMessage(response)
                    .setPositiveButton(android.R.string.ok) {
                            dialog, which -> TODO("Not yet implemented")
                    }
                    .setNegativeButton(R.string.save){
                        dialog, which ->

                       Thread{
                           val app = application as App
                           val dao = app.db.calcDao()
                           dao.insert(CalcModel(type = "imc", result = result))

                           runOnUiThread {
                               openListActivity()
                           }

                       }.start()

                    }
                    .create()
                    .show()
                
                //implementacao de methodo de esconder teclado
                val keyboardService = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                keyboardService.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            }
        }
    }

    private fun imcResponse(imc: Double): Int{
        return when{
            imc < 15.0 -> R.string.imc_severely_low_weight
            imc < 16.0 -> R.string.imc_very_low_weight
            imc < 18.5 -> R.string.imc_low_weight
            imc < 25.0 -> R.string.imc_normal_weight
            imc < 30.0 -> R.string.imc_high_weight
            imc < 35.0 -> R.string.imc_so_high_weight
            imc < 40.0 -> R.string.imc_severely_high_weight
            else -> R.string.imc_extreme_weight
        }
    }

    private fun CalculateImc(weight: Int, height: Int): Double {
        return weight/((height/100.0) * (height/100.0))
    }

    private fun Validate(): Boolean{
        //se peso ou altura forem = 0 ou vazio retorne false
        return(weightField.text.toString().isEmpty()
            || weightField.text.toString().startsWith("0")
            || heightField.text.toString().isEmpty()
            || heightField.text.toString().startsWith("0")
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.menu_search){
            finish()
            openListActivity()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openListActivity(){
        val intent = Intent(this@ImcActivity, ListCalcActivity::class.java)
        intent.putExtra("type", "imc")
        startActivity(intent)
    }
}