# 🎤 План презентации проекта (5-10 минут)

## 📋 Введение (30 секунд)

**Приветствие:**
"Добрый день! Представляю вашему вниманию мобильное приложение **Rhythm MusicHub** - современный музыкальный стриминговый сервис для Android."

**Основная идея:**
- Приложение для прослушивания музыки с интеграцией Deezer API
- Поддержка 3 языков (EN, RU, KK)
- Современный дизайн в стиле Spotify

---

## 🎯 Выполненные требования (1 минута)

### Чек-лист:
✅ **3+ экрана:** Home, Search, Library, Settings, Player  
✅ **2 Activities:** MainActivity, PlayerActivity  
✅ **Navigation:** Navigation Compose  
✅ **API:** Deezer API через Retrofit  
✅ **Room Database:** SQLite для избранных треков  
✅ **DAO:** TrackDao с SQL запросами  
✅ **Локализация:** EN, RU, KK языки  

**Дополнительно реализовано:**
- Material Design 3 с темной/светлой темой
- ExoPlayer для воспроизведения музыки
- MVVM архитектура
- Jetpack Compose для UI

---

## 🏗️ Архитектура (2 минуты)

### MVVM Pattern

```
VIEW (Compose UI)
    ↓ наблюдает StateFlow
VIEWMODEL (MusicViewModel)
    ↓ использует Repository
REPOSITORY (MusicRepository)
    ↓ координирует
API (Retrofit) ← → DATABASE (Room)
```

### Объяснение каждого слоя:

**1. VIEW (UI Layer)**
```kotlin
@Composable
fun HomeScreen(viewModel: MusicViewModel) {
    val tracks by viewModel.chartTracks.collectAsState()
    // UI автоматически обновляется при изменении tracks
}
```
- Jetpack Compose - декларативный UI
- Только отображение, без логики

**2. VIEWMODEL**
```kotlin
class MusicViewModel : AndroidViewModel() {
    private val _chartTracks = MutableStateFlow<List<TrackEntity>>(emptyList())
    val chartTracks: StateFlow<List<TrackEntity>> = _chartTracks.asStateFlow()
    
    fun loadChartTracks() {
        viewModelScope.launch {
            repository.fetchChartTracks().fold(
                onSuccess = { _chartTracks.value = it },
                onFailure = { _errorMessage.value = it.message }
            )
        }
    }
}
```
- Управление состоянием
- Бизнес-логика
- Переживает поворот экрана

**3. REPOSITORY**
```kotlin
class MusicRepository(
    private val trackDao: TrackDao,
    private val apiService: DeezerApiService
) {
    fun getFavoriteTracks(): Flow<List<TrackEntity>> = 
        trackDao.getFavoriteTracks() // Из БД
    
    suspend fun fetchChartTracks(): Result<List<TrackResponse>> = 
        try { apiService.getChart() } // Из API
        catch { ... }
}
```
- Single Source of Truth
- Координирует локальные и удаленные данные

**4. DATA SOURCES**
- **Room Database:** хранение избранных треков
- **Retrofit API:** загрузка музыки из Deezer

---

## 💾 Room Database (1.5 минуты)

### Структура:

**Entity (Таблица):**
```kotlin
@Entity(tableName = "tracks")
data class TrackEntity(
    @PrimaryKey val id: String,
    val title: String,
    val artist: String,
    val isFavorite: Boolean = false
)
```

**DAO (Операции):**
```kotlin
@Dao
interface TrackDao {
    @Query("SELECT * FROM tracks WHERE isFavorite = 1")
    fun getFavoriteTracks(): Flow<List<TrackEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)
    
    @Query("SELECT * FROM tracks WHERE title LIKE '%' || :query || '%'")
    fun searchTracks(query: String): Flow<List<TrackEntity>>
}
```

**Database (Singleton):**
```kotlin
@Database(entities = [TrackEntity::class], version = 1)
abstract class MusicDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    
    companion object {
        @Volatile private var INSTANCE: MusicDatabase? = null
        fun getDatabase(context: Context): MusicDatabase = 
            INSTANCE ?: synchronized(this) { /* создание */ }
    }
}
```

**Преимущества Room:**
- SQL проверяется на этапе компиляции (ошибки сразу видны)
- Автоматическая генерация кода
- Поддержка Flow - UI обновляется автоматически
- Type-safe запросы

---

## 🌐 API Integration (1.5 минуты)

### Retrofit настройка:

```kotlin
object RetrofitClient {
    private const val BASE_URL = "https://api.deezer.com/"
    
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor()) // Логи
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    val deezerApiService = retrofit.create(DeezerApiService::class.java)
}
```

### API Interface:

```kotlin
interface DeezerApiService {
    @GET("chart")
    suspend fun getChart(): ChartResponse
    
    @GET("search")
    suspend fun searchTracks(@Query("q") query: String): SearchResponse
}
```

