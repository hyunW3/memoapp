package com.example.memoapp_1

import android.content.SharedPreferences
import android.os.Bundle
import android.os.SystemClock
import android.widget.CalendarView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.d_day.*
import java.util.Calendar

class d_day : AppCompatActivity() {
    var prefs1: SharedPreferences? =null
    var val_year =1
    var val_month=1
    var val_day =1
    val CMD_PUT:Int = 3
    var day_left:String? =null
    val Key_title:Int =7
    val Key_day:Int=8
    val Key_day_left:Int = 9
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.d_day)
        prefs1 = getSharedPreferences("Pref1", MODE_PRIVATE);
        //Toast.makeText(applicationContext,"year : $val_year",Toast.LENGTH_SHORT).show()
        if(val_year == 1){ // resume case
            checkFirstRun()
        }
        CalendarView.setOnDateChangeListener { calendarView: CalendarView, year: Int, month: Int, day: Int ->
                d_day_info.text = String.format("%d %d %d", year, month + 1, day)
                val_year = year
                val_month = month+1
                val_day = day

            //var present_date = Calendar.getInstance()
            //d_day_info.text = "${present_date.get(Calendar.YEAR)} month ${present_date.get(Calendar.MONTH)} day ${present_date.get(Calendar.DAY_OF_MONTH)}".toString()

            var present_date = Calendar.getInstance().timeInMillis
            var d_day_date = Calendar.getInstance()
            d_day_date.set(val_year,val_month-1,val_day)
            var d_day = d_day_date.timeInMillis
            day_left = (((d_day -present_date)/(24*60*60*1000))).toString()
            var int_day_left:Int = (((d_day -present_date)/(24*60*60*1000))).toInt()
            display_7segment(int_day_left)

            //Toast.makeText(applicationContext,"$day_left $val_year month $val_month Day $val_day",Toast.LENGTH_SHORT).show()
            val txt:String = "오늘로부터${day_left}일 남았습니다."
            d_day_info.text = txt
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        if(Dday_title.text.length!! <2){
            kvssd_op(CMD_PUT, Key_title, "${Dday_title.text} ")

        }else   kvssd_op(CMD_PUT, Key_title, "${Dday_title.text}")
        kvssd_op(CMD_PUT,Key_day,"${val_year}_${val_month}/${val_day}")

        if(day_left?.length!! <2){
            kvssd_op(CMD_PUT, Key_day_left, " ${day_left}")
        }else kvssd_op(CMD_PUT, Key_day_left, "${day_left}")
        Toast.makeText(applicationContext, "onDestroy started $day_left", Toast.LENGTH_SHORT).show()

    }

    fun checkFirstRun() {
        val isFirstRun1: Boolean = prefs1!!.getBoolean("isFirstRun1", true)
        if (isFirstRun1) {
            Toast.makeText(applicationContext, "is firstRun1",Toast.LENGTH_SHORT).show()
            kvssd_op(CMD_PUT, Key_title,"New D-day")
            kvssd_op(CMD_PUT, Key_day, "2021_05/26")
            kvssd_op(CMD_PUT, Key_day_left, "00")
            prefs1!!.edit().putBoolean("isFirstRun1",false).apply()
        }else {
            day_left= get_key_kvssd(Key_day_left)
            var date = get_key_kvssd(Key_day)
            var int_day_left:Int = parse_(date)
            display_7segment(int_day_left)
            //Toast.makeText(applicationContext, "day_left:$day_left",Toast.LENGTH_SHORT).show()
            val txt:String = "${date}:오늘로부터${day_left}일 남았습니다."
            d_day_info.text = txt
            val title_txt = get_key_kvssd(Key_title)
            Dday_title.setText(title_txt)
        }
    }

    private fun parse_(date: String) :Int{
        var present_date = Calendar.getInstance().timeInMillis
        var d_day_date = Calendar.getInstance()
        val_year = date.split("_")[0].toInt()
        var str:String = date.split("_")[1]
        val_month = str.split("/")[0].toInt()
        val_day = str.split("/")[1].toInt()
        //Toast.makeText(applicationContext, "$val_year $val_month $val_day",Toast.LENGTH_SHORT).show()
        d_day_date.set(val_year,val_month-1,val_day)
        CalendarView.setDate(d_day_date.timeInMillis,true,true )
        return (((d_day_date.timeInMillis -present_date)/(24*60*60*1000))).toInt()
    }

    external fun display_7segment(value: Int)
    external fun led_on(position: Int)
    external fun led_off(position: Int)
    external fun kvssd_op(op_num: Int, key: Int, value: String?)
    external fun get_key_kvssd(key: Int): String
    companion object {
        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}