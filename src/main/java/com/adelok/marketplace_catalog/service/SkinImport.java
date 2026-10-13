package com.adelok.marketplace_catalog.service;

import com.adelok.marketplace_catalog.repository.SkinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class SkinImport {
    private final SkinRepository skinRepository;
    private final RestClient.Builder builder;
    private final ObjectMapper mapper;


}
