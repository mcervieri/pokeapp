package com.pokeapp.application.repository;

import com.pokeapp.domain.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    Optional<Item> findByPokeapiId(Integer pokeapiId);

    Optional<Item> findByName(String name);
}