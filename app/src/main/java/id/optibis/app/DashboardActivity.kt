package id.optibis.app
import android.content.Intent

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import com.google.firebase.auth.FirebaseAuth

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val roleUser = intent.getStringExtra("USER_ROLE") ?: "CS"
        val namaUser = intent.getStringExtra("USER_NAMA") ?: "Staf"

        when (roleUser) {
            "CS" -> setContentView(R.layout.activity_dashboard_cs)
            "DEVELOPER" -> setContentView(R.layout.activity_dashboard_developer)
            "FINANCE" -> setContentView(R.layout.activity_dashboard_finance)
            "MARKETING" -> setContentView(R.layout.activity_dashboard_marketing)
            "OWNER" -> setContentView(R.layout.activity_dashboard_owner)
            else -> setContentView(R.layout.activity_dashboard_cs)
        }

        // Update Nama & Role di Header
        val tvStaffName = findViewById<TextView>(R.id.tvStaffName)
        val chipRole = findViewById<Chip>(R.id.chipRole)
        tvStaffName?.text = namaUser
        findViewById<TextView>(R.id.tvInisial)?.text = namaUser.take(1).uppercase()
        chipRole?.text = "Role: $roleUser"

        // Tombol Logout
        val btnLogout = findViewById<ImageButton>(R.id.btnLogout)
        btnLogout?.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // Card Menu
        val cardMenu1 = findViewById<MaterialCardView>(R.id.cardMenu1)
        val cardMenu2 = findViewById<MaterialCardView>(R.id.cardMenu2)
        val cardMenu3 = findViewById<MaterialCardView>(R.id.cardMenu3)

        // 5. Logika Klik Spesifik Per Divisi
        cardMenu1?.setOnClickListener {
            val (judul, deskripsi) = when (roleUser) {
                "CS" -> "Pesanan Baru Masuk" to "Validasi dan konfirmasi pesanan awal dari klien"
                "DEVELOPER" -> "Pengembangan Kode Web" to "Lihat lembar instruksi fitur dan pengerjaan script website"
                "MARKETING" -> "Pantau Performa Iklan" to "Analisis biaya dan jumlah jangkauan Meta & Google Ads"
                "FINANCE" -> "Verifikasi Pembayaran" to "Cek bukti transfer masuk dan uang muka (DP) dari klien"
                "OWNER" -> "Laporan Keuangan Perusahaan" to "Pantau grafik total laba bersih dan pengeluaran operasional"
                else -> "Menu 1" to ""
            }
            val i = Intent(this, DetailMenuActivity::class.java)
            i.putExtra("JUDUL", judul)
            i.putExtra("DESKRIPSI", deskripsi)
            startActivity(i)
        }

        cardMenu2?.setOnClickListener {
            val (judul, deskripsi) = when (roleUser) {
                "CS" -> "Database Klien" to "Kelola kontak WA, email, dan histori transaksi klien"
                "DEVELOPER" -> "Pelacakan Bug & Error" to "Perbaiki error server dan keluhan teknis dari sistem klien"
                "MARKETING" -> "Data Leads Potensial" to "Kumpulkan data instansi/UMKM yang tertarik bikin web"
                "FINANCE" -> "Laporan & Invoice" to "Buat, cetak, dan kirim nota tagihan proyek kepada instansi klien"
                "OWNER" -> "Evaluasi Kinerja Divisi" to "Monitor persentase kepuasan klien dan kecepatan coding developer"
                else -> "Menu 2" to ""
            }
            val i = Intent(this, DetailMenuActivity::class.java)
            i.putExtra("JUDUL", judul)
            i.putExtra("DESKRIPSI", deskripsi)
            startActivity(i)
        }

        cardMenu3?.setOnClickListener {
            if (roleUser == "OWNER") {
                startActivity(Intent(this, KotakMasukActivity::class.java))
            } else {
                val i = Intent(this, LaporanHarianActivity::class.java)
                i.putExtra("USER_ROLE", roleUser)
                i.putExtra("USER_NAMA", namaUser)
                startActivity(i)
            }
        }
    }
}