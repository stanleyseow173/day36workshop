package ibf2022.batch3.csf.day36.weatherserver.repositories;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CityRepository {

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    public List<String> getCities() {
        Set<String> cities = redisTemplate.keys("*");
        // System.out.println("------>>>>>>" + cities);
        // System.out.println(cities.toString());
        List<String> cityList = cities.stream()
                .collect(Collectors.toList());
        // System.out.println(cityList);
        // System.out.println(cityList.get(1));
        return cityList;
    }

    public boolean addCity(String city) {

        String cityLower = city.toLowerCase().trim();
        List<String> cities = getCities();
        for (String cityAdded : cities) {
            System.out.println("cityAdded == " + cityAdded);
            System.out.println("cityLower == " + cityLower);
            if (cityLower.equals(cityAdded)) {
                return false;
            }
        }
        redisTemplate.opsForValue().set(cityLower, "1");
        return true;
    }
}
