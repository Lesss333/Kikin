package com.kikin.gastos.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import com.kikin.gastos.data.AppDatabase
import com.kikin.gastos.data.ExpenseEntity
import com.kikin.gastos.databinding.FragmentAddEditExpenseBinding
import kotlinx.coroutines.launch

class AddEditExpenseDialogFragment : DialogFragment() {
    private var _binding: FragmentAddEditExpenseBinding? = null
    private val binding get() = _binding!!
    private var expense: ExpenseEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        expense = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(ARG_EXPENSE, ExpenseEntity::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(ARG_EXPENSE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditExpenseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.deleteExpense.visibility = if (expense == null) View.GONE else View.VISIBLE

        expense?.let {
            binding.expenseDescription.setText(it.description)
            binding.expenseAmount.setText(it.amount.toString())
        }

        binding.saveExpense.setOnClickListener {
            val description = binding.expenseDescription.text?.toString().orEmpty()
            val amount = binding.expenseAmount.text?.toString()?.toDoubleOrNull() ?: 0.0
            val db = AppDatabase.getInstance(requireContext())
            lifecycleScope.launch {
                if (expense == null) {
                    db.expenseDao().insert(ExpenseEntity(description = description, amount = amount))
                } else {
                    db.expenseDao().update(expense!!.copy(description = description, amount = amount))
                }
                setFragmentResult(ExpensesFragment.RESULT_EXPENSE_CHANGED, Bundle())
                dismiss()
            }
        }

        binding.deleteExpense.setOnClickListener {
            expense?.let { existing ->
                val db = AppDatabase.getInstance(requireContext())
                lifecycleScope.launch {
                    db.expenseDao().delete(existing)
                    setFragmentResult(ExpensesFragment.RESULT_EXPENSE_CHANGED, Bundle())
                    dismiss()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_EXPENSE = "arg_expense"

        fun newInstance(expense: ExpenseEntity?): AddEditExpenseDialogFragment {
            val fragment = AddEditExpenseDialogFragment()
            if (expense != null) {
                fragment.arguments = Bundle().apply { putParcelable(ARG_EXPENSE, expense) }
            }
            return fragment
        }
    }
}
