import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mleon.mydeliveryapp.R
import com.mleon.mydeliveryapp.databinding.FragmentBottomSheetDialogBinding
import com.mleon.mydeliveryapp.presentation.activities.MainActivity
import android.text.TextWatcher
import android.text.Editable

class BottomSheetDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetDialogBinding? = null
    private val binding get() = _binding!!
    private var isEmailValid = false
    private var isNameValid = false
    private var isPasswordValid = false
    private var isConfirmValid = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentBottomSheetDialogBinding.inflate(inflater, container, false)

        val email = binding.etEmail
        val name = binding.etName
        val password = binding.etPassword
        val confirm = binding.etConfirmPassword
        val btnRegister = binding.btnRegister

        email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val emailText = s.toString().trim()
                isEmailValid = Patterns.EMAIL_ADDRESS.matcher(emailText).matches()
                email.error = if (!isEmailValid) "El e-mail es inválido" else null
                updateButtonState()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val nameText = s.toString().trim()
                isNameValid = nameText.length in 6..50
                name.error = if (!isNameValid) "El nombre debe tener entre 6 y 50 caracteres" else null
                updateButtonState()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val passwordText = s.toString()
                isPasswordValid = passwordText.length in 8..12
                password.error = if (!isPasswordValid) "La contraseña debe tener entre 8 y 12 caracteres" else null
                val confirmText = confirm.text.toString()
                isConfirmValid = passwordText == confirmText && confirmText.isNotEmpty()
                confirm.error = if (!isConfirmValid && confirmText.isNotEmpty()) "Las contraseñas no coinciden" else null
                updateButtonState()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        confirm.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val confirmText = s.toString()
                val passwordText = password.text.toString()
                isConfirmValid = passwordText == confirmText && confirmText.isNotEmpty()
                confirm.error = if (!isConfirmValid && confirmText.isNotEmpty()) "Las contraseñas no coinciden" else null
                updateButtonState()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        btnRegister.isEnabled = false

        btnRegister.setOnClickListener {
            val emailText = email.text.toString().trim()
            val nameText = name.text.toString().trim()
            val passwordText = password.text.toString()

            val prefs = requireContext().getSharedPreferences("preferencias", Context.MODE_PRIVATE)
            prefs.edit()
                .putString("reg_email", emailText)
                .putString("reg_name", nameText)
                .apply()

            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            dismiss()
        }

        return binding.root
    }

    fun updateButtonState() {
        binding.btnRegister.isEnabled = isEmailValid && isNameValid && isPasswordValid && isConfirmValid
    }
}