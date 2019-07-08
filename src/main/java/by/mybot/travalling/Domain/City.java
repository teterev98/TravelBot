package by.mybot.travalling.Domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO )
    private Long id;
    private String name;
    private String description;

    private City(){

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return Objects.equals(id, city.id) &&
                Objects.equals(name, city.name) &&
                Objects.equals(description, city.description);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, description);
    }

    public Long getId() {

        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return name + " \"" + description+ '"';
    }

    public static Builder builder() {
        return new City().new Builder();
    }

    public class Builder {

        private Builder() {

        }

        public Builder setName(String name) {
            City.this.name = name;

            return this;
        }

        public Builder setDescription(String description) {
            City.this.description = description;

            return this;
        }

        public City build() {
            return City.this;
        }

    }
}