**Как это работает:**
1. Retrofit автоматически создает реализацию интерфейса
2. `@GET("chart")` → HTTP GET запрос к `https://api.deezer.com/chart`
3. `suspend` → корутина, не блокирует UI поток
4. Gson конвертирует JSON в Kotlin объекты

### Использование в Repository:

```kotlin
suspend fun fetchChartTracks(): Result<List<TrackResponse>> {
    return try {
        val response = apiService.getChart()
        Result.success(response.tracks.data)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
```

---

## 🧭 Navigation (1 минута)

### Структура:

**Screen.kt (маршруты):**
```kotlin
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Search : Screen("search")
    object Library : Screen("library")
    object Settings : Screen("settings")
}
```

**NavGraph.kt:**
```kotlin
@Composable
fun NavGraph(navController: NavHostController, viewModel: MusicViewModel) {
    NavHost(navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { 
            HomeScreen(viewModel) 
        }
        composable(Screen.Search.route) { 
            SearchScreen(viewModel) 
        }
        // ...
    }
}
```

**NavigationExtensions.kt (удобная навигация):**
```kotlin
fun NavController.navigateToHome() {
    navigate(Screen.Home.route) {
        popUpTo(Screen.Home.route) { inclusive = true }
        launchSingleTop = true // Не создавать дубликаты
    }
}
```

**В UI:**
```kotlin
IconButton(onClick = { navController.navigateToSettings() }) {
    Icon(Icons.Default.Settings)
}
```

---

## 🌍 Локализация (1 минута)

### Файлы ресурсов:

```
res/
├── values/strings.xml           (English - default)
├── values-ru/strings.xml        (Русский)
└── values-kk/strings.xml        (Қазақша)
```

**Пример strings.xml:**
```xml
<string name="app_name">Rhythm MusicHub</string>
<string name="nav_home">Home</string>
<string name="search_hint">What do you want to listen to?</string>
```

**Использование в коде:**
```kotlin
Text(text = stringResource(R.string.nav_home))
```

### Динамическая смена языка:

**LocaleHelper.kt:**
```kotlin
object LocaleHelper {
    fun setLocale(context: Context, languageCode: String): Context {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        
        val config = Configuration()
        config.setLocale(locale)
        
        return context.createConfigurationContext(config)
    }
    
    fun getLanguage(context: Context): String {
        return context.getSharedPreferences("app_settings", MODE_PRIVATE)
            .getString("selected_language", "en") ?: "en"
    }
}
```

**В Activity:**
```kotlin
override fun attachBaseContext(newBase: Context) {
    val languageCode = LocaleHelper.getLanguage(newBase)
    super.attachBaseContext(LocaleHelper.setLocale(newBase, languageCode))
}
```

---

## 🎵 Демонстрация функционала (2 минуты)

### 1. Главный экран (Home)
**Показать:**
- Загрузка топ-чартов из Deezer API
- Приветствие по времени суток (Good morning/afternoon/evening)
- Список треков с обложками
- Кнопка настроек

**Код:**
```kotlin
Text(text = getGreeting()) // "Good morning" утром

fun getGreeting(): String {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return when (hour) {
        in 0..11 -> stringResource(R.string.home_greeting_morning)
        in 12..17 -> stringResource(R.string.home_greeting_afternoon)
        else -> stringResource(R.string.home_greeting_evening)
    }
}
```

### 2. Поиск (Search)
**Показать:**
- Поиск с debounce (не отправляем запрос на каждую букву)
- Результаты из API в реальном времени
- Пустое состояние

**Код:**
```kotlin
LaunchedEffect(query) {
    if (query.isNotBlank()) {
        delay(500) // Debounce
        viewModel.searchTracks(query)
    }
}
```

### 3. Библиотека (Library)
**Показать:**
- Избранные треки из Room Database
- Удаление из избранного
- Автоматическое обновление списка

**Код:**
```kotlin
val favoriteTracks by viewModel.favoriteTracks.collectAsState()
// Автоматически обновляется при изменениях в БД

fun toggleFavorite(track: TrackEntity) {
    viewModelScope.launch {
        val updated = track.copy(isFavorite = !track.isFavorite)
        repository.insertTrack(updated) // Flow автоматически обновит UI
    }
}
```

### 4. Плеер (PlayerActivity)
**Показать:**
- Воспроизведение через ExoPlayer
- Play/Pause
- Progress bar с возможностью перемотки
- Добавление в избранное

**Код:**
```kotlin
player = ExoPlayer.Builder(this).build().apply {
    setMediaItem(MediaItem.fromUri(audioUrl))
    prepare()
    playWhenReady = true
}

// Обновление прогресса
LaunchedEffect(player) {
    while (isActive) {
        currentProgress = player.currentPosition / duration
        delay(100)
    }
}
```

### 5. Настройки (Settings)
**Показать:**
- Смена языка (EN/RU/KK) с перезапуском
- Смена темы (Light/Dark/System)
- Сохранение в SharedPreferences

---

## 🔑 Ключевые технологии

