-> Неделя 6: Многопоточность, Handler, Coroutines, Flow
  
1. Сделать новое приложение с двумя фрагментами (один ниже другого)  
1.1. На верхнем фрагменте генерировать случайное число  
1.2. На нижнем фрагменте показывать сообщение о том сколько прошло времени со старта  
1.3. Каждые 20 секунд менять цвет фона нижнего фрагмента  
1.4. В нижнем фрагменте добавить кнопки:  
  "пауза"/ "плейЭ по которой останавливать/возобновить выполнение, 
  "ресет" по которой начинать подсчет заново

- Для работы с потоками использовать только Thread-ы и Handler
  
2. Сделать то же самое что в 1.
  
- Для работы с потоками использовать только Coroutine-ы
  
3. Сделать то же самое что в 1.  
  
- Для работы с потоками использовать только Flow

4. Ответить на вопросы:  
a) Для чего нужен Handler? Что с помощью него можно делать? Как он работает?  
Handler - это механизм, который позволяет работать с очередью сообщений. Он привязан к конкретному потоку (thread) и работает с его очередью.  
Handler позволяет отправлять сообщения в другие потоки с задержкой или без, а также обрабатывать полученные сообщения.  
Handler используется для обновление view из другого потока, так как другой поток не может обновлять UI  
b) Как устроены корутины? В чем плюсы корутин? Какие есть минусы?  
Когрутины это сопрограммы, отличии от многопоточсности в том, что многопоточность - это выполнение одной программы в нескольких системных потоках, а корутины - это потоки исполнения кода поверх системного потока  
Т.е. блоки кода выполняются паралельно с остальным потоком безблокировок  
Плюсы: эффективное рассходывание ресурсов, снижение нагрузки на систему, быстрое переключение, что помогает не нагружаать и не блокировать пользовательский интерфейс  
Минусы: Корутины не позволят ускорить высокоуровневые вычисления, лучше использовать многопоточность  
c) Как работает Flow под капотом? В каких случаях его удобно использовать?  
Flow необходим для асинхронной обработки потока данных, получение значение происходит при помощи emit(), это позволяет обрабатывать отдельные значение потока данных, а не сначала получить все данные, а потом обрабатывать их  
Flow гораздо удобнее, необходимо возвращать какие либо значения постоянно, например сообщения в чате  
