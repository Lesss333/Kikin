package com.kikin.gastos.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import com.kikin.gastos.data.AppDatabase
import com.kikin.gastos.data.ExpenseEntity
import com.kikin.gastos.databinding.FragmentExpensesBinding
import kotlinx.coroutines.launch

class ExpensesFragment : Fragment() {
    private var _binding: FragmentExpensesBinding? = null
    private val binding get() = _binding!!
    private val expenses = mutableListOf<ExpenseEntity>()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(RESULT_EXPENSE_CHANGED) { _, _ ->
            loadExpenses()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExpensesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, mutableListOf())
        binding.expenseList.adapter = adapter

        binding.expenseList.setOnItemClickListener { _, _, position, _ ->
            val expense = expenses[position]
            AddEditExpenseDialogFragment.newInstance(expense).show(parentFragmentManager, "editExpense")
        }

        binding.addExpenseFab.setOnClickListener {
            AddEditExpenseDialogFragment.newInstance(null).show(parentFragmentManager, "addExpense")
        }

        loadExpenses()
    }

    private fun loadExpenses() {
        val db = AppDatabase.getInstance(requireContext())
        lifecycleScope.launch {
            val items = db.expenseDao().getAll()
            expenses.clear()
            expenses.addAll(items)
            adapter.clear()
            adapter.addAll(items.map { "${it.description} - $${String.format("%.2f", it.amount)}" })
            adapter.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val RESULT_EXPENSE_CHANGED = "expense_changed"
    }
}
