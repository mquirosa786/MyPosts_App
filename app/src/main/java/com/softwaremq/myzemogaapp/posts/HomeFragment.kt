package com.softwaremq.myzemogaapp.posts

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.softwaremq.myzemogaapp.MainActivity
import com.softwaremq.myzemogaapp.R
import com.softwaremq.myzemogaapp.base.BaseFragment
import com.softwaremq.myzemogaapp.base.BaseRepository
import com.softwaremq.myzemogaapp.databinding.FragmentHomeBinding
import com.softwaremq.myzemogaapp.models.PostData
import com.softwaremq.myzemogaapp.network.getNetworkService
import androidx.lifecycle.Lifecycle


class HomeFragment() : BaseFragment() {

    private lateinit var binding : FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var adapter: PostsAdapter
    private var primaryColor: Int = 0
    private var blackColor: Int = 0
    private var allSelected = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        //set binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home,container,false)

        //setStatusBarColor and started the loading dialog
        val mainActivity : MainActivity = activity as MainActivity
        mainActivity.showLoadingDialog()

        //initialized the repository and viewmodel
        val repository = BaseRepository(getNetworkService())
        homeViewModel = ViewModelProvider(this,HomeViewModel.FACTORY(repository)).get(HomeViewModel::class.java)

        primaryColor = resources.getColor(R.color.colorPrimary,null)
        blackColor = resources.getColor(R.color.black,null)

        //set binding variable
        binding.lifecycleOwner = (this)

        homeViewModel.callPosts()
        initAdapter()
        observerData(activity as MainActivity)
        setOnClicks()


        return binding.root
    }

    private fun initAdapter(){
        adapter = PostsAdapter{
            val postData = it.tag as PostData
            goToFragment(PostDetailFragment(postData))
        }
        binding.rvPosts.adapter = adapter
        initRefresher()
        swipeToDismiss()
    }

    private fun initRefresher(){
        val primaryColor = resources.getColor(R.color.colorPrimary,null)
        binding.swipeContainer.setColorSchemeColors(
            primaryColor, primaryColor
        )
        binding.swipeContainer.setOnRefreshListener {
            clearAdapter(false)
            homeViewModel.callPosts()
        }
    }

    private fun clearAdapter(deleteCache: Boolean = true){
        homeViewModel.deleteAllPosts(deleteCache)
    }

    private fun setOnClicks(){
        binding.btnRemoveAll.setOnClickListener{clearAdapter()}
        binding.btnAll.setOnClickListener{
            if(!allSelected){
                toggleBottomBox()
                adapter.submitList(homeViewModel.posts.value)
                adapter.notifyDataSetChanged()
                allSelected = true
            }
        }
        binding.btnFav.setOnClickListener{
            if(allSelected){
                toggleBottomBox()
                adapter.submitList(homeViewModel.posts.value?.filter { it.favorite })
                adapter.notifyDataSetChanged()
                allSelected = false
            }
        }
    }

    private fun toggleBottomBox(){
        toggleAllClick()
        toggleFavClick()
    }

    private fun toggleAllClick(){
        if(allSelected){
            binding.btnAll.setTextColor(blackColor)
            binding.btnAll.setTypeface(Typeface.DEFAULT);
        }else{
            binding.btnAll.setTextColor(primaryColor)
            binding.btnAll.setTypeface(Typeface.DEFAULT_BOLD);
        }
    }

    private fun toggleFavClick(){
        if(allSelected){
            binding.btnFav.setTextColor(primaryColor)
            binding.btnFav.setTypeface(Typeface.DEFAULT_BOLD);
        }else{
            binding.btnFav.setTextColor(blackColor)
            binding.btnFav.setTypeface(Typeface.DEFAULT);
        }
    }

    private fun swipeToDismiss(){

        val swipeToDismissTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val post = (viewHolder as PostsAdapter.ViewHolder).binding.clItem.tag as PostData
                homeViewModel.deletePost(postId = post.id)
                adapter.notifyItemRemoved(viewHolder.absoluteAdapterPosition)
            }
        })

        swipeToDismissTouchHelper.attachToRecyclerView(binding.rvPosts)
    }

    private fun observerData(mainActivity: MainActivity){
        homeViewModel.posts.observe(viewLifecycleOwner, {
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                mainActivity.hideLoadingDialog()
                if(it!=null){
                    adapter.submitList(it)
                    binding.swipeContainer.isRefreshing = false
                }
            }
        })
    }

}