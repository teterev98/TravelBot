Для старта бота в travvelling/src/application.properties добавить к первым трем  строкам 
1 Ссылку на базу данных postgesql
2 Имя пользователя (postgres по дефолту)
3 Пароль пользователя
Пример:
spring.datasource.url=jdbc:postgresql://localhost:5432/citytravel
spring.datasource.username=postgres
spring.datasource.password=12345

Запустить мейн метод класса TravelBot

Имя бота - TaravelTestMinskBot
Токен - 703640161:AAHSsIoFpej8INl-CtAsFFzE5Q282C2nHF0

Комманды бота:
CityName - для получения описания одного города
all - для получения названия и описания всех городов

Работать с базой даеный можно прямо из телеграмма  комаандами - 
add CityName "Description"  - для добавления
upd CityName "new Description"  - для изменения описаня
del CityName - для удаления города

Также работать с базой данный можно по адруссу http://localhost:8080/ где есть базовый UI

http запросы к REST сервису предаются по адрессам:
GET/DEL/PUT запросы http://localhost:8080/travel/CityName
POST запросы http://localhost:8080/travel