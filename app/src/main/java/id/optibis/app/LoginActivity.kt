package id.optibis.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan Password wajib diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid ?: ""

                    // Ambil Role dari Realtime Database berdasarkan UID
                    val ref = FirebaseDatabase.getInstance("https://optibis-id-76e73-default-rtdb.asia-southeast1.firebasedatabase.app")
                        .getReference("users").child(uid)
                    ref.get().addOnSuccessListener { snapshot ->
                        if (snapshot.exists()) {
                            val nama = snapshot.child("nama").value.toString()
                            val role = snapshot.child("role").value.toString()

                            // Pindah ke Dashboard dengan membawa data Asli
                            val intent = Intent(this, DashboardActivity::class.java)
                            intent.putExtra("USER_NAMA", nama)
                            intent.putExtra("USER_ROLE", role)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Data role tidak ditemukan!", Toast.LENGTH_SHORT).show()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(this, "Gagal mengambil data database", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Login Gagal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}