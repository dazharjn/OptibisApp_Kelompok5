package id.optibis.app

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class LaporanHarianActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_laporan_harian)

        val roleUser = intent.getStringExtra("USER_ROLE") ?: ""
        val namaUser = intent.getStringExtra("USER_NAMA") ?: ""

        val etIsiLaporan = findViewById<EditText>(R.id.etIsiLaporan)
        val btnKirimLaporan = findViewById<Button>(R.id.btnKirimLaporan)

        btnKirimLaporan.setOnClickListener {
            val isi = etIsiLaporan.text.toString().trim()
            if (isi.isEmpty()) {
                Toast.makeText(this, "Isi laporan tidak boleh kosong!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            btnKirimLaporan.isEnabled = false

            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
            val tanggal = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id")).format(Date())

            val dataLaporan = mapOf(
                "uid" to uid,
                "nama" to namaUser,
                "role" to roleUser,
                "isi" to isi,
                "tanggal" to tanggal,
                "timestamp" to System.currentTimeMillis()
            )

            val ref = FirebaseDatabase.getInstance("https://optibis-id-76e73-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("laporan_harian").push()

            ref.setValue(dataLaporan)
                .addOnSuccessListener {
                    Toast.makeText(this, "Laporan berhasil dikirim ke Owner!", Toast.LENGTH_LONG).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    btnKirimLaporan.isEnabled = true
                    Toast.makeText(this, "Gagal mengirim laporan: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}