package id.optibis.app

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_menu)

        val judul = intent.getStringExtra("JUDUL") ?: "Menu"
        val deskripsi = intent.getStringExtra("DESKRIPSI") ?: ""

        findViewById<TextView>(R.id.tvJudulDetail).text = judul
        findViewById<TextView>(R.id.tvDeskripsiDetail).text = deskripsi
    }
}