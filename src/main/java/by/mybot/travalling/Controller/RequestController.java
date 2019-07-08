package by.mybot.travalling.Controller;

import by.mybot.travalling.Domain.City;
import by.mybot.travalling.Repo.CityRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

@RestController
@RequestMapping("travel")
public class RequestController {

    private final CityRepo cityRepo;

    @Inject
    public RequestController(CityRepo cityRepo) {
        this.cityRepo = cityRepo;
    }


    @GetMapping
    public List<City> getCities(){
        return cityRepo.findAll();
    }

    @GetMapping("{name}")
    public City getInfByCity(@PathVariable String name){
        return cityRepo.findByName(name);
    }

    @PostMapping
    public City addCity(@RequestBody City city){
        return cityRepo.save(city);
    }

    @PutMapping("{name}")
    public City update(@PathVariable String name, @RequestBody City city){
        City cityFromDb = cityRepo.findByName(name);
            BeanUtils.copyProperties(city, cityFromDb , "name", "id");
        return cityRepo.save(cityFromDb);
    }

    @DeleteMapping("{name}")
    public void deleteCity(@PathVariable String name){
        cityRepo.delete(cityRepo.findByName(name));
    }

}
