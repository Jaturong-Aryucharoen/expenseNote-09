package com.example.expensnote_09

import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var etAmount: EditText
    private lateinit var etNote: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnClear: Button
    private lateinit var tvLastItem: TextView
    private lateinit var tvTotal: TextView

    private var total: Double = 0.0

    private var totalAmount = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etAmount = findViewById<EditText>(R.id.etAmount)
        etNote = findViewById<EditText>(R.id.etNote)
        btnClear = findViewById<Button>(R.id.btnClear)
        btnAdd = findViewById<Button>(R.id.btnAdd)
        tvLastItem = findViewById<TextView>(R.id.tvLastItem)
        tvTotal = findViewById<TextView>(R.id.tvTotal)

        btnAdd.setOnClickListener {
            val amountText = etAmount.text.toString().trim()
            val noteText = etNote.text.toString().trim()

            if (amountText.isEmpty()){
                Toast.makeText(this,"กรุณากรอกจํานวนเงิน", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val amount = amountText.toDoubleOrNull()
            if (amount == null){
                Toast.makeText(this,"กรุณากรอกจํานวนเงินเป็นตัวเลข", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
                }

                total += amount

                val showNote = if (noteText.isNotEmpty()) noteText else "ไม่ระบุ"

                tvLastItem.text = "รายการล่าสุด: $showNote - %.2f บาท ".format(amount)
                tvTotal.text = "ยอดรวมวันนี้ %.2f บาท".format(total)

            etAmount.text.clear()
            etNote.text.clear()

        }
        btnClear.setOnClickListener {
            total = 0.0

            etAmount.text.clear()
            etNote.text.clear()

            tvLastItem.text = "รายการล่าสุด: -"
            tvTotal.text = "ยอดรวมวันนี้ 0.00 บาท"
        }

        val etAmount = findViewById<EditText>(R.id.etAmount)
        val etNote = findViewById<EditText>(R.id.etNote)
        val btnClear = findViewById<Button>(R.id.btnClear)
        val btnAdd = findViewById<Button>(R.id.btnAdd)
        val tvLastItem = findViewById<TextView>(R.id.tvLastItem)
        val tvTotal = findViewById<TextView>(R.id.tvTotal)

        btnClear.setOnClickListener {
            hideKeyboard()
            etAmount.text.clear()
            etNote.text.clear()
            etAmount.error = null
            etNote.error = null
            totalAmount = 0.0
            tvLastItem.text = "รายการล่าสุด: -"
            tvTotal.text = "ยอดรวมวันนี้: 0.00 บาท"
            Toast.makeText(this, "ล้างข้อมูลเรียบร้อย", Toast.LENGTH_SHORT).show()
        }

        btnAdd.setOnClickListener {
            val amountStr = etAmount.text.toString().trim()
            val note = etNote.text.toString().trim()

            if (amountStr.isEmpty()) {
                etAmount.error = "กรุณากรอกจำนวนเงิน"
                etAmount.requestFocus()
                return@setOnClickListener
            }

            val amount = amountStr.toDoubleOrNull()
            if (amount == null || amount <= 0) {
                etAmount.error = "จำนวนเงินไม่ถูกต้อง"
                etAmount.requestFocus()
                return@setOnClickListener
            }

            hideKeyboard()

            totalAmount += amount
            val desc = if (note.isNotEmpty()) note else "ไม่ระบุ"
            tvLastItem.text = "รายการล่าสุด: $desc (${String.format(Locale.getDefault(), "%.2f", amount)} บาท)"
            tvTotal.text = "ยอดรวมวันนี้: ${String.format(Locale.getDefault(), "%.2f", totalAmount)} บาท"

            // แสดงข้อความบันทึกสำเร็จ
            val message = "เพิ่มรายการสำเร็จ: ฿$amount" + if (note.isNotEmpty()) " ($note)" else ""
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

            // เคลียร์ฟอร์ม
            etAmount.text.clear()
            etNote.text.clear()
            etAmount.error = null
            etNote.error = null
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        currentFocus?.let { view ->
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
