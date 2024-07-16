# Электронная система учета книг и читателей

## Стэк используемых технологий:

- Java 21
- Maven
- Hibernate
- Spring Framework
- Spring Boot
- Spring Data JPA
- Spring Web
- Spring Security
- Spring Test
- Spring AOP
- Keycloak
- Thymeleaf
- Swagger
- H2 database

## Сущности:

В проекте реализованы 3 сущности: книга (Book), читатель (Reader) и выдача книги (Issue).

Сущность Book имеет следующие параметры: id, название.

Сущность Reader имеет следующие параметры: id, имя.

Сущность Issue имеет следующие параметры: id, id книги, id читателя, имя читателя,
название книги, дата выдачи, дата сдачи.

## Функционал:

1. Получение, добавление, изменение и удаление читателя;
2. Получение, добавление, изменение и удаление книги;
3. Получения полного списка всех книг, людей и выдач;

Все эндпоинты вы можете посмотреть через Swagger: http://localhost:8180/swagger-ui/index.html

## Страницы

#### Список всех читателей: http://localhost:8180/ui/reader/all

<image src="images/reader_list.png" alt="Список всех читателей">

#### Добавление читателя:  http://localhost:8180/ui/reader/new

<image src="images/reader_add.png" alt="Добавление читателя">

#### Редактирование данных читателя: http://localhost:8180/ui/reader/{id}/edit

<image src="images/reader_edit.png" alt="Редактирование данных читателя">

#### Список всех книг: http://localhost:8180/ui/book/all

<image src="images/book_list.png" alt="Список всех книг">

#### Добавление новой книги: http://localhost:8180/ui/book/new

<image src="images/book_add.png" alt="Добавление новой книги">

#### Редактирование данных книги: http://localhost:8180/ui/book/{id}/edit

<image src="images/book_edit.png" alt="Редактирование данных книги">

#### Выдачи книг по читателю: http://localhost:8180/ui/issue/reader/{id}

<image src="images/issuesByReader.png" alt="Выдачи книг по читателю">

#### Полный список выдачи книг: http://localhost:8180/ui/issue/all

<image src="images/issue_list.png" alt="Полный список выдач">

## Реализация аутентификации и авторизации

Вход в систему доступен только при получении JWT-токена
на сервере авторизации(Keycloak), в качестве клиента можно использовать Postman или Keycloak.

Для доступа к ресурсам приложения нужно сделать следующее:

1. Запустите приложение
2. В командной строке выполните команду (должен быть установлен Docker):

```
docker run --name keycloak -p 8080:8080 -e KEYCLOAK_ADMIN=admin 
-e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:latest start-dev
```

3. Зайдите в админку Keycloak в браузере: http://localhost:8080/
4. Введите логин admin, пароль admin
5. Создайте новый realm (изолированное пространство пользователей):
   <image src="images/realm_create.png" alt="Создание realm">
6. Добавьте пользователя через вкладку "Users", нажмите "Create new user":
   <image src="images/user_create.png" alt="Создание пользователя">
7. Далее зайдите в раздел "Credentials" и нажмите "Set password",
   после чего введите пароль и выключите обновление пароля:
   <image src="images/set_password.png" alt="Создание пароля">
8. Перейдите во вкладку "Clients" и нажмите "Create client"
9. Добавьте клиента, будем использовать Postman:
   <image src="images/client_create.png" alt="Создание клиента">
   <image src="images/client_config.png" alt="Конфигурация клиента">
   <image src="images/client_config2.png" alt="Конфигурация клиента2">
10. Добавьте роли пользователей, в проекте предусмотрены роли admin и reader:
    <image src="images/role_create.png" alt="Создание роли admin">
    <image src="images/role_create2.png" alt="Создание роли reader">
11. Добавьте пользователю user роль reader (Users -> Role mapping -> Assign role):
    <image src="images/user_add_role.png" alt="Добавление роли reader юзеру">
12. Создайте свой маппер для ролей юзера(Сlient Scopes -> roles -> Mappers ->
    Add Mapper By configuration -> User Realm Role):
    <image src="images/mapper_create.png" alt="Создание маппера">

Если в качестве клиента используете Postman:

1. Отправьте POST-запрос в Keycloak для получения jwt-токена, client_secret
   следует скопировать из админки (Clients -> postman -> Credentials):
   <image src="images/postman_post.png" alt="POST запрос">
2. Отправьте GET-запрос в приложение, например, для получения читателя по id,
   предварительно скопируйте "access_token" в post-ответе и вставьте в заголовок Authorization:
   <image src="images/postman_get_success.png" alt="Get-запрос успешный">
3. Если отправите запрос на получение всех выдач книг, то вы не получите информацию,
   т.к. у текущего пользователя нет таких прав:
   <image src="images/postman_get_fail.png" alt="Get-запрос провальный">

Если в качестве клиента используете браузер:

1. При попытке запроса к разным ресурсам,сначала нужно заполнить форму входа
   логина и пароля Keycloak-client, раннее вами был создан "user" c паролем "user":
   <image src="images/browser_get.png" alt="Авторизация в браузере">
2. Запрос на получение читателя по id:
   <image src="images/browser_get_reader.png" alt="Get-запрос читателя по id">
3. Запрос на получение выдач книг, прав не хватает:
   <image src="images/browser_get_issues.png" alt="Get-запрос выдач книг">
