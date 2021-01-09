package kz.adamant.todoapp.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kz.adamant.todoapp.R
import kz.adamant.todoapp.data.models.ToDoData
import kz.adamant.todoapp.data.viewmodels.SharedViewModel
import kz.adamant.todoapp.data.viewmodels.ToDoViewModel
import kz.adamant.todoapp.databinding.FragmentUpdateBinding

class UpdateFragment : Fragment() {
    private val args by navArgs<UpdateFragmentArgs>()
    private val mSharedViewModel: SharedViewModel by viewModels()
    private val mToDoViewModel: ToDoViewModel by viewModels()

    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        binding.args = args

        binding.currentPrioritiesSpinner.onItemSelectedListener = mSharedViewModel.listener

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> updateItem()
            R.id.menu_delete -> confirmItemRemoval()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateItem() {
        val title = binding.currentTitleEt.text.toString()
        val description = binding.currentDescriptionEt.text.toString()
        val priority = binding.currentPrioritiesSpinner.selectedItem.toString()

        val validation = mSharedViewModel.verifyDataFromUser(title, description)

        if (validation) {
            val updatedItem = ToDoData(
                args.currentItem.id,
                title,
                mSharedViewModel.parseStringPriority(priority),
                description,
            )

            mToDoViewModel.updateData(updatedItem)
            Toast.makeText(requireContext(), "Successfully updated!", Toast.LENGTH_SHORT)
                .show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "Empty fields", Toast.LENGTH_SHORT)
                .show()
        }
    }

    // Called when user click delete menu button
    // Asks confirmation via alert dialog
    private fun confirmItemRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder
            .setPositiveButton("Yes") { _, _ ->
                mToDoViewModel.deleteItem(args.currentItem)
                Toast.makeText(requireContext(), "Successfully deleted!", Toast.LENGTH_SHORT)
                    .show()
                findNavController().navigate(R.id.action_updateFragment_to_listFragment)
            }
            .setNegativeButton("No") { _, _ -> }
            .setTitle("Delete '${args.currentItem.title}?'")
            .setMessage("Are you sure you want to delete?")
            .create()
            .show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}