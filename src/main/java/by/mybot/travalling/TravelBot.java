package by.mybot.travalling;

import by.mybot.travalling.Controller.BotController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;


@SpringBootApplication
public class TravelBot{

    public static void main(String[] args) {
        SpringApplication.run(TravelBot.class, args);
    }

    @PostConstruct
    public void initBot(){
        ApiContextInitializer.init();
        TelegramBotsApi botapi = new TelegramBotsApi();
        try {
            botapi.registerBot(new BotController());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }


}