### Kotlin Features:
- **Coroutines** - асинхронность без блокировки
- **Flow** - реактивные потоки данных
- **Extension functions** - расширение функционала
- **Data classes** - автоматические equals/hashCode/copy
- **Sealed classes** - type-safe навигация

### Jetpack:
- **Compose** - современный декларативный UI
- **Navigation** - навигация между экранами
- **Room** - SQLite ORM
- **ViewModel** - управление состоянием
- **Lifecycle** - lifecycle-aware компоненты

### Библиотеки:
- **Retrofit** - REST API клиент
- **OkHttp** - HTTP клиент
- **Gson** - JSON парсинг
- **Coil** - загрузка изображений
- **ExoPlayer** - медиа плеер

---

## 💡 Почему именно эти технологии?

### Jetpack Compose вместо XML:
- ✅ На 40-50% меньше кода
- ✅ Декларативный подход (как React)
- ✅ Автоматическое обновление UI
- ✅ Рекомендуется Google для новых проектов
- ✅ Type-safe

### MVVM вместо MVC:
- ✅ Разделение ответственности
- ✅ Легко тестировать
- ✅ Переживает configuration changes
- ✅ Реактивное обновление

### Coroutines вместо Callbacks:
- ✅ Нет callback hell
- ✅ Структурированная конкурентность
- ✅ Автоматическая отмена
- ✅ Читаемый код

### Room вместо чистого SQLite:
- ✅ Проверка SQL на этапе компиляции
- ✅ Автоматическая генерация кода
- ✅ Поддержка Flow и LiveData
- ✅ Миграции БД

---

## ❓ Ожидаемые вопросы и ответы

### 1. "Что такое StateFlow и зачем он нужен?"

**Ответ:**
StateFlow - это реактивный поток данных (как LiveData, но лучше):
```kotlin
private val _chartTracks = MutableStateFlow<List<TrackEntity>>(emptyList())
val chartTracks: StateFlow<List<TrackEntity>> = _chartTracks.asStateFlow()

// В UI:
val tracks by viewModel.chartTracks.collectAsState()
// UI автоматически обновится при изменении tracks
```

**Преимущества:**
- Всегда имеет текущее значение
- Thread-safe
- Поддержка корутин
- Меньше boilerplate кода

### 2. "Как работает Room под капотом?"

**Ответ:**
Room - это ORM (Object-Relational Mapping):
1. Генерирует SQLite код на этапе компиляции
2. Проверяет SQL запросы (ошибки видны сразу)
3. Конвертирует результаты в Kotlin объекты
4. Поддерживает Flow - при изменении БД автоматически эмитит новые данные

### 3. "Почему две Activities, а не фрагменты?"

**Ответ:**
- MainActivity - основная навигация (Home, Search, Library)
- PlayerActivity - отдельный жизненный цикл для плеера
- Плеер может продолжать работать в фоне
- Разделение ответственности

Внутри используются Composable функции, а не фрагменты (современный подход).

### 4. "Как обрабатываете ошибки сети?"

**Ответ:**
```kotlin
suspend fun fetchChartTracks(): Result<List<TrackResponse>> {
    return try {
        val response = apiService.getChart()
        Result.success(response.tracks.data)
    } catch (e: Exception) {
        Result.failure(e) // IOException, JsonParseException и т.д.
    }
}

// В ViewModel:
repository.fetchChartTracks().fold(
    onSuccess = { data -> _chartTracks.value = data },
    onFailure = { error -> 
        _errorMessage.value = error.message
        // Показываем кнопку Retry
    }
)
```

### 5. "Почему используете Deezer API?"

**Ответ:**
- Бесплатный доступ без ключа API
- Большая база музыки
- Preview треки (30 секунд) легально
- REST API с JSON
- Хорошая документация

---

## 🎯 Заключение (30 секунд)

**Подведение итогов:**
"Проект демонстрирует современные Android практики:
- ✅ MVVM архитектура
- ✅ Jetpack Compose для UI
- ✅ Room для локального хранения
- ✅ Retrofit для работы с API
- ✅ Корутины для асинхронности
- ✅ Navigation Component
- ✅ Многоязычность
- ✅ Material Design 3

Все требования курса выполнены и превзойдены!"

**Благодарность:**
"Спасибо за внимание! Готов ответить на ваши вопросы."

---

## 📊 Статистика проекта

- **Строк кода:** ~3000+
- **Файлов:** 50+
- **Экранов:** 5
- **Языков:** 3
- **API endpoints:** 2
- **Database таблиц:** 1
- **Dependencies:** 20+

---

## 🎁 Бонусные фичи (если спросят)

1. **Темная/Светлая тема** - динамическая смена
2. **Edge-to-edge display** - современный UI
3. **Debounce в поиске** - оптимизация запросов
4. **Loading states** - индикаторы загрузки
5. **Error handling** - обработка всех ошибок
6. **Memory management** - корректное освобождение ресурсов
7. **Type-safe navigation** - безопасная навигация
8. **Singleton pattern** - для Database и API client

---

**Удачи на защите! 🚀**

