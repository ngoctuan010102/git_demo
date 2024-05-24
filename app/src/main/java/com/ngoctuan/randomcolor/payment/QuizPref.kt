package com.ngoctuan.randomcolor.payment

import android.content.Context
import android.content.SharedPreferences


class QuizPref private constructor(context: Context) {
    init {
        sharedPreferences = context.getSharedPreferences("shared", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }
    /*
        fun saveName(name: String?) {
            editor.putString("name", name)
            editor.apply()
        }

        fun historyQuiz(quizList: String?) {
            editor.putString("historys", quizList)
            editor.apply()
        }

        fun showResultQuiz(quizList: String?) {
            editor.putString("showResultQuiz", quizList)
            editor.apply()
        }

        val showResultQuiz: String?
            get() = sharedPreferences.getString("showResultQuiz", "")
        val historyQuiz: String?
            get() = sharedPreferences.getString("historys", "")
        val name: String?
            get() = sharedPreferences.getString("name", "")

        fun totalCount(): Int {
            val userId = sharedPreferences.getString("UserId", "")
            val count = sharedPreferences.getInt("countstart_$userId", 5)
            Log.i("SON", "totalCount: $count")
            return count
        }
    */

    /*  fun countPlayers(step: Int) {
          val userId = sharedPreferences.getString("UserId", "")
          var count = sharedPreferences.getInt("countstart_$userId", 0)
          val editor = sharedPreferences.edit()
          count += step
          editor.putInt("countstart_$userId", count)
          editor.apply()
      }

      fun giamPlayers(step: Int) {
          val userId = sharedPreferences.getString("UserId", "")
          var count = sharedPreferences.getInt("countstart_$userId", 5)
          val editor = sharedPreferences.edit()
          count--
          Log.i("SON", "giamPlayers: $count")
          editor.putInt("countstart_$userId", count)
          editor.apply()
      }*/
    fun currentUserId(userid: String?) {
        editor.putString("UserId", userid)
        editor.apply()
    }

    var isPremium: Boolean?
        get() {
            val userId = sharedPreferences.getString("UserId", "")
            return sharedPreferences.getBoolean("PremiumPlan_\$userId$userId", false)
        }
        set(state) {
            val userId = sharedPreferences.getString("UserId", "")
            editor.putBoolean("PremiumPlan_\$userId$userId", state!!)
            editor.apply()
        }

    companion object {
        private lateinit var sharedPreferences: SharedPreferences
        private lateinit var editor: SharedPreferences.Editor
        var instance: QuizPref? = null
        // private set

        fun init(context: Context) {
            if (instance == null) {
                instance = QuizPref(context)
            }
        }
    }
}
