# Qase.io UI & API Test Automation Framework

Проект построен на базе гибридного подхода (UI + API) с использованием передовых практик автоматизации и динамической генерации тестовых данных с помощью локальной большой языковой модели (LLM).

## 🚀 Технологический стек

* **Язык программирования:** Java 17
* **UI Автоматизация:** Selenide 7.16.2 (на базе Selenium WebDriver 4.44.0)
* **API Автоматизация:** Rest Assured 6.0.0
* **Тестовый движок:** TestNG 7.12.0
* **Генерация данных:** Локальная LLM модель **Qwen2.5-Coder:7b** (через Ollama API)
* **Архитектурные паттерны:** Page Object Model (POM), Fluent API, Binds/DTO, API-Seeding
* **Отчетность:** Allure Framework & Log4j2
* **CI/CD Интеграция:** Jenkins Pipeline, GitHub Actions

---

## 📋 Чек-лист автоматизации (Матрица покрытия регресса)

Поскольку в бесплатной (Free) версии Qase.io модули тест-ранов и дефектов заблокированы, автоматизация на 100% сфокусирована на **управлении тест-дизайном по методу CRUD** (Create, Read, Update, Delete) и продвинутых сценариях вложенности.

### 1. Авторизация (Auth / Logging)
- [x] **UI:** Успешный вход в систему с валидными учетными данными (из config.properties) и редирект на дашборд.
- [x] **UI:** Отображение глобальной ошибки безопасности (`These credentials do not match our records.`) при неверном пароле.
- [x] **UI:** Блокировка отправки формы и отображение ошибки валидации формата поля (`Value should be a valid email`) при некорректном Email.

### 2. Управление проектами (Projects)
- [x] **API:** Создание проекта (`POST /project`) с жестким контрактом `access: "all"` и валидацией JSON-схемы.
- [x] **API:** Получение информации о созданном проекте (`GET /project/{code}`) и проверка соответствия ИИ-данных.
- [x] **API:** Удаление проекта (`DELETE /project/{code}`) с проверкой статус-кода HTTP 200.
- [x] **UI:** Сквозной цикл: создание проекта, проверка его имени в репозитории через текстовый Xpath и каскадное удаление.

### 3. Управление папками (Suites)
- [x] **API:** Создание корневого тест-сьюта в рамках сгенерированного ИИ проекта.
- [x] **API:** Создание вложенного (дочернего) тест-сьюта с передачей корректного `parent_id` и проверка структуры дерева.
- [x] **API:** Каскадное удаление родительского сьюта вместе со всей вложенной иерархией.
- [x] **UI:** Создание корневой папки и валидация её отображения в левом дереве репозитория.
- [x] **UI:** Создание дочерней папки через контекстное меню (`svg[@data-icon='ellipsis']`) с изоляцией полей (`form.NWLa0T #title`).
- [x] **UI:** Клонирование (дублирование) структуры папок через JavaScript-клик по контекстному меню и проверка появления копии.

### 4. Управление тест-кейсами (Cases)
- [x] **API:** Создание тест-кейса со сложной структурой шагов (массивы `action` и `expected_result`) через эндпоинт `/case/{code}`.
- [x] **API:** Удаление тест-кейса и проверка очистки репозитория.
- [x] **UI:** Проверка отображения и выбора созданного кейса в интерфейсах смежных модулей.

### 5. Управление тест-планами (Test Plans)
- [x] **API:** Создание тест-плана (`POST /plan/{code}`), привязка массива ID кейсов и верификация через GET-запрос.
- [x] **UI:** Навигация в раздел планов через левое SPA-меню для предотвращения ошибок 404 серверного роутинга.
- [x] **UI:** Создание тест-плана на базе подготовленных через API-Seed данных: заполнение инпута `name='title'` и выбор кейсов кнопкой `Select all`.
- [x] **UI:** Сохранение тест-плана нажатием на нативную кнопку `Assign selected` / `Done` и проверка видимости плана в списке.

