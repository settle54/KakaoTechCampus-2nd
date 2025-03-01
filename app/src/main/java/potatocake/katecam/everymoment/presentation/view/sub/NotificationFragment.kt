package potatocake.katecam.everymoment.presentation.view.sub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import potatocake.katecam.everymoment.databinding.FragmentNotificationBinding
import potatocake.katecam.everymoment.presentation.adapter.NotificationAdapter
import potatocake.katecam.everymoment.presentation.viewModel.NotificationViewModel

@AndroidEntryPoint
class NotificationFragment : Fragment() {

    private lateinit var binding: FragmentNotificationBinding
    private val viewModel: NotificationViewModel by viewModels ()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        val adapter = NotificationAdapter(viewModel)
        setupRecyclerView(adapter)
        observeViewModel(adapter)
        viewModel.fetchNotifications()
    }
    private fun setupRecyclerView(adapter: NotificationAdapter) {
        binding.notificationRecyclerView.adapter = adapter
        binding.notificationRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeViewModel(adapter: NotificationAdapter) {
        viewModel.notifications.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }
    }
}