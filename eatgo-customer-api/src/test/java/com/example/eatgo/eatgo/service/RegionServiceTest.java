package com.example.eatgo.eatgo.service;

import com.example.eatgo.domain.Region;
import com.example.eatgo.repository.RegionRepository;
import com.example.eatgo.service.RegionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

class RegionServiceTest {

    private RegionService regionService;

    @Mock
    private RegionRepository regionRepository;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this );

        regionService = new RegionService(regionRepository);
    }

    @Test
    public void 지역정보들을_가져온다() {
        List<Region> mockRegions = new ArrayList<>();
        mockRegions.add(Region.builder()
                .name("Seoul")
                .build());
        given(regionRepository.findAll()).willReturn(mockRegions);


        List<Region> regions = regionService.getRegions();
        Region region = regions.get(0);
        assertThat(region.getName()).isEqualTo("Seoul");
    }
}