### 6. Непрерывная интеграция (CI/CD)
- [x] **Jenkins:** Покрытие сценария декларативного пайплайна с динамическим развертыванием Ollama и генерацией Allure.
- [x] **GitHub Actions:** Покрытие запуска тестов в Headless-режиме по триггеру `push`.

---

## 📂 Структура проекта (Архитектура)

```text
src
├── main
│   └── java
│       ├── adapters          # API-клиенты (ProjectAdapter, SuiteAdapter, CaseAdapter, PlanAdapter)
│       ├── models            # DTO-модели для JSON (ProjectRq/Rs, SuiteRq/Rs, CaseRq/Rs, PlanRq/Rs)
│       ├── pages             # Page Objects (LoginPage, ProjectsPage, ProjectPage, TestPlansPage)
│       └── utils             # Утилиты и ИИ-генератор данных (QwenDataGenerator, PropertyReader)
└── test
    ├── java
    │   ├── listeners         # Слушатели TestNG (TestListener для скриншотов, RetryListener)
    │   └── tests
    │       ├── api           # Изолированные API тесты бэкенда (ProjectApiTest, SuiteApiTest, etc.)
    │       └── ui            # UI тесты фронтенда (LoginTest, ProjectTest, SuiteUiTest, TestPlanUiTest)
    └── resources
        ├── schemas           # JSON-схемы валидации API (create_project_schema.json)
        ├── config.properties # Конфигурационный  файл (URL, Токены, Пароли)
        └── testng.xml        # Конфигурация параллельного запуска пакетов
Jenkinsfile                   # Декларативный пайплайн для Jenkins
.github/workflows/maven.yml   # Сценарий автоматизации для GitHub Actions
```

---

## 🧠 Интеграция с ИИ (Динамические данные)

Вся генерация тестовых данных полностью переведена с хардкода и библиотек Faker на локальную нейросеть `qwen2.5-coder:7b`. Это гарантирует 100% уникальность сущностей при каждом запуске, исключая конфликты дублирования записей в БД.

Интеграция реализована через универсальный класс `QwenDataGenerator.java`, отправляющий строго типизированные промпты на OpenAI-совместимый эндпоинт Ollama (`http://localhost:11434/api/chat`) с принудительным флагом `"format": "json"`. На уровне Java внедрены блокировки `try-catch` и фолбэк-генераторы для защиты от инфраструктурных сбоев LLM.

---

## ⚙️ Предварительные требования

Перед запуском тестов убедитесь, что на вашей локальной машине развернуто и запущено следующее окружение:

1. **Java Development Kit (JDK):** Версия 17 или выше.
2. **Apache Maven:** Сборщик проектов.
3. **Google Chrome:** Актуальная версия браузера.
4. **Ollama Daemon:** Запущен локально (`http://localhost:11434`).
5. **Загруженная модель Qwen:** Выполните команду в терминале для скачивания:
   ```bash
   ollama run qwen2.5-coder:7b
   ```

---

## 🛠️ Настройка конфигурации

Отредактируйте файл `src/test/resources/config.properties`, указав ваш персональный API-токен Qase.io и учетные данные для авторизации:

```properties
url=https://qase.io
api_url=https://qase.io
token=ВАШ_API_TOKEN_QASE
user=ваш_email@example.com
password=ваш_секретный_пароль
```

---

## 🏃 Запуск тестов

Фреймворк поддерживает запуск тестов как точечно (через Maven-фильтры), так и общим регрессионным пакетом в 2 параллельных потока на уровне тестовых классов.

### 1. Запуск всего регрессионного сьюта (UI + API):
```bash
mvn clean test
```

### 2. Запуск только API-тестов бэкенда:
```bash
mvn clean test -Dtest=*ApiTest
```

### 3. Запуск конкретного UI-сценария (например, создание тест-сьютов):
```bash
mvn clean test -Dtest=SuiteUiTest
```

---

## 📊 Генерация Allure-отчетов

Проект интегрирован со слушателем Allure TestNG, который автоматически логирует шаги `@Step`, прикрепляет скриншоты браузера в случае падения (`onTestFailure`) и выводит полную аналитику прогона.

