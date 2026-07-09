package id.optibis.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Perintah di bawah ini bertugas memanggil layout buatan Anda ke emulator
        setContentView(R.layout.activity_main)
    }
}