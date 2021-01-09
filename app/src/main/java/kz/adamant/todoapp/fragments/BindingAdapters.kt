package kz.adamant.todoapp.fragments

import android.view.View
import android.widget.Spinner
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kz.adamant.todoapp.R
import kz.adamant.todoapp.data.models.Priority
import kz.adamant.todoapp.data.models.ToDoData
import kz.adamant.todoapp.fragments.list.ListFragmentDirections

class BindingAdapters {
    companion object {

        @BindingAdapter("android:navigateToAddFragment")
        @JvmStatic
        fun navigateToAddFragment(view: FloatingActionButton, navigate: Boolean) {
            view.setOnClickListener {
                if (navigate) {
                    view.findNavController().navigate(R.id.action_listFragment_to_addFragment)
                }
            }
        }

        @BindingAdapter("android:emptyDatabase")
        @JvmStatic
        fun emptyDatabase(view: View, emptyDatabase: MutableLiveData<Boolean>) {
            when (emptyDatabase.value) {
                true -> view.visibility = View.VISIBLE
                false -> view.visibility = View.INVISIBLE
            }
        }

        @BindingAdapter("android:parsePriority")
        @JvmStatic
        fun parsePriorityToInt(view: Spinner, priority: Priority) {
            view.setSelection(
                when (priority) {
                    Priority.HIGH -> 0
                    Priority.MEDIUM -> 1
                    Priority.LOW -> 2
                }
            )
        }

        @BindingAdapter("android:parsePriorityColor")
        @JvmStatic
        fun parsePriorityColor(view: CardView, priority: Priority) {
            view.setCardBackgroundColor(
                ContextCompat.getColor(
                    view.context,
                    when (priority) {
                        Priority.HIGH -> R.color.red
                        Priority.MEDIUM -> R.color.yellow
                        Priority.LOW -> R.color.green
                    }
                )
            )
        }

        @BindingAdapter("android:sendDataToUpdateFragment")
        @JvmStatic
        fun sendDataToUpdateFragment(view: ConstraintLayout, toDoData: ToDoData) {
            view.setOnClickListener {
                val action = ListFragmentDirections.actionListFragmentToUpdateFragment(toDoData)
                view.findNavController().navigate(action)
            }
        }
    }
}