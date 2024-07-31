package com.example.datastoragesql

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var db: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        var rollNo = findViewById<EditText>(R.id.rollNo)
        var name = findViewById<EditText>(R.id.name)
        var marks = findViewById<EditText>(R.id.marks)
        var insertButton = findViewById<Button>(R.id.insert)
        val viewAllButton = findViewById<Button>(R.id.viewAll)
        db = openOrCreateDatabase("studentDB", Context.MODE_PRIVATE, null)
        db.execSQL("CREATE TABLE IF NOT EXISTS student(rollno VARCHAR,name VARCHAR,marks VARCHAR);")

        insertButton.setOnClickListener {
            if (rollNo.text.isEmpty()) {
                rollNo.error = "Rollno should not be empty"
            } else if (name.text.isEmpty()) {
                name.error = "Name should not be empty"
            } else if (marks.text.isEmpty()) {
                marks.error = "Marks should not be empty"
            } else {
                db.execSQL("INSERT INTO student VALUES('" + rollNo.text + "','" + name.text + "','" + marks.text + "');");
                Toast.makeText(this, "Record Added", Toast.LENGTH_SHORT).show()
                rollNo.setText("")
                name.setText("")
                marks.setText("")


            }


        }

        viewAllButton.setOnClickListener {
            val cursor = db.rawQuery("SELECT name FROM student", null)
            if (cursor.count == 0) {
                Toast.makeText(this, "No Record Found", Toast.LENGTH_SHORT).show()
            }
            val buffer = StringBuffer()
            while (cursor.moveToNext()) {
                buffer.append("Rollno:" + cursor.getString(0) + "\n")
                buffer.append("Name:" + cursor.getString(1) + "\n")
                buffer.append("Marks" + cursor.getString(2) + "\n\n")
            }

            showMessage("Student Details", buffer.toString())


        }


    }

    private fun showMessage(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.show()


    }
}