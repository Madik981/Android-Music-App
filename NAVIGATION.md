# Навигация в Rhythm MusicHub

## Архитектура навигации

Приложение использует **Jetpack Navigation Compose** для управления навигацией между экранами.

## Структура файлов навигации

### 1. `Screen.kt` - Определение маршрутов
Содержит sealed class со всеми экранами приложения:
- `Home` - главный экран
- `Search` - экран поиска
- `Library` - библиотека/избранное
- `Player` - экран плеера (с параметром trackId)
- `Settings` - настройки

### 2. `NavGraph.kt` - Граф навигации
Определяет NavHost с композируемыми функциями для каждого экрана:
- Настроен startDestination на Home
- Поддержка аргументов для Player экрана
- Передача NavController и ViewModel во все экраны

### 3. `NavigationExtensions.kt` - Extension функции
Вспомогательные функции для упрощенной навигации:
- `navigateToHome()` - навигация на главный экран
- `navigateToSearch()` - навигация на поиск
- `navigateToLibrary()` - навигация в библиотеку
- `navigateToPlayer(trackId)` - навигация к плееру с ID трека
- `navigateToSettings()` - навигация в настройки
- `navigateBack()` - безопасная навигация назад

## Особенности реализации

### Управление Back Stack
Все основные экраны (Home, Search, Library) используют следующую конфигурацию:
```kotlin
popUpTo(graph.findStartDestination().id) {
    saveState = true  // Сохраняем состояние
}
launchSingleTop = true  // Избегаем дубликатов
restoreState = true  // Восстанавливаем состояние
```

### Условная видимость Bottom Navigation
Bottom Navigation Bar отображается только на основных экранах:
```kotlin
if (currentRoute in listOf(Screen.Home.route, Screen.Search.route, Screen.Library.route))
```

### Передача параметров
Для навигации с параметрами (например, Player):
```kotlin
navController.navigateToPlayer(trackId = 123L)
```

## Использование

### В Activity
```kotlin
val navController = rememberNavController()
val viewModel: MusicViewModel = viewModel()

NavGraph(
    navController = navController,
    innerPadding = innerPadding,
    viewModel = viewModel
)
```

### В Composable функциях
```kotlin
// Простая навигация
navController.navigateToSearch()

// Навигация с параметрами
navController.navigateToPlayer(track.id)

// Навигация назад
navController.navigateBack()
```

## Преимущества новой архитектуры

1. **Типобезопасность** - использование sealed class вместо строк
2. **Переиспользуемость** - extension функции упрощают код
3. **Сохранение состояния** - автоматическое сохранение/восстановление
4. **Единая точка управления** - централизованный NavGraph
5. **Расширяемость** - легко добавлять новые экраны

## Добавление нового экрана

1. Добавить объект в `Screen.kt`:
```kotlin
object NewScreen : Screen("new_screen")
```

2. Добавить маршрут в `NavGraph.kt`:
```kotlin
composable(route = Screen.NewScreen.route) {
    NewScreen(...)
}
```

3. Создать extension функцию в `NavigationExtensions.kt`:
```kotlin
fun NavController.navigateToNewScreen() {
    navigate(Screen.NewScreen.route)
}
```
