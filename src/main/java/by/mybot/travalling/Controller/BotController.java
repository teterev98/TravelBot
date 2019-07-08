package by.mybot.travalling.Controller;

import by.mybot.travalling.Domain.City;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class BotController extends TelegramLongPollingBot {
    @Value("${url}")
    private String url;

    @Override
    public String getBotUsername() {
        return "TaravelTestMinskBot";
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message.getText().toLowerCase().equals("/help") || message.getText().toLowerCase().equals("/start")) {
            sendMsg(message, "Введите All для получения информации о всех городе\n\n" +
                    "Введите НазваниеГорода для получения информации о городе\n\n" +
                    "Введите add НазваниеГорода \"Информация\" для добавления города\n\n"+
                    "Введите upd НазваниеГорода \"Информация\" для Обновления информации о городе\n\n"+
                    "Введите del НазваниеГорода для удаления города "
            );
        } else checkCommand(message);

    }

    @Override
    public String getBotToken() {
        return "703640161:AAHSsIoFpej8INl-CtAsFFzE5Q282C2nHF0";

    }


    private void sendMsg(Message msg, String text) {
        SendMessage s = new SendMessage();
        s.setChatId(msg.getChatId());
        s.setText(text);
        try {
            execute(s);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendCity(Message message) {
        final RestTemplate restTemplate = new RestTemplate();
        String name = toTemplate(message);
        final City city = restTemplate.getForObject("http://localhost:8080/travel/" + name, City.class);
        if (city == null) {
            sendMsg(message, "Город '" + name + "' не найден");
        } else {
            sendMsg(message, city.getDescription());
        }

    }

    private String toTemplate(Message message) {
        return Character.toUpperCase(message.getText().charAt(0)) + message.getText().substring(1).toLowerCase();
    }

    private City addCity(String name, String description) {
        final RestTemplate restTemplate = new RestTemplate();
        City cityToAdd = restTemplate.getForObject("http://localhost:8080/travel/" + name, City.class);
        if (cityToAdd == null) {
            cityToAdd = City.builder().setDescription(description).setName(name).build();
            final City insertedCity = restTemplate.postForObject("http://localhost:8080/travel/",
                    cityToAdd, City.class);
            return insertedCity;
        } else return null;
    }

    private void deleteCity(String name) {
        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete("http://localhost:8080/travelin/" + name);
    }

    private void updateCity(String name, String description) {
        final RestTemplate restTemplate = new RestTemplate();
        final City cityToUpdate = City.builder().setDescription(description).setName(name).build();

        restTemplate.put("http://localhost:8080/travel/" + name, cityToUpdate);
    }

    private void sendAllCities(Message message) {
        final RestTemplate restTemplate = new RestTemplate();
        final City[] cities = restTemplate.getForObject("http://localhost:8080/travel/", City[].class);
        if (cities == null) {
            sendMsg(message, "Не добавленно ни одного города");
        } else {
            for (final City city : cities) {
                sendMsg(message, city.getName() + ": " + city.getDescription());
            }

        }

    }

    private void checkCommand(Message message) {
        Pattern addOrUpdatePattern = Pattern.compile("(\\w+)\\s+(.+)\\s+\"(.+)\"");
        Pattern deletePattern = Pattern.compile("(\\w+)\\s+(.+)");

        String command;
        String name;
        String description;

        Matcher matcher = addOrUpdatePattern.matcher(message.getText());
        if (matcher.matches()) {
            command = matcher.group(1);
            name = matcher.group(2);
            description = matcher.group(3);
            if (command.toLowerCase().equals("add")) {
                City city = addCity(name, description);
                if (city == null) {
                    sendMsg(message, "Город" + name + " уже существует");
                } else {
                    sendMsg(message, "Город  добавлен " + city.toString());
                }
            } else if (command.toLowerCase().contains("upd")) {
                updateCity(name, description);
                sendMsg(message, "Город обновлён");
            }
        } else if ((matcher = deletePattern.matcher(message.getText())).matches()) {
            command = matcher.group(1);
            name = matcher.group(2);
            if (command.contains("Del")) {
                deleteCity(name);
                sendMsg(message, "Город удалён");
            }
        } else if (message.getText().toLowerCase().equals("all")) {
            sendAllCities(message);
        } else sendCity(message);
    }
}

