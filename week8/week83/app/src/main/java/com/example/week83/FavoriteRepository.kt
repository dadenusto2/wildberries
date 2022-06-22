package com.example.week83

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.example.week83.db.MyDatabase
import com.example.week83.model.CatDataItem
import com.example.week83.model.FavoriteData
import com.example.week83.model.FavoritesUrl
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@DelicateCoroutinesApi
class FavoriteRepository {

    /**
     * Обновляем лайкнутых котов в БД
     *
     * @param curUser - текущий пользователь
     * @param myDatabase - база данных
     * @param context - контекст
     */
    private suspend fun updateFavoriteCats(
        curUser: String?,
        myDatabase: MyDatabase,
        context: FragmentActivity?
    ) {
        Log.d("--=", "Update")
        val favoriteCatsFromApi = getFavoriteFromApi(curUser)//ссылки из бд
        val favoriteCatsFromDB = getFavoriteFromDB(curUser, myDatabase)//ссылки из API

        //если равны, то не обновляем
        if (isEqual(favoriteCatsFromDB, favoriteCatsFromApi)) {
            context?.lifecycleScope?.launch {
                Toast.makeText(
                    context,
                    "DB не требует обновления!",
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            //если не равны, то  обновляем
            updateDB(favoriteCatsFromApi, myDatabase)
            context?.lifecycleScope?.launch {
                Toast.makeText(context, "DB обновлена!", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    /**
     * Получаем ссылки на лайкнутых котов
     *
     * @param curUser - текущий пользователь
     * @param myDatabase - база данных
     * @param context - контекст
     *
     * @return ссылки на лайкнутых котов
     */
    suspend fun getFavoriteCats(
        curUser: String?,
        myDatabase: MyDatabase,
        context: FragmentActivity?
    ): MutableList<FavoritesUrl?> {
        // возвращаемый список
        var favoriteCats: MutableList<FavoritesUrl?> = mutableListOf()
        //список из API
        var favoriteCatsFromApi = mutableListOf<FavoritesUrl?>()
        //список из БД
        var favoriteCatsFromDB = mutableListOf<FavoritesUrl?>()

        //получаем списки
        val jobGet = GlobalScope.launch {
            favoriteCatsFromApi = getFavoriteFromApi(curUser)
            favoriteCatsFromDB = getFavoriteFromDB(curUser, myDatabase)
        }
        jobGet.start()
        jobGet.join()

        val job: Job
        // если список бд не нулевой и совпадет со список API
        if (favoriteCatsFromDB.size > 1 && (isEqual(
                favoriteCatsFromDB,
                favoriteCatsFromApi
            ) || favoriteCatsFromApi.size == 0)
        ) {
            job = GlobalScope.launch {
                //Берем ссылки из БД
                favoriteCats = favoriteCatsFromDB
                context?.lifecycleScope?.launch {
                    if (favoriteCatsFromApi.size == 0)
                        Toast.makeText(
                            context,
                            "Данные из DB!\nОбновить не удалось",
                            Toast.LENGTH_LONG
                        ).show()
                    else
                        Toast.makeText(
                            context,
                            "Данные из DB!\nОбновление не требуется",
                            Toast.LENGTH_LONG
                        ).show()

                }
            }
            //иначе список из API
        } else if (favoriteCatsFromApi.size > 0) {
            job = GlobalScope.launch {
                Log.d("---DB", "Данные из API!")
                favoriteCats = favoriteCatsFromApi
                //обновляем список бд
                updateFavoriteCats(curUser, myDatabase, context)
                context?.lifecycleScope?.launch {
                    Toast.makeText(context, "Данные из API!\nDB обновленна", Toast.LENGTH_LONG)
                        .show()
                }
            }
            //если нет списка в бд и в API
        } else {
            job = context?.lifecycleScope?.launch {
                Toast.makeText(context, "Ошибка! Нет подходящих данных", Toast.LENGTH_LONG)
                    .show()
            }!!

        }
        job.start()
        job.join()
        return favoriteCats
    }

    /**
     * Получаем сслыки из API
     *
     * @param curUser - текущий пользователь
     *
     * @return ссылки из API
     */
    private suspend fun getFavoriteFromApi(curUser: String?): MutableList<FavoritesUrl?> {
        try {
            //создаем запрос на все оценки у данного пользователя
            val clientPost = HttpClient(Android) {}
            val get = clientPost.get<String> {
                header("x-api-key", API_KEY)
                parameter("sub_id", curUser)
                url {
                    protocol = URLProtocol.HTTPS
                    host = "api.thecatapi.com"
                    path(VOTES_URL)
                }
            }
            val favoriteCatsFromApi = Json.decodeFromString<MutableList<FavoriteData>>(get)
            return getImages(favoriteCatsFromApi)
        } catch (e: Exception) {//если ошибка подключения
            return mutableListOf()
        }

    }

    /**
     * Получаем сслыки из БД
     *
     * @param curUser - текущий пользователь
     * @param myDatabase - база данных
     *
     * @return ссылки из БД
     */
    private fun getFavoriteFromDB(
        curUser: String?,
        myDatabase: MyDatabase,

        ): MutableList<FavoritesUrl?> {
        val favoritesFromDB = myDatabase.favoriteDao().getFavorites()
        return if (favoritesFromDB[0]?.subId == curUser)
            myDatabase.favoriteDao().getFavorites()
        else
            mutableListOf()
    }

    /**
     * Получаем сслыки на фото из API
     *
     * @param favoriteImages - лайкнутые каты
     *
     * @return ссылки из API
     */
    private suspend fun getImages(favoriteImages: MutableList<FavoriteData>): MutableList<FavoritesUrl?> {
        // Хранение ссылок на объекты котов, котьорые были пролайканы
        val imageUrls = mutableListOf<String>()
        val subId = favoriteImages[0].sub_id
        for (element in favoriteImages) {//получаем ссылка на фото с оценокой 1(лайк)
            if (element.value == 1) {
                imageUrls.add(
                    "https://api.thecatapi.com/v1/images/" +
                            element.image_id

                )
            }
        }

        //получаем фото всех лайкнутых котов из объектов котов
        var images = mutableListOf<FavoritesUrl?>()
        try {
            for (element in imageUrls) {
                val clientGet = HttpClient(Android) {}
                val jsonText = clientGet.get<String>(element)
                val favoriteImage = Json.decodeFromString<CatDataItem>(jsonText)
                images.add(0, FavoritesUrl(0, subId, favoriteImage.url))
            }
        } catch (e: Exception) {
            images = mutableListOf()
            Log.d("---", "error")
        }
        return images
    }

    /**
     * Сравнение списка ссылок
     *
     * @param first - первый список ссылок
     * @param second - первый список ссылок
     *
     * @return равны ли списки
     */
    private fun isEqual(
        first: MutableList<FavoritesUrl?>,
        second: MutableList<FavoritesUrl?>
    ): Boolean {
        //если разные размеры
        if (first.size != second.size) {
            return false
        }
        first.forEachIndexed { index, _ ->
            // если пара элементов не равна
            if (first[index]?.equals(second[index]) == false) {
                return false
            }
        }
        return true
    }

    /**
     * Обновление БД
     *
     * @param favoriteCatsFromApi - сслыки из API
     * @param myDatabase - база данных
     */
    private fun updateDB(favoriteCatsFromApi: MutableList<FavoritesUrl?>, myDatabase: MyDatabase) {
        // очищаем бД
        myDatabase.favoriteDao().deleteAll()
        // всавлем элементы из API
        for (favoriteCat in favoriteCatsFromApi) {
            myDatabase.favoriteDao().insertFavorite(favoriteCat)
        }
    }
}
