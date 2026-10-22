package com.adelok.marketplace_catalog.config;

import com.adelok.marketplace_catalog.service.SkinImport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(1)
@Component
@RequiredArgsConstructor
@Slf4j
public class ImportRunner implements CommandLineRunner {
    private final SkinImport skinImport;

    @Override
    public void run(String... args) throws Exception {
        try {
            skinImport.importSkins();
        } catch (Exception e) {
            log.error("Import failed", e);
        }
    }
}
