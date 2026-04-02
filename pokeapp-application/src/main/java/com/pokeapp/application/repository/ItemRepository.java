package com.pokeapp.application.repository;

import com.pokeapp.domain.item.Item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    Optional<Item> findByPokeapiId(Integer pokeapiId);

    Optional<Item> findByName(String name);

    @Query("SELECT i FROM Item i WHERE CAST(:search AS string) IS NULL OR LOWER(i.name) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%'))")
    Page<Item> findBySearch(@Param("search") String search, Pageable pageable);
}