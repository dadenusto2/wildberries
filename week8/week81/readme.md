-> Неделя 5: Работа с сетью, Retrofit, Okhttp, Ktor, Coil, Gson/Moshi

1. Сделать новый проект в котором реализовать приложение с двумя экранами:
1.1. На первом экране показать список героев из Dota 2 (иконка + название, остальное по желанию)
1.2. По клику на героя открывается экран с детальным описанием героя (сверху картинка героя на всю ширину экрана, ниже характеристики героя)
1.3. Для запросов к API использовать Okhttp + Moshi, показывать картинки с помощью Coil

- Использовать API https://docs.opendota.com/ (или подобное)

Загрузка происходит автоматически, для обновление потянуть вверх
при отсуствии интернета фото не грузятся, для обновляния потянуть вниз


-> Неделя 7: Хранение данных

(Сделать для 1-го приложения 5-й недели)
2. Сохранять данные что получили с сервера в файл  
2.2. На старте приложения - загружаем данные из файла(если есть) , если нет берем с бэкенда  

Данные сохраняются в файл json/heroes.json, по умолчанию из файла, при отсутсвии из API при этом данные записываются в файл  

В меню можно обновить и удалить локальный список  

-> Неделя 8: Навигация между экранами через FragmentManage
3.0. Добавить экран "О приложении" в каждый проект, там написать имя/ник автора, ссылку на гитхаб и дату создания приложения
3.1 Реализовать переключение между экранами через FragmentManager  

"О приложении" в меню