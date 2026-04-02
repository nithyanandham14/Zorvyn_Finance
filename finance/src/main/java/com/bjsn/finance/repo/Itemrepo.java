package com.bjsn.finance.repo;

import com.bjsn.finance.Module.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface Itemrepo extends JpaRepository<Item,Integer> {
    List<Item> findByReminderDate(LocalDate date);
    List<Item> findByItemtype(String itemtype);

    List<Item> findByNameContainingIgnoreCase(String name);
    List<Item> findByStatus(boolean status);
}
