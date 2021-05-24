package com.example.memoapp_1


import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.SystemClock
import android.provider.Settings
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.memo_main.*


class MainActivity : AppCompatActivity(), View.OnClickListener{
    private var present_id:Int=0
    private var item_number:Int=0
    private var max_id:Int=0
    //var tvIds: List<Int> = {R.id.memo9 ,R.id.memo4,R.id.memo5,R.id.memo6}
    private var after_write:Int =0
    private var db:MemoDatabase? = null
    val CMD_PUT:Int = 3
    var prefs: SharedPreferences? =null
    var rv_id = arrayListOf<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Example of a call to a native method
        btn_plus.setOnClickListener{
            onClick(it)
        }
        db = MemoDatabase.getInstance(this)
        prefs = getSharedPreferences("Pref", MODE_PRIVATE);
        layout_for_memos.setHasFixedSize(true)
        layout_for_memos.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        db!!.memoDao().getAll_live().observe(this, androidx.lifecycle.Observer<List<Memo>>() {
            var adapter = RecyclerViewAdapter(this, db!!, it)
            layout_for_memos.adapter = adapter
            //Toast.makeText(applicationContext, "database ${adapter.itemCount}",Toast.LENGTH_SHORT).show()
            item_number = adapter.itemCount
            if(item_number!=0) max_id = adapter.getItemId(0).toInt()
            else max_id =0
            //Toast.makeText(applicationContext, "max_id $max_id", Toast.LENGTH_SHORT).show()

            display_7segment(item_number)
            if (after_write != 1) {
                //Toast.makeText(applicationContext, "item changed started $item_number", Toast.LENGTH_SHORT).show()
                val tag_all = arrayOf(Tag0, Tag1, Tag2, Tag3, Tag4, Tag5, Tag6)
                for (i in 0 until 7) {
                    val tagname: String = get_key_kvssd(i)
                    tag_all[i].text = tagname
                }
            }


        })

        layout_for_memos.addOnItemTouchListener(object : RecyclerView.
        OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                if (e.action == MotionEvent.ACTION_MOVE) {
                    //MotionEvent.ACTION_BUTTON_PRESS //11
                } else if (e.action == 1) {
                    var child = rv.findChildViewUnder(e.getX(), e.getY())
                    if (child != null) {
                        var position = rv.getChildAdapterPosition(child)
                        var view = rv.layoutManager?.findViewByPosition(position)
                        view?.setBackgroundResource(R.color.ligth_gray)
                        //present_id = item_number - position - 1
                        present_id = rv.adapter?.getItemId(position)!!.toInt()
                        //if(present_id > max_id) max_id = present_id
                        for (i in 0..rv.adapter!!.itemCount) {
                            var otherView = rv.layoutManager?.findViewByPosition(i)
                            if (otherView != view) {
                                otherView?.setBackgroundResource(R.color.white)
                            } else {
                            }
                        }
                        //Toast.makeText(applicationContext, "clicked $item_number $position ${rv.adapter?.getItemId(position)?.toInt()} $max_id", Toast.LENGTH_SHORT).show()

                        onClick(view)
                    }
                }
                return false
            }
            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
                //TODO("Not yet implemented")
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
                //TODO("Not yet implemented")
            }
        })
        checkFirstRun()

    }


    override fun onClick(v: View?){
        if(v?.id == btn_plus?.id){
            max_id++
            present_id = max_id
            display_7segment(0)
        }
        val intent = Intent(this, memo_main::class.java)
        var tag_all = arrayOf(Tag0, Tag1, Tag2, Tag3, Tag4, Tag5, Tag6)
        var tag_string = arrayListOf<String>()
        for(i in 0 until 7){
            tag_string.add(tag_all[i].text.toString())
            //tag_string.add("aa")
        }
        //makeText(applicationContext,"$tag_string",Toast.LENGTH_SHORT).show()
        intent.putExtra("ID", present_id)
        intent.putExtra("tag", tag_string)
        startActivityForResult(intent, 100)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        after_write = 1
        val extra: Bundle? = intent.extras
        //makeText(applicationContext,"${data?.extras?.getString("test")}",Toast.LENGTH_SHORT).show()
        if(requestCode == 100 && resultCode == Activity.RESULT_OK){//&& data != null){

            var tag_string = arrayListOf<String>()
            if(data?.hasExtra("tag") != null){
                tag_string = data?.getStringArrayListExtra("tag") as ArrayList<String>
                var tag_all = arrayOf(Tag0, Tag1, Tag2, Tag3, Tag4, Tag5, Tag6)
                for(i in 0 until 7){
                    var tmp = tag_string[i]
                    tag_all[i].text = tmp
                }
            }else {
                Toast.makeText(applicationContext,"no tag",Toast.LENGTH_SHORT).show()
            }
        } else Toast.makeText(applicationContext,"data == null",Toast.LENGTH_SHORT).show()
        display_7segment(item_number)
        for(i in 0 until 7){
            led_off(i)
        }
    }

    fun checkFirstRun() {
        val isFirstRun: Boolean = prefs!!.getBoolean("isFirstRun", true)
        if (isFirstRun) {
            Toast.makeText(applicationContext, "is firstRun",Toast.LENGTH_SHORT).show()
            for (i in 0 until 7) {
                kvssd_op(CMD_PUT, i, "T$i")
            }
            prefs!!.edit().putBoolean("isFirstRun",false).apply()
        }
    }
    override fun onStart() {
        super.onStart()
        //Toast.makeText(applicationContext,"onStart started $item_number",Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        val tag_all = arrayOf(Tag0, Tag1, Tag2, Tag3, Tag4, Tag5, Tag6)
        for(i in 0 until 7){
            kvssd_op(CMD_PUT, i, tag_all[i].text.toString())
        }
    Toast.makeText(applicationContext, "onDestroy started", Toast.LENGTH_SHORT).show()

    }
    /*
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */


    external fun display_7segment(value: Int)
    external fun led_on(position: Int)
    external fun led_off(position: Int)
    external fun kvssd_op(op_num: Int, key: Int, value: String?)
    external fun get_key_kvssd(key: Int): String
    companion object {
        // Used to load the 'native-lib' library on application startup.
        init {
            try{
                System.loadLibrary("native-lib")
            } finally {
                    // skip
            }
        }
    }


}