1. **Сгенерировать отчет на основе результатов:**
   ```bash
   allure generate target/allure-results --clean -o target/allure-report
   ```
2. **Открыть интерактивный веб-сервер с отчетом:**
   ```bash
   allure serve target/allure-results
   ```
## 🔄 Настройка Непрерывной Интеграции (CI/CD)

Так как тесты завязаны на LLM-модель, агенты сборщиков должны иметь доступ к Ollama. Ниже представлены готовые конфигурации для автоматического развертывания окружения.

### 🔹 GitHub Actions (`.github/workflows/maven.yml`)
Создайте файл по указанному пути в корне проекта. Сценарий автоматически поднимет Docker-контейнер Ollama на стороне GitHub-раннера, скачает модель Qwen, запустит тесты в режиме Chrome Headless и опубликует Allure-отчет на GitHub Pages.

```yaml
name: Qase.io AI Regression CI

on:
  push:
    branches: [ "main", "master" ]
  pull_request:
    branches: [ "main", "master" ]

jobs:
  build:
    runs-on: ubuntu-latest

    services:
      # Поднимаем локальную Ollama в Docker-контейнере прямо на раннере GitHub
      ollama:
        image: ollama/ollama:latest
        ports:
          - 11434:11434

    steps:
    - uses: actions/checkout@v4

    - name: Set up JDK 17
      uses: actions/setup-java@v4
      with:
        java-version: '17'
        distribution: 'temurin'
        cache: maven

    - name: Pull Qwen Model inside Container
      run: |
        curl http://localhost:11434/api/pull -d '{"name": "qwen2.5-coder:7b"}'

    - name: Run Regression Tests (Headless)
      run: mvn clean test -Dselenide.headless=true
      env:
        # Передаем секреты из настроек репозитория GitHub
        QASE_TOKEN: ${{ secrets.QASE_TOKEN }}
        QASE_USER: ${{ secrets.QASE_USER }}
        QASE_PASSWORD: ${{ secrets.QASE_PASSWORD }}

    - name: Get Allure History
      final: always()
      uses: actions/checkout@v4
      if: always()
      with:
        ref: gh-pages
        path: gh-pages

    - name: Generate Allure Report
      uses: simple-elf/allure-report-action@master
      if: always()
      with:
        allure_results: target/allure-results
        allure_history: allure-history

    - name: Deploy Allure to GitHub Pages
      if: always()
      uses: peaceiris/actions-gh-pages@v3
      with:
        github_token: ${{ secrets.GITHUB_TOKEN }}
        publish_dir: allure-history
```

### 🔹 Jenkins Pipeline (`Jenkinsfile`)
Создайте файл `Jenkinsfile` в корневом каталоге. Пайплайн написан на базе Declarative синтаксиса, использует преднастроенные инструменты автоматизации и генерирует отчет через встроенный Allure-плагин Jenkins.


---

 🛡️ Best Practices & Стабилизация паттернов

* **API-Seeding в UI-тестах:** Для тяжелых тестов (например, сборка тест-планов) создание проекта и наполнение его тест-кейсами происходит мгновенно через быстрые API-адаптеры в `@BeforeMethod`, а в браузере проверяются только нативные клики.
* **Изоляция модальных окон (Form Scoping):** Поля ввода и кнопки подтверждения ищутся строго внутри активного CSS-контейнера формы (`form.NWLa0T #title`), что защищает Selenide от взаимодействия со старыми скрытыми React-элементами в DOM.
* **Пуленепробиваемые локаторы:** Работа с выпадающими списками и иерархией папок React Aria Components переведена на оси XPath (`ancestor::button`) и поиск по константным атрибутам доступности (`button[contains(@aria-label, 'suite name actions')]`, `[data-key='create_suite']`), что полностью нивелирует падения из-за динамических ID и ховер-эффектов.
* **Атомарная очистка данных (Single Responsibility Cleanup):** UI-тесты больше не удаляют за собой проекты через клики по интерфейсу. Полное каскадное удаление перенесено на API-адаптер в `@AfterMethod`, который всегда возвращает честный `HTTP 200` и защищает бесплатные лимиты аккаунта от забивания мусором.