@file:Suppress("SyntaxError")

package jwhh.com

import android.os.Bundle


import androidx.appcompat.app.AppCompatActivity

import androidx.navigation.ui.AppBarConfiguration

import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView

import jwhh.com.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    //private val textNoteTitle = "textNoteTitle"
    //private val textNoteText = "textNoteText"
    //private val spinnerCourses= "'spinnerCourses"
    private var notePosition = POSITION_NOT_SET

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        //val dm = DataManager()

        val adapterCourses = ArrayAdapter<CourseInfo>(
            this@MainActivity,
            android.R.layout.simple_spinner_item,
            DataManager.courses.values.toList()
        )

        // val adapterCourses = <CourseInfo>(this,android.R.layout.simple_spinner_item, dm.courses.values.toString())
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val spinnerCourses: Spinner = findViewById(R.id.spinnerCourses)

        spinnerCourses.adapter = adapterCourses
        notePosition = intent.getIntExtra(EXTRA_NOTE_POSITION, POSITION_NOT_SET)

        if (notePosition != POSITION_NOT_SET)
            displayNote()
        else{

            DataManager.notes.add(NoteInfo())
            notePosition = DataManager.notes.lastIndex


        }
    }



    private fun displayNote() {
        val note = DataManager.notes[notePosition]
        val title = findViewById<TextView>(R.id.textNoteTitle).setText(note.title)
        val text = findViewById<TextView>(R.id.textNoteText).setText(note.text)


        val coursePosition = DataManager.courses.values.indexOf(note.course)

        val spinner = findViewById<Spinner>(R.id.spinnerCourses).setSelection(coursePosition)


    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_next -> {
                moveNext()
                true

            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun moveNext() {

        invalidateOptionsMenu()
        ++notePosition
        displayNote()
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {

        if (notePosition >= DataManager.notes.lastIndex) {
            val menuItem = menu?.findItem(R.id.action_next)


            if (menuItem != null) {
                menuItem.icon = getDrawable(R.drawable.ic_white_block_24)
                menuItem.isEnabled = false
            }
        }
            return super.onPrepareOptionsMenu(menu)
        }

    override fun onPause() {
        super.onPause()

        saveNote()
    }

    private fun saveNote() {
        val note = DataManager.notes[notePosition]
        note.title = findViewById<TextView>(R.id.textNoteTitle).text.toString()
        note.text = findViewById<TextView>(R.id.textNoteText).text.toString()
        note.course = findViewById<Spinner>(R.id.spinnerCourses).selectedItem as CourseInfo
    }
}





