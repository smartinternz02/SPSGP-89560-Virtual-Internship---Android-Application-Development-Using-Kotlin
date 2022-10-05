package com.websarve.wings.android.androidmapapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import java.net.URLEncoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    var _menuList: MutableList<MutableMap<String,String>> = mutableListOf()
    private val _helper = DatabaseHelper(this@MainActivity)

    /**
     * onCreate
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("MainActivity onCreate", "start!!")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val historyList = findViewById<ListView>(R.id.lvHistory)
        historyList.onItemClickListener = ListItemClickListener()


        readMap()
        updateHistory()
        Log.i("MainActivity onCreate", "end!!")
    }

    override fun onDestroy() {
        _helper.close()
        super.onDestroy()
    }

    /**
     * onItemClick
     */
    private inner class ListItemClickListener : AdapterView.OnItemClickListener{
        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, Id: Long) {
            Log.i("MainActivity ListItemClickListener", "start!!")
            val item = parent.getItemAtPosition(position) as MutableMap<String, String>
            openMap(item["position"] as String)
            Log.i("MainActivity ListItemClickListener", "end!!")
        }
    }

    /**
     * onCreateOptionsMenu
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Log.i("MainActivity onCreateOptionsMenu", "start!!")
        menuInflater.inflate(R.menu.menu_option_menu_list, menu)
        Log.i("MainActivity onCreateOptionsMenu", "end!!")
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * onOptionsItemSelected
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.i("MainActivity onOptionsItemSelected", "start!!")
        when(item.itemId){
            R.id.menuListOptionLocation -> {
                Log.i("MainActivity onOptionsItemSelected", "selected menuListOptionLocation")
                val intent = Intent(applicationContext, GPSActivity::class.java)
                startActivity(intent)
            }
            R.id.menuListOptionExit -> {
                Log.i("MainActivity onOptionsItemSelected", "selected menuListOptionExit")
                finish()
            }
            R.id.menu_list_option_delete -> {
                Log.i("MainActivity onOptionsItemSelected", "selected menu_list_option_delete")

                clearMap()
                Toast.makeText(applicationContext, "History Cleared", Toast.LENGTH_LONG).show()
            }
        }
        Log.i("MainActivity onOptionsItemSelected", "end!!")
        return super.onOptionsItemSelected(item)
    }

    /**
     * updateHistory
     */
    private fun updateHistory(){
        Log.i("MainActivity updateHistory", "start!!")

        val lvHistory = findViewById<ListView>(R.id.lvHistory)
        val from = arrayOf("position","day")
        val to = intArrayOf(android.R.id.text1, android.R.id.text2)
        val adapter = SimpleAdapter(applicationContext, _menuList, android.R.layout.simple_list_item_2, from, to)
        lvHistory.adapter = adapter
        Log.i("MainActivity updateHistory", "end!!")
    }

    /**
     * onMapButtonClick MAP
     */
    fun onKeywordMapButtonClick(view: View){
        Log.i("MainActivity onKeywordMapButtonClick", "start!!")
        val etKey = findViewById<EditText>(R.id.edKeyInput).text.toString()
        if(etKey.isEmpty())
        {
            Log.i("MainActivity onKeywordMapButtonClick", "keyword Empty !!")
            return
        }
        openMap(etKey)


        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val formatted = current.format(formatter)
        val menu = mutableMapOf("position" to etKey, "day" to formatted)
        _menuList.add(0,menu)
        updateHistory()

        // DB
        registerMap(etKey, formatted)
        Log.i("MainActivity onMapButtonClick", "end!!")
    }

    /**
     * openMap
     */
    private fun openMap(keyword: String){
        Log.i("MainActivity openMap", "start!!")
        var keyUrl = URLEncoder.encode(keyword, "UTF-8")
        val keyUri = "geo:0,0?q=${keyUrl}"
        val uri = Uri.parse(keyUri)
        val intent = Intent(Intent.ACTION_VIEW, uri)

        Log.i("MainActivity onMapButtonClick", "startActivity!!")
        startActivity(intent)
        Log.i("MainActivity openMap", "end!!")
    }

    /**
     * registerMap
     */
    fun registerMap(keyword: String, date:String){
        Log.i("MainActivity registerMap", "start!!")
        val db = _helper.writableDatabase
        Log.i("MainActivity registerMap keyword", keyword)
        Log.i("MainActivity registerMap date", date)

        // AUTOINCRIMENT
        val sql = "INSERT INTO maps (keyword, date) VALUES (?,?)"
        Log.i("MainActivity compileStatement", "compileStatement")
        val stmt = db.compileStatement(sql)
        Log.i("MainActivity bindString", "bindstring")
        stmt.bindString(1, keyword)
        stmt.bindString(2, date)
        stmt.executeInsert()
        Log.i("MainActivity registerMap", "end!!")
    }

    /**
     * clearMap
     */
    fun clearMap(){
        Log.i("MainActivity clearMap", "start!!")
        val db = _helper.writableDatabase
        db.delete("maps",null, null)

        _menuList.clear()
        updateHistory()
        Log.i("MainActivity clearMap", "end!!")
    }

    /**
     * readMap
     */
    fun readMap(){
        Log.i("MainActivity readMap", "start!!")

        var id = ""
        var keyword = ""
        var date = ""
        var menu = mutableMapOf<String,String>()

        val db = _helper.writableDatabase
        val sql = "SELECT * FROM maps"
        val cursor = db.rawQuery(sql,null)
        Log.i("MainActivity rawQuery", "rawQuery")

        while(cursor.moveToNext()){
            var idxNote = cursor.getColumnIndex("keyword")
            keyword = cursor.getString(idxNote)
            Log.i("MainActivity keyword", keyword)

            idxNote = cursor.getColumnIndex("date")
            date = cursor.getString(idxNote)
            Log.i("MainActivity date", date)
            menu = mutableMapOf("position" to keyword, "day" to date)

            _menuList.add(0,menu)
        }
        Log.i("MainActivity readMap", "end!!")
    }
}