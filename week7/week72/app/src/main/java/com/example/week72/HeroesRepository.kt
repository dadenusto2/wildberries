package com.example.week72

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.week72.Model.HeroData
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.io.File
import java.io.Serializable

/**
 * Репозиторий списка героев
 */
@DelicateCoroutinesApi
class HeroesRepository(private val context: MainActivity) : Serializable {
    private var heroesList: List<HeroData>? = mutableListOf()

    /**
     * Получаем список героев
     *
     * @return список героев
     */
    suspend fun getHeroesList(): List<HeroData>? {
        val job: Job
        heroesList = mutableListOf()
        // получаеа героев из Shared Preferences
        val heroesFromPreferences = getHeroesFromPref()
        //если не пустой, то из него
        if (heroesFromPreferences?.isNotEmpty() == true) {
            job = GlobalScope.launch {
                heroesList = heroesFromPreferences
            }
            context.lifecycleScope.launch {
                Toast.makeText(context, "Данные из Shared Preferences!", Toast.LENGTH_LONG)
                    .show()
            }
        } else { // иначе из API
            job = GlobalScope.launch {
                heroesList = getHeroesFromAPI()
            }
            context.lifecycleScope.launch {
                Toast.makeText(context, "Данные из API!", Toast.LENGTH_LONG)
                    .show()
            }
        }
        job.start()
        job.join()
        context.swipeRefreshLayout.isRefreshing = false
        return heroesList
    }

    /**
     * Получаем список героев из Shared Preferences
     *
     * @return список героев из Shared Preferences
     */
    fun getHeroesFromPref(): List<HeroData>? {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        //создаем запрос
        val mPrefs: SharedPreferences = context.getPreferences(MODE_PRIVATE)
        val heroFromPref: String? = mPrefs.getString(
            "HeroJson",
            null
        )
        val myType = object : TypeToken<List<HeroData>>() {}.type
         //возвращаем список героев из Shared Preferences
        return gson.fromJson(heroFromPref, myType)
    }

    /**
     * Получаем список героев из API
     *
     * @return список героев из API
     */
    @SuppressLint("CommitPrefEdits")
    fun getHeroesFromAPI(): List<HeroData>? {
        var heroListFromApi: List<HeroData>? = mutableListOf()
        val gson = GsonBuilder()
            .setLenient()
            .create()
        //создаем запрос
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        //указываем класс для класс интерфеса для запрроса
        val service = retrofit.create(RetrofitService::class.java)
        val call: Call<List<HeroData>> = service.heroesData
        //делаем запрос
        try {
            val response = call.execute()
            writeHeroesToPref(response.body())
            heroListFromApi = response.body()
        } catch (e: Exception) {
            context.lifecycleScope.launch {
                context.swipeRefreshLayout.isRefreshing = false
                Toast.makeText(context, "Нет соединения!", Toast.LENGTH_LONG)
                    .show()
            }
        }
        return heroListFromApi
    }

    /**
     * Обновлем список героев в Shared Preferences
     */
    fun updateHeroesList() {
        //Список из Shared Preferences
        val heroesListFromFile = getHeroesFromPref()
        //Список из API
        val heroesListFromApi = getHeroesFromAPI()
        if (isEqual(heroesListFromFile, heroesListFromApi)) {// Если одинаковые, то из Shared Pref
            context.lifecycleScope.launch() {
                Toast.makeText(
                    context,
                    "Shared Preferences не требует обновления!",
                    Toast.LENGTH_LONG
                ).show()
            }
        } else { // иначе обновляем
            writeHeroesToPref(heroesListFromApi)
            heroesList = heroesListFromApi?.toMutableList()
            context.lifecycleScope.launch() {
                Toast.makeText(context, "Shared Preferences обновлен!", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    /**
     * Сравнение списка ссылок
     *
     * @param first - первый список ссылок
     * @param second - первый список ссылок
     *
     * @return равны ли списки
     */
    private fun isEqual(first: List<HeroData>?, second: List<HeroData>?): Boolean {
        if (first?.size != second?.size) {
            return false
        }
        first?.forEachIndexed { index, _ ->
            if (!first[index].equals(second?.get(index))) {
                return false
            }
        }
        return true
    }

    /**
     * Записываем данные в Shared Preferences
     *
     * @param heroesList - список для обновления
     */
    private fun writeHeroesToPref(heroesList: List<HeroData>?) {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        val mPrefs: SharedPreferences = context.getPreferences(MODE_PRIVATE)

        val prefsEditor: SharedPreferences.Editor = mPrefs.edit()
        val json = gson.toJson(heroesList)
        // вставляем обновленный список
        prefsEditor.putString("HeroJson", json)
        prefsEditor.apply()
    }

    /**
     * Удаляем Shared Preferences
     */
    fun deleteFile() {
        val mPrefs: SharedPreferences = context.getPreferences(MODE_PRIVATE)
        val prefsEditor: SharedPreferences.Editor = mPrefs.edit()
        prefsEditor.clear()
        prefsEditor.apply()
        context.lifecycleScope.launch() {
            Toast.makeText(context, "Shared Preferences удален!", Toast.LENGTH_LONG)
                .show()
        }
    }

    interface RetrofitService {
        //интерфейс для
        @get:GET("all.json")
        val heroesData: Call<List<HeroData>>
    }
}