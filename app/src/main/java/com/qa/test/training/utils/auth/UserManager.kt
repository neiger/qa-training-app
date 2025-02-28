package com.qa.test.training.utils.auth

import android.content.Context
import android.content.SharedPreferences
import com.qa.test.training.R
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class UserManager(private val context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    private val userFile = File(context.filesDir, "users.json")

    init {
        ensureDefaultAdmin()
    }

    fun setLoggedIn(isLoggedIn: Boolean) {
        sharedPreferences.edit().putBoolean("is_logged_in", isLoggedIn).apply()
    }

    private fun ensureDefaultAdmin() {
        if (!userFile.exists()) {
            val (defaultUsername, defaultPassword) = loadDefaultAdminFromConfig()
            val initialUsers = JSONArray().apply {
                put(JSONObject().apply {
                    put("name", defaultUsername)
                    put("username", defaultUsername)
                    put("password", defaultPassword)
                })
            }
            userFile.writeText(initialUsers.toString()) // Save to JSON file
        }
    }

    private fun loadDefaultAdminFromConfig(): Pair<String, String> {
        return try {
            val inputStream = context.resources.openRawResource(R.raw.admin_config)
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            val jsonObject = JSONObject(jsonString)
            Pair(jsonObject.getString("username"), jsonObject.getString("password"))
        } catch (e: Exception) {
            println(e.message)
            Pair("", "") // Fallback return empty instead of hardcoded values
        }
    }


    fun saveUser(name: String, username: String, password: String) {
        val usersArray = getUsersJson()
        usersArray.put(JSONObject().apply {
            put("name", name)
            put("username", username)
            put("password", password)
        })
        userFile.writeText(usersArray.toString()) // Save back to JSON file
    }

    fun isValidUser(username: String, password: String): Boolean {
        val usersArray = getUsersJson()
        for (i in 0 until usersArray.length()) {
            val user = usersArray.getJSONObject(i)
            if (user.getString("username") == username && user.getString("password") == password) {
                return true
            }
        }
        return false
    }

    private fun getUsersJson(): JSONArray {
        return try {
            JSONArray(userFile.readText())
        } catch (e: Exception) {
            println(e.message)
            JSONArray()
        }
    }
}
