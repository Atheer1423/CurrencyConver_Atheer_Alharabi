package com.example.currencyconver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.View
import android.widget.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private var Details1: Details? = null
    lateinit var input: EditText
    lateinit var b1: Button
    lateinit var spinner: Spinner
    lateinit var tv: TextView
    lateinit var date: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        input=findViewById(R.id.ed)
        b1=findViewById(R.id.b1)
        spinner=findViewById(R.id.sp)
        tv=findViewById(R.id.tv)
        date=findViewById(R.id.date)


        var cur = arrayListOf("gbp", "usd", "aud", "sar", "kwd", "jpy", "syp")

        var selection = 0

        if (spinner != null) {
            var adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cur)
            spinner.adapter = adapter
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    selection=position
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        }

        b1.setOnClickListener {

            var sel = input.text.toString()
            var currency: Double = sel.toDouble()

            getCurrency(onResult = {
                Details1 = it

                when (selection) {
                    0 -> disp(calc(Details1?.eur?.aud?.toDouble(), currency))
                    1 -> disp(calc(Details1?.eur?.usd?.toDouble(), currency))
                    2 -> disp(calc(Details1?.eur?.aud?.toDouble(), currency))
                    3 -> disp(calc(Details1?.eur?.sar?.toDouble(), currency))
                    4 -> disp(calc(Details1?.eur?.kwd?.toDouble(), currency))
                    5 -> disp(calc(Details1?.eur?.jpy?.toDouble(), currency))
                    6 -> disp(calc(Details1?.eur?.syp?.toDouble(), currency))
                    7 -> disp(calc(Details1?.eur?.gbp?.toDouble(), currency))
                }
            })
        }

    }

    private fun disp(calc: Double) {



       tv.text = "result " + calc
    }

    private fun calc(i: Double?, sel: Double): Double {
        var s = 0.0
        if (i != null) {
            s = (i * sel)
        }
        return s
    }

    private fun getCurrency(onResult: (Details?) -> Unit) {
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)

        if (apiInterface != null) {
            apiInterface.getCurrency()?.enqueue( object : Callback<Details> {
                override fun onResponse(
                    call: Call<Details>,
                    response: Response<Details>
                ) {
                    onResult(response.body())

                }

                override fun onFailure(call: Call<Details>, t: Throwable) {
                    onResult(null)
                    Toast.makeText(applicationContext, "" + t.message, Toast.LENGTH_SHORT).show();
                }

            })
        }
    }
}