package com.base.openeye.search

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.DefaultItemAnimator
import android.text.TextUtils
import android.view.*
import android.widget.TextView
import android.widget.Toast
import com.base.openeye.App
import com.base.openeye.R
import com.base.openeye.ui.MainActivity
import com.base.openeye.ui.ResultActivity
import com.base.openeye.utils.KeyBoardUtils
import com.base.openeye.utils.inflate
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.android.flexbox.FlexboxLayoutManager
import kotlinx.android.synthetic.main.search_fragment.*


/**
 * Created by lvruheng on 2017/7/9.
 */


class SearchFragment : DialogFragment() {

    var data: ArrayList<String> = arrayListOf("脱口秀", "城会玩", "666", "笑cry", "漫威",
            "清新", "匠心", "VR", "心理学", "舞蹈", "品牌广告", "粉丝自制", "电影相关", "萝莉", "魔性"
            , "第一视角", "教程", "毕业设计", "奥斯卡", "燃", "冰与火之歌", "温情", "线下campaign", "公益")

    private lateinit var mRootView: View
    private lateinit var mCircularRevealAnim: CircularRevealAnim

    /**
     * 绑定的Activity
     */
    private lateinit var attachActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        attachActivity = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogStyle)
    }

    override fun onStart() {
        super.onStart()
        initDialog()
    }

    private fun initDialog() {
        with(dialog.window) {
            setLayout((resources.displayMetrics.widthPixels * 0.98).toInt(),
                    WindowManager.LayoutParams.MATCH_PARENT)
            setGravity(Gravity.TOP)
            //取消过渡动画 , 使DialogSearch的出现更加平滑
            setWindowAnimations(R.style.DialogEmptyAnimation)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        return parent?.inflate(R.layout.search_fragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setData()
    }

    private fun setData() {
        val baseQuickAdapter = object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_search, data) {
            override fun convert(helper: BaseViewHolder, item: String) {
                val tvTitle = helper.getView<TextView>(R.id.tv_title)
                tvTitle.text = item
                val params = tvTitle.layoutParams
                if (params is FlexboxLayoutManager.LayoutParams) {
                    params.flexGrow = 1.0f
                }
            }
        }

        with(recyclerView) {
            layoutManager = FlexboxLayoutManager(context).let {
                with(it) {
                    //设置主轴排列方式
                    flexDirection = com.google.android.flexbox.FlexDirection.ROW
                    //设置是否换行
                    flexWrap = com.google.android.flexbox.FlexWrap.WRAP
                }
                it
            }
            itemAnimator = DefaultItemAnimator()
            adapter = baseQuickAdapter
        }

        baseQuickAdapter.setOnItemClickListener { adapter, view, position ->
            val keyWord = baseQuickAdapter.getItem(position)
            val intent = Intent(context, ResultActivity::class.java)
            intent.putExtra("keyWord", keyWord)
            attachActivity.startActivity(intent)
        }

    }

    private fun init() {
        tv_hint.typeface = App.fontFZLanTingHeiSDb
        mCircularRevealAnim = CircularRevealAnim()

        mCircularRevealAnim.setAnimListener(object : CircularRevealAnim.AnimListener {
            override fun onShowAnimationEnd() {
                if (isVisible) {
                    KeyBoardUtils.openKeyboard(activity!!, et_search_keyword)
                }
            }

            override fun onHideAnimationEnd() {
                et_search_keyword.setText("")
                dismiss()
            }
        })

        dialog.setOnKeyListener { dialog, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event?.action == KeyEvent.ACTION_UP) {
                hideAnim()
            } else if (keyCode == KeyEvent.KEYCODE_ENTER && event?.action == KeyEvent.ACTION_DOWN) {
                search()
            }
            false
        }

        iv_search_search.viewTreeObserver.addOnPreDrawListener {
            mCircularRevealAnim.show(iv_search_search, mRootView)
            true
        }

        iv_search_search.setOnClickListener {
            search()
        }

        iv_search_back.setOnClickListener {
            hideAnim()
        }
    }

    private fun search() {
        val searchKey = et_search_keyword.text.toString()
        if (TextUtils.isEmpty(searchKey.trim({ it <= ' ' }))) {
            Toast.makeText(activity, "请输入关键字", Toast.LENGTH_SHORT).show()
        } else {
            hideAnim()
            var keyWord = et_search_keyword.text.toString().trim()
            var intent = Intent(activity, ResultActivity::class.java)
            intent.putExtra("keyWord", keyWord)
            activity?.startActivity(intent)
        }
    }

    private fun hideAnim() {
        KeyBoardUtils.closeKeyboard(activity!!, et_search_keyword);
        mCircularRevealAnim.hide(iv_search_search, mRootView)
    }
}