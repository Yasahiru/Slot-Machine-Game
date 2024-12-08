package com.cmc.slotmachineapp2

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cmc.slotmachineapp2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var sold = 100
    private var deposit = 0

    fun updateRadioButtonStates() {
        binding.rbtn3.isEnabled = sold >= 5
        binding.rbtn2.isEnabled = sold >= 2
        binding.rbtn1.isEnabled = sold >= 1
    }

    fun updatesold() {
        binding.sold.text = getString(R.string.solde_joueur, sold)
        if (sold == 0) {
            binding.add.isEnabled = true
            binding.add.setOnClickListener {
                val code = binding.secret.text.toString()
                if (code == "1234") {
                    sold += 100
                    updateRadioButtonStates()
                    binding.add.isEnabled = false
                }
                binding.sold.text = "Solde du joueur : $sold$"
                binding.secret.text.clear()
                binding.secret.clearFocus()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        updateRadioButtonStates()
        updatesold()

        if (savedInstanceState != null) {
            sold = savedInstanceState.getInt("sold")
            binding.casse.isChecked = savedInstanceState.getBoolean("caseEnabel")
            binding.rbtn1.isEnabled = savedInstanceState.getBoolean("rbtn1")
            binding.rbtn2.isEnabled = savedInstanceState.getBoolean("rbtn2")
            binding.rbtn3.isEnabled = savedInstanceState.getBoolean("rbtn3")
            binding.rbtn3.isChecked = savedInstanceState.getBoolean("rbtn3check")
            binding.rbtn2.isChecked = savedInstanceState.getBoolean("rbtn2check")
            binding.rbtn1.isChecked = savedInstanceState.getBoolean("rbtn1check")
            binding.play.isEnabled = savedInstanceState.getBoolean("btnstate")

            val checkedboxId = savedInstanceState.getInt("btnId")
            if (checkedboxId != -1) {
                binding.radios.check(checkedboxId)
            }
            updatesold()
        }

        binding.play.isEnabled = false

        val img1 = binding.img1
        val img2 = binding.img2
        val img3 = binding.img3

        val image1 = R.drawable.image1
        val image2 = R.drawable.image2
        val image3 = R.drawable.image3
        val image4 = R.drawable.image4
        val image5 = R.drawable.image5
        val image6 = R.drawable.image6
        val image7 = R.drawable.image7
        val image8 = R.drawable.image8
        val image9 = R.drawable.image9
        val image10 = R.drawable.image10
        val images = listOf(image1, image2, image3, image4, image5, image6, image7, image8, image9, image10)

        binding.add.isEnabled = false

        binding.radios.setOnCheckedChangeListener { _, _ ->
            binding.play.isEnabled = binding.radios.checkedRadioButtonId != -1
            when (binding.radios.checkedRadioButtonId) {
                R.id.rbtn1 -> deposit = 1
                R.id.rbtn2 -> deposit = 2
                R.id.rbtn3 -> deposit = 3
            }
        }

        binding.play.setOnClickListener {
            updatesold()
            var gain = 0
            var win = false

            if (sold >= deposit) {

                sold -= deposit
                updatesold()

                // Randomize images when game starts
                val imgr1 = images.random()
                val imgr2 = images.random()
                val imgr3 = images.random()

                img1.setImageResource(imgr1)
                img2.setImageResource(imgr2)
                img3.setImageResource(imgr3)

                if (!binding.casse.isChecked) {
                    // Normal Mode
                    if (imgr1 == imgr2 || imgr2 == imgr3 || imgr1 == imgr3) {
                        gain = deposit
                        win = true
                    }
                    if (imgr1 == imgr2 && imgr2 == imgr3) {
                        gain = (deposit * 25)
                        win = true
                    }
                } else {
                    // Special Mode
                    if ((imgr1 == image8 && imgr2 == image8) || (imgr1 == image8 && imgr3 == image8) || (imgr2 == image8 && imgr3 == image8)) {
                        gain = (deposit * 10)
                        win = true
                    }
                    if (imgr1 == image5 && imgr2 == image5 && imgr3 == imgr2) {
                        gain = (deposit * 100)
                        win = true
                    }
                }

                if (win) {
                    sold += deposit
                    sold += gain
                }
                updatesold()
            } else {
                Toast.makeText(this, "Charge your solde", Toast.LENGTH_LONG).show()
            }

            updateRadioButtonStates()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("sold", sold)
        outState.putInt("DEPOSIT", deposit)
        outState.putBoolean("caseEnabel", binding.casse.isChecked)
        outState.putBoolean("btnstate", binding.play.isEnabled)

        val checkedBox = binding.radios.checkedRadioButtonId
        outState.putInt("rbtnId", checkedBox)

        outState.putBoolean("rbtn1Checked", binding.rbtn1.isChecked)
        outState.putBoolean("rbtn2Checked", binding.rbtn2.isChecked)
        outState.putBoolean("rbtn3Checked", binding.rbtn3.isChecked)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        sold = savedInstanceState.getInt("sold")
        deposit = savedInstanceState.getInt("DEPOSIT", 0)

        binding.casse.isChecked = savedInstanceState.getBoolean("caseEnabel")
        binding.play.isEnabled = savedInstanceState.getBoolean("btnstate")

        val checkedboxId = savedInstanceState.getInt("btnId", -1)
        if (checkedboxId != -1) {
            binding.radios.check(checkedboxId)
        }

        binding.rbtn1.isChecked = savedInstanceState.getBoolean("rbtn1Checked", false)
        binding.rbtn2.isChecked = savedInstanceState.getBoolean("rbtn2Checked", false)
        binding.rbtn3.isChecked = savedInstanceState.getBoolean("rbtn3Checked", false)
        updateRadioButtonStates()
    }
}
