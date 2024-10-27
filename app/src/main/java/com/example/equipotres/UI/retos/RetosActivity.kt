package com.example.equipotres.ui.retos

import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.equipotres.R
import com.example.equipotres.database.RetoDatabaseHelper
import com.example.equipotres.models.Reto
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RetosActivity : AppCompatActivity() {

    private lateinit var dbHelper: RetoDatabaseHelper
    private lateinit var retoAdapter: RetoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retos)

        dbHelper = RetoDatabaseHelper.getInstance(this)
        retoAdapter = RetoAdapter(
            dbHelper.getAllRetos().toMutableList(),
            onEditClick = { reto -> showRetoDialog(reto) },
            onDeleteClick = { reto -> confirmDeleteReto(reto) }
        )

        setupToolbar()
        setupRecyclerView()
        setupAddRetoButton()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val titleTextView = TextView(this).apply {
            text = getString(R.string.retos)
            textSize = 24f
            setTextColor(ContextCompat.getColor(this@RetosActivity, R.color.carrot_orange))
            setTypeface(null, Typeface.BOLD)
            textAlignment = TextView.TEXT_ALIGNMENT_CENTER
            layoutParams = Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT,
                Toolbar.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER
            }
        }
        toolbar.addView(titleTextView)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.navigationIcon?.setTint(ContextCompat.getColor(this, R.color.carrot_orange))
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun setupRecyclerView() {
        findViewById<RecyclerView>(R.id.recycler_view_retos).apply {
            adapter = retoAdapter
            layoutManager = LinearLayoutManager(this@RetosActivity)
        }
    }

    private fun setupAddRetoButton() {
        findViewById<FloatingActionButton>(R.id.fab_add_reto).setOnClickListener {
            showRetoDialog()
        }
    }

    private fun showRetoDialog(reto: Reto? = null) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_reto, null)
        val dialogBuilder = AlertDialog.Builder(this).setView(dialogView)

        dialogView.findViewById<TextView>(R.id.dialog_title).text =
            if (reto == null) "Agregar reto" else "Editar reto"
        val descriptionEditText = dialogView.findViewById<EditText>(R.id.dialog_description)
        descriptionEditText.setText(reto?.description)

        val dialog = dialogBuilder.create().apply { setCancelable(false) }

        dialogView.findViewById<Button>(R.id.cancel_button).setOnClickListener { dialog.dismiss() }
        dialogView.findViewById<Button>(R.id.save_button).setOnClickListener {
            val description = descriptionEditText.text.toString()
            if (description.isNotBlank()) {
                if (reto == null) {
                    val newReto = Reto(
                        description = description,
                        iconResId = R.drawable.beer,
                        createdAt = System.currentTimeMillis().toString()
                    )
                    dbHelper.addReto(newReto)
                    retoAdapter.addReto(newReto)
                } else {
                    val updatedReto = reto.copy(description = description)
                    dbHelper.updateReto(updatedReto)
                    retoAdapter.updateReto(updatedReto)
                }
                dialog.dismiss()
            } else {
                Toast.makeText(this, "La descripción no puede estar vacía", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show()
    }

    private fun confirmDeleteReto(reto: Reto) {
        AlertDialog.Builder(this)
            .setMessage("¿Estás seguro de que deseas eliminar este reto?")
            .setPositiveButton("Eliminar") { _, _ ->
                deleteReto(reto)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun deleteReto(reto: Reto) {
        dbHelper.deleteReto(reto)
        val updatedRetos = dbHelper.getAllRetos().toMutableList()
        retoAdapter.setRetos(updatedRetos)
    }
}