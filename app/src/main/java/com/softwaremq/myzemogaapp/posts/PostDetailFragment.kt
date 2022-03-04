package com.softwaremq.myzemogaapp.posts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.softwaremq.myzemogaapp.base.BaseRepository
import com.softwaremq.myzemogaapp.MainActivity
import com.softwaremq.myzemogaapp.R
import com.softwaremq.myzemogaapp.base.BaseFragment
import com.softwaremq.myzemogaapp.databinding.FragmentPostDetailBinding
import com.softwaremq.myzemogaapp.models.PostData
import com.softwaremq.myzemogaapp.network.getNetworkService
import com.vicpin.krealmextensions.save


class PostDetailFragment(val postData: PostData) : BaseFragment() {

    private lateinit var binding : FragmentPostDetailBinding
    private lateinit var postDetailsViewModel: PostDetailsViewModel
    private lateinit var adapter: CommentsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //set binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_post_detail,container,false)

        //setting data
        binding.postData = postData

        //setStatusBarColor and started the loading dialog
        val mainActivity : MainActivity = activity as MainActivity
        mainActivity.showLoadingDialog()

        //initialized the repository and viewmodel
        val repository = BaseRepository(getNetworkService())
        postDetailsViewModel = ViewModelProvider(this,PostDetailsViewModel.FACTORY(repository)).get(PostDetailsViewModel::class.java)

        //set binding variable
        binding.lifecycleOwner = (this)

        initAdapter()
        observeData(mainActivity)

        //start api calls
        postDetailsViewModel.loadFragment(
            postId = postData.id,
            userId = postData.userId
        )

        //set go back
        setBackPressed{ popBackStack()}
        binding.ivBack.setOnClickListener{mainActivity.onBackPressed()}
        binding.ivFav.setOnClickListener{
            postData.favorite = !postData.favorite
            postData.save()
            binding.postData = postData
            val toastText: String = if (postData.favorite) {"Post added to favorites"} else "Post removed from favorites"
            Toast.makeText(requireContext(), toastText , Toast.LENGTH_SHORT).show()
        }
        return binding.root
    }

    private fun initAdapter(){
        adapter = CommentsAdapter()
        binding.rvComments.adapter = adapter
    }

    private fun observeData(mainActivity: MainActivity){
        postDetailsViewModel.comments.observe(viewLifecycleOwner, {
            mainActivity.hideLoadingDialog()

            if(it!=null){
                adapter.submitList(it)
                binding.count = it.size
            }else{
                binding.count = 0
            }
        })

        postDetailsViewModel.user.observe(viewLifecycleOwner, {

            if(!it.isNullOrEmpty()){
                binding.userData = it[0]
            }
        })
    }
}