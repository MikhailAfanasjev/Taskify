Taskify — это Android-приложение для создания и управления задачами с напоминаниями. Оно позволяет пользователям добавлять задачи с указанием даты и времени выполнения, сохранять их в локальную базу данных, а также получать уведомления о предстоящих делах.

Основные функции:
Создание задач: Пользователь может добавлять задачи с указанием названия, даты и времени.
Уведомления: Напоминания с настраиваемым звуковым сигналом уведомляют пользователя о предстоящих задачах.
Просмотр и управление задачами: Все задачи отображаются в списке с возможностью удаления.
Автоматическое планирование напоминаний: Используя WorkManager, приложение планирует уведомления, основываясь на указанной пользователем дате и времени.
Стек технологий:
Jetpack Navigation: Для навигации между фрагментами.
Dagger Hilt: Для внедрения зависимостей.
Room: Для работы с базой данных.
WorkManager: Для фоновой обработки и планирования задач.
ViewModel и LiveData: Для управления данными и состоянием UI.
ViewBinding: Для работы с элементами интерфейса.
