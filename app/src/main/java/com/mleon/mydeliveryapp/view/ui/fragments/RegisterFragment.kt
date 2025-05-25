import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mleon.mydeliveryapp.databinding.FragmentBottomSheetDialogBinding
import com.mleon.mydeliveryapp.view.ui.activities.MainActivity
import android.text.TextWatcher
import android.text.Editable
import androidx.fragment.app.viewModels
import com.mleon.mydeliveryapp.view.viewmodel.RegisterViewModel
import androidx.core.content.edit

class RegisterFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetDialogBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBottomSheetDialogBinding.inflate(inflater, container, false)

        val email = binding.etEmail
        val name = binding.etName
        val password = binding.etPassword
        val confirm = binding.etConfirmPassword
        val btnRegister = binding.btnRegister

        email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.onEmailChanged(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.onNameChanged(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.onPasswordChanged(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        confirm.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.onConfirmChanged(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        btnRegister.isEnabled = false

        viewModel.isEmailValid.observe(viewLifecycleOwner) { isValid ->
            binding.etEmail.error = if (!isValid && binding.etEmail.text.isNotEmpty()) "El e-mail es inválido" else null
        }
        viewModel.isNameValid.observe(viewLifecycleOwner) { isValid ->
            binding.etName.error = if (!isValid && binding.etName.text.isNotEmpty()) "El nombre debe tener entre 6 y 50 caracteres" else null
        }
        viewModel.isPasswordValid.observe(viewLifecycleOwner) { isValid ->
            val password = binding.etPassword.text.toString()
            binding.etPassword.error = when {
                password.isEmpty() -> "La contraseña está vacía"
                !isValid -> "La contraseña debe tener entre 8 y 12 caracteres"
                else -> null
            }
        }

        viewModel.isConfirmValid.observe(viewLifecycleOwner) { isValid ->
            val confirm = binding.etConfirmPassword.text.toString()
            binding.etConfirmPassword.error = if (!isValid && confirm.isNotEmpty()) "Las contraseñas no coinciden" else null
        }
        viewModel.isFormValid.observe(viewLifecycleOwner) { isValid ->
            binding.btnRegister.isEnabled = isValid
        }

        btnRegister.setOnClickListener {
            val prefs = requireContext().getSharedPreferences("preferencias", Context.MODE_PRIVATE)
            prefs.edit() {
                putString("reg_email", binding.etEmail.text.toString().trim())
                    .putString("reg_name", binding.etName.text.toString().trim())
            }
            startActivity(Intent(requireContext(), MainActivity::class.java))
            dismiss()
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}