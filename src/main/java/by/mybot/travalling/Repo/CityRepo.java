package by.mybot.travalling.Repo;

import by.mybot.travalling.Domain.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepo extends JpaRepository<City, Long> {
    City findByName(String name);
}
