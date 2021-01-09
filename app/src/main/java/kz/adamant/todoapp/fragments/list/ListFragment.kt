package kz.adamant.todoapp.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kz.adamant.todoapp.R
import kz.adamant.todoapp.data.models.ToDoData
import kz.adamant.todoapp.data.viewmodels.SharedViewModel
import kz.adamant.todoapp.data.viewmodels.ToDoViewModel
import kz.adamant.todoapp.databinding.FragmentListBinding
import kz.adamant.todoapp.fragments.list.adapter.ListAdapter

class ListFragment : Fragment(), SearchView.OnQueryTextListener {

    private val mToDoViewModel: ToDoViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val adapter: ListAdapter by lazy { ListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mSharedViewModel = mSharedViewModel
        // Setting up recyclerView
        setupRecyclerView()

        // Observing changes in db
        mToDoViewModel.allData.observe(viewLifecycleOwner) {
            mSharedViewModel.checkIfDatabaseEmpty(it)
            adapter.setData(it)
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.itemAnimator = SlideInUpAnimator().apply {
            addDuration = 300
        }

        // Make recyclerView swipe to be able to delete
        swipeToDelete(recyclerView)
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemToDelete = adapter.getDataList()[viewHolder.adapterPosition]
                mToDoViewModel.deleteItem(itemToDelete)
//                adapter.notifyItemRemoved(viewHolder.adapterPosition)
                restoreDeletedData(viewHolder.itemView, itemToDelete, viewHolder.adapterPosition)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreDeletedData(view: View, deletedItem: ToDoData, position: Int) {
        Snackbar
            .make(binding.floatingActionButton, "Successfully Removed!", Snackbar.LENGTH_LONG)
            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.green_500))
            .setActionTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            .setAction("Undo") {
                mToDoViewModel.insertData(deletedItem)
//                adapter.notifyItemChanged(position)
            }
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_delete_all -> confirmRemovalAll()
            R.id.menu_priority_high -> mToDoViewModel.allDataHighPriority.observe(this) {
                adapter.setData(it)
            }
            R.id.menu_priority_low -> mToDoViewModel.allDataLowPriority.observe(this) {
                adapter.setData(it)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun confirmRemovalAll() {
        val builder = AlertDialog.Builder(requireContext())
        builder
            .setPositiveButton("Yes") { _, _ ->
                mToDoViewModel.deleteAll()
                Toast.makeText(requireContext(), "All items have been successfully deleted!", Toast.LENGTH_SHORT)
                    .show()
            }
            .setNegativeButton("No") { _, _ -> }
            .setTitle("Delete All Items?")
            .setMessage("Are you sure you want to delete all?")
            .create()
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            searchThroughDatabase(newText)
        }
        return true
    }

    private fun searchThroughDatabase(query: String) {
        val searchQuery = "%$query%"

        mToDoViewModel.searchQuery(searchQuery).observe(this) {
            it?.let {
                adapter.setData(it)
            }
        }
    }
}