# SklaDinya backend

Серверная часть приложения для сервиса бронирования ячеек хранения SklaDinya.

## Запуск

1. Создать .env файл в директории deploy/dev на основе файла .env.example. Занести свои значения параметров для запуска.
2. Перейти в репозиторий фронтенда и скомпилировать там приложение с помощью следующих команд:
```bash
npm install
npm run build
```
3. Переместить полученную там папку dist в deploy/dev/caddy текущего репозитория.
4. Выполнить остальные действия в зависимости от операционной системы.

### Linux

На Linux сборка приложения и генерация данных входит в скрипт деплоя. 
Поэтому единственный оставшийся шаг - запустить команду:

```bash
./deploy/dev/deploy.sh
```

Приложение будет доступно на порту, указанном в переменной CADDY_OUTER_PORT в файле .env.

### Windows

На Linux сборка приложения и генерация данных не входят в скрипт деплоя.
Выполнить эти шаги придётся самостоятельно.

Сборка приложения (запускать в папке src):

```
gradlew.bat application:bootJar --no-daemon
```

Генерация данных (запускать в папке src):

```
gradlew.bat utils-bdfiller:bootRun --no-daemon
```

Полученное приложение (src/application/build/libs/skladinya-application.jar) скопировать в папку deploy/dev/spring под именем app.jar.
Полученные данные (src/utils-bdfiller/generatedData/main.sql) скопировать в папку deploy/dev/postgres/scripts под именем fill.sql.

Запуск приложения (запускать в папке deploy/dev):

```
deploy.bat
```

