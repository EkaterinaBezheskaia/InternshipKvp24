# InternshipKvp24
Web application for accounting of metering devices and readings

**Описание проекта**

Данный проект представляет собой RESTful API для управления адресами, типами приборов учета и показаниями приборов учета. Он разработан с использованием Spring Boot и предоставляет функциональность для создания, редактирования, получения и удаления данных.

**Стек технологий**

Java 24
Spring 
Boot 3.4.0
PostgreSQL - база данных
Lombok - для упрощения кода
Jakarta EE - для работы с аннотациями и валидацией
Maven - для управления зависимостями

**Установка**

1. Клонируйте репозиторий:

```
git clone https://github.com/ваш_репозиторий/TestTask.git
```

Перейдите в директорию проекта:

```
cd TestTask
```

Настройте базу данных:

Убедитесь, что PostgreSQL установлен и запущен.
Создайте базу данных с именем kvp24.
Измените параметры подключения к базе данных в файле application.properties:
properties

```
spring.datasource.url=jdbc:postgresql://localhost:5433/kvp24
spring.datasource.username=postgres
spring.datasource.password=ваш_пароль
```

Соберите проект с помощью Maven:

```
mvn clean install
```

Запустите приложение:

```
mvn spring-boot:run
```

**Использование API**

**Эндпоинты**

1. Адреса

   1.1. Создать адрес:

```POST /api/addresses```
Параметры: street, number, literal, flat

1.2. Редактировать адрес:

```PATCH /api/addresses/{street}/{number}```
Параметры: literal, flat, newStreet, newNumber, newLiteral, newFlat

1.3. Получить все адреса:

```GET /api/addresses```
Параметры: page, size

1.4. Получить адрес по улице и номеру:

```GET /api/addresses/{street}/{number}```

1.5. Удалить адрес:

```DELETE /api/addresses/{street}/{number}```

1.6. Удалить все адреса:

```DELETE /api/addresses/reset```

2. Типы приборов учета

2.1. Создать тип прибора учета:

```POST /api/meterTypes```
Параметры: meterTypeTitle

2.2. Редактировать тип прибора учета:

```PATCH /api/meterTypes/{meterTypeTitle}```
Параметры: newMeterTypeTitle

2.3. Получить все типы приборов учета:

```GET /api/meterTypes```
Параметры: page, size

2.4. Получить тип прибора учета по названию:

```GET /api/meterTypes/{meterTypeTitle}```

2.5. Удалить тип прибора учета:

```DELETE /api/meterTypes/{meterTypeTitle}```

2.6. Удалить все типы приборов учета:

```DELETE /api/meterTypes/reset```

3. Показания приборов учета

   3.1. Создать показания:

```POST /api/meterReadings/{metersSerialNumber}/{readingsDate}```
Параметры: readings

3.2. Редактировать показания:

```PATCH /api/meterReadings/{metersSerialNumber}/{readingsDate}```
Параметры: newReadings

3.3. Получить все показания для прибора учета:

```GET /api/meterReadings/{metersSerialNumber}```
Параметры: page, size, sortBy

3.4. Получить показания по дате:

```GET /api/meterReadings/{metersSerialNumber}/{readingsDate}```

3.5. Удалить показания:

```DELETE /api/meterReadings/{metersSerialNumber}/{readingsDate}```

3.6. Удалить все показания:

```DELETE /api/meterReadings/reset```

***Исключения***

Проект включает обработку исключений, таких как BadRequestException2 и NotFoundException2, которые возвращают соответствующие сообщения об ошибках и HTTP-статусы.

***Логирование***

Логирование осуществляется с помощью Log 4j, и все сообщения выводятся в консоль.

***Тестирование***

Для тестирования API вы можете использовать инструменты, такие как Postman или cURL. Убедитесь, что сервер запущен, и используйте указанные эндпоинты для выполнения запросов.

***Примечания***

Убедитесь, что все зависимости Maven установлены корректно.
Для работы с базой данных PostgreSQL необходимо установить соответствующий драйвер.
В случае возникновения ошибок, проверьте логи приложения для получения дополнительной информации.
Контрибьюция
Если вы хотите внести изменения или улучшения в проект, пожалуйста, создайте форк репозитория, внесите свои изменения и отправьте пулл-реквест.
