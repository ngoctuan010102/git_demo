package com.ngoctuan.randomcolor

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ngoctuan.randomcolor.payment.SubBuyAct
import com.ngoctuan.randomcolor.databinding.FragmentColorBinding
import com.ngoctuan.randomcolor.db.Account
import com.ngoctuan.randomcolor.db.AccountViewModel
import kotlin.random.Random

// TODO: Rename parameter arguments, choose names that match


/**
 * A simple [Fragment] subclass.
 * Use the [ColorFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ColorFragment : Fragment() {
    private lateinit var colorFragmentBinding: FragmentColorBinding

    private val accountViewModel: AccountViewModel by lazy {
        ViewModelProvider(
            this,
            AccountViewModel.AccountViewModelFactory(requireActivity().application)
        )[AccountViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        colorFragmentBinding = FragmentColorBinding.inflate(layoutInflater)
        accountViewModel.getAccountByID(1).observe(viewLifecycleOwner, Observer {
            if (it == null) {
                val acc: Account = Account(1, 10)
                accountViewModel.insertAccount(acc)
                Log.d("ERRPR", "INSERT SUCCESS")
            }
        })

        return colorFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var count: Int = 0
        accountViewModel.getAccountByID(1).observe(viewLifecycleOwner, Observer {
            count = it.count
            Log.d("ERRPR", "Count SUCCESS ${it.count}")
        })
        with(colorFragmentBinding) {
            this.tvColor.setOnClickListener {
                if (count > 0 && count < 10000) {
                    this.tvColor.setBackgroundColor(getRandomColor())
                    accountViewModel.updateAccount(Account(1, count - 1))
                    count--;
                } else if (count > 10000) {
                    this.tvColor.setBackgroundColor(getRandomColor())
                } else {
                    this.tvColor.isClickable = false
                    this.idBuy.isVisible = true
                }
            }
            this.idBuy.setOnClickListener {
                this.tvColor.isClickable = true
                this.idBuy.isVisible = false
                val intent = Intent(requireContext(), SubBuyAct::class.java)
                startActivity(intent)
            }
        }
    }

    fun getRandomColor(): Int {
        val random = Random
        // Tạo giá trị ngẫu nhiên cho mỗi thành phần màu
        val red = random.nextInt(256) // Giá trị từ 0 đến 255 (255 là giá trị tối đa)
        val green = random.nextInt(256)
        val blue = random.nextInt(256)
        // Tạo màu hoàn chỉnh từ các giá trị đã được tạo ra
        return Color.rgb(red, green, blue)
    }
}