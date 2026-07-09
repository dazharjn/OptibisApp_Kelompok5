package id.optibis.app

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class KotakMasukActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotak_masuk)

        val listView = findViewById<ListView>(R.id.listLaporan)

        // Menampung teks tampilan laporan
        val daftarLaporan = mutableListOf<String>()
        // Menampung ID unik Firebase dari masing-masing laporan
        val daftarKey = mutableListOf<String>()

        val ref = FirebaseDatabase.getInstance("https://optibis-id-76e73-default-rtdb.asia-southeast1.firebasedatabase.app")
            .getReference("laporan_harian")

        // Fungsi untuk mengambil data dari Firebase
        fun muatDataLaporan() {
            ref.get().addOnSuccessListener { snapshot ->
                daftarLaporan.clear()
                daftarKey.clear()

                if (snapshot.exists()) {
                    for (item in snapshot.children) {
                        val key = item.key ?: ""
                        val nama = item.child("nama").value?.toString() ?: "-"
                        val role = item.child("role").value?.toString() ?: "-"
                        val isi = item.child("isi").value?.toString() ?: "-"
                        val tanggal = item.child("tanggal").value?.toString() ?: "-"

                        daftarLaporan.add("[$role] $nama\n$tanggal\n\n$isi")
                        daftarKey.add(key)
                    }
                    // Dibalik agar yang terbaru muncul di atas
                    daftarLaporan.reverse()
                    daftarKey.reverse()
                } else {
                    daftarLaporan.add("Belum ada laporan masuk.")
                    daftarKey.add("")
                }

                // Membuat custom adapter agar bisa mengontrol tombol hapus di dalam list
                val adapter = object : ArrayAdapter<String>(this, R.layout.item_laporan_staff, android.R.id.text1, daftarLaporan) {
                    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                        val view = super.getView(position, convertView, parent)
                        val btnHapus = view.findViewById<TextView>(R.id.btnHapusLaporan)
                        val keyLaporan = daftarKey[position]

                        // Jika list kosong/tidak ada key, sembunyikan tombol hapus
                        if (keyLaporan.isEmpty()) {
                            btnHapus.visibility = View.GONE
                        } else {
                            btnHapus.visibility = View.VISIBLE // <-- GANTI MENJADI INI
                            btnHapus.setOnClickListener {
                                // Eksekusi hapus ke Firebase berdasarkan ID (key)
                                ref.child(keyLaporan).removeValue()
                                    .addOnSuccessListener {
                                        Toast.makeText(context, "Laporan berhasil dihapus", Toast.LENGTH_SHORT).show()
                                        muatDataLaporan() // Refresh tampilan setelah dihapus
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(context, "Gagal menghapus: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        }
                        return view
                    }
                }
                listView.adapter = adapter

            }.addOnFailureListener { e ->
                Toast.makeText(this, "Gagal memuat laporan: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        // Jalankan fungsi memuat data pertama kali saat halaman dibuka
        muatDataLaporan()
    }
}