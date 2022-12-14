package com.example.veggie_table

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import com.example.veggie_table.databinding.ActivityMainBinding
import com.example.veggie_table.databinding.DrawlayoutHeaderBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    lateinit var toggle : ActionBarDrawerToggle // 토글 선언

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        // display user info
        displayUserInfo()
        setContentView(binding.root)

        // 툴바 설정
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
        toggle = ActionBarDrawerToggle(this, binding.drawer, R.string.app_name, R.string.app_name)
        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false) // title 제거
        toggle.syncState() // 토글 동기화

        // logout alert 이벤트
        val eventHandler = DialogInterface.OnClickListener { p0, p1 ->
            if(p1 == DialogInterface.BUTTON_POSITIVE){
                //  로그아웃 처리
                SharedPreference.clearAll(baseContext)

                // 시작 페이지로 이동
                val intent = Intent(baseContext, StartActivity::class.java)
                startActivity(intent)
            }
        }

        // 드로어 레이아웃 이벤트
        binding.mainDrawerView.setNavigationItemSelectedListener {
            //Log.d("mobileApp", "Navigation selected...${it.title}")
            when(it!!.title){
                "로그아웃" ->{
                    AlertDialog.Builder(this).run {
                        setTitle("알림창 테스트")
                        setIcon(android.R.drawable.ic_dialog_info)
                        setMessage("로그아웃을 하시겠습니까?")
                        setPositiveButton("YES", eventHandler) // 이벤트 설정
                        setNegativeButton("NO", null)
                        show()
                    }

                }
            }

            true
        }

        // 스피너
        val mainSpinner = ArrayAdapter.createFromResource(this, R.array.catrgories, android.R.layout.simple_spinner_dropdown_item)
        binding.mainSpinner.adapter = mainSpinner;


       // 검색창
        binding.searchBtn.setOnClickListener {
            val inputSearch = binding.mainSearch.text

            val type = binding.mainSpinner.selectedItem
            Log.d("mobileApp", "$type")

            // list page에 검색어 전달
            val intent = Intent(this, ListMemberActivity::class.java)
            intent.putExtra("search", inputSearch.toString())
            intent.putExtra("type", type.toString())
            startActivity(intent)
        }

        // 이동
        binding.gotoMemberList.setOnClickListener {
            val intent = Intent(this, ListMemberActivity::class.java)
            startActivity(intent)
        }

        binding.gotoMemberStatistics.setOnClickListener {
            val intent = Intent(this, StatisticsActivity::class.java)
            startActivity(intent)
        }

        binding.gotoWebpageList.setOnClickListener {
            val intent = Intent(this, LinkListActivity::class.java)
            startActivity(intent)
        }

        binding.gotoContact.setOnClickListener {
            val intent = Intent(this, ContactSelectActivity::class.java)
            startActivity(intent)
        }

    }

    // 툴바 메뉴 버튼이 클릭 됐을 때 실행하는 함수
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val drawerLayout = binding.drawer

        when(item!!.itemId){
            android.R.id.home->{
                // 햄버거 버튼 클릭시 네비게이션 드로어 열기
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun displayUserInfo(){
        val drawBinding = DrawlayoutHeaderBinding.inflate(layoutInflater)
        val nikname = drawBinding.profileEmail
        val email = drawBinding.profileName
        val residence = drawBinding.profileResidence

        // 유저가 로그인했을 경우
        if(SharedPreference.getUserEmail(this)?.length != 0){
            Log.d("mobileApp", "너 왜 안 바뀌어?")
            nikname.text = SharedPreference.getUserName(this)
            email.text = SharedPreference.getUserEmail(this)
            residence.text = SharedPreference.getUserResidence(this)

            Log.d("mobileApp", "${nikname.text}")
        }
    }
}