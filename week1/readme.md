<h1>Задание</h1>
 Неделя 1: Манифест, основные компоненты Android-приложения, для решения каких задач их использовать<br>

1.1. Создать мобильное приложение в котором будут объявлены в манифесте все основные компоненты андроиd<br>
1.2. На каждый компонент нужно в приложении завести отдельный экран на котором привести пример использования этого компонента

Пример структуры:<br>
        - MainActivity <br>
                - ServiceExampleActivity<br>
                - ReceiverExampleActivity<br>
                - ...<br>
1.3. В комментариях к экрану описать понятным языком что на этом экране происходит<br>
1.4. В комментариях к экрану привести примеры популярных приложений в которых подобный функционал используется<br>
<h1>Описание компонентов</h1>
<h2>Активности (Activities)</h2>
Видимая часть приложения, графическое отображение интерфейса пользователя.<br>
Activity применяется почти во всех приложения, написаных на Android<br>
<h3>Пример использования в приложениях</h3>gmail, Telegram<br>
<h3>пример</h3>В данном приложении MainActivity расположены кнопки для пере хода к примерам других активностей android

<br><h2>Приемники широковещательных сообщений (Broadcast Receivers)</h2>
Предназначен для получения различных сообщений от системы или других приложений
<h3>Пример использования в приложениях</h3>Chrome, яндекс.клавиатура, различные лаунчеры<br>
Broadcast Receivers можно объявлять динамически(примеры 1-3), начиная с API 26 большенсво регестрируются так,
или с помощью манифеста(https://developer.android.com/guide/components/broadcast-exceptions) (пример 4)<br>
<h3>пример 1</h3>При включени режима в самолете появлется AlertDialog с предложением выключить его через настройки(получение сообщений) и вывод статуса в toast<br>
<h3>пример 2</h3>Отправка соообщений всей системе по нажатию кнопки и получение его в toast<br>
<h3>пример 3</h3>Отправка соообщений всей локально(лучше для защиты данных) и получение его в toast<br>
<h3>пример 4</h3>Получение сообщений через объявленый в манифесте Receiver() при изменении часового пояса и получение его в toast<br>

<br><h2>Поставщик содержимого (ContentProvider)</h2>
Используется для предоставления доступа к хранилищу данных приложения другим приложениям.
<h3>Пример использования в приложениях</h3>Приложения для контактов, приложение настроек
<h3>пример</h3>Список контактов(не из контактов телефона), вставка, удаление всех контактов<br>

<br><h2>Сервисы (Services)</h2>
Используется для исполнения долгих операций, которые работают в фоновом режиме<br>
Например, музыкальные плееры
Пример использования в приложениях: Яндекс.Музыка
<h3>Пример использования в приложениях</h3>Яндекс.Музыка
<h3>пример 1</h3>Включение музыки, будет играть пока не остановится сервис нажатием кнопки, работает вне данной активности и указание статуса в toast<br>
<h3>пример 2</h3>Включение вибрции, будет играть пока находимся на текущем экране и указание статуса в toast<br>
