package com.oguzhan.pathmarvelapp.ui.view.CharacterList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oguzhan.pathmarvelapp.R
import com.oguzhan.pathmarvelapp.data.model.Results
import com.oguzhan.pathmarvelapp.ui.view.CharacterList.adapter.CharacterAdapter
import com.oguzhan.pathmarvelapp.ui.view.CharacterList.adapter.CharacterClickListener
import com.oguzhan.pathmarvelapp.ui.view.MainActivity
import com.oguzhan.pathmarvelapp.ui.viewmodel.CharactersViewModel
import com.oguzhan.pathmarvelapp.util.MD5Hash
import com.oguzhan.pathmarvelapp.util.PaginationScrollListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_characters.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.reflect.KProperty

@AndroidEntryPoint
class CharactersFragment : Fragment(), CharacterClickListener {

    companion object {
        const val PRIVATE_KEY = "cdf13a0060de0db3a5592bc0dcdb20f5ffd921e7"
        const val PUBLIC_KEY = "65caac27b5790806c42cfdca1100b1de"
    }

    var isLastPage: Boolean = false
    var isLoading: Boolean = false

    lateinit var rv: RecyclerView
    lateinit var adapter: CharacterAdapter
    lateinit var layoutManager: LinearLayoutManager

    lateinit var tvPageNo: TextView
    lateinit var progressBar: ProgressBar

    private var currentPage = 0
    private var s: String = ""
    private var ts: Long = 0

    lateinit var mD5Hash: MD5Hash

    lateinit var characterList: ArrayList<Results>

    private var characterViewModel: CharactersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_characters, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI(view)
        ts = Date().time
        getHashCode()
        characterViewModel.getCharacters(s, ts, 0)
        initRecycler()
        observeLiveData()
    }

    private fun initRecycler() {
        adapter =
            CharacterAdapter(
                arrayListOf(),
                this
            )
        recyclerView.adapter = adapter
        layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            characterViewModel.currentPage.value =
                layoutManager.findFirstVisibleItemPosition() / 30 + 1
        }

        recyclerView?.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                isLoading = true
                getMoreCharacters()
            }

        })
    }

    private fun getHashCode(): String {
        mD5Hash = MD5Hash()
        s =
            mD5Hash.md5("$ts" + PRIVATE_KEY + PUBLIC_KEY)
                .toLowerCase(Locale.ROOT)
        return s
    }

    private fun initUI(rootView: View) {
        rv = rootView.findViewById(R.id.recyclerView)
        tvPageNo = rootView.findViewById(R.id.tv_page_no)
        progressBar = rootView.findViewById(R.id.progressBar)
        characterViewModel.currentPage.value = 1

    }

    private fun getMoreCharacters() {
        currentPage++
        characterViewModel.getCharacters(s, ts, currentPage * 30)
    }

    private fun observeLiveData() {
        characterViewModel.characters.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                characterList = it.data.results as ArrayList<Results>
                adapter.addData(characterList)
                adapter.notifyDataSetChanged()
                isLoading = false
            }
        })

        characterViewModel.loading.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.INVISIBLE
            }
        })

        characterViewModel.error.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
        })

        characterViewModel.currentPage.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            tvPageNo.text = it.toString()
        })
    }

    override fun onCharacterClickListener(data: Results) {
        MainActivity.currentCharacter = data
        val action = CharactersFragmentDirections.actionCharactersFragmentToCharacterInfoFragment()
        Navigation.findNavController(requireView()).navigate(action)
    }
}

private operator fun <T> Lazy<T>.setValue(
    charactersFragment: CharactersFragment,
    property: KProperty<*>,
    t: T
) {
}