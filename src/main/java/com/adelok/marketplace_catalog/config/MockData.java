package com.adelok.marketplace_catalog.config;

import com.adelok.marketplace_catalog.entity.Listing;
import com.adelok.marketplace_catalog.entity.Skin;
import com.adelok.marketplace_catalog.entity.enumerated.ListingStatus;
import com.adelok.marketplace_catalog.repository.ListingRepository;
import com.adelok.marketplace_catalog.repository.SkinRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Order(2)
@Component
@Slf4j
public class MockData implements CommandLineRunner {
    private final SkinRepository skinRepository;
    private final ListingRepository listingRepository;
    private final UUID mockSellerId;

    public MockData(
            @Value("${app.mock-seller-id}") UUID sellerId,
            SkinRepository skinRepository,
            ListingRepository listingRepository
    ) {
        this.listingRepository = listingRepository;
        this.skinRepository = skinRepository;
        this.mockSellerId = sellerId;
    }

    //Проверить есть ли уже загруженные листинги, если ещё не загружали смотрим не равно ли количество скинов в бд 0 иначе выходим из метода и кидаем предупреждение
//Далее заводим список в котором делаем выборку 2000 рандомных скинов из бд в skinRepository далее мапим скины к оружию на которое наложен данный скин
//Для этого мы заводим стрим т.к операция одна: нам надо глянуть присвоен ли скину weapon и собрать в группы по оружию
//Затем мы делаем цикл, в котором для каждого оружия выбираем по 5 скинов и через билдер создаем листинги со случайной ценой в диапазоне и даем им статус доступен, после чего сохраняем наши листинги в бд
    public void run(String... args) {
        if (listingRepository.count() > 0) {
            return;
        }
        if (skinRepository.count() == 0) {
            log.warn("no skins in catalog");
            return;
        }
        List<Skin> skins = skinRepository.findAll(PageRequest.of(0, 2000)).getContent();
        Map<String, List<Skin>> skinWeaponMap = skins.stream().filter(skin -> skin.getWeapon() != null).collect(Collectors.groupingBy(Skin::getWeapon));
        List<Listing> listings = new ArrayList<>();
        Random random = new Random();
        skinWeaponMap.forEach((weapon, weaponSkins) -> {
            weaponSkins.stream().limit(5).forEach(skin -> {
                listings.add(Listing.builder().skin(skin).sellerId(mockSellerId).price(BigDecimal.valueOf(50 + random.nextInt(1950))).status(ListingStatus.AVAILABLE).build());
            });
        });
        listingRepository.saveAllAndFlush(listings);
    }
}
