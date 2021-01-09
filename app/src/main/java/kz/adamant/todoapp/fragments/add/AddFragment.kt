package kz.adamant.todoapp.fragments.add

import android.app.ActivityManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import kz.adamant.todoapp.R
import kz.adamant.todoapp.data.models.Priority
import kz.adamant.todoapp.data.models.ToDoData
import kz.adamant.todoapp.data.viewmodels.SharedViewModel
import kz.adamant.todoapp.data.viewmodels.ToDoViewModel
import kz.adamant.todoapp.databinding.FragmentAddBinding
import kz.adamant.todoapp.databinding.FragmentUpdateBinding

class AddFragment : Fragment() {

    private val mToDoViewModel: ToDoViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddBinding.inflate(inflater, container, false)

        binding.prioritiesSpinner.onItemSelectedListener = mSharedViewModel.listener

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_add) {
            insertDataToDb()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertDataToDb() {
        val mTitle = binding.titleEt.text.toString()
        val mPriority = binding.prioritiesSpinner.selectedItem.toString()
        val mDescription = binding.descriptionEt.text.toString()
        val validation = mSharedViewModel.verifyDataFromUser(mTitle, mDescription)
        if (validation) {
            // Insert data to db
            val newData = ToDoData(
                0,
                mTitle,
                mSharedViewModel.parseStringPriority(mPriority),
                mDescription
            )

            mToDoViewModel.insertData(newData)
            Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_SHORT)
                .show()
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "Empty fields!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}