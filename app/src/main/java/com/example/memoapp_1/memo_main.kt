package com.example.memoapp_1

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.memo_main.*

class memo_main : AppCompatActivity(), View.OnClickListener {
    var present_id:Int=0
    var taginfo = arrayOf<Int>()
    val tag:Array<Int> = arrayOf(0,1,2,3,4,5,6)
    val CMD_PUT:Int = 3
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.memo_main)
        var tag_string = arrayListOf<String>()

        val extra: Bundle? = intent.extras
        if(extra != null){
            //var memo_id:Int = extra.getInt("ID")
            present_id = extra.getInt("ID")
            tag_string = extra.getStringArrayList("tag") as ArrayList<String>
        }
        val tag_all = arrayOf(Tag0_text,Tag1_text,Tag2_text,Tag3_text,Tag4_text,Tag5_text,Tag6_text)
        val tag_button = arrayOf(Tag0_check,Tag1_check,Tag2_check,Tag3_check,Tag4_check,Tag5_check,Tag6_check)
        for(i in 0 until 7){
            tag_all[i].setText(tag_string[i])
            led_off(i)
            tag_button[i].setOnClickListener{
                checking(it,tag_button[i],i)
            }
        }
        //TODO get info related to memoID
        //Toast.makeText(applicationContext, "Clicked ${present_id}", Toast.LENGTH_SHORT).show()

        var memo1: Memo? = null
        var job = Thread({
            memo1 = MemoDatabase.getInstance(applicationContext).memoDao().getByid(present_id)
        }).start()
        SystemClock.sleep(50)
        memo_main_Title.setText(memo1?.title)
        memo_main_Content.setText(memo1?.contents)
        //Toast.makeText(applicationContext, "Clicked ${memo1?.id} ${memo1?.title} ", Toast.LENGTH_SHORT).show()
      /*
        kvssd_op(CMD_PUT,0,"AAAA")
        val name:String = get_key_kvssd(1)
        Toast.makeText(applicationContext, "Tag $name ", Toast.LENGTH_SHORT).show()
*/

        save_button.setOnClickListener{
            onClick(it)
        }

    }

    private fun checking(v:View?, checkBox: CheckBox,position: Int) {
        if(checkBox.isChecked == true){
            //led_off(position)
            led_on(position)
        }else {
            led_off(position)
        }

    }


    override fun onClick(v: View?) {
        val intent = Intent()
        val title:String = memo_main_Title.text.toString()
        val content:String = memo_main_Content.text.toString()
        // db에 추가
        Thread(Runnable {
            MemoDatabase.getInstance(applicationContext)
                    .memoDao().insert(Memo(present_id,title,content))
            // TODO update case
        }).start()
        var tag_string = arrayListOf<String>()
        val tag_all = arrayOf(Tag0_text,Tag1_text,Tag2_text,Tag3_text,Tag4_text,Tag5_text,Tag6_text)
        for(i in 0 until 7){
            var str:String = tag_all[i].text.toString()
            if(str.length <2){
                str = tag_all[i].text.toString()+"  "
            }
            tag_string.add(str)

        }
        intent.putExtra("tag",tag_string)
        setResult(Activity.RESULT_OK,intent)

        finish()
    }

    external fun led_on(position:Int)
    external fun led_off(position:Int)
    companion object {
        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }

}